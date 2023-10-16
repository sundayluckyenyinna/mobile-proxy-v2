package com.accionmfb.proxy.mobileproxyv2.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebRequestInfo
{
    private String url;
    private GenericPayload genericPayload;
    private Map<String, String> headers;
    private Map<String, Object> params;
}
