package com.accionmfb.proxy.mobileproxyv2.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> generateApiParameters(String channel, String username, String password);

    ResponseEntity<String> generateUserParams(String appChannel);
}
