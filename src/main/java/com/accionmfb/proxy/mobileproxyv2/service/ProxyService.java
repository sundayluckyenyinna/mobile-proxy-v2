package com.accionmfb.proxy.mobileproxyv2.service;

import com.accionmfb.proxy.mobileproxyv2.payload.GenericPayload;
import com.accionmfb.proxy.mobileproxyv2.payload.WebRequestInfo;

public interface ProxyService {
    GenericPayload processPostRequest(WebRequestInfo webRequestInfo);

    GenericPayload processGetRequest(WebRequestInfo webRequestInfo);

    GenericPayload processPutRequest(WebRequestInfo webRequestInfo);

    GenericPayload processHeadRequest(WebRequestInfo webRequestInfo);

    GenericPayload processDeleteRequest(WebRequestInfo webRequestInfo);

    GenericPayload processOptionsRequest(WebRequestInfo webRequestInfo);

    String getOmnixToken(String username, String password);
}
