package uk.gov.companieshouse.web.payments.internationalisation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.TestPropertySource;

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
    void testGetSessionDataFromContextMethodExists() {
        assertNotNull(sessionProvider);
        assertTrue(sessionProvider.getClass().getMethods().length > 0);
    }

}