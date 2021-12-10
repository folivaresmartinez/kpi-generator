package com.santander.oasis.kpigenerator.service;


import com.santander.oasis.kpigenerator.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

@ExtendWith(MockitoExtension.class)
public class KafkaServiceTest {

    @InjectMocks
    private KafkaService kafkaService;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private KpiService kpiService;
    private static final SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();

    @Test
    public void whenSendMessageWithKafkaTemplateIsOk(){
        Mockito.lenient().when(kafkaTemplate.send(Constants.TOPIC, Constants.TEST_MESSAGE)).thenReturn(future);
        kafkaService.sendMessage(Constants.TEST_MESSAGE);
    }

    @Test
    public void whenListenerReceiveMessageIsOk(){
        kafkaService.listener(Constants.TEST_MESSAGE);
    }

}
