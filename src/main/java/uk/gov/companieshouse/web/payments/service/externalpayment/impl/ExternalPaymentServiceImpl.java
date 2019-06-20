package uk.gov.companieshouse.web.payments.service.externalpayment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.externalpayment.ExternalPaymentApi;
import uk.gov.companieshouse.web.payments.api.ApiClientService;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.service.externalpayment.ExternalPaymentService;

@Service
public class ExternalPaymentServiceImpl implements ExternalPaymentService{

    private static final UriTemplate POST_EXTERNAL_PAYMENT_URI =
            new UriTemplate("/private/payments/{paymentId}/external-journey");

    @Autowired
    ApiClientService apiClientService;

    @Override
    public String createExternalPayment(String paymentId) throws ServiceException {

        InternalApiClient apiClient = apiClientService.getPrivateApiClient();
        String externalPaymentUrl;

        try {
            String uriPost = POST_EXTERNAL_PAYMENT_URI.expand(paymentId).toString();
            ExternalPaymentApi externalPaymentApi = new ExternalPaymentApi();
            ApiResponse<ExternalPaymentApi> apiResponse;
            apiResponse = apiClient.privateExternalPayment().create(uriPost, externalPaymentApi).execute();
            externalPaymentUrl = apiResponse.getData().getNextUrl();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error creating external journey", e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for ExternalPayment", e);
        }

        return externalPaymentUrl;
    }
}
