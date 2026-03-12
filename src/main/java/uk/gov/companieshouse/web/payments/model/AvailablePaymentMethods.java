package uk.gov.companieshouse.web.payments.model;

import java.util.List;

public class AvailablePaymentMethods {

    private List<String> paymentMethods;

    public List<String> getAvailablePaymentMethods() {
        return paymentMethods;
    }

    public void setAvailablePaymentMethods(List<String> availablePaymentMethods) {
        this.paymentMethods = availablePaymentMethods;
    }
}
