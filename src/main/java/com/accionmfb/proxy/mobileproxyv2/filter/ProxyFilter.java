package com.accionmfb.proxy.mobileproxyv2.filter;

import com.accionmfb.proxy.mobileproxyv2.constant.ResponseCodes;
import com.accionmfb.proxy.mobileproxyv2.payload.GenericPayload;
import com.accionmfb.proxy.mobileproxyv2.payload.OmnixResponsePayload;
import com.accionmfb.proxy.mobileproxyv2.payload.WebRequestInfo;
import com.accionmfb.proxy.mobileproxyv2.service.ProxyService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProxyFilter extends OncePerRequestFilter
{
    private final ProxyService proxyService;
    private final Gson gson;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();

        if(!url.startsWith("/generate") && !url.startsWith("/f/app")) {
            Map<String, String> headers = getAllHeadersFromHttpRequest(httpServletRequest);
            Map<String, Object> params = getParametersFromHttpRequest(httpServletRequest);
            String requestMethod = httpServletRequest.getMethod().toUpperCase();

            GenericPayload responsePayload;
            WebRequestInfo webRequestInfo = WebRequestInfo.builder()
                    .url(url)
                    .params(params)
                    .headers(headers)
                    .build();

            OmnixResponsePayload omnixResponsePayload = new OmnixResponsePayload();
            omnixResponsePayload.setResponseCode(ResponseCodes.FAILED_MODEL.getResponseCode());
            omnixResponsePayload.setResponseMessage("Unsupported request method!");
            responsePayload = new GenericPayload();
            responsePayload.setResponse(gson.toJson(omnixResponsePayload));

            List<String> methodWithoutBody = Arrays.asList(GET.name(), HEAD.name(), DELETE.name(), OPTIONS.name());
            List<String> methodWithBody = Arrays.asList(POST.name(), PUT.name());

            GenericPayload requestPayload = null;
            if (methodWithoutBody.contains(requestMethod)) {
                webRequestInfo.setGenericPayload(null);
                switch (requestMethod) {
                    case "GET": {
                        responsePayload = proxyService.processGetRequest(webRequestInfo);
                        break;
                    }
                    case "HEAD": {
                        responsePayload = proxyService.processHeadRequest(webRequestInfo);
                        break;
                    }
                    case "OPTIONS": {
                        responsePayload = proxyService.processOptionsRequest(webRequestInfo);
                        break;
                    }
                    case "DELETE": {
                        responsePayload = proxyService.processDeleteRequest(webRequestInfo);
                        break;
                    }
                }
            } else if (methodWithBody.contains(requestMethod)) {
                BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
                String requestJson = bufferedRequest.getRequestBody();
                requestPayload = gson.fromJson(requestJson, GenericPayload.class);

                webRequestInfo.setGenericPayload(requestPayload);
                switch (requestMethod) {
                    case "POST": {
                        responsePayload = proxyService.processPostRequest(webRequestInfo);
                        break;
                    }
                    case "PUT": {
                        responsePayload = proxyService.processPutRequest(webRequestInfo);
                        break;
                    }
                }
            }

            log.info("WEB-REQUEST-INFO: {}", gson.toJson(webRequestInfo));
            log.info("REQUEST: {}", Optional.ofNullable(requestPayload).isPresent() ? gson.toJson(requestPayload) : "");
            log.info("RESPONSE: {}", gson.toJson(responsePayload));

            String responseJson = gson.toJson(responsePayload);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.getWriter().write(responseJson);
        }
        else{
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private Map<String, String> getAllHeadersFromHttpRequest(HttpServletRequest servletRequest){
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            if(!headerName.equalsIgnoreCase("Content-Length")) {
                headers.put(headerName, servletRequest.getHeader(headerName));
            }
        }
        return headers;
    }

    private Map<String, Object> getParametersFromHttpRequest(HttpServletRequest servletRequest){
        Map<String, Object> queryMap = new HashMap<>();
        String queryString = servletRequest.getQueryString();
        if(Optional.ofNullable(queryString).isPresent() && !queryString.trim().isEmpty()) {
            String[] groups = queryString.trim().replace("?", "").split("&");
            for (String group : groups) {
                String[] keyValue = group.split("=");
                queryMap.put(keyValue[0], keyValue[1]);
            }
        }
        return queryMap;
    }

}
