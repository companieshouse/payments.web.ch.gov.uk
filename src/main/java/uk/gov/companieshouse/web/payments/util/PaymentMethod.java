package uk.gov.companieshouse.web.payments.util;

public enum PaymentMethod {
    GOV_PAY("GovPay");

    private String paymentMethod;

    private PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}
