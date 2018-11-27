package uk.gov.companieshouse.web.payments.transformer.impl;

import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.model.Payment;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;

import java.util.List;
import java.util.stream.Collectors;

import static org.joda.money.CurrencyUnit.GBP;

@Component
public class PaymentTransformerImpl implements PaymentTransformer {

    @Override
    public PaymentSummary getPaymentSummary(PaymentApi paymentApi) {

        MoneyFormatter f = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();

        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setTotal(formatAmount(paymentApi.getAmount(), f));

        if (paymentApi.getCreatedBy() != null) {
            paymentSummary.setEmail(paymentApi.getCreatedBy().getEmail());
        }

        paymentSummary.setStatus(paymentApi.getStatus());

        List<Payment> payments = paymentApi.getItems().stream()
                .map(p -> new Payment( p.getDescription(), formatAmount(p.getAmount(), f))).collect(Collectors.toList());

        paymentSummary.setPayments(payments);
        return paymentSummary;
    }

    private String formatAmount(String amount, MoneyFormatter f) {
        return f.print(Money.of(GBP, Double.parseDouble(amount)));
    }
}
