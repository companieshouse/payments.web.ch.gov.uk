package uk.gov.companieshouse.web.payments.service.payment;

import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;

public interface PaymentService {

    PaymentSummary getPayment(String paymentId)
            throws ServiceException;

    void patchPayment(String paymentId, String paymentMethod)
            throws ServiceException;
}
