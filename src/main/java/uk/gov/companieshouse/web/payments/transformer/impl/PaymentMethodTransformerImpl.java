package uk.gov.companieshouse.web.payments.transformer.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.payments.transformer.PaymentMethodTransformer;
import uk.gov.companieshouse.web.payments.util.PaymentMethodData;
import uk.gov.companieshouse.web.payments.util.PaymentMethodReadable;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMethodTransformerImpl implements PaymentMethodTransformer {

    @Override
    public List<String> getReadablePaymentMethods(List<String> paymentMethods) {
        List<String> readablePaymentMethods = new ArrayList<>();

        for (String paymentMethod : paymentMethods) {
            readablePaymentMethods.add(formatReadablePaymentMethod(paymentMethod));
        }

        return readablePaymentMethods;
    }

    @Override
    public String getDataPaymentMethod(String paymentMethod) {
        return formatDataPaymentMethod(paymentMethod);
    }

    private String formatReadablePaymentMethod(String paymentMethod) {

        if (paymentMethod.equalsIgnoreCase(PaymentMethodData.GOVPAY.getPaymentMethod())) {
            return PaymentMethodReadable.GOVPAY.getPaymentMethod();
        } else if (paymentMethod.equalsIgnoreCase(PaymentMethodData.PAYPAL.getPaymentMethod())) {
            return PaymentMethodReadable.PAYPAL.getPaymentMethod();
        } else {
            return "unrecognised payment method";
        }
    }

    private String formatDataPaymentMethod(String paymentMethod) {

        if (paymentMethod.equalsIgnoreCase(PaymentMethodReadable.GOVPAY.getPaymentMethod())) {
            return PaymentMethodData.GOVPAY.getPaymentMethod();
        } else if (paymentMethod.equalsIgnoreCase(PaymentMethodReadable.PAYPAL.getPaymentMethod())) {
            return PaymentMethodData.PAYPAL.getPaymentMethod();
        } else {
            return "UNRECOGNISED_PAYMENT_METHOD";
        }
    }
}
