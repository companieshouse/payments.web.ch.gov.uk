package uk.gov.companieshouse.web.payments;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.web.payments.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.web.payments.interceptor.UserDetailsInterceptor;

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

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {
            @Override
            public void customize(TomcatServletWebServerFactory factory) {
                TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) factory;
                tomcat.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
            }
        };
    }
}
