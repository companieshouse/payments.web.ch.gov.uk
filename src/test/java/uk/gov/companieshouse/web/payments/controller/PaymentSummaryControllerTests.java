package uk.gov.companieshouse.web.payments.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class PaymentSummaryControllerTests {

    private static final String PAYMENT_ID = "paymentId";
    private static final String PAYMENT_SUMMARY_PATH = "/payments/" + PAYMENT_ID + "/payment-summary";

    private MockMvc mockMvc;

    @InjectMocks
    private PaymentSummaryController controller;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Payment Summary view success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(PAYMENT_SUMMARY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(PaymentSummaryController.PAYMENT_SUMMARY_VIEW));
    }
}
