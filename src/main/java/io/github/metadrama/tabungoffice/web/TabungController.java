//each method here is called when requested via URL, and whatever it returns goes back as JSON
package io.github.metadrama.tabungoffice.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.metadrama.tabungoffice.Contribution;
import io.github.metadrama.tabungoffice.ContributionRepository;
import io.github.metadrama.tabungoffice.Tabung;
import io.github.metadrama.tabungoffice.TabungRepository;

@RestController
public class TabungController {

    private final TabungRepository tabungs;
    private final ContributionRepository contributions;

    // what the client sends when creating a tabung
    record NewTabung(String name, String purpose, double targetAmount, String eventDate) {

    }

    // what the client sends when adding money
    record NewContribution(String memberName, double amount, String note) {

    }

    // tabung + how much has been collected so far, sent back to the client
    record TabungSummary(Long id, String name, String purpose, double targetAmount, String eventDate,
            double collected, int memberCount) {

    }

    public TabungController(TabungRepository tabungs, ContributionRepository contributions) {
        this.tabungs = tabungs;
        this.contributions = contributions;
    }

    // --- tabungs ---
    @PostMapping("/tabungs")
    ResponseEntity<Tabung> createTabung(@RequestBody NewTabung body, @RequestParam String ownerPhone) {
        Tabung t = new Tabung();
        t.setName(body.name());
        t.setPurpose(body.purpose());
        t.setTargetAmount(body.targetAmount());
        t.setEventDate(java.time.LocalDate.parse(body.eventDate()));
        t.setCreatedAt(LocalDateTime.now());
        t.setOwnerPhone(ownerPhone);
        return ResponseEntity.ok(tabungs.save(t));
    }

    @GetMapping("/tabungs")
    List<Tabung> listTabungs(@RequestParam(required = false) String ownerPhone) {
        if (ownerPhone == null) {
            return tabungs.findAll();
        }
        return tabungs.findByOwnerPhone(ownerPhone);
    }

    @GetMapping("/tabungs/{id}")
    ResponseEntity<TabungSummary> getTabung(@PathVariable Long id) {
        return tabungs.findById(id).map(this::summarise).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/tabungs/{id}")
    ResponseEntity<Tabung> updateTabung(@RequestBody NewTabung body, @PathVariable Long id) {
        Tabung existing = tabungs.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setName(body.name());
        existing.setPurpose(body.purpose());
        existing.setTargetAmount(body.targetAmount());
        existing.setEventDate(java.time.LocalDate.parse(body.eventDate()));
        return ResponseEntity.ok(tabungs.save(existing));
    }

    @DeleteMapping("/tabungs/{id}")
    ResponseEntity<Void> deleteTabung(@PathVariable Long id) {
        if (!tabungs.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // remove the money records first, otherwise they point at nothing
        contributions.deleteAll(contributions.findByTabungId(id));
        tabungs.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- contributions ---
    @PostMapping("/tabungs/{id}/contributions")
    ResponseEntity<Contribution> addContribution(@RequestBody NewContribution body, @PathVariable Long id) {
        if (!tabungs.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Contribution c = new Contribution();
        c.setTabungId(id);
        c.setMemberName(body.memberName());
        c.setAmount(body.amount());
        c.setNote(body.note());
        c.setPaidAt(LocalDateTime.now());
        return ResponseEntity.ok(contributions.save(c));
    }

    @GetMapping("/tabungs/{id}/contributions")
    List<Contribution> listContributions(@PathVariable Long id) {
        return contributions.findByTabungId(id);
    }

    @DeleteMapping("/contributions/{id}")
    ResponseEntity<Void> deleteContribution(@PathVariable Long id) {
        if (!contributions.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        contributions.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- total collected plus how many members contributed ---
    private ResponseEntity<TabungSummary> summarise(Tabung t) {
    	Set<String> members = new HashSet<>();
        List<Contribution> list = contributions.findByTabungId(t.getId());
        double total = 0;
        for (Contribution c : list) {
            total += c.getAmount();
            members.add(c.getMemberName());
        }
        TabungSummary s = new TabungSummary(t.getId(), t.getName(), t.getPurpose(), t.getTargetAmount(),
                t.getEventDate() == null ? null : t.getEventDate().toString(), total, members.size());
        return ResponseEntity.ok(s);
    }
}
