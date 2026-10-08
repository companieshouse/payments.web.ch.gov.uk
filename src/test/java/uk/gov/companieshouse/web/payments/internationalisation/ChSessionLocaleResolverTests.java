package uk.gov.companieshouse.web.payments.internationalisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.session.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChSessionLocaleResolverTests {

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private Session session;

    @Mock
    private SessionProvider sessionProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private Map<String, Object> sessionData;

    @InjectMocks
    private ChSessionLocaleResolver localeResolver;

    @BeforeEach
    public void setUp() {
        localeResolver.setSessionProvider(sessionProvider);
        ReflectionTestUtils.setField(localeResolver, "defaultLocale", Locale.ENGLISH);
    }

    private void initSessionData(String lang) {
        sessionData = new HashMap<>();
        if (lang != null) {
            sessionData.put("lang", lang);
        }
    }

    private void setupSessionData(String lang) {
        initSessionData(lang);
        when(sessionProvider.getSessionDataFromContext()).thenReturn(sessionData);
    }

    private void setupLegacyExtraDataSessionData(String lang) {
        sessionData = new HashMap<>();
        Map<String, String> extraData = new HashMap<>();
        if (lang != null) {
            extraData.put("lang", lang);
        }
        sessionData.put("extra_data", extraData);
        when(sessionProvider.getSessionDataFromContext()).thenReturn(sessionData);
    }

    private void setupSession() {
        initSessionData(null);
        when(sessionProvider.getSessionFromContext()).thenReturn(session);
        when(session.getData()).thenReturn(sessionData);
    }

    @Test
    @DisplayName("Resolves to the default locale if no locale is in the session")
    void testResolveDefaultLocale() {
        setupSessionData(null);
        Locale locale = localeResolver.resolveLocale(request);

        assertEquals(Locale.ENGLISH, locale);
    }

    @Test
    @DisplayName("Resolves to the locale in the session when present")
    void testResolveLocale() {
        setupSessionData("cy");
        Locale locale = localeResolver.resolveLocale(request);

        Locale welshLocale = Locale.forLanguageTag("cy");
        assertEquals(welshLocale, locale);
    }

    @Test
    @DisplayName("Locale gets set in session when resolver sets locale")
    void testSetLocale() {
        setupSession();
        Locale welshLocale = Locale.forLanguageTag("cy");

        localeResolver.setLocale(request, response, welshLocale);

        String languageTagInSession = (String) session.getData().get("lang");

        assertEquals(welshLocale.toLanguageTag(), languageTagInSession);
        verify(session).store();
    }

    @Test
    @DisplayName("Set default locale and resolves to the default locale if no locale is in the session")
    void testSetDefaultLocaleAndResolveDefaultLocale() {
        setupSessionData(null);
        localeResolver.setDefaultLocale(Locale.US);
        Locale locale = localeResolver.resolveLocale(request);

        assertEquals(Locale.US, locale);
    }

    @Test
    @DisplayName("Falls back to the legacy extra_data.lang value when the top-level lang key is absent")
    void testResolveLocaleFallsBackToLegacyExtraDataLang() {
        setupLegacyExtraDataSessionData("cy");
        Locale locale = localeResolver.resolveLocale(request);

        Locale welshLocale = Locale.forLanguageTag("cy");
        assertEquals(welshLocale, locale);
    }

    @Test
    @DisplayName("Top-level lang key takes precedence over the legacy extra_data.lang value when both are present")
    void testResolveLocalePrefersTopLevelLangOverLegacyExtraDataLang() {
        initSessionData("cy");
        Map<String, String> extraData = new HashMap<>();
        extraData.put("lang", "en");
        sessionData.put("extra_data", extraData);
        when(sessionProvider.getSessionDataFromContext()).thenReturn(sessionData);

        Locale locale = localeResolver.resolveLocale(request);

        Locale welshLocale = Locale.forLanguageTag("cy");
        assertEquals(welshLocale, locale);
    }
}