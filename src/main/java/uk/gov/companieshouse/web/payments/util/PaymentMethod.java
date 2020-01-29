package uk.gov.companieshouse.web.payments.util;

public enum PaymentMethod {
    GOV_PAY("GovPay");

    private String payMethod;

    PaymentMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayMethod() {
        return payMethod;
    }

}
