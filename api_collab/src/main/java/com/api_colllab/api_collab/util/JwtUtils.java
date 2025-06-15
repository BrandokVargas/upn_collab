package com.api_colllab.api_collab.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${security.jwt.key.private}")
    private String privateKey;
    @Value("${security.jwt.key.generator}")
    private String userGenerator;

    private static final long ACCESS_TOKEN_VALIDITY = 1800000;
    private static final long REFRESH_TOKEN_VALIDITY = 86400000;

    public String createToken(Authentication authentication, boolean isRefreshToken){
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        String userEmail = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + (isRefreshToken ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY));


        JWTCreator.Builder tokenBuilder = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(userEmail)
                .withClaim("authorities",authorities)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()));

        if (!isRefreshToken) {
            tokenBuilder.withClaim("authorities", authorities);
        }

        String token = tokenBuilder.sign(algorithm);
        return token;
    }


    public DecodedJWT validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalido, no autorizado");
        }
    }


    public String extractUserEmail(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT,String claimName){
        return decodedJWT.getClaim(claimName);
    }

    public Map<String,Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }


}
