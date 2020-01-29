package uk.gov.companieshouse.web.payments.service.payment.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.payment.PaymentResourceHandler;
import uk.gov.companieshouse.api.handler.payment.PrivatePaymentResourceHandler;
import uk.gov.companieshouse.api.handler.payment.request.PaymentGet;
import uk.gov.companieshouse.api.handler.payment.request.PaymentPatch;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.api.ApiClientService;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentServiceImplTests {

    private static final String PAYMENT_ID = "123456";

    private static final String AMOUNT = "987654";

    private static final String GET_PAYMENT_VALID_URI = "/payments/" + PAYMENT_ID;

    private static final String PATCH_PAYMENT_VALID_URI = "/private/payments/" + PAYMENT_ID;

    private static final String PAYMENT_METHOD_GOV_PAY = "GovPay";

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private InternalApiClient internalApiClient;

    @Mock
    private PaymentResourceHandler paymentResourceHandler;

    @Mock
    private PrivatePaymentResourceHandler privatePaymentResourceHandler;

    @Mock
    private PaymentGet paymentGet;

    @Mock
    private PaymentPatch paymentPatch;

    @Mock
    private PaymentTransformer paymentTransformer;

    @InjectMocks
    private PaymentService paymentService = new PaymentServiceImpl();

    @Test
    @DisplayName("Get Payment Session - Success Path")
    void getPaymentSessionSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getPublicApiClient()).thenReturn(apiClient);

        PaymentApi paymentApi = new PaymentApi();
        paymentApi.setAmount(AMOUNT);
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setTotal(AMOUNT);
        ApiResponse<PaymentApi> apiResponse = new ApiResponse<PaymentApi>(201, null, paymentApi);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);
        when(apiClient.payment().get(GET_PAYMENT_VALID_URI)).thenReturn(paymentGet);
        when(paymentGet.execute()).thenReturn(apiResponse);
        when(paymentTransformer.getPayment(paymentApi)).thenReturn(paymentSummary);

        String totalAmount = paymentService.getPayment(PAYMENT_ID).getTotal();

        assertEquals(AMOUNT, totalAmount);
    }

    @Test
    @DisplayName("Get Payment Session - API Error Response Exception Thrown")
    void getPaymentSessionApiResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getPublicApiClient()).thenReturn(apiClient);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);
        when(apiClient.payment().get(GET_PAYMENT_VALID_URI)).thenReturn(paymentGet);
        when(paymentGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () -> paymentService.getPayment(PAYMENT_ID));
    }

    @Test
    @DisplayName("Get Payment Session - URI Validation Exception Thrown")
    void getPaymentSessionUriValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getPublicApiClient()).thenReturn(apiClient);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);
        when(apiClient.payment().get(GET_PAYMENT_VALID_URI)).thenReturn(paymentGet);
        when(paymentGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> paymentService.getPayment(PAYMENT_ID));
    }

    @Test
    @DisplayName("Patch Payment Session - Success Path")
    void patchPaymentSessionSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getPrivateApiClient()).thenReturn(internalApiClient);

        ApiResponse<Void> apiResponse = new ApiResponse<Void>(201, null);

        when(internalApiClient.privatePayment()).thenReturn(privatePaymentResourceHandler);
        when(internalApiClient.privatePayment().patch(eq(PATCH_PAYMENT_VALID_URI), any(PaymentApi.class))).thenReturn(paymentPatch);
        when(paymentPatch.execute()).thenReturn(apiResponse);

        paymentService.patchPayment(PAYMENT_ID, PAYMENT_METHOD_GOV_PAY);

        verify(paymentPatch, times(1)).execute();
    }

    @Test
    @DisplayName("Patch Payment Session - API Error Response Exception Thrown")
    void patchPaymentSessionApiResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getPrivateApiClient()).thenReturn(internalApiClient);

        when(internalApiClient.privatePayment()).thenReturn(privatePaymentResourceHandler);
        when(internalApiClient.privatePayment().patch(eq(PATCH_PAYMENT_VALID_URI), any(PaymentApi.class))).thenReturn(paymentPatch);
        when(paymentPatch.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () -> paymentService.patchPayment(PAYMENT_ID, PAYMENT_METHOD_GOV_PAY));
    }

    @Test
    @DisplayName("Patch Payment Session - URI Validation Exception Thrown")
    void patchPaymentSessionUriValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getPrivateApiClient()).thenReturn(internalApiClient);

        when(internalApiClient.privatePayment()).thenReturn(privatePaymentResourceHandler);
        when(internalApiClient.privatePayment().patch(eq(PATCH_PAYMENT_VALID_URI), any(PaymentApi.class))).thenReturn(paymentPatch);
        when(paymentPatch.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> paymentService.patchPayment(PAYMENT_ID, PAYMENT_METHOD_GOV_PAY));
    }
}
