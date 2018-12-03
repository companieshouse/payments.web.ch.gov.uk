package uk.gov.companieshouse.web.payments.service.externalpayment;

import uk.gov.companieshouse.web.payments.exception.ServiceException;

public interface ExternalPaymentService {

    String createExternalPayment(String paymentId)
        throws ServiceException;
}
