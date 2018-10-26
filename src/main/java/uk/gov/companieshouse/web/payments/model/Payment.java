package uk.gov.companieshouse.web.payments.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {

    private String description;

    private String cost;

    public Payment(String description, String cost) {
        this.description = description;
        this.cost = cost;
    }
}
