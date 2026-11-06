package uk.gov.companieshouse.web.payments.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.LocaleResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalControllerTest {

    private static final String PAYMENT_URI = "/payments/paymentId";

    @Mock
    private LocaleResolver localeResolver;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalController controller;

    @Test
    void shouldReturnCurrentRequestURI() {
        when(request.getRequestURI()).thenReturn(PAYMENT_URI);

        assertEquals(PAYMENT_URI, controller.getRequestURI(request));

        verify(request).getRequestURI();
    }

    @Test
    void shouldReturnAPIKeyRequestURI() {
        String apiKeyURI = PAYMENT_URI + "/api-key";
        when(request.getRequestURI()).thenReturn(apiKeyURI);

        assertEquals(apiKeyURI, controller.getRequestURI(request));
    }

    @Test
    void shouldReturnEnglishLocale() {
        when(localeResolver.resolveLocale(request)).thenReturn(Locale.ENGLISH);

        assertEquals("en", controller.getLocale(request));

        verify(localeResolver).resolveLocale(request);
    }

    @Test
    void shouldReturnWelshLocale() {
        when(localeResolver.resolveLocale(request)).thenReturn(Locale.forLanguageTag("cy"));

        assertEquals("cy", controller.getLocale(request));

        verify(localeResolver).resolveLocale(request);
    }

    @Test
    void shouldReturnSameRequestForSharedFragments() {
        assertSame(request, controller.getRequest(request));
    }
}