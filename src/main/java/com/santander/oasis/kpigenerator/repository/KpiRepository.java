package com.santander.oasis.kpigenerator.repository;

import com.santander.oasis.kpigenerator.domain.Kpi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface KpiRepository extends ElasticsearchRepository<Kpi, String> {
}
