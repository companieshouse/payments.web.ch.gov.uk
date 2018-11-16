package uk.gov.companieshouse.web.payments.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.api.ApiClientService;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.PaymentMethod;
import uk.gov.companieshouse.web.payments.service.PaymentService;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final UriTemplate GET_PAYMENT_URI =
            new UriTemplate("/payments/{paymentId}");

    private static final UriTemplate PATCH_PAYMENT_URI =
            new UriTemplate("/private/payments/{paymentId}");

    @Autowired
    ApiClientService apiClientService;

    @Autowired
    private PaymentTransformer transformer;

    @Autowired
    private Environment env;

    @Override
    public PaymentSummary getPaymentSummary(String paymentId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getPublicApiClient();
        PaymentApi paymentApi;

        try {
            String uri = GET_PAYMENT_URI.expand(paymentId).toString();
            paymentApi = apiClient.payment().get(uri).execute();
        } catch (ApiErrorResponseException ex) {
            throw new ServiceException("Error retrieving Payment", ex);
        } catch (URIValidationException ex) {

            throw new ServiceException("Invalid URI for Payment", ex);
        }

        // Convert the API model to the web model
        return transformer.getPaymentSummary(paymentApi);
    }

    @Override
    public String getExternalPaymentUrl(String paymentId)
            throws ServiceException {

        InternalApiClient apiClient = apiClientService.getPrivateApiClient();

        try {
            // Firstly patch the Payment Method..
            String uri = PATCH_PAYMENT_URI.expand(paymentId).toString();
            PaymentApi payment = new PaymentApi();
            payment.setPaymentMethod(PaymentMethod.GOV_PAY.getPaymentMethod());
            apiClient.privatePayment().patch(uri, payment).execute();

            // TODO Get the next URL here..


        } catch (ApiErrorResponseException ex) {
            throw new ServiceException("Error retrieving Payment", ex);
        } catch (URIValidationException ex) {

            throw new ServiceException("Invalid URI for Payment", ex);
        }

        return "'NextURL'";
    }
}
