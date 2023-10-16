package com.accionmfb.proxy.mobileproxyv2.web;

import java.util.Map;

public interface OmnixWebClient {
    String getForEntity(String url, Map<String, String> headers, Map<String, Object> params);

    String postForEntity(String url, Object body, Map<String, String> headers, Map<String, Object> params);

    String putForEntity(String url, Object body, Map<String, String> headers, Map<String, Object> params);

    String patchForEntity(String url, Object body, Map<String, String> headers, Map<String, Object> params);

    String headForEntity(String url, Map<String, String> headers, Map<String, Object> params);

    String deleteForEntity(String url, Map<String, String> headers, Map<String, Object> params);

    String optionsForEntity(String url, Map<String, String> headers, Map<String, Object> params);
}
