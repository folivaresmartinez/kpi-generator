package com.santander.oasis.kpigenerator.service;

import com.santander.oasis.kpigenerator.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Slf4j
@Service
@AllArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KpiService kpiService;



    @KafkaListener(topics = Constants.TOPIC, groupId = Constants.GROUP_ID)
    public void listener(String message) {
        log.info("Message received on topic {}: {}", Constants.TOPIC, message);
        kpiService.calculateAllKpis();
    }


    public void sendMessage(String message){
        kafkaTemplate.send(Constants.TOPIC, message);
    }

    @PostConstruct
    public void postConstruct() throws InterruptedException {
        Thread.sleep(10000);
        this.sendMessage("Demo 16/11/2021");
    }

}

