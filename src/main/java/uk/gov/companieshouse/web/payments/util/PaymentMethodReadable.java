package uk.gov.companieshouse.web.payments.util;

public enum PaymentMethodReadable {
    GOVPAY("Credit or debit card", "payment.method.card"),
    PAYPAL("PayPal", "payment.method.paypal"),
    UNRECOGNISED("unrecognised payment method", "payment.method.unrecognised");

    private final String paymentMethod;
    private final String messageKey;

    /**
     * Creates a payment method with its existing journey value and message key.
     *
     * @param paymentMethod the value used by the payment journey
     * @param messageKey the key used to look up the translated label
     */
    PaymentMethodReadable(String paymentMethod, String messageKey) {
        this.paymentMethod = paymentMethod;
        this.messageKey = messageKey;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Returns the message key for a readable payment method.
     *
     * @param paymentMethod the payment method value used by the payment journey
     * @return the matching message key, or the unrecognised method key
     *         if the value is null or does not match a known method
     */
    public static String messageKeyFor(String paymentMethod) {
        for (PaymentMethodReadable readableMethod : values()) {
            if (readableMethod.paymentMethod.equalsIgnoreCase(paymentMethod)) {
                return readableMethod.messageKey;
            }
        }
        return UNRECOGNISED.messageKey;
    }

}
