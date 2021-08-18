package uk.gov.companieshouse.web.payments.service.externalpayment;

import uk.gov.companieshouse.web.payments.exception.ServiceException;

/**
 * Interfaces with external payment providers. e.g: GovPay.
 */
public interface ExternalPaymentService {

    String createExternalPayment(String paymentId, Boolean isAPIKey)
        throws ServiceException;
}
