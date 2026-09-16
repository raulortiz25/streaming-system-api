package com.Streaming.StreamingSystem.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Value(("${jwt.issuer}"))
    private String issuer;

    private final Algorithm algorithm;

    public JwtUtils(@Value("${jwt.key.private}") String privateKey){
        this.algorithm = Algorithm.HMAC256(privateKey);
    }

    public String createToken(Authentication authentication){

       String userName = authentication.getName();

        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.issuer)
                .withSubject(userName)
                .withClaim("role", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(this.algorithm);

        return jwtToken;
    }

    public DecodedJWT validateToken (String token){
        try{
            JWTVerifier verifier = JWT.require(this.algorithm)
                    .withIssuer(this.issuer)
                    .build();

            return verifier.verify(token);

        } catch (JWTVerificationException exception) {
            throw new BadCredentialsException("Token invalido o expirado");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim (DecodedJWT decodedJWT, String calimName){
        return decodedJWT.getClaim(calimName);
    }

    public Map<String, Claim>returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
