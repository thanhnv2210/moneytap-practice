package com.mtvn.common.http;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service("restService")
public class RestService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired private Gson apiLogPinter;

    public <T> T post(String url, HttpHeaders requestHeaders, HttpMethod method,
                      Object requestBody, Class<T> clazz) {
        return post(this.restTemplate, url, requestHeaders, method, requestBody, clazz, true, true);
    }

    public <T> T post(String url, HttpHeaders requestHeaders, HttpMethod method,
                      Object requestBody, Class<T> clazz, boolean logInput, boolean logOutput) {
        return post(this.restTemplate, url, requestHeaders, method, requestBody, clazz, logInput, logOutput);
    }

    public <T> T post(String url, HttpHeaders requestHeaders, HttpMethod method,
                      Object requestBody, Map<String, Object> queryParams, Class<T> clazz) {
        return this.post(this.restTemplate, url, requestHeaders, method, requestBody, queryParams, clazz, true, true);
    }

    public <T> T post(RestTemplate restTemplate, String url, HttpHeaders requestHeaders,
                      HttpMethod method, Object requestBody, Map<String, Object> queryParams, Class<T> clazz) {
        return this.post(restTemplate, url, requestHeaders, method, requestBody, queryParams, clazz, true, true);
    }

    public <T> T post(RestTemplate restTemplate, String url, HttpHeaders requestHeaders, HttpMethod method,
                      Object requestBody, Class<T> clazz) {
        return post(restTemplate, url, requestHeaders, method, requestBody, clazz, true, true);
    }

    public <T> T post(RestTemplate restTemplate, String url, HttpHeaders requestHeaders, HttpMethod method,
                      Object requestBody, Class<T> clazz, boolean logInput, boolean logOutput) {
        long start = new Date().getTime();
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestBody, requestHeaders);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        String uri = builder.toUriString();
        String sLog = logInput ? apiLogPinter.toJson(requestBody) : "suppressed";
        log.debug("HTTP Input {} : {}", url, requestBody instanceof String ? requestBody : sLog);
        try {
            return makeRequest(restTemplate, url, method, clazz, start, requestEntity, uri, logInput, logOutput);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus status = e.getStatusCode();
            String respBody = e.getResponseBodyAsString();
            log.error("HTTP Error {} : {} - Response:{} ", status, url, respBody);
            throw e;
        } catch (ResourceAccessException e){
            if(e.getCause() instanceof SocketTimeoutException) {
                log.error("HTTP Error {} : {}", HttpStatus.REQUEST_TIMEOUT, url);
            }
            throw e;
        }
    }

    public <T> T post(RestTemplate restTemplate, String url, HttpHeaders requestHeaders,
                      HttpMethod method, Object requestBody, Map<String, Object> queryParams, Class<T> clazz,
                      boolean logInput, boolean logOutput) {
        long start = new Date().getTime();
        HttpEntity<?> requestEntity = requestBody != null ? new HttpEntity<Object>(requestBody, requestHeaders) : new HttpEntity<>(requestHeaders);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if(queryParams != null && !queryParams.isEmpty()) {
            for(String param : queryParams.keySet()) {
                Object value = queryParams.get(param);
                builder.queryParam(param, value);
            }
        }
        String uri = builder.toUriString();
        String sLog = logInput ? apiLogPinter.toJson(requestBody) : "suppressed";
        log.debug("HTTP Input {} : {}", url, requestBody instanceof String ? requestBody : sLog);
        try {
            return makeRequest(restTemplate, url, method, clazz, start, requestEntity, uri, logInput, logOutput);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus status = e.getStatusCode();
            String respBody = e.getResponseBodyAsString();
            log.error("HTTP Error {} : {} - Response:{} ", status, url, respBody);
            throw e;
        } catch (ResourceAccessException e){
            if(e.getCause() instanceof SocketTimeoutException) {
                log.error("HTTP Error {} : {}", HttpStatus.REQUEST_TIMEOUT, url);
            }
            throw e;
        }
    }


    private <T> T makeRequest(RestTemplate restTemplate, String url, HttpMethod method,
                              Class<T> clazz,
                              long start, HttpEntity<?> requestEntity, String uri, boolean logInput, boolean logOutput) {

        ResponseEntity<T> response = restTemplate.exchange(uri, method, requestEntity, clazz);
        int statusCode = 0;
        if(response != null) {
            statusCode = response.getStatusCodeValue();
            String sLog = logOutput ? apiLogPinter.toJson(response.getBody()) : "suppressed";
            log.debug("HTTP Output {} : {}", url, sLog);
            log.debug("HTTP call {} finished with Status {} in {} ms.", url, statusCode, (new Date().getTime() - start));
            if(statusCode == 477){
                //just logging the response for now, but needs to be properly handled.
                log.error("Received {} DisplayMessageException :{}",statusCode, response.getBody());
            }
            return response.getBody();
        }
        log.error("Got Null Response from {}", url);
        return null;
    }
}
