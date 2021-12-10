package com.santander.oasis.kpigenerator.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Incident {
    @CsvBindByName(column = "actual_start", required = true)
    @CsvDate(value = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime startDate;
    @CsvBindByName(column = "actual_end")
    @CsvDate(value = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime endDate;
}
