package com.texxas.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_number")
    private String number;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Column(name = "nip")
    private String nip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public Invoice number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public Invoice date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Invoice companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNip() {
        return nip;
    }

    public Invoice nip(String nip) {
        this.nip = nip;
        return this;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", date='" + getDate() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", nip='" + getNip() + "'" +
            "}";
    }
}
