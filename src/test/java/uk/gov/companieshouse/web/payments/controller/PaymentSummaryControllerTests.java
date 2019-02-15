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
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.util.PaymentStatus;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.companieshouse.web.payments.controller.BaseController.ERROR_VIEW;

@ExtendWith(MockitoExtension.class)
public class PaymentSummaryControllerTests {

    private static final String PAYMENT_ID = "paymentId";
    private static final String PAYMENT_SUMMARY_PATH = "/payments/" + PAYMENT_ID + "/pay";

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentSummaryController controller;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Payment Summary view success path")
    void getRequestSuccess() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PENDING.paymentStatus());
        when(paymentService.getPayment(PAYMENT_ID)).thenReturn(paymentSummary);
        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(controller.getTemplateName()))
                .andExpect(model().attributeExists("paymentSummary"));
    }

    @Test
    @DisplayName("Failure due to error on GET request for payment summary")
    void getRequestFailureInGetPayment() throws Exception {

        doThrow(ServiceException.class).when(paymentService).getPayment(PAYMENT_ID);

        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Get payment processing failure due to payment status being paid")
    void getRequestErrorWhenPaymentStatusPaid() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setStatus(PaymentStatus.PAYMENT_STATUS_PAID.paymentStatus());
        when(paymentService.getPayment(PAYMENT_ID)).thenReturn(paymentSummary);
        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
