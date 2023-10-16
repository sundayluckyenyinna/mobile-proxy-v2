package com.accionmfb.proxy.mobileproxyv2.controller;

import com.accionmfb.proxy.mobileproxyv2.payload.GenericPayload;
import com.accionmfb.proxy.mobileproxyv2.security.crypto.AesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/f/app")
public class EncryptionController
{
    private final AesService aesService;

    @PostMapping(value = "/encrypt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericPayload> encryptRequest(@Valid @RequestBody String object){
        GenericPayload payload = new GenericPayload();
        log.info("Request to encrypt: {}", object);
        String encRequest = aesService.doEncryption(object);
        log.info("Encrypted Request: {}", encRequest);
        payload.setRequest(encRequest);
        return ResponseEntity.ok(payload);
    }

    @PostMapping(value = "/decrypt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> decryptResponse(@Valid @RequestBody GenericPayload genericPayload){
        String responseString;
        if(genericPayload.getResponse() != null){
            responseString = genericPayload.getResponse();
        }else{
            responseString = genericPayload.getRequest();
        }
        if(Optional.ofNullable(responseString).isEmpty()){
            responseString = "Invalid request. Request and response fields cannot be both null!";
            return ResponseEntity.badRequest().body(responseString);
        }
        log.info("Response to Decrypt: {}", responseString);
        String decResponse = aesService.doDecryption(responseString);
        log.info("Decrypted Response: {}", decResponse);
        return ResponseEntity.ok(decResponse);
    }
}
