package uk.gov.companieshouse.web.payments.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MessageConfigTest {

    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MessageConfig messageConfig = new MessageConfig();
        this.messageSource = messageConfig.messageSource();
    }

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

    @Test
    @DisplayName("MessageSource should resolve English messages")
    void testMessageSourceResolvesEnglishMessages() {
        String message = messageSource.getMessage("label.lang.en", null, Locale.ENGLISH);
        assertEquals("English", message);
    }

    @Test
    @DisplayName("MessageSource should resolve Welsh messages")
    void testMessageSourceResolvesWelshMessages() {
        String message = messageSource.getMessage("label.lang.cy", null, new Locale("cy"));
        assertEquals("Cymraeg", message);
    }
}