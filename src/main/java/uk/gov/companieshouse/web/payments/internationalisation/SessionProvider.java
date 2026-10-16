package uk.gov.companieshouse.web.payments.internationalisation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.session.Session;
import uk.gov.companieshouse.session.handler.SessionHandler;

import java.util.Map;

/**
 * Provides access to the CH session associated with the current context.
 * Wraps the static session handler so callers can inject and mock session access.
 */
@Component
public class SessionProvider {
        /**
         * Returns the data held in the current CH session.
         *
         * @return the current session's data
         */
        public Map<String, Object> getSessionDataFromContext() {
            return SessionHandler.getSessionDataFromContext();
        }
        /**
         * Returns the current CH session.
         *
         * @return the session associated with the current context
         */
        public Session getSessionFromContext() {
            return SessionHandler.getSessionFromContext();
        }
}

