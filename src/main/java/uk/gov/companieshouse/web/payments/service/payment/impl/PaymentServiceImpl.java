package uk.gov.companieshouse.web.payments.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.api.ApiClientService;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final UriTemplate GET_PAYMENT_URI =
            new UriTemplate("/payments/{paymentId}");

    private static final UriTemplate PATCH_PAYMENT_URI =
            new UriTemplate("/private/payments/{paymentId}");

    @Autowired
   private ApiClientService apiClientService;

    @Autowired
    private PaymentTransformer transformer;

    @Override
    public PaymentSummary getPayment(String paymentId, Boolean isAPIKey)
            throws ServiceException {
        
        ApiClient apiClient;
        if (isAPIKey) {
            apiClient = apiClientService.getPublicApiClientWithKey();
        } else {
            apiClient = apiClientService.getPublicApiClient();
        }
        ApiResponse<PaymentApi> apiResponse;

        try {
            String uri = GET_PAYMENT_URI.expand(paymentId).toString();
            apiResponse = apiClient.payment().get(uri).execute();
        } catch (ApiErrorResponseException ex) {
            throw new ServiceException("Error retrieving Payment", ex);
        } catch (URIValidationException ex) {
            throw new ServiceException("Invalid URI for Payment", ex);
        }
        // Convert the API model to the web model
        return transformer.getPayment(apiResponse.getData());
    }

    @Override
    public void patchPayment(String paymentId, String paymentMethod, Boolean isAPIKey)
            throws ServiceException {

        InternalApiClient apiClient;
        if (isAPIKey) {
            apiClient = apiClientService.getPrivateApiClientWithKey();
        } else {
            apiClient = apiClientService.getPrivateApiClient();
        }

        try {
            String uriPatch = PATCH_PAYMENT_URI.expand(paymentId).toString();
            PaymentApi payment = new PaymentApi();
            payment.setPaymentMethod(paymentMethod);
            apiClient.privatePayment().patch(uriPatch, payment).execute();
        } catch (ApiErrorResponseException ex) {
            throw new ServiceException("Error patching Payment", ex);
        } catch (URIValidationException ex) {
            throw new ServiceException("Invalid URI for Payment", ex);
        }
    }
}
