package uk.gov.companieshouse.web.payments.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.api.ApiClientService;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.Payment;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.PaymentService;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final UriTemplate GET_PAYMENT_URI =
            new UriTemplate("/payments/{paymentId}");

    @Autowired
    private PaymentTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;

    @Override
    public PaymentSummary getPaymentSummary(String paymentId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        PaymentApi paymentApi;

        try {
            String uri = GET_PAYMENT_URI.expand(paymentId).toString();
            paymentApi = apiClient.payment().get(uri).execute();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error retrieving Payment", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for Payment", e);
        }

        // Convert the API model to the web model
        return transformer.getPaymentSummary(paymentApi);
    }
}
