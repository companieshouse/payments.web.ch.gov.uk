package uk.gov.companieshouse.web.payments.transformer;

import java.util.List;

public interface PaymentMethodTransformer {
    /**
     * Converts the payment methods from the API into readable payment methods for the customer
     * @param paymentMethod incoming payment method in data format
     * @return converted readable payment method for user.
     */
    List<String> getReadablePaymentMethods(List<String> paymentMethod);

    /**
     * Converts a selected readable payment method chosen by the customer to the data value expected by the API
     * @param paymentMethod incoming payment method in readable format
     * @return converted data payment method for API.
     */
    String getDataPaymentMethod(String paymentMethod);
}
