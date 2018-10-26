package uk.gov.companieshouse.web.payments.service;

import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;

public interface PaymentService {

    PaymentSummary getPaymentSummary(String paymentId)
            throws ServiceException;
}
