package uk.gov.companieshouse.web.payments.service.externalpayment.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.externalpayment.PrivateExternalPaymentResourceHandler;
import uk.gov.companieshouse.api.handler.externalpayment.request.ExternalPaymentCreate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.externalpayment.ExternalPaymentApi;
import uk.gov.companieshouse.web.payments.api.ApiClientService;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.service.externalpayment.ExternalPaymentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExternalPaymentServiceImplTests {
    private static final String PAYMENT_ID = "123456";

    private static final String VALID_URI = "/private/payments/" + PAYMENT_ID + "/external-journey";

    private static final String NEXT_URL = "http://next_url-test";

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private InternalApiClient apiClient;

    @Mock
    private ExternalPaymentCreate externalPaymentCreate;

    @Mock
    private PrivateExternalPaymentResourceHandler privateExternalPaymentResourceHandler;

    @InjectMocks
    private ExternalPaymentService externalPaymentService = new ExternalPaymentServiceImpl();

    @BeforeEach
    private void init() {
        when(apiClientService.getPrivateApiClient()).thenReturn(apiClient);
    }

    @Test
    @DisplayName("Create external payment journey - Success Path")
    void createExternalPaymentSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        ExternalPaymentApi externalPaymentApi = new ExternalPaymentApi();
        externalPaymentApi.setNextUrl(NEXT_URL);
        ApiResponse<ExternalPaymentApi> apiResponse = new ApiResponse<>(201, null, externalPaymentApi);

        when(apiClient.privateExternalPayment()).thenReturn(privateExternalPaymentResourceHandler);
        when(apiClient.privateExternalPayment().create(eq(VALID_URI), any(ExternalPaymentApi.class))).thenReturn(externalPaymentCreate);
        when(externalPaymentCreate.execute()).thenReturn(apiResponse);

        String nextUrl = externalPaymentService.createExternalPayment(PAYMENT_ID, false);

        assertEquals(NEXT_URL, nextUrl);
    }

    @Test
    @DisplayName("Create external payment journey - API Error Response Exception Thrown")
    void createExternalPaymentApiResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.privateExternalPayment()).thenReturn(privateExternalPaymentResourceHandler);
        when(apiClient.privateExternalPayment().create(eq(VALID_URI), any(ExternalPaymentApi.class))).thenReturn(externalPaymentCreate);
        when(externalPaymentCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () -> externalPaymentService.createExternalPayment(PAYMENT_ID, false));
    }

    @Test
    @DisplayName("Create external payment journey - URI Validation Exception Thrown")
    void createExternalPaymentUriValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.privateExternalPayment()).thenReturn(privateExternalPaymentResourceHandler);
        when(apiClient.privateExternalPayment().create(eq(VALID_URI), any(ExternalPaymentApi.class))).thenReturn(externalPaymentCreate);
        when(externalPaymentCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> externalPaymentService.createExternalPayment(PAYMENT_ID, false));
    }
}
