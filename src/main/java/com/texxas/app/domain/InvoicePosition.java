package com.texxas.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A InvoicePosition.
 */
@Entity
@Table(name = "invoice_position")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoiceposition")
public class InvoicePosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_value", precision=10, scale=2)
    private BigDecimal value;

    @NotNull
    @Column(name = "tax", precision=10, scale=2, nullable = false)
    private BigDecimal tax;

    @ManyToOne(optional = false)
    @NotNull
    private Invoice invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public InvoicePosition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public InvoicePosition value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public InvoicePosition tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public InvoicePosition invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoicePosition invoicePosition = (InvoicePosition) o;
        if (invoicePosition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoicePosition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoicePosition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            ", tax='" + getTax() + "'" +
            "}";
    }
}
