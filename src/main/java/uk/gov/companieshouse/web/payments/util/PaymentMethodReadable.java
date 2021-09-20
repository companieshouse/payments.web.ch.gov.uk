package uk.gov.companieshouse.web.payments.util;

public enum PaymentMethodReadable {
    GOVPAY("Credit or debit card"),
    PAYPAL("PayPal");

    private String paymentMethod;

    private PaymentMethodReadable(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}
