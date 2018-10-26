package uk.gov.companieshouse.web.payments.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentSummary {

    private String total;
    private List<Payment> payments;
}
