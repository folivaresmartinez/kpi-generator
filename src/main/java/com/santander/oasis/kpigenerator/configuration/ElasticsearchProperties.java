package com.santander.oasis.kpigenerator.configuration;

import lombok.Data;

@Data
public class ElasticsearchProperties {
    private String server;
    private Integer port;
    private String username;
    private String password;
}
