package com.nordclan.samsara_grabber.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nordclan.samsara_grabber.service.SamsaraService;
import com.nordclan.samsara_grabber.web.api.client.FileDownloadClient;
import com.nordclan.samsara_grabber.web.api.client.SamsaraClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@PropertySource("classpath:./application.properties")
public class TestAppConfig {

    @Value("${env.location}")
    private String ENV_LOCATION;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {

        RestTemplateBuilder rtb = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);

        when(rtb.build()).thenReturn(restTemplate);
        return rtb;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public SamsaraClient samsaraClient(RestTemplateBuilder restTemplate) {
        return new SamsaraClient(restTemplate.build());
    }

    @Bean
    public FileDownloadClient fileDownloadClient() {
        return new FileDownloadClient();
    }

    @Bean
    public SamsaraService samsaraService() {
        return new SamsaraService( objectMapper(), samsaraClient(restTemplateBuilder()), fileDownloadClient());
    }

}