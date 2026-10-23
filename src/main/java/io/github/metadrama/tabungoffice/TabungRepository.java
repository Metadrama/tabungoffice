package io.github.metadrama.tabungoffice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring writes the class that talks to the tabungs table
public interface TabungRepository extends JpaRepository<Tabung, Long> {

	List<Tabung> findByOwnerPhone(String ownerPhone);
}