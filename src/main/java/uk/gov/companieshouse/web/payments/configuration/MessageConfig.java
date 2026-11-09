package uk.gov.companieshouse.web.payments.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    @Bean("messageSource")
    public MessageSource messageSource() {
        var messageSource = new ResourceBundleMessageSource();

        // This will match the resources folder files that begin with 'messages'
        // e.g. messages_wl.properties, messages_fr.properties
        messageSource.setBasenames("locales/common-messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
