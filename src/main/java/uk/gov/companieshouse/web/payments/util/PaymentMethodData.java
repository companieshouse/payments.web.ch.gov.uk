package uk.gov.companieshouse.web.payments.util;

public enum PaymentMethodData {
    GOVPAY("GovPay"),
    PAYPAL("PayPal");

    private String paymentMethod;

    private PaymentMethodData(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}
