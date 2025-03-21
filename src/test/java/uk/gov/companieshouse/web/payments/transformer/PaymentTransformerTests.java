package uk.gov.companieshouse.web.payments.transformer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.payment.Cost;
import uk.gov.companieshouse.api.model.payment.CreatedByApi;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.transformer.impl.PaymentTransformerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PaymentTransformerTests {

    private static final String MULTIPLE_PAYMENTS_AMOUNT = "300";
    private static final String MULTIPLE_PAYMENTS_AMOUNT_FORMATTED = "£300.00";
    private static final String SINGLE_PAYMENT_AMOUNT = "150";
    private static final String SINGLE_PAYMENT_AMOUNT_FORMATTED = "£150.00";
    private static final String DESCRIPTION_ITEM_1 = "Item Description 1";
    private static final String DESCRIPTION_ITEM_2 = "Item Description 2";
    private static final String AMOUNT_ITEM_1 = "200";
    private static final String AMOUNT_ITEM_1_FORMATTED = "£200.00";
    private static final String AMOUNT_ITEM_2 = "100";
    private static final String AMOUNT_ITEM_2_FORMATTED = "£100.00";
    private static final String EMAIL = "email@companieshouse.gov.uk";

    @Mock
    private PaymentMethodTransformer paymentMethodTransformer;

    @InjectMocks
    private PaymentTransformer transformer = new PaymentTransformerImpl();

    @Test
    @DisplayName("Get Single Payment")
    void getSinglePayment() {
        PaymentApi mockSinglePayment = createSingleMockPayment();
        PaymentSummary paymentSummary = transformer.getPayment(mockSinglePayment);

        assertNotNull(paymentSummary);
        assertNotNull(paymentSummary.getPayments());
        assertEquals(SINGLE_PAYMENT_AMOUNT_FORMATTED, paymentSummary.getTotal());
        assertEquals(EMAIL, paymentSummary.getEmail());
        assertEquals(1, paymentSummary.getPayments().size());
        assertEquals(SINGLE_PAYMENT_AMOUNT_FORMATTED, paymentSummary.getPayments().getFirst().getCost());
        assertEquals(DESCRIPTION_ITEM_1, paymentSummary.getPayments().getFirst().getDescription());
    }

    @Test
    @DisplayName("Get Multiple Payments")
    void getMultiplePayments() {
        PaymentApi mockSinglePayment = createMultipleMockPayments();
        PaymentSummary paymentSummary = transformer.getPayment(mockSinglePayment);

        assertNotNull(paymentSummary);
        assertNotNull(paymentSummary.getPayments());
        assertEquals(MULTIPLE_PAYMENTS_AMOUNT_FORMATTED, paymentSummary.getTotal());
        assertEquals(2, paymentSummary.getPayments().size());
        assertEquals(EMAIL, paymentSummary.getEmail());

        assertEquals(AMOUNT_ITEM_1_FORMATTED, paymentSummary.getPayments().get(0).getCost());
        assertEquals(DESCRIPTION_ITEM_1, paymentSummary.getPayments().get(0).getDescription());

        assertEquals(AMOUNT_ITEM_2_FORMATTED, paymentSummary.getPayments().get(1).getCost());
        assertEquals(DESCRIPTION_ITEM_2, paymentSummary.getPayments().get(1).getDescription());
    }

    private PaymentApi createSingleMockPayment() {
        List<Cost> costs  = new ArrayList<>();
        PaymentApi paymentApi = new PaymentApi();
        paymentApi.setAmount(SINGLE_PAYMENT_AMOUNT);
        CreatedByApi createdBy = new CreatedByApi();
        createdBy.setEmail(EMAIL);
        paymentApi.setCreatedBy(createdBy);
        Cost cost  = new Cost();
        cost.setAmount(SINGLE_PAYMENT_AMOUNT);
        cost.setDescription(DESCRIPTION_ITEM_1);
        cost.setAvailablePaymentMethods(Arrays.asList("GovPay", "PayPal"));
        costs.add(cost);
        paymentApi.setCosts(costs);
        return paymentApi;
    }
    private PaymentApi createMultipleMockPayments() {
        List<Cost> costs  = new ArrayList<>();
        PaymentApi paymentApi = new PaymentApi();
        paymentApi.setAmount(MULTIPLE_PAYMENTS_AMOUNT);
        CreatedByApi createdBy = new CreatedByApi();
        createdBy.setEmail(EMAIL);
        paymentApi.setCreatedBy(createdBy);

        // Add Cost 1
        Cost cost1  = new Cost();
        cost1.setAmount(AMOUNT_ITEM_1);
        cost1.setDescription(DESCRIPTION_ITEM_1);
        cost1.setAvailablePaymentMethods(Arrays.asList("GovPay", "PayPal"));
        costs.add(cost1);

        // Add Cost 2
        Cost cost2  = new Cost();
        cost2.setAmount(AMOUNT_ITEM_2);
        cost2.setDescription(DESCRIPTION_ITEM_2);
        cost2.setAvailablePaymentMethods(Arrays.asList("GovPay", "PayPal"));
        costs.add(cost2);

        paymentApi.setCosts(costs);
        return paymentApi;
    }
}
