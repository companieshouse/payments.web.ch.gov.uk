package uk.gov.companieshouse.web.payments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.gov.companieshouse.auth.filter.UserAuthFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;
import uk.gov.companieshouse.auth.filter.HijackFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurity {
    // NOTE: These configurations should not be modified without thorough testing of all scenarios. These configurations
    // are set up to allow no authentication for api key users, so beware of modifying.
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
    @Order(1)
    public static class APIKeyPaymentsWebSecurityFilterConfig {

        @Bean
        protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(requests -> requests
                            .requestMatchers(new AntPathRequestMatcher("/payments/*/pay/api-key")).permitAll()
                            .anyRequest().authenticated());
            return http.build();
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
    @Order(2)
    public static class PaymentsWebSecurityFilterConfig {
        @Bean
        protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(requests -> requests
                    .requestMatchers(new AntPathRequestMatcher("/payments/*/pay")).permitAll()
                    .anyRequest().authenticated())
                    .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new UserAuthFilter(), BasicAuthenticationFilter.class);
            return http.build();
        }
    }

}
