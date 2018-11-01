package uk.gov.companieshouse.web.payments.transformer;

import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;

public interface PaymentTransformer {
    /**
     * Gets the Payment Summary for the Payment Session.
     * @param paymentApi populated payment summary
     * @return converted payment summary for web.
     */
    PaymentSummary getPaymentSummary(PaymentApi paymentApi);
}
