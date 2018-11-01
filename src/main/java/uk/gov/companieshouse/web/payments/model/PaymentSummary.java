package uk.gov.companieshouse.web.payments.model;

import java.util.List;

public class PaymentSummary {

    private String total;
    private List<Payment> payments;


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

}
