package com.accionmfb.proxy.mobileproxyv2.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiLogger {

    private final ObjectMapper gson;
    private final Gson json;

    @SneakyThrows
    public void logApiRequest(String message, String url, HttpEntity<?> httpEntity, Map<String, Object> params){
        String requestBodyJson = Optional.ofNullable(httpEntity.getBody()).isPresent() ? gson.writeValueAsString(httpEntity.getBody()) : "{}";
        params = Optional.ofNullable(params).isPresent() ? params : new HashMap<>();
        String crossLine = getCrossLine(url, requestBodyJson);

        log.info(message);
        log.info(crossLine);
        log.info("URL: {}", url);
        log.info("Request Params: {}", gson.writeValueAsString(params));
        log.info("Request Headers: {}", gson.writeValueAsString(getHeaderMapFromHttpHeaders(httpEntity.getHeaders())));
        log.info("Request Body: {}", requestBodyJson);
        log.info(crossLine);
    }

    @SneakyThrows
    public void  logApiRequest(String message, String url, String httpMethod, Object body, Map<String, String> headers, Map<String, Object> params){
        String requestBodyJson = Optional.ofNullable(body).isPresent() ? gson.writeValueAsString(body) : "{}";
        String crossLine = getCrossLine(url, requestBodyJson);

        log.info(message);
        log.info(crossLine);
        log.info("URL: {}", url);
        log.info("Request Body: {}", requestBodyJson);
        log.info("Request Params: {}", gson.writeValueAsString(params));
        log.info("Request Headers: {}", headers);
        log.info(crossLine);
    }

    @SneakyThrows
    public void logApiResponse(String message, ResponseEntity<?> responseEntity){
        String responseBodyJson = Optional.ofNullable(responseEntity.getBody()).isPresent() ? gson.writeValueAsString(responseEntity.getBody()) : "{}";
        String headerJson = json.toJson(getHeaderMapFromHttpHeaders(responseEntity.getHeaders()));
        String crossLine = getCrossLine(headerJson, responseBodyJson);

        log.info(message);
        log.info(crossLine);
        log.info("Http Status: {}", responseEntity.getStatusCodeValue() +  " " + responseEntity.getStatusCode().name());
        log.info("Response Cookies: {}", gson.writeValueAsString(new HashMap<>()));
        log.info("Response Headers: {}", headerJson);
        log.info("Response Body: {}", responseBodyJson);
        log.info("Response DateTime: {}", LocalDateTime.now());
        log.info(crossLine);
    }

    @SneakyThrows
    public void logApiResponse(String message, HttpResponse<?> httpResponse){
        String responseBodyJson = Optional.ofNullable(httpResponse.getBody()).isPresent() ? gson.writeValueAsString(httpResponse.getBody()) : "{}";
        String headerJson = Optional.ofNullable(httpResponse.getHeaders()).isPresent() ? json.toJson(httpResponse.getHeaders()) : json.toJson(new HashMap<>());
        String crossLine = getCrossLine(headerJson, responseBodyJson);

        log.info(message);
        log.info(crossLine);
        log.info("Http Status: {}", httpResponse.getStatus() +  " " + httpResponse.getStatusText());
        log.info("Response Cookies: {}", gson.writeValueAsString(httpResponse.getCookies()));
        log.info("Response Headers: {}", headerJson);
        log.info("Response Body: {}", responseBodyJson);
        log.info("Response DateTime: {}", LocalDateTime.now());
        log.info(crossLine);
    }

    @SneakyThrows
    private Map<String, String> getHeaderMapFromHttpHeaders(HttpHeaders headers){
        Map<String, String> headerMap = new HashMap<>();
        for(Map.Entry<String, List<String>> entry : headers.entrySet()){
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if(Optional.ofNullable(value).isPresent() && !value.isEmpty()){
                headerMap.put(key, gson.writeValueAsString(value));
            }
        }
        return headerMap;
    }

    private static int getMaxValueForLinePrint(String url, String bodyJson){
        return Math.max(url.length(), bodyJson.length()) + 20;
    }

    private static String increaseByTimes(String value, int times){
        return String.valueOf(value).repeat(Math.max(0, times));
    }

    private static String getCrossLine(String url, String bodyJson){
        int times = getMaxValueForLinePrint(url, bodyJson);
        return increaseByTimes("=", times);
    }
}
