package uk.gov.companieshouse.web.payments.configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class OpenTelemetryAppenderInitializerTest {

    @Test
    void shouldInstallOpenTelemetryAppenderDuringInitialisation() {
        OpenTelemetry openTelemetry = mock(OpenTelemetry.class);
        OpenTelemetryAppenderInitializer initializer = new OpenTelemetryAppenderInitializer(openTelemetry);

        try (MockedStatic<OpenTelemetryAppender> mockedAppender = mockStatic(OpenTelemetryAppender.class)) {
            initializer.afterPropertiesSet();

            mockedAppender.verify(() -> OpenTelemetryAppender.install(openTelemetry));
        }
    }
}

