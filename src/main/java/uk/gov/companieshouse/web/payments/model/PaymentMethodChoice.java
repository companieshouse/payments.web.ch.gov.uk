package uk.gov.companieshouse.web.payments.model;

import jakarta.validation.constraints.NotNull;

public class PaymentMethodChoice {

    @NotNull(message = "{payment.method.required}")
    private String selectedPaymentMethod;

    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(String selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }
}
