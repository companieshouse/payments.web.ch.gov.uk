package uk.gov.companieshouse.web.payments.transformer.impl;

import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.model.Payment;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.transformer.PaymentMethodTransformer;
import uk.gov.companieshouse.web.payments.transformer.PaymentTransformer;
import uk.gov.companieshouse.web.payments.util.PaymentMethodData;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.joda.money.CurrencyUnit.GBP;

@Component
public class PaymentTransformerImpl implements PaymentTransformer {

    @Autowired
    private PaymentMethodTransformer paymentMethodTransformer;

    @Override
    public PaymentSummary getPayment(PaymentApi paymentApi) {

        Locale.setDefault(Locale.UK);
        MoneyFormatter f = new MoneyFormatterBuilder()
                .appendLiteral(Currency.getInstance(Locale.UK).getSymbol())
                .appendAmountLocalized()
                .toFormatter();

        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setTotal(formatAmount(paymentApi.getAmount(), f));

        if (paymentApi.getCreatedBy() != null) {
            paymentSummary.setEmail(paymentApi.getCreatedBy().getEmail());
        }

        paymentSummary.setStatus(paymentApi.getStatus());

        List<String> availablePaymentMethods = new ArrayList<>();

        // Set the available payment methods to only acceptable methods from the first cost resource
        for (String paymentMethod : paymentApi.getCosts().get(0).getAvailablePaymentMethods()) {
            if (paymentMethod.equalsIgnoreCase(PaymentMethodData.PAYPAL.getPaymentMethod()) || paymentMethod.equalsIgnoreCase(PaymentMethodData.GOVPAY.getPaymentMethod())) {
                availablePaymentMethods.add(paymentMethod);
            }
        }

        List<Payment> payments = paymentApi.getCosts().stream()
                .map(p -> new Payment(
                        p.getDescription(),
                        formatAmount(p.getAmount(), f),
                        paymentMethodTransformer.getReadablePaymentMethods(availablePaymentMethods))
                ).collect(Collectors.toList());

        paymentSummary.setPayments(payments);
        return paymentSummary;
    }

    private String formatAmount(String amount, MoneyFormatter f) {
        return f.print(Money.of(GBP, Double.parseDouble(amount)));
    }
}
