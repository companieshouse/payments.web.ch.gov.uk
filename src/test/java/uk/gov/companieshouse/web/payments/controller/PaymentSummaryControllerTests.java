package uk.gov.companieshouse.web.payments.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.Payment;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.externalpayment.ExternalPaymentService;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.transformer.PaymentMethodTransformer;
import uk.gov.companieshouse.web.payments.util.PaymentMethodData;
import uk.gov.companieshouse.web.payments.util.PaymentMethodReadable;
import uk.gov.companieshouse.web.payments.util.PaymentStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.gov.companieshouse.web.payments.controller.BaseController.ERROR_VIEW;

@ExtendWith(MockitoExtension.class)
public class PaymentSummaryControllerTests {

    private static final String PAYMENT_ID = "paymentId";
    private static final String PAYMENT_SUMMARY_PATH = "/payments/" + PAYMENT_ID + "/pay";
    private static final String SUMMARY_FALSE_PARAMETER = "?summary=false";
    private static final String PAYMENT_SUMMARY_DONT_DISPLAY_PATH = PAYMENT_SUMMARY_PATH + SUMMARY_FALSE_PARAMETER;
    private static final String JOURNEY_NEXT_URL = "payment.service/gov/uk/123456789";
    private static final String PAYMENT_METHODS_MODEL_ATTR = "paymentMethods";
    private static final String SELECTED_PAYMENT_METHOD_MODEL_ATTR = "selectedPaymentMethod";

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ExternalPaymentService externalPaymentService;

    @Mock
    private PaymentMethodTransformer paymentMethodTransformer;

    @InjectMocks
    private PaymentSummaryController controller;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Failure due to error on GET request for payment summary")
    void getRequestFailureInGetPayment() throws Exception {

        doThrow(ServiceException.class).when(paymentService).getPayment(PAYMENT_ID, false);

        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Get payment processing failure due to payment status being paid")
    void getRequestErrorWhenPaymentStatusPaid() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PAID.paymentStatus());
        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);
        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Payment Summary view success path - GovPay - Display Summary Screen")
    void getRequestSuccessDisplaySummary() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Collections.singletonList(PaymentMethodReadable.GOVPAY.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);

        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(controller.getTemplateName()))
                .andExpect(model().attributeExists("paymentSummary"));
    }

    @Test
    @DisplayName("Payment Summary view success path - GovPay - Don't Display Summary Screen")
    void getRequestSuccessDontDisplaySummary() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Collections.singletonList(PaymentMethodReadable.GOVPAY.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Collections.singletonList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);
        when(externalPaymentService.createExternalPayment(PAYMENT_ID, false)).thenReturn(JOURNEY_NEXT_URL);
        when(paymentMethodTransformer.getDataPaymentMethod(PaymentMethodReadable.GOVPAY.getPaymentMethod())).thenReturn(PaymentMethodData.GOVPAY.getPaymentMethod());

        this.mockMvc.perform(get(PAYMENT_SUMMARY_DONT_DISPLAY_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + JOURNEY_NEXT_URL));

        verify(paymentService, times(1)).patchPayment(PAYMENT_ID, PaymentMethodData.GOVPAY.getPaymentMethod(), false);
    }

    @Test
    @DisplayName("Payment Summary view - GovPay and PayPal - Cannot skip summary screen")
    void getRequestFailureCannotSkipSummaryScreen() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Arrays.asList(PaymentMethodReadable.GOVPAY.getPaymentMethod(), PaymentMethodReadable.PAYPAL.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);

        this.mockMvc.perform(get(PAYMENT_SUMMARY_DONT_DISPLAY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Payment Summary view success path - GovPay and PayPal - Display Choose Payment Method")
    void getRequestSuccessDisplayChoosePaymentMethod() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Arrays.asList(PaymentMethodReadable.GOVPAY.getPaymentMethod(), PaymentMethodReadable.PAYPAL.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);

        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("payments/paymentMethodChoice"))
                .andExpect(model().attributeExists("availablePaymentMethods"))
                .andExpect(model().attributeExists("paymentMethods"));
    }

    @Test
    @DisplayName("Post Payment Summary Failure - no payment method selected")
    void postRequestFailureNoPaymentMethodSelected() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Arrays.asList(PaymentMethodReadable.GOVPAY.getPaymentMethod(), PaymentMethodReadable.PAYPAL.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("payments/paymentMethodChoice"))
                .andExpect(model().attributeExists("availablePaymentMethods"))
                .andExpect(model().attributeExists("paymentMethods"));
    }

    @Test
    @DisplayName("Post Payment Summary Failure - no payment method selected and error returning payment session")
    void postRequestFailureNoPaymentMethodSelectedErrorReturningPaymentSession() throws Exception {
        doThrow(ServiceException.class).when(paymentService).getPayment(PAYMENT_ID, false);

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post Payment Summary success path")
    void postRequestSuccess() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Arrays.asList(PaymentMethodReadable.GOVPAY.getPaymentMethod(), PaymentMethodReadable.PAYPAL.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);
        when(externalPaymentService.createExternalPayment(PAYMENT_ID, false)).thenReturn(JOURNEY_NEXT_URL);
        when(paymentMethodTransformer.getDataPaymentMethod(PaymentMethodReadable.GOVPAY.getPaymentMethod())).thenReturn(PaymentMethodData.GOVPAY.getPaymentMethod());

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH)
                .param(SELECTED_PAYMENT_METHOD_MODEL_ATTR, PaymentMethodReadable.GOVPAY.getPaymentMethod()))
                .andExpect(model().errorCount(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + JOURNEY_NEXT_URL));

        verify(paymentService, times(1)).patchPayment(PAYMENT_ID, PaymentMethodData.GOVPAY.getPaymentMethod(), false);
    }

    @Test
    @DisplayName("Failure due to error on PATCH request for external payment method")
    void postRequestFailureOnPatchPayment() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Arrays.asList(PaymentMethodReadable.GOVPAY.getPaymentMethod(), PaymentMethodReadable.PAYPAL.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);
        when(paymentMethodTransformer.getDataPaymentMethod(PaymentMethodReadable.GOVPAY.getPaymentMethod())).thenReturn(PaymentMethodData.GOVPAY.getPaymentMethod());
        doThrow(ServiceException.class).when(paymentService).patchPayment(PAYMENT_ID, PaymentMethodData.GOVPAY.getPaymentMethod(), false);

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH)
                .param(SELECTED_PAYMENT_METHOD_MODEL_ATTR, PaymentMethodReadable.GOVPAY.getPaymentMethod()))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Failure due to error on POST request to GovPay")
    void postRequestFailureOnPostExternal() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        Payment payment = new Payment(
                "description",
                "750",
                Arrays.asList(PaymentMethodReadable.GOVPAY.getPaymentMethod(), PaymentMethodReadable.PAYPAL.getPaymentMethod())
        );

        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        paymentSummary.setPayments(Arrays.asList(payment));

        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);
        when(paymentMethodTransformer.getDataPaymentMethod(PaymentMethodReadable.GOVPAY.getPaymentMethod())).thenReturn(PaymentMethodData.GOVPAY.getPaymentMethod());
        doThrow(ServiceException.class).when(externalPaymentService).createExternalPayment(PAYMENT_ID, false);

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH)
                .param(SELECTED_PAYMENT_METHOD_MODEL_ATTR, PaymentMethodReadable.GOVPAY.getPaymentMethod()))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));

        verify(paymentService, times(1)).patchPayment(PAYMENT_ID, PaymentMethodData.GOVPAY.getPaymentMethod(), false);
    }
}
