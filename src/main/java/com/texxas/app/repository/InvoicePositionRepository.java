package com.texxas.app.repository;

import com.texxas.app.domain.InvoicePosition;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InvoicePosition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoicePositionRepository extends JpaRepository<InvoicePosition,Long> {

}
