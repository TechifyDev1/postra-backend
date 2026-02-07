package com.qudus.postra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import com.qudus.postra.filters.JwtFilter;
import com.qudus.postra.security.CustomUserDetailService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailService;

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            // Disable CSRF
            Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new Customizer<CsrfConfigurer<HttpSecurity>>() {
                @Override
                public void customize(CsrfConfigurer<HttpSecurity> t) {
                    t.disable();
                }
            };

            // Enable CORS using our custom source
            Customizer<CorsConfigurer<HttpSecurity>> custCors = new Customizer<CorsConfigurer<HttpSecurity>>() {
                @Override
                public void customize(CorsConfigurer<HttpSecurity> t) {
                    t.configurationSource(corsConfigurationSource()).and();
                }
            };

            // Authorize routes
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> custReqConf =
                    new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                        @Override
                        public void customize(
                                AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry t) {
                            t
                                .requestMatchers("/api/users/login", "/api/users/register", "/error", "/api/users/profile/**", "/api/upload", "/api/posts/", "/api/posts/**", "/api/comments/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/users/update/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/posts/update/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                                .anyRequest().authenticated();
                        }
                    };

            // Stateless session
            Customizer<SessionManagementConfigurer<HttpSecurity>> custSession =
                    new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
                        @Override
                        public void customize(SessionManagementConfigurer<HttpSecurity> t) {
                            t.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                        }
                    };

            return http
                    .csrf(custCsrf)
                    .cors(custCors) // 👈 very important
                    .authorizeHttpRequests(custReqConf)
                    .sessionManagement(custSession)
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();

        } catch (Exception e) {
            System.out.println("Unable to configure security");
            throw new Exception("unable to configure security " + e);
        }
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        try {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailService);
            provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
            return provider;
        } catch (Exception e) {
            System.out.println("authentication provider");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            System.out.println("auth manager " + e.getMessage());
            return null;
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://postra-frontend.vercel.app"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
