package uk.gov.companieshouse.web.payments.model;

import java.util.List;

public class Payment {

    private String description;

    private String cost;

    private List<String> paymentMethods;

    public Payment(String description, String cost, List<String> paymentMethods) {
        this.description = description;
        this.cost = cost;
        this.paymentMethods = paymentMethods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
