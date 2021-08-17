package uk.gov.companieshouse.web.payments.service.payment;

import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;

/**
 * Interfaces with the internal payment session that is stored in Mongo through the payments API.
 */
public interface PaymentService {

    PaymentSummary getPayment(String paymentId, Boolean isAPIKey)
            throws ServiceException;

    void patchPayment(String paymentId, String paymentMethod, Boolean isAPIKey)
            throws ServiceException;
}
