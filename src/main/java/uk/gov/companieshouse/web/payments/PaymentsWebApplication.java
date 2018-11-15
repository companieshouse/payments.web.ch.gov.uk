package uk.gov.companieshouse.web.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.web.payments.interceptor.PaymentInterceptor;
import uk.gov.companieshouse.web.payments.interceptor.UserDetailsInterceptor;

@SpringBootApplication
public class PaymentsWebApplication implements WebMvcConfigurer {

    public static final String APPLICATION_NAME_SPACE = "payments.web.ch.gov.uk";

    private UserDetailsInterceptor userDetailsInterceptor;
    private PaymentInterceptor paymentInterceptor;

    @Autowired
    public PaymentsWebApplication(UserDetailsInterceptor userDetailsInterceptor, PaymentInterceptor
            paymentInterceptor) {
        this.userDetailsInterceptor = userDetailsInterceptor;
        this.paymentInterceptor = paymentInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentsWebApplication.class, args);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(userDetailsInterceptor);
        registry.addInterceptor(paymentInterceptor).addPathPatterns("/payments/{id}**",
                "/payments/{id}/**");
    }
}
