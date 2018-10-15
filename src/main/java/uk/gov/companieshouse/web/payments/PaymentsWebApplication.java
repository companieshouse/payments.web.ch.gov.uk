package uk.gov.companieshouse.web.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.web.payments.interceptor.UserDetailsInterceptor;

@SpringBootApplication
public class PaymentsWebApplication implements WebMvcConfigurer {

    private UserDetailsInterceptor userDetailsInterceptor;

    @Autowired
    public PaymentsWebApplication(UserDetailsInterceptor userDetailsInterceptor) {
        this.userDetailsInterceptor = userDetailsInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentsWebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userDetailsInterceptor);
    }
}
