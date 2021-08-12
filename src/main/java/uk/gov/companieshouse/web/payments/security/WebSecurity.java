package uk.gov.companieshouse.web.payments.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uk.gov.companieshouse.auth.filter.UserAuthFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;
import uk.gov.companieshouse.auth.filter.HijackFilter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class APIKeyPaymentsWebSecurityFilterConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("in api key");
            System.out.println(http);
            http.antMatcher("/payments/*/pay/legacy").addFilterBefore(new AuthTypeFilter(), BasicAuthenticationFilter.class);
        }
    }

    @Configuration
    @Order(2)
    public static class PaymentsWebSecurityFilterConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("HTTP TO FOLLOW");
            System.out.println(http);
            //http.ignoring().antMatchers("/authFailure");
            http.antMatcher("/payments/*/pay")
//                    .addFilterBefore(new AuthTypeFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new UserAuthFilter(), BasicAuthenticationFilter.class);
        }
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers("/authFailure");
//        }
    }

}
