package uk.gov.companieshouse.web.payments.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mockStatic;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

class OpenTelemetryAppenderInitializerTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void afterPropertiesSetInstallsAppenderWithOpenTelemetryInstance() {
        OpenTelemetry openTelemetry = OpenTelemetry.noop();

        try (MockedStatic<OpenTelemetryAppender> appender = mockStatic(OpenTelemetryAppender.class)) {
            new OpenTelemetryAppenderInitializer(openTelemetry).afterPropertiesSet();

            appender.verify(() -> OpenTelemetryAppender.install(openTelemetry));
        }
    }

    @Test
    void initializerBeanIsAbsentByDefault() {
        contextRunner.run(context ->
                assertThat(context.getBeanNamesForType(OpenTelemetryAppenderInitializer.class).length > 0, is(false)));
    }

    @Test
    void initializerBeanIsAbsentWhenDisabled() {
        contextRunner.withPropertyValues("management.opentelemetry.enabled=false")
                .run(context ->
                        assertThat(context.getBeanNamesForType(OpenTelemetryAppenderInitializer.class).length > 0, is(false)));
    }

    @Test
    void initializerBeanIsPresentWhenEnabled() {
        try (MockedStatic<OpenTelemetryAppender> ignored = mockStatic(OpenTelemetryAppender.class)) {
            contextRunner.withPropertyValues("management.opentelemetry.enabled=true")
                    .run(context ->
                            assertThat(context.getBeanNamesForType(OpenTelemetryAppenderInitializer.class).length > 0, is(true)));
        }
    }

    @Configuration
    @Import(OpenTelemetryAppenderInitializer.class)
    static class TestConfig {

        @Bean
        OpenTelemetry openTelemetry() {
            return OpenTelemetry.noop();
        }
    }
}
