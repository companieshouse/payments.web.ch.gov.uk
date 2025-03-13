package uk.gov.companieshouse.web.payments.util;

public enum PaymentStatus {
    PAYMENT_STATUS_PAID("paid"),
    PAYMENT_STATUS_PENDING("pending");

    private final String paymentStatus;

    PaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String paymentStatus() {
        return paymentStatus;
    }

}
