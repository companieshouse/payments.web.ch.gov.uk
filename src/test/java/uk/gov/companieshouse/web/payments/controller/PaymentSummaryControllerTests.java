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
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.PaymentService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
        when(paymentService.getPaymentSummary(PAYMENT_ID)).thenReturn(new PaymentSummary());
        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(controller.getTemplateName()))
                .andExpect(model().attributeExists("paymentSummary"));
    }
}
