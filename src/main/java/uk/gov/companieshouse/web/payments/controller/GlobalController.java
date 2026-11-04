package uk.gov.companieshouse.web.payments.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Supplies shared template attributes to application controllers only,
 * excluding actuator endpoints that have no CH session.
 */
@ControllerAdvice(basePackageClasses = BaseController.class)
public class GlobalController {

    private final LocaleResolver localeResolver;

    /**
     * Creates the shared template attribute provider.
     *
     * @param localeResolver the MVC locale resolver
     */
    public GlobalController(
            @Qualifier("localeResolver") LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    /**
     * Supplies the current path for language-switch links.
     *
     * @param request the current request
     * @return the request URI without its query string
     */
    @ModelAttribute("requestURI")
    public String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * Supplies the resolved locale for the language selector.
     *
     * @param request the current request
     * @return the resolved locale as a string
     */
    @ModelAttribute("locale")
    public String getLocale(HttpServletRequest request) {
        return localeResolver.resolveLocale(request).toString();
    }

    /**
     * Supplies the request required by common-web-java 4.1.5 fragments.
     *
     * @param request the current request
     * @return the current request
     */
    @ModelAttribute("request")
    public HttpServletRequest getRequest(HttpServletRequest request) {
        return request;
    }
}