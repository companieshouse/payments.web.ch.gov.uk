package uk.gov.companieshouse.web.payments.util;

public enum PaymentMethodData {
    GOVPAY("credit-card"),
    PAYPAL("PayPal");

    private final String paymentMethod;

    PaymentMethodData(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}
