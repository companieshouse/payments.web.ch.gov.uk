package uk.gov.companieshouse.web.payments.util;

public enum PaymentStatus {
    PAYMENT_STATUS_PAID("paid"),
    PAYMENT_STATUS_PENDING("pending");

    private final String status;

    PaymentStatus(String paymentStatus) {
        this.status = paymentStatus;
    }

    public String paymentStatus() {
        return status;
    }

}
