package com.accionmfb.proxy.mobileproxyv2.web;

import com.accionmfb.proxy.mobileproxyv2.constant.ResponseCodes;
import com.accionmfb.proxy.mobileproxyv2.payload.OmnixResponsePayload;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OmnixWebClientImpl implements OmnixWebClient{

    private final Gson gson;
    private final static String OMNIX_API_MESSAGE = "OMNIX API REQUEST";
    private final static String OMNIX_API_MESSAGE_RESPONSE = "OMNIX API RESPONSE";

    @Override
    public String getForEntity(String url, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.get(url)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();
            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }

    @Override
    public String postForEntity(String url, Object body, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        String requestJson = (body instanceof String) ? (String) body : gson.toJson(body);

        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.post(url)
                    .body(requestJson)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();

            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }


    @Override
    public String putForEntity(String url, Object body, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        String requestJson = (body instanceof String) ? (String) body : gson.toJson(body);

        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.put(url)
                    .body(requestJson)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();
            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }


    @Override
    public String patchForEntity(String url, Object body, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        String requestJson = (body instanceof String) ? (String) body : gson.toJson(body);

        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.patch(url)
                    .body(requestJson)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();
            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }

    @Override
    public String headForEntity(String url, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.head(url)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();
            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }

    @Override
    public String deleteForEntity(String url, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.delete(url)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();
            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }

    @Override
    public String optionsForEntity(String url, Map<String, String> headers, Map<String, Object> params){
        String responseJson;
        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        HttpResponse<String> httpResponse;
        try{
            Unirest.config().verifySsl(false);
            httpResponse = Unirest.options(url)
                    .headers(Optional.ofNullable(headers).isPresent() ? headers : new HashMap<>())
                    .queryString(Optional.ofNullable(params).isPresent() ? params : new HashMap<>())
                    .asString();
            return getResponseJsonFromHttpResponse(httpResponse);
        }catch (Exception exception){
            exception.printStackTrace();
            omnixResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
            omnixResponsePayload.setResponseMessage("Proxy server error with cause: " + exception.getMessage());
            responseJson = gson.toJson(omnixResponsePayload);
        }
        return responseJson;
    }

    private String getResponseJsonFromHttpResponse(HttpResponse<String> httpResponse){
        OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
        if(httpResponse.isSuccess()){
            return httpResponse.getBody();
        }else if(is4xxBadRequest(httpResponse.getStatus()) || is5xxServerError(httpResponse.getStatus())){
            omnixResponsePayload.setResponseCode(ResponseCodes.THIRD_PARTY_FAILURE.getResponseCode());
            omnixResponsePayload.setResponseMessage(httpResponse.getBody());
            return gson.toJson(omnixResponsePayload);
        }else{
            omnixResponsePayload.setResponseCode(ResponseCodes.SERVICE_UNAVAILABLE.getResponseCode());
            omnixResponsePayload.setResponseMessage("Service unavailable: " + httpResponse.getBody());
            return gson.toJson(omnixResponsePayload);
        }
    }

    private boolean is4xxBadRequest(int status){
        return status >= 400 && status < 500;
    }

    private boolean is5xxServerError(int status){
        return status >= 500;
    }
}
