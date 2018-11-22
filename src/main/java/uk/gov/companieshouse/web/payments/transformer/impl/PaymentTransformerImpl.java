package uk.gov.companieshouse.web.payments.transformer.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.model.Payment;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentTransformerImpl implements PaymentTransformer {

    @Override
    public PaymentSummary getPaymentSummary(PaymentApi paymentApi) {

        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setTotal(paymentApi.getAmount());
        paymentSummary.setEmail(paymentApi.getCreatedBy().getEmail());

        List<Payment> payments = paymentApi.getItems().stream()
                                .map(p -> new Payment( p.getDescription(), p.getAmount())).collect(Collectors.toList());

        paymentSummary.setPayments(payments);
        return paymentSummary;
    }
}
