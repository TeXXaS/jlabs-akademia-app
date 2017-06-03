package com.texxas.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.texxas.app.domain.InvoicePosition;

import com.texxas.app.repository.InvoicePositionRepository;
import com.texxas.app.repository.search.InvoicePositionSearchRepository;
import com.texxas.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InvoicePosition.
 */
@RestController
@RequestMapping("/api")
public class InvoicePositionResource {

    private final Logger log = LoggerFactory.getLogger(InvoicePositionResource.class);

    private static final String ENTITY_NAME = "invoicePosition";
        
    private final InvoicePositionRepository invoicePositionRepository;

    private final InvoicePositionSearchRepository invoicePositionSearchRepository;

    public InvoicePositionResource(InvoicePositionRepository invoicePositionRepository, InvoicePositionSearchRepository invoicePositionSearchRepository) {
        this.invoicePositionRepository = invoicePositionRepository;
        this.invoicePositionSearchRepository = invoicePositionSearchRepository;
    }

    /**
     * POST  /invoice-positions : Create a new invoicePosition.
     *
     * @param invoicePosition the invoicePosition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoicePosition, or with status 400 (Bad Request) if the invoicePosition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-positions")
    @Timed
    public ResponseEntity<InvoicePosition> createInvoicePosition(@Valid @RequestBody InvoicePosition invoicePosition) throws URISyntaxException {
        log.debug("REST request to save InvoicePosition : {}", invoicePosition);
        if (invoicePosition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new invoicePosition cannot already have an ID")).body(null);
        }
        InvoicePosition result = invoicePositionRepository.save(invoicePosition);
        invoicePositionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/invoice-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-positions : Updates an existing invoicePosition.
     *
     * @param invoicePosition the invoicePosition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoicePosition,
     * or with status 400 (Bad Request) if the invoicePosition is not valid,
     * or with status 500 (Internal Server Error) if the invoicePosition couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-positions")
    @Timed
    public ResponseEntity<InvoicePosition> updateInvoicePosition(@Valid @RequestBody InvoicePosition invoicePosition) throws URISyntaxException {
        log.debug("REST request to update InvoicePosition : {}", invoicePosition);
        if (invoicePosition.getId() == null) {
            return createInvoicePosition(invoicePosition);
        }
        InvoicePosition result = invoicePositionRepository.save(invoicePosition);
        invoicePositionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoicePosition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-positions : get all the invoicePositions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invoicePositions in body
     */
    @GetMapping("/invoice-positions")
    @Timed
    public List<InvoicePosition> getAllInvoicePositions() {
        log.debug("REST request to get all InvoicePositions");
        List<InvoicePosition> invoicePositions = invoicePositionRepository.findAll();
        return invoicePositions;
    }

    /**
     * GET  /invoice-positions/:id : get the "id" invoicePosition.
     *
     * @param id the id of the invoicePosition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoicePosition, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-positions/{id}")
    @Timed
    public ResponseEntity<InvoicePosition> getInvoicePosition(@PathVariable Long id) {
        log.debug("REST request to get InvoicePosition : {}", id);
        InvoicePosition invoicePosition = invoicePositionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invoicePosition));
    }

    /**
     * DELETE  /invoice-positions/:id : delete the "id" invoicePosition.
     *
     * @param id the id of the invoicePosition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-positions/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoicePosition(@PathVariable Long id) {
        log.debug("REST request to delete InvoicePosition : {}", id);
        invoicePositionRepository.delete(id);
        invoicePositionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/invoice-positions?query=:query : search for the invoicePosition corresponding
     * to the query.
     *
     * @param query the query of the invoicePosition search 
     * @return the result of the search
     */
    @GetMapping("/_search/invoice-positions")
    @Timed
    public List<InvoicePosition> searchInvoicePositions(@RequestParam String query) {
        log.debug("REST request to search InvoicePositions for query {}", query);
        return StreamSupport
            .stream(invoicePositionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
