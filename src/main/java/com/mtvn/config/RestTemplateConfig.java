package com.mtvn.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mtvn.common.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Value("${mt.http.connectTimeOut:3000}") private int connectTimeOut;
    @Value("${mt.http.readTimeOut:15000}") private int readTimeOut;
    @Value("${mt.http.proxy.enabled:false}") private boolean proxyEnabled;
    @Value("${mt.http.proxy.host:10.99.166.120}") private String proxyHost;
    @Value("${mt.http.proxy.port:3128}") private int proxyPort;
    @Value("${mt.http.proxy.nonProxyHosts:localhost|127.*|10.99.*|10.159.*}") private String nonProxyHosts;

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder();
    }

    //TODO: this restTemplate does not allow MULTIPART/FORM-DATA and MULTIPART/FORM-URLENCODED requests. It needs to be configured for these requests.
    // TODO: Also the encoding format used is ISO_8859_1 but MULTIPART/FORM-DATA and MULTIPART/FORM-URLENCODED requires UTF-8 encoding.
    @Bean
    @Autowired
    public RestTemplate restTemplate(Jackson2ObjectMapperBuilder objectMapperBuilder) {
        HttpClient httpClient = getHttpClientWithProxy();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                httpClient == null
                        ? new HttpComponentsClientHttpRequestFactory()
                        : new HttpComponentsClientHttpRequestFactory(httpClient);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeOut);
        httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeOut);

        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);

        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper(objectMapperBuilder));
        converters.add(jsonConverter);
        converters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(converters);
        return restTemplate;
    }

    //method made public to use proxy for custom restTemplate used for perfios APIs
    public CloseableHttpClient getHttpClientWithProxy() {
        if(!proxyEnabled) {
            log.debug("Proxy Disabled");
            return null;
        }
        Set<String> noProxyHosts = StringUtils.hasText(nonProxyHosts)
                ? new HashSet<>(Arrays.asList(nonProxyHosts.split("\\|")))
                : new HashSet<>();
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        log.debug("Setting Proxy {}, nonProxyHosts: {}", proxyHost + ":" + proxyPort, nonProxyHosts);
        return HttpClients.custom()
                .setProxy(proxy)
                .setRoutePlanner(new NonProxyListProxyRoutePlanner(proxy, noProxyHosts))
                .build();
    }

    @Bean
    @Autowired
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder objectMapperBuilder) {
        ObjectMapper objectMapper = objectMapperBuilder.dateFormat(new StdDateFormat()).createXmlMapper(false).build();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}
