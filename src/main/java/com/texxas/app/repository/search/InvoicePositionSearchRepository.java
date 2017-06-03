package com.texxas.app.repository.search;

import com.texxas.app.domain.InvoicePosition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InvoicePosition entity.
 */
public interface InvoicePositionSearchRepository extends ElasticsearchRepository<InvoicePosition, Long> {
}
