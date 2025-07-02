package com.qudus.postra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.qudus.postra.security.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new Customizer<CsrfConfigurer<HttpSecurity>>() {
                @Override
                public void customize(CsrfConfigurer<HttpSecurity> t) {
                    t.disable();
                }
            };
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> custReqConf = new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                @Override
                public void customize(
                        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry t) {
                    t.anyRequest().permitAll();
                    // requestMatchers("/api/users/login", "/api/users/register").permitAll().anyRequest()
                    //         .authenticated();
                }
            };
            Customizer<SessionManagementConfigurer<HttpSecurity>> custSeason = new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
                @Override
                public void customize(SessionManagementConfigurer<HttpSecurity> t) {
                    t.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }
            };

            return http.csrf(custCsrf).authorizeHttpRequests(custReqConf).httpBasic(Customizer.withDefaults())
                    .sessionManagement(custSeason).authenticationProvider(authenticationProvider()).build();
        } catch (Exception e) {
            System.out.println("Unable to configure security");
            throw new Exception("unable to configure security " + e);
        }
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}