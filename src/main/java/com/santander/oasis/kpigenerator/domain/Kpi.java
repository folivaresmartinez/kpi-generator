package com.santander.oasis.kpigenerator.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

//@Document(indexName = "test-subires")
@Data
@Builder
public class Kpi {

    @Id
    private String id;
    
    private String kpi;
    private String value;
    private LocalDateTime date;

    public Kpi(String id, String kpi, String value, LocalDateTime date){
    	this.kpi = kpi;
        this.value = value;
        this.date = date;
    }
}
