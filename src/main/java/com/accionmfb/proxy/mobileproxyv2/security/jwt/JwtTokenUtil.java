/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accionmfb.proxy.mobileproxyv2.security.jwt;

import com.accionmfb.proxy.mobileproxyv2.repository.MobileProxyRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringJoiner;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    @Value("${security.omnix.encryption.key}")
    private String omnixEncryptionKey;
    public static SecretKeySpec secretKey;
    private final Environment env;

    @Value("${security.jwt.signing.key}")
    private String jwtKey;

    private final MobileProxyRepository mobileProxyRepository;

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getChannelFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        return (String) claims.get("Channel");
    }

    public String getUserCredentialFromToken(String token) {
        String channel = getChannelFromToken(token);
        return env.getProperty("omnix.channel.user." + channel.toLowerCase(Locale.ENGLISH));
    }

    public String getIPFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        return (String) claims.get("IP");
    }

    public String getEncryptionKeyFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        String encryptionKey = (String) claims.get("auth");
        return this.decryptString(encryptionKey, omnixEncryptionKey);
    }

    public boolean userHasRole(String token, String role) {
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        String roles = (String) claims.get("roles").toString();
        return roles.contains(role);
    }

    public String decryptString(String textToDecrypt, String encryptionKey) {
        try {
            String secret = encryptionKey.trim();
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String decryptedResponse = new String(cipher.doFinal(java.util.Base64.getDecoder().decode(textToDecrypt.trim())));
            String[] splitString = decryptedResponse.split(":");
            StringJoiner rawString = new StringJoiner(":");
            for (String str : splitString) {
                rawString.add(str.trim());
            }
            return rawString.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public static void setKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception ex) {
            log.error("Exception occurred while trying to set key: {}", ex.getMessage());
            log.error("Exception localized message: {}", ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
