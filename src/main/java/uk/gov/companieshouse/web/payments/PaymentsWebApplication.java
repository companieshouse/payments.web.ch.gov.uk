package uk.gov.companieshouse.web.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.web.payments.interceptor.UserDetailsInterceptor;
import uk.gov.companieshouse.web.payments.interceptor.LoggingInterceptor;


@SpringBootApplication
public class PaymentsWebApplication implements WebMvcConfigurer {

    public static final String APPLICATION_NAME_SPACE = "payments.web.ch.gov.uk";

    private UserDetailsInterceptor userDetailsInterceptor;
    private LoggingInterceptor loggingInterceptor;


    @Autowired
    public PaymentsWebApplication(UserDetailsInterceptor userDetailsInterceptor,
                                    LoggingInterceptor loggingInterceptor) {
        this.userDetailsInterceptor = userDetailsInterceptor;
        this.loggingInterceptor = loggingInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentsWebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
        registry.addInterceptor(userDetailsInterceptor);

    }
}
