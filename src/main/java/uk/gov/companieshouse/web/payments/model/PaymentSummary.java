package uk.gov.companieshouse.web.payments.model;

import java.util.List;

public class PaymentSummary {

    private String total;
    private List<Payment> payments;
    private String email;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
