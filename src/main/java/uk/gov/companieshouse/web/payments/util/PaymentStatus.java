package uk.gov.companieshouse.web.payments.util;

public enum PaymentStatus {
    PAYMENT_STATUS_PAID("paid"),
    PAYMENT_STATUS_PENDING("pending");

    private String payStatus;

    PaymentStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String paymentStatus() {
        return payStatus;
    }

}
