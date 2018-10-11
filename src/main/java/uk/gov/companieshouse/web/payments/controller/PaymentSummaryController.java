package uk.gov.companieshouse.web.payments.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments/{paymentId}/payment-summary")
public class PaymentSummaryController  {

    static final String PAYMENT_SUMMARY_VIEW = "payments/paymentSummary";

    @GetMapping
    public String getPaymentSummary(@PathVariable String paymentId,
                                     Model model) {
        return PAYMENT_SUMMARY_VIEW;
    }

}
