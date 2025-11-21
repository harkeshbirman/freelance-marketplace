package com.marketplace.freelancer.jwt;

import com.marketplace.freelancer.dto.AuthPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final String authServiceURL;
    private final RestTemplate restTemplate;

    public JwtFilter(@Value("${service.auth.baseURL}") String authServiceURL, RestTemplateBuilder builder) {
        this.authServiceURL = authServiceURL;
        this.restTemplate = builder.build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws java.io.IOException, jakarta.servlet.ServletException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            Map<String, String> stringStringMap = verifyToken(token);

            if (stringStringMap != null && stringStringMap.containsKey("userId")
                    && stringStringMap.containsKey("role")) {

                Long userId = Long.parseLong(stringStringMap.get("userId"));
                String role = stringStringMap.get("role");

                AuthPrincipal authPrincipal = AuthPrincipal.builder()
                        .userId(userId)
                        .token(token)
                        .build();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        authPrincipal, null, List.of(new SimpleGrantedAuthority(role))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }

        }

        filterChain.doFilter(request, response);
    }

    private Map<String, String> verifyToken(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = Map.of("token", token);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                authServiceURL + "/validate/token",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

}