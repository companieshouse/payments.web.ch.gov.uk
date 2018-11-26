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
import uk.gov.companieshouse.web.payments.service.PaymentService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.gov.companieshouse.web.payments.controller.BaseController.ERROR_VIEW;

@ExtendWith(MockitoExtension.class)
public class PaymentConfirmationControllerTests {

    private static final String PAYMENT_ID = "paymentId";
    private static final String PAYMENT_CONFIRM_PATH = "/payments/" + PAYMENT_ID + "/confirm";

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentConfirmationController controller;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Payment Confirmation view success path")
    void getRequestSuccess() throws Exception {
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setStatus("paid");
        when(paymentService.getPaymentSummary(PAYMENT_ID)).thenReturn(paymentSummary);
        this.mockMvc.perform(get(PAYMENT_CONFIRM_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(controller.getTemplateName()))
                .andExpect(model().attributeExists("paymentSummary"))
                .andExpect(model().attribute("paymentID", PAYMENT_ID));
    }

    @Test
    @DisplayName("Get payment view failure path due to error on payment confirmation retrieval")
    void getRequestFailureInGetPayment() throws Exception {

        doThrow(ServiceException.class).when(paymentService).getPaymentSummary(PAYMENT_ID);

        this.mockMvc.perform(get(PAYMENT_CONFIRM_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
