package uk.gov.companieshouse.web.payments.controller;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.health.actuate.endpoint.HealthEndpointWebExtension;
import org.springframework.boot.webmvc.autoconfigure.error.BasicErrorController;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.MapPropertySource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import uk.gov.companieshouse.web.payments.configuration.MessageConfig;
import uk.gov.companieshouse.web.payments.internationalisation.ChSessionLocaleResolver;
import uk.gov.companieshouse.web.payments.internationalisation.SessionProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class ErrorPageRenderingTest {

    private static final String PAYMENT_URI = "/payments/paymentId/pay";

    private AnnotationConfigWebApplicationContext context;
    private MockMvc mockMvc;
    private Map<String, Object> sessionData;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test", Map.of(
                "welshLanguage.enabled", "true",
                "cdn.url", "//cdn.example",
                "chs.url", "https://chs.example",
                "developer.url", "https://developer.example",
                "enquiries", "mailto:test@example.invalid")));
        context.register(TestConfiguration.class);
        context.refresh();

        sessionData = new HashMap<>();
        when(context.getBean(SessionProvider.class).getSessionDataFromContext()).thenReturn(sessionData);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @ParameterizedTest
    @CsvSource({"400,en", "400,cy", "404,en", "404,cy", "500,en", "500,cy"})
    void shouldRenderErrorPageWithLanguageLinksToOriginalPayment(int errorStatus, String language) throws Exception {
        sessionData.put("lang", language);
        String otherLanguage = language.equals("en") ? "cy" : "en";
        String errorHeading = context.getBean(MessageConfig.class).messageSource()
                .getMessage("label.error.page.title", null, Locale.forLanguageTag(language));

        var result = mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setDispatcherType(DispatcherType.ERROR);
                            return request;
                        })
                        .accept("text/html")
                        .queryParam("summary", "notaboolean")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, errorStatus)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, PAYMENT_URI))
                .andExpect(status().is(errorStatus))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("requestURI", PAYMENT_URI))
                .andExpect(model().attribute("locale", language))
                .andExpect(model().attributeExists("request"))
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertTrue(HtmlUtils.htmlUnescape(html).contains(errorHeading));
        assertTrue(html.contains("govuk-language-select"));
        assertTrue(html.contains("href=\"" + PAYMENT_URI + "?summary=notaboolean&amp;lang=" + otherLanguage + "\""));
        assertFalse(html.contains("href=\"/error?"));
        assertFalse(html.contains("??"));
    }

    @Test
    void shouldRenderLanguageLinksWhenErrorRequestHasNoQueryString() throws Exception {
        sessionData.put("lang", "en");

        var result = mockMvc.perform(get("/error")
                        .accept("text/html")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, PAYMENT_URI))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("href=\"" + PAYMENT_URI + "?lang=cy\""));
    }

    @Test
    void shouldIncludeBootErrorControllerButExcludeActuatorHealthFromAdvice() {
        var advice = ControllerAdviceBean.findAnnotatedBeans(context).stream()
                .filter(bean -> bean.getBeanType() == GlobalController.class)
                .findFirst()
                .orElseThrow();

        assertTrue(advice.isApplicableToBeanType(PaymentSummaryController.class));
        assertTrue(advice.isApplicableToBeanType(BasicErrorController.class));
        assertFalse(advice.isApplicableToBeanType(HealthEndpointWebExtension.class));
    }

    @Configuration
    @EnableWebMvc
    @Import({MessageConfig.class, GlobalController.class})
    static class TestConfiguration {

        @Bean
        BasicErrorController errorController() {
            return new BasicErrorController(new DefaultErrorAttributes(), new ErrorProperties());
        }

        @Bean
        SessionProvider sessionProvider() {
            return mock(SessionProvider.class);
        }

        @Bean
        ChSessionLocaleResolver chSessionLocaleResolver(SessionProvider sessionProvider) {
            return new ChSessionLocaleResolver(sessionProvider, true);
        }

        @Bean
        SpringTemplateEngine templateEngine(AnnotationConfigWebApplicationContext applicationContext,
                MessageConfig messageConfig) {
            var resolver = new SpringResourceTemplateResolver();
            resolver.setApplicationContext(applicationContext);
            resolver.setPrefix("classpath:/templates/");
            resolver.setSuffix(".html");
            resolver.setCharacterEncoding("UTF-8");

            var engine = new SpringTemplateEngine();
            engine.setTemplateResolver(resolver);
            engine.setTemplateEngineMessageSource(messageConfig.messageSource());
            engine.addDialect(new LayoutDialect());
            return engine;
        }

        @Bean
        ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
            var resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(templateEngine);
            resolver.setCharacterEncoding("UTF-8");
            return resolver;
        }
    }
}
