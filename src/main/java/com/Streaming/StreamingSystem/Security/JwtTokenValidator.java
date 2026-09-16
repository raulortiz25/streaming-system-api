package com.Streaming.StreamingSystem.Security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private ObjectMapper objectMapper;

    public JwtTokenValidator(JwtUtils jwtUtils, ObjectMapper objectMapper){
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken != null && jwtToken.startsWith("Bearer ")){
            jwtToken = jwtToken.substring(7);

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

                String username = jwtUtils.extractUsername(decodedJWT);
                String role = jwtUtils.getSpecificClaim(decodedJWT, "role").asString();

                List<GrantedAuthority>roleList = AuthorityUtils.commaSeparatedStringToAuthorityList(role);

                Authentication authentication = new UsernamePasswordAuthenticationToken(username,
                        null, roleList);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JWTVerificationException exception) {

            }
        }
        filterChain.doFilter(request, response);

    }
}
