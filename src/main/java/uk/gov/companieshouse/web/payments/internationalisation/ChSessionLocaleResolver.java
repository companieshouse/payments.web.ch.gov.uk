// FILE 2: src/main/java/.../internationalisation/ChSessionLocaleResolver.java
package uk.gov.companieshouse.web.payments.internationalisation;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.gov.companieshouse.session.Session;

@Component
public class ChSessionLocaleResolver implements LocaleResolver {
    private static final String LANG_SESSION_KEY = "lang";
    private static final String EXTRA_DATA_SESSION_KEY = "extra_data";

    private SessionProvider sessionProvider;
    private Locale defaultLocale = Locale.getDefault();

    @Autowired
    public ChSessionLocaleResolver(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        Locale locale = getLocaleFromSession();
        httpServletRequest.setAttribute("onWelshJourney", locale != null);
        if (locale == null) {
            locale = defaultLocale;
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        setLocaleInSession(locale);
    }

    private Locale getLocaleFromSession() {
        Map<String, Object> sessionData = sessionProvider.getSessionDataFromContext();
        String languageTag = languageTagFrom(sessionData.get(LANG_SESSION_KEY));

        if (StringUtils.isEmpty(languageTag)) {
            languageTag = languageTagFromLegacyExtraData(sessionData);
        }

        if (StringUtils.isEmpty(languageTag)) {
            return null;
        }

        return Locale.forLanguageTag(languageTag);
    }

    private String languageTagFrom(Object languageTagObj) {
        return languageTagObj instanceof String string ? string : null;
    }

    private String languageTagFromLegacyExtraData(Map<String, Object> sessionData) {
        Object extraDataObj = sessionData.get(EXTRA_DATA_SESSION_KEY);
        if (!(extraDataObj instanceof Map)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> extraData = (Map<String, String>) extraDataObj;
        return extraData.get(LANG_SESSION_KEY);
    }

    private void setLocaleInSession(Locale locale) {
        Session session = sessionProvider.getSessionFromContext();
        session.getData().put(LANG_SESSION_KEY, locale.toLanguageTag());
        session.store();
    }

    public void setSessionProvider(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
}