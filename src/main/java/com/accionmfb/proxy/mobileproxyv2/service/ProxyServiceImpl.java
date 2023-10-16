package com.accionmfb.proxy.mobileproxyv2.service;

import com.accionmfb.proxy.mobileproxyv2.payload.GenericPayload;
import com.accionmfb.proxy.mobileproxyv2.payload.UserParamsPayload;
import com.accionmfb.proxy.mobileproxyv2.payload.WebRequestInfo;
import com.accionmfb.proxy.mobileproxyv2.web.OmnixWebClient;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyServiceImpl implements ProxyService{

    private static final String PROXY_PREFIX = "/proxy";
    private static final String PROXY_PREFIX_END_WITH_SLASH = "/proxy/";


    @Value("${security.omnix.base-url}")
    private String omnixBaseUrl;

    private final OmnixWebClient omnixWebClient;
    private final Gson gson;

    @Override
    public GenericPayload processPostRequest(WebRequestInfo webRequestInfo){
        GenericPayload genericPayload = new GenericPayload();
        String responseJson = omnixWebClient.postForEntity(
                resolveOmnixResourceUrl(webRequestInfo.getUrl()),
                webRequestInfo.getGenericPayload(),
                webRequestInfo.getHeaders(),
                webRequestInfo.getParams()
        );
        genericPayload.setResponse(responseJson);
        return genericPayload;
    }

    @Override
    public GenericPayload processGetRequest(WebRequestInfo webRequestInfo){
        GenericPayload genericPayload = new GenericPayload();
        String responseJson = omnixWebClient.getForEntity(
                resolveOmnixResourceUrl(webRequestInfo.getUrl()),
                webRequestInfo.getHeaders(),
                webRequestInfo.getParams()
        );
        genericPayload.setResponse(responseJson);
        return genericPayload;
    }

    @Override
    public GenericPayload processPutRequest(WebRequestInfo webRequestInfo){
        GenericPayload genericPayload = new GenericPayload();
        String responseJson = omnixWebClient.putForEntity(
                resolveOmnixResourceUrl(webRequestInfo.getUrl()),
                webRequestInfo.getGenericPayload(),
                webRequestInfo.getHeaders(),
                webRequestInfo.getParams()
        );
        genericPayload.setResponse(responseJson);
        return genericPayload;
    }

    @Override
    public GenericPayload processHeadRequest(WebRequestInfo webRequestInfo){
        GenericPayload genericPayload = new GenericPayload();
        String responseJson = omnixWebClient.headForEntity(
                resolveOmnixResourceUrl(webRequestInfo.getUrl()),
                webRequestInfo.getHeaders(),
                webRequestInfo.getParams()
        );
        genericPayload.setResponse(responseJson);
        return genericPayload;
    }

    @Override
    public GenericPayload processDeleteRequest(WebRequestInfo webRequestInfo){
        GenericPayload genericPayload = new GenericPayload();
        String responseJson = omnixWebClient.deleteForEntity(
                resolveOmnixResourceUrl(webRequestInfo.getUrl()),
                webRequestInfo.getHeaders(),
                webRequestInfo.getParams()
        );
        genericPayload.setResponse(responseJson);
        return genericPayload;
    }

    @Override
    public GenericPayload processOptionsRequest(WebRequestInfo webRequestInfo){
        GenericPayload genericPayload = new GenericPayload();
        String responseJson = omnixWebClient.getForEntity(
                resolveOmnixResourceUrl(webRequestInfo.getUrl()),
                webRequestInfo.getHeaders(),
                webRequestInfo.getParams()
        );
        genericPayload.setResponse(responseJson);
        return genericPayload;
    }

    private String resolveOmnixResourceUrl(String url){
        String startingUrl;
        if(url.startsWith(PROXY_PREFIX)){
            startingUrl = url;
        }else {
            startingUrl = url.substring(url.indexOf(PROXY_PREFIX));
        }
        String urlWithoutProxy = startingUrl.replace(PROXY_PREFIX, "");
        List<String> urlTokens = Arrays.stream(urlWithoutProxy.split("/")).filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
        if(urlTokens.size() >= 2){
            urlTokens.add(1, PROXY_PREFIX_END_WITH_SLASH);
        }else{
            urlTokens.add(PROXY_PREFIX);
        }
        String joined = String.join("/", urlTokens);
        joined = joined.startsWith("/") ? joined : "/".concat(joined);
        joined = joined.replaceAll("//", "/");
        log.info("Resolved Omnix Resource URL: {}", joined);
        return  omnixBaseUrl.concat(joined);
    }

    @Override
    public String getOmnixToken(String username, String password){
        String url = omnixBaseUrl.concat("/token/generate-token");
        UserParamsPayload payload = new UserParamsPayload();
        payload.setUsername(username);
        payload.setPassword(password);

        Unirest.config().verifySsl(false);
        HttpResponse<String> httpResponse = Unirest.post(url)
                .body(payload)
                .header("Content-Type", "application/json")
                .asString();

        return httpResponse.getBody();
    }
}
