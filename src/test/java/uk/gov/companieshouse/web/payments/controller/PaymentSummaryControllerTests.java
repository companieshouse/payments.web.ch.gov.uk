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
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.externalpayment.ExternalPaymentService;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.util.PaymentMethod;
import uk.gov.companieshouse.web.payments.util.PaymentStatus;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.companieshouse.web.payments.controller.BaseController.ERROR_VIEW;

@ExtendWith(MockitoExtension.class)
public class PaymentSummaryControllerTests {

    private static final String PAYMENT_ID = "paymentId";
    private static final String PAYMENT_SUMMARY_PATH = "/payments/" + PAYMENT_ID + "/pay";
    private static final String JOURNEY_NEXT_URL = "payment.service/gov/uk/123456789";

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ExternalPaymentService externalPaymentService;

    @InjectMocks
    private PaymentSummaryController controller;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Payment Summary view success path - Don't Display Summary Screen")
    void getRequestSuccessDontDisplaySummary() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        when(paymentService.getPayment(PAYMENT_ID, false)).thenReturn(paymentSummary);
        when(externalPaymentService.createExternalPayment(PAYMENT_ID, false)).thenReturn(JOURNEY_NEXT_URL);
        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + JOURNEY_NEXT_URL));
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
    @DisplayName("Post Payment Summary success path")
    void postRequestSuccess() throws Exception {
        when(externalPaymentService.createExternalPayment(PAYMENT_ID, false)).thenReturn(JOURNEY_NEXT_URL);
        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + JOURNEY_NEXT_URL));
    }

    @Test
    @DisplayName("Failure due to error on PATCH request for external payment method")
    void postRequestFailureOnPatchPayment() throws Exception {

        doThrow(ServiceException.class).when(paymentService).patchPayment(PAYMENT_ID, PaymentMethod.GOV_PAY.getPaymentMethod(), false);

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Failure due to error on POST request to GovPay")
    void postRequestFailureOnPostExternal() throws Exception {

        doThrow(ServiceException.class).when(externalPaymentService).createExternalPayment(PAYMENT_ID, false);

        this.mockMvc.perform(post(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
