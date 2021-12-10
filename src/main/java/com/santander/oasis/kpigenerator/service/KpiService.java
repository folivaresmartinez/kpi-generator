package com.santander.oasis.kpigenerator.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.santander.oasis.kpigenerator.domain.Incident;
import com.santander.oasis.kpigenerator.domain.Kpi;
import com.santander.oasis.kpigenerator.repository.KpiRepository;
import com.santander.oasis.kpigenerator.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class KpiService {
    private final S3Service s3Service;
    private final KpiRepository kpiRepository;

    public Page<Kpi> findAll(Pageable pageable) {
        return kpiRepository.findAll(pageable);
    }

    public Kpi save(Kpi kpi) {
        return kpiRepository.save(kpi);
    }

    public Iterable<Kpi> saveAll(List<Kpi> kpis) {
        return kpiRepository.saveAll(kpis);
    }

    public void calculateAllKpis() {
        List<Kpi> kpis = new ArrayList<>();
        calculateMTTR().ifPresent(kpi -> {
            log.info("Saving MTTR kpi on elasticSearch. Kpi: {}", kpi);
            kpis.add(kpi);
        });

        //saveAll(kpis);
    }

    private Optional<Kpi> calculateMTTR() {
        InputStream inputStream = s3Service.getObject(Constants.INCIDENT_FILE).getObjectContent();
        try (Reader reader = new InputStreamReader(inputStream)) {
            CsvToBean<Incident> csvToBean = new CsvToBeanBuilder(reader).withType(Incident.class).withIgnoreLeadingWhiteSpace(true).build();
            List<Incident> incidents = csvToBean.stream().filter(incident -> incident.getEndDate() != null).collect(Collectors.toList());
            long mttrTotal = incidents.stream().map(incident -> ChronoUnit.MINUTES.between(incident.getStartDate(), incident.getEndDate())).reduce(0l, Long::sum).longValue();
            return Optional.of(new Kpi("",Constants.MTTR, (mttrTotal / incidents.size()) + "", LocalDateTime.now()));
        } catch (Exception e) {
            log.error("Error reading file {}. Error: {}", Constants.INCIDENT_FILE, e.getMessage());
        }
        return Optional.empty();
    }

}

