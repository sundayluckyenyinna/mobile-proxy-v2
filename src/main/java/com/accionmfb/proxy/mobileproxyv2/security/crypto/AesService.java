
package com.accionmfb.proxy.mobileproxyv2.security.crypto;

import com.accionmfb.proxy.mobileproxyv2.payload.GenericPayload;
import com.accionmfb.proxy.mobileproxyv2.payload.ValidationPayload;

public interface AesService {
    
    public String encryptString(String textToEncrypt, String encryptionKey);
    public String decryptString(String textToDecrypt, String encryptionKey);
    
     public String encryptFlutterString(String strToEncrypt, String secret) ;

    String doEncryption(String strToEncrypt);
    String doDecryption(String strToDecrypt);

    public String decryptFlutterString(final String textToDecrypt, final String encryptionKey);


    ValidationPayload validateRequest(GenericPayload genericRequestPayload);
}
