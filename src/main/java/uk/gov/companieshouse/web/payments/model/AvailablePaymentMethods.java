package uk.gov.companieshouse.web.payments.model;

import java.util.List;

public class AvailablePaymentMethods {

    private List<String> availablePaymentMethods;

    public List<String> getAvailablePaymentMethods() {
        return availablePaymentMethods;
    }

    public void setAvailablePaymentMethods(List<String> availablePaymentMethods) {
        this.availablePaymentMethods = availablePaymentMethods;
    }
}
