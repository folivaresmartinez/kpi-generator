package com.santander.oasis.kpigenerator.configuration;

import com.santander.oasis.kpigenerator.utils.Constants;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Configuration
@ConfigurationProperties("elasticsearch")
@EnableElasticsearchRepositories
@Data
public class ElasticsearchConfiguration {

    private ElasticsearchProperties properties;

    @Bean
    public RestHighLevelClient client() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider
                .setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword()));

        SSLContextBuilder sslBuilder = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true);
        final SSLContext sslContext = sslBuilder.build();

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(properties.getServer(), properties.getPort(), Constants.HTTPS))
                        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                            @Override
                            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                                httpClientBuilder.disableAuthCaching();
                                httpClientBuilder.setSSLContext(sslContext);
                                httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                                return httpClientBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
                            }
                        })

        );
        return restHighLevelClient;
    }
}
