package uk.gov.companieshouse.web.payments.internationalisation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import uk.gov.companieshouse.session.handler.SessionHandler;

import java.util.Map;

class SessionProviderTests {

    private SessionProvider sessionProvider;

    @BeforeEach
    void setUp() {
        sessionProvider = new SessionProvider();
    }

    @Test
    void testSessionProviderInstantiation() {
        assertNotNull(sessionProvider);
    }

    @Test
    void testGetSessionDataFromContextReturnsSessionData() {
        Map<String, Object> sessionData = Map.of("lang", "cy");

        try (MockedStatic<SessionHandler> sessionHandler = mockStatic(SessionHandler.class)) {
            sessionHandler.when(SessionHandler::getSessionDataFromContext)
                    .thenReturn(sessionData);

            assertSame(sessionData, sessionProvider.getSessionDataFromContext());
            sessionHandler.verify(SessionHandler::getSessionDataFromContext);
        }
    }

}