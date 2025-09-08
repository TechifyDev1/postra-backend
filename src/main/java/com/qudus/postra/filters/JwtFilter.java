package com.qudus.postra.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.qudus.postra.security.CustomUserDetailService;
import com.qudus.postra.service.JwtService;

import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String usernameOrEmail = null;

        // Try from Authorization Header
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Token extracted from Authorization header: " + token);
        }

        // Second: Try from Cookie (if token still null)
        System.out.println(request.getCookies());
        System.out.println(request.toString());
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Token extracted from cookie: " + token);
                    break;
                }
            }
        }

        if (token == null) {
            System.out.println("No token found in header or cookie");
        }

        // Extract and validate token
        if (token != null) {
            try {
                usernameOrEmail = jwtService.extractUsernameOrEmail(token);
                System.out.println("Username/Email from token: " + usernameOrEmail);
            } catch (Exception e) {
                System.out.println("Failed to extract username/email from token: " + e.getMessage());
            }
        }

        //  Set authentication in security context
        if (usernameOrEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = context.getBean(CustomUserDetailService.class)
                        .loadUserByUsername(usernameOrEmail);
                System.out.println("Loaded user details for: " + userDetails.getUsername());

                if (jwtService.validateToken(token, userDetails)) {
                    System.out.println("Token validated successfully");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set in context");
                } else {
                    System.out.println("Token validation failed");
                }

            } catch (Exception e) {
                System.out.println("Error during user loading or token validation: " + e.getMessage());
            }
        }

        // Debug
        System.out.println("Final Principal: " + SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);
    }
}
