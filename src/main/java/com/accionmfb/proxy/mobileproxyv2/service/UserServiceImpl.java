package com.accionmfb.proxy.mobileproxyv2.service;

import com.accionmfb.proxy.mobileproxyv2.constant.ResponseCodes;
import com.accionmfb.proxy.mobileproxyv2.model.AppUser;
import com.accionmfb.proxy.mobileproxyv2.payload.OmnixResponsePayload;
import com.accionmfb.proxy.mobileproxyv2.payload.ParameterPayload;
import com.accionmfb.proxy.mobileproxyv2.payload.UserParamsPayload;
import com.accionmfb.proxy.mobileproxyv2.repository.MobileProxyRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Value("${security.aes.encryption.key}")
    private String aesEncryptionKey;

    @Value("${security.proxy.url}")
    private String proxyUrl;

    private final Gson gson;
    private final MobileProxyRepository mobileProxyRepository;
    private final ProxyService proxyService;


    @Override
    public ResponseEntity<String> generateUserParams(String appChannel){
        String channel = appChannel.toUpperCase();
        AppUser appUser = mobileProxyRepository.getAppUserUsingChannel(channel);
        OmnixResponsePayload errorResponse = new OmnixResponsePayload();

        // Validate App User existence
        if(Optional.ofNullable(appUser).isEmpty()){
            errorResponse.setResponseCode(ResponseCodes.INVALID_CREDENTIALS.getResponseCode());
            errorResponse.setResponseMessage("Invalid channel");
            return new ResponseEntity<>(gson.toJson(errorResponse), HttpStatus.UNAUTHORIZED);
        }

        String encodedUsername = Base64.getEncoder().encodeToString(appUser.getUsername().getBytes(StandardCharsets.UTF_8));
        String encodedPassword = Base64.getEncoder().encodeToString(appUser.getPassword().getBytes(StandardCharsets.UTF_8));

        UserParamsPayload paramsResponsePayload = new UserParamsPayload();
        paramsResponsePayload.setUsername(encodedUsername);
        paramsResponsePayload.setPassword(encodedPassword);

        return ResponseEntity.ok(gson.toJson(paramsResponsePayload));
    }

    @Override
    public ResponseEntity<String> generateApiParameters(String channel, String username, String password){
        AppUser appUser = mobileProxyRepository.getAppUserUsingChannel(channel);
        OmnixResponsePayload errorResponse = new OmnixResponsePayload();

        // Validate App User existence
        if(Optional.ofNullable(appUser).isEmpty()){
            errorResponse.setResponseCode(ResponseCodes.INVALID_CREDENTIALS.getResponseCode());
            errorResponse.setResponseMessage("Invalid channel");
            return new ResponseEntity<>(gson.toJson(errorResponse), HttpStatus.UNAUTHORIZED);
        }

        // Check for the match between channel and username.
        String appUsername = appUser.getUsername();
        if(!appUsername.equalsIgnoreCase(username)){
            errorResponse.setResponseCode(ResponseCodes.INVALID_CREDENTIALS.getResponseCode());
            errorResponse.setResponseMessage("Application user channel and username mismatch!");
            return new ResponseEntity<>(gson.toJson(errorResponse), HttpStatus.FORBIDDEN);
        }

        ParameterPayload payload = new ParameterPayload();
        String encodedKey = Base64.getEncoder().encodeToString(aesEncryptionKey.getBytes(StandardCharsets.UTF_8));
        String encodedEndpoint = Base64.getEncoder().encodeToString(proxyUrl.getBytes(StandardCharsets.UTF_8));

        payload.setAccessToken(proxyService.getOmnixToken(username, password));
        payload.setEncryptionKey(encodedKey);
        payload.setEndPoint(encodedEndpoint);
        return ResponseEntity.ok(gson.toJson(payload));
    }

}
