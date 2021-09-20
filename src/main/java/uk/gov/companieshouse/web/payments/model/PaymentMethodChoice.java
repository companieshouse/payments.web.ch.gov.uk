package uk.gov.companieshouse.web.payments.model;

import javax.validation.constraints.NotNull;

public class PaymentMethodChoice {

    @NotNull(message = "Please choose a payment method")
    private String selectedPaymentMethod;

    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(String selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }
}
