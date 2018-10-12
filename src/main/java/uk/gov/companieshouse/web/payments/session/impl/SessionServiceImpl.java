package uk.gov.companieshouse.web.payments.session.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.session.handler.SessionHandler;
import uk.gov.companieshouse.web.payments.session.SessionService;

import java.util.Map;

@Component
public class SessionServiceImpl implements SessionService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getSessionDataFromContext() {

        return SessionHandler.getSessionDataFromContext();
    }
}
