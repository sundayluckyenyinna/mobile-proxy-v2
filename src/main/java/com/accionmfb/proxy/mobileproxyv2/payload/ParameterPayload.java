package com.accionmfb.proxy.mobileproxyv2.payload;

import lombok.Data;

@Data
public class ParameterPayload
{
   private String accessToken;
   private String endPoint;
   private String privateKey;
   private String publicKey;
   private String encryptionKey;

}
