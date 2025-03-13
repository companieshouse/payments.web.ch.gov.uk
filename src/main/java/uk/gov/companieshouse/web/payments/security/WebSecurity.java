package uk.gov.companieshouse.web.payments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uk.gov.companieshouse.auth.filter.UserAuthFilter;
import uk.gov.companieshouse.csrf.config.ChsCsrfMitigationHttpSecurityBuilder;
import uk.gov.companieshouse.auth.filter.HijackFilter;

@EnableWebSecurity
@Configuration
public class WebSecurity {
    @Bean
    @Order(1)
    public SecurityFilterChain apiKeySecurityFilterChain(HttpSecurity http) throws Exception {
        return ChsCsrfMitigationHttpSecurityBuilder.configureApiCsrfMitigations(
                        http.securityMatcher("/payments/*/pay/api-key"))
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webKeySecurityFilterChain(HttpSecurity http) throws Exception {
        return ChsCsrfMitigationHttpSecurityBuilder.configureWebCsrfMitigations(
                        http.securityMatcher("/**")
                                .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                                .addFilterBefore(new UserAuthFilter(), BasicAuthenticationFilter.class))
                .build();
    }
}
