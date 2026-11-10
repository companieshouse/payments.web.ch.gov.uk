package uk.gov.companieshouse.web.payments.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MessageConfigTest {

    @Test
    void testMessageSourceBeanConfiguration() {
        // Arrange
        MessageConfig messageConfig = new MessageConfig();

        // Act
        MessageSource messageSource = messageConfig.messageSource();

        // Assert
        assertInstanceOf(ResourceBundleMessageSource.class, messageSource, "MessageSource should be an instance of ResourceBundleMessageSource");
        ResourceBundleMessageSource resourceBundleMessageSource = (ResourceBundleMessageSource) messageSource;
        assertEquals("locales/common-messages", resourceBundleMessageSource.getBasenameSet().iterator().next(), "Basename should be 'locales/common-messages'");
    }
}