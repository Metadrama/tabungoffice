package io.github.metadrama.tabungoffice;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// one row = one person putting money into a tabung
@Entity
@Table(name = "contributions")
public class Contribution {

    @Id
    @GeneratedValue
    private Long id;

    private Long tabungId;
    private String memberName;
    private double amount;
    private String note;
    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public Long getTabungId() {
        return tabungId;
    }

    public void setTabungId(Long tabungId) {
        this.tabungId = tabungId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}