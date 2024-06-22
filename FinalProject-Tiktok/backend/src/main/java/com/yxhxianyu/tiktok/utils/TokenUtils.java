package com.yxhxianyu.tiktok.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final String KEY = "JHZ_is_MY#dog#hahahha";

    public static Result<String> tokenEncoder(String username, String password) {
        Map<String, Object> headers = new HashMap<>();
        JWTCreator.Builder jwtBuilder = JWT.create();
        headers.put("typ", "jwt");
        headers.put("alg", "HS256");

        String token = jwtBuilder.withHeader(headers)
                .withClaim("username", username)
                .withClaim("password", password)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(KEY));

        return new Result<>(null, token);
    }

    public static Result<String> tokenDecoder(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(KEY)).build();
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getClaim("username").asString();
            return new Result<>(null, username);
        } catch (TokenExpiredException exception) {
            // Invalid token
            return new Result<>("Token has expired", null);
        } catch (Exception e) {
            return new Result<>("Token is invalid", null);
        }

    }
}
