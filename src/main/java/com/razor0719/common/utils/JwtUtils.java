package com.razor0719.common.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author baoyl
 * @created 2018/6/29
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    private static final String ISSUER = "com.razor0719";

    /**
     * 生成jwt
     * @param audience
     * @param expireTime
     * @param notBeforeTime
     * @param secret
     * @param subject
     * @param claims
     * @param signatureAlgorithm
     * @return String
     */
    public static String createJwt(String audience,
                                   Date expireTime,
                                   Date notBeforeTime,
                                   String secret,
                                   String subject,
                                   Map<String, Object> claims,
                                   SignatureAlgorithm signatureAlgorithm) {
        Map<String, Object> header = Maps.newHashMap();
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm.getValue());
        JwtBuilder builder = Jwts.builder()
                .setAudience(audience)
                .setClaims(claims)
                .setExpiration(expireTime)
                .setHeader(header)
                .setIssuedAt(new Date())
                .setIssuer(ISSUER)
                .setNotBefore(notBeforeTime)
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .signWith(signatureAlgorithm, generalKey(secret, signatureAlgorithm));
        return builder.compact();
    }

    /**
     * 验证jwt
     *
     * @param token
     * @return Claims
     * @throws Exception
     */
    public static Claims verifyJwt(String token, String secret, SignatureAlgorithm signatureAlgorithm) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(generalKey(secret, signatureAlgorithm)).parseClaimsJws(token);
            return jws.getBody();
        } catch (JwtException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    private static Key generalKey(String secret, SignatureAlgorithm signatureAlgorithm) {
        return new SecretKeySpec(secret.getBytes(), signatureAlgorithm.getJcaName());
    }

}
