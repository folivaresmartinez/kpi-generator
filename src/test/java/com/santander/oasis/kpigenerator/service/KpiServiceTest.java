package com.santander.oasis.kpigenerator.service;

import com.amazonaws.services.s3.model.S3Object;
import com.santander.oasis.kpigenerator.domain.Kpi;
import com.santander.oasis.kpigenerator.repository.KpiRepository;
import com.santander.oasis.kpigenerator.utils.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class KpiServiceTest {

    @InjectMocks
    private KpiService kpiService;
    @Mock
    private KpiRepository kpiRepository;
    @Mock
    private S3Service s3Service;
    private static List<Kpi> kpis = new ArrayList<>();
    private static Page<Kpi> kpisPage;


    @BeforeAll
    public static void prepareKpis(){
        kpis.add(Kpi.builder().id(Constants.ID).value(Constants.VALUE).date(LocalDateTime.now()).build());
        kpis.add(Kpi.builder().id("02").value("value 02").date(LocalDateTime.now()).build());
        kpisPage = new PageImpl<Kpi>(kpis);
    }

    @Test
    public void whenFindAllReturnsValues(){
        Mockito.lenient().when(kpiRepository.findAll(Pageable.ofSize(10))).thenReturn(kpisPage);
        Page<Kpi> anotherPage = kpiService.findAll(Pageable.ofSize(10));
        assertEquals(kpisPage, anotherPage);
        assertTrue(anotherPage.getTotalElements() == 2);
        assertTrue(anotherPage.getContent().get(0).getId().equals(Constants.ID));
        assertTrue(anotherPage.getContent().get(0).getValue().equals(Constants.VALUE));
    }

    @Test
    public void whenSaveKpiIsOkAndReturnObject(){
        Mockito.lenient().when(kpiRepository.save(kpis.get(0))).thenReturn(kpis.get(0));
        Kpi anotherKpi = kpiService.save(kpis.get(0));
        assertEquals(kpis.get(0), anotherKpi);
        assertTrue(anotherKpi.getId().equals(Constants.ID));
        assertTrue(anotherKpi.getValue().equals(Constants.VALUE));
    }

    @Test
    public void whenSaveAllKpiIsOkAndReturnListObject(){
        Mockito.lenient().when(kpiRepository.saveAll(kpis)).thenReturn(kpis);
        List<Kpi> anotherKpis = new ArrayList<>();
        kpiService.saveAll(kpis).forEach(anotherKpis::add);
        assertEquals(anotherKpis, kpis);
        assertTrue(anotherKpis.size() == 2);
    }

    @Test
    public void whenCalculateAllKpisIsOk(){
        // Mockito.lenient().when(s3Service.getObject(Constants.INCIDENT_FILE)).thenReturn(new S3Object());
        // kpiService.calculateMTTR(Constants.INCIDENT_FILE);
    }

}
