package uk.gov.companieshouse.web.payments.transformer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.payment.CreatedByApi;
import uk.gov.companieshouse.api.model.payment.ItemsApi;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.transformer.impl.PaymentTransformerImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentTransformerTests {

    private static final String MULTIPLE_PAYMENTS_AMOUNT = "300";
    private static final String SINGLE_PAYMENT_AMOUNT = "150";
    private static final String DESCRIPTION_ITEM_1 = "Item Description 1";
    private static final String DESCRIPTION_ITEM_2 = "Item Description 2";
    private static final String AMOUNT_ITEM_1 = "200";
    private static final String AMOUNT_ITEM_2 = "100";
    private static final String EMAIL = "email@companieshouse.gov.uk";

    private PaymentTransformer transformer = new PaymentTransformerImpl();

    @Test
    @DisplayName("Get Single Payment")
    void getSinglePayment() {
        PaymentApi mockSinglePayment = createSingleMockPayment();
        PaymentSummary paymentSummary = transformer.getPaymentSummary(mockSinglePayment);

        assertNotNull(paymentSummary);
        assertNotNull(paymentSummary.getPayments());
        assertEquals(SINGLE_PAYMENT_AMOUNT, paymentSummary.getTotal());
        assertEquals(EMAIL, paymentSummary.getEmail());
        assertEquals(1, paymentSummary.getPayments().size());
        assertEquals(SINGLE_PAYMENT_AMOUNT, paymentSummary.getPayments().get(0).getCost());
        assertEquals(DESCRIPTION_ITEM_1, paymentSummary.getPayments().get(0).getDescription());
    }

    @Test
    @DisplayName("Get Multiple Payments")
    void getMultiplePayments() {
        PaymentApi mockSinglePayment = createMultipleMockPayments();
        PaymentSummary paymentSummary = transformer.getPaymentSummary(mockSinglePayment);

        assertNotNull(paymentSummary);
        assertNotNull(paymentSummary.getPayments());
        assertEquals(MULTIPLE_PAYMENTS_AMOUNT, paymentSummary.getTotal());
        assertEquals(2, paymentSummary.getPayments().size());
        assertEquals(EMAIL, paymentSummary.getEmail());

        assertEquals(AMOUNT_ITEM_1, paymentSummary.getPayments().get(0).getCost());
        assertEquals(DESCRIPTION_ITEM_1, paymentSummary.getPayments().get(0).getDescription());

        assertEquals(AMOUNT_ITEM_2, paymentSummary.getPayments().get(1).getCost());
        assertEquals(DESCRIPTION_ITEM_2, paymentSummary.getPayments().get(1).getDescription());
    }

    private PaymentApi createSingleMockPayment() {
        List<ItemsApi> items  = new ArrayList<>();
        PaymentApi paymentApi = new PaymentApi();
        paymentApi.setAmount(SINGLE_PAYMENT_AMOUNT);
        CreatedByApi createdBy = new CreatedByApi();
        createdBy.setEmail(EMAIL);
        paymentApi.setCreatedBy(createdBy);
        ItemsApi item  = new ItemsApi();
        item.setAmount(SINGLE_PAYMENT_AMOUNT);
        item.setDescription(DESCRIPTION_ITEM_1);
        items.add(item);
        paymentApi.setItems(items);
        return paymentApi;
    }
    private PaymentApi createMultipleMockPayments() {
        List<ItemsApi> items  = new ArrayList<>();
        PaymentApi paymentApi = new PaymentApi();
        paymentApi.setAmount(MULTIPLE_PAYMENTS_AMOUNT);
        CreatedByApi createdBy = new CreatedByApi();
        createdBy.setEmail(EMAIL);
        paymentApi.setCreatedBy(createdBy);

        // Add Item 1
        ItemsApi item1  = new ItemsApi();
        item1.setAmount(AMOUNT_ITEM_1);
        item1.setDescription(DESCRIPTION_ITEM_1);
        items.add(item1);

        // Add Item 2
        ItemsApi item2  = new ItemsApi();
        item2.setAmount(AMOUNT_ITEM_2);
        item2.setDescription(DESCRIPTION_ITEM_2);
        items.add(item2);

        paymentApi.setItems(items);
        return paymentApi;
    }
}
