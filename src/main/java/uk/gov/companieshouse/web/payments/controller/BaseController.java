package uk.gov.companieshouse.web.payments.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.payments.PaymentsWebApplication;

public abstract class BaseController {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(PaymentsWebApplication.APPLICATION_NAME_SPACE);

    protected static final String ERROR_VIEW = "error";

    protected BaseController() {
    }

    @ModelAttribute("templateName")
    protected abstract String getTemplateName();
}
