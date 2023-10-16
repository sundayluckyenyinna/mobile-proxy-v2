package com.accionmfb.proxy.mobileproxyv2.controller;

import com.accionmfb.proxy.mobileproxyv2.payload.UserParamsPayload;
import com.accionmfb.proxy.mobileproxyv2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/generate/{channel}/")
public class UserRestController
{

    private final UserService userService;

    @GetMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateUserParameters(@PathVariable("channel") String channel){
        return userService.generateUserParams(channel);
    }

    @PostMapping(value = "/parameters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateAppParameters(@Valid @RequestBody UserParamsPayload payload, @PathVariable("channel") String channel){
        return userService.generateApiParameters(channel, payload.getUsername(), payload.getPassword());
    }

}
