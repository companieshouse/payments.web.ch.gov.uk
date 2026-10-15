package uk.gov.companieshouse.web.payments.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.gov.companieshouse.web.payments.model.PaymentMethodChoice;

import java.util.Locale;
import java.util.Set;

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
        assertEquals(Set.of("locales/messages", "locales/common-messages"), resourceBundleMessageSource.getBasenameSet(), "Basename should include the local and shared message files");    }

    @Test
    @DisplayName("MessageSource should resolve English messages")
    void testMessageSourceResolvesEnglishMessages() {
        String message = messageSource.getMessage("label.lang.en", null, Locale.ENGLISH);
        assertEquals("English", message);
    }

    @Test
    @DisplayName("MessageSource should resolve Welsh messages")
    void testMessageSourceResolvesWelshMessages() {
        String message = messageSource.getMessage("label.lang.cy", null, Locale.forLanguageTag("cy"));
        assertEquals("Cymraeg", message);
    }

    @Test
    @DisplayName("MessageSource should resolve local payments messages")
    void testMessageSourceResolvesLocalMessages() {
        String message = messageSource.getMessage("payment.summary.heading", null, Locale.ENGLISH);
        assertEquals("Review your payment", message);
    }

    @Test
    @DisplayName("MessageSource should resolve shared common-web-java footer messages")
    void testMessageSourceResolvesSharedFooterMessages() {
        String message = messageSource.getMessage("label.footer.cookies", null, Locale.ENGLISH);
        assertEquals("Cookies", message);
    }

    @Test
    @DisplayName("Missing payment method resolves validation message")
    void testMissingPaymentMethodResolvesValidationMessage() {
        try (var validator = new LocalValidatorFactoryBean()) {
            validator.setValidationMessageSource(messageSource);
            validator.afterPropertiesSet();

            var violations = validator.validate(new PaymentMethodChoice());

            assertEquals(1, violations.size());
            var violation = violations.iterator().next();
            assertEquals("selectedPaymentMethod", violation.getPropertyPath().toString());
            assertEquals("Please choose a payment method", violation.getMessage());
        }
    }
}