package uk.gov.companieshouse.web.payments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.gov.companieshouse.auth.filter.UserAuthFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;
import uk.gov.companieshouse.auth.filter.HijackFilter;

@EnableWebSecurity
public class WebSecurity {

    @Configuration
    @Order(1)
    public static class APIKeyPaymentsWebSecurityFilterConfig {
        @Bean
        public SecurityFilterChain apiKeySecurityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(requests -> requests
                    .requestMatchers(new AntPathRequestMatcher("/payments/*/pay/api-key")).permitAll()
                    .anyRequest().authenticated());
            return http.build();
        }
    }
}

@Configuration
@Order(2)
class PaymentsWebSecurityFilterConfig {
    @Bean
    public SecurityFilterChain paymentsSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/payments/*/pay")).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new UserAuthFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }
}
