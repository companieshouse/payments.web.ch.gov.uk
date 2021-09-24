package uk.gov.companieshouse.web.payments.transformer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.web.payments.transformer.impl.PaymentMethodTransformerImpl;
import uk.gov.companieshouse.web.payments.util.PaymentMethodData;
import uk.gov.companieshouse.web.payments.util.PaymentMethodReadable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentMethodTransformerTests {

    private PaymentMethodTransformer transformer = new PaymentMethodTransformerImpl();

    @Test
    @DisplayName("Get Readable Payment Methods - GovPay")
    void getGovPayReadable() {

        List<String> readablePaymentMethods = transformer.getReadablePaymentMethods(Collections.singletonList(PaymentMethodData.GOVPAY.getPaymentMethod()));

        assertNotNull(readablePaymentMethods);
        assertEquals(1, readablePaymentMethods.size());
        assertTrue(readablePaymentMethods.contains(PaymentMethodReadable.GOVPAY.getPaymentMethod()));
    }

    @Test
    @DisplayName("Get Readable Payment Methods - PayPal")
    void getPayPalReadable() {

        List<String> readablePaymentMethods = transformer.getReadablePaymentMethods(Collections.singletonList(PaymentMethodData.PAYPAL.getPaymentMethod()));

        assertNotNull(readablePaymentMethods);
        assertEquals(1, readablePaymentMethods.size());
        assertTrue(readablePaymentMethods.contains(PaymentMethodReadable.PAYPAL.getPaymentMethod()));
    }

    @Test
    @DisplayName("Get Readable Payment Methods - GovPay & PayPal")
    void getGovPayAndPayPalReadable() {

        List<String> readablePaymentMethods = transformer.getReadablePaymentMethods(Arrays.asList(PaymentMethodData.GOVPAY.getPaymentMethod(), PaymentMethodData.PAYPAL.getPaymentMethod()));

        assertNotNull(readablePaymentMethods);
        assertEquals(2, readablePaymentMethods.size());
        assertTrue(readablePaymentMethods.contains(PaymentMethodReadable.GOVPAY.getPaymentMethod()));
        assertTrue(readablePaymentMethods.contains(PaymentMethodReadable.PAYPAL.getPaymentMethod()));
    }

    @Test
    @DisplayName("Get Readable Payment Methods - GovPay & Unrecognised")
    void getGovPayAndUnrecognisedReadable() {

        List<String> readablePaymentMethods = transformer.getReadablePaymentMethods(Arrays.asList(PaymentMethodData.GOVPAY.getPaymentMethod(), "invalid"));

        assertNotNull(readablePaymentMethods);
        assertEquals(2, readablePaymentMethods.size());
        assertTrue(readablePaymentMethods.contains(PaymentMethodReadable.GOVPAY.getPaymentMethod()));
        assertTrue(readablePaymentMethods.contains("unrecognised payment method"));
    }

    @Test
    @DisplayName("Get Data Payment Method - GovPay")
    void getGovPayData() {

        String dataPaymentMethod = transformer.getDataPaymentMethod(PaymentMethodReadable.GOVPAY.getPaymentMethod());

        assertEquals(PaymentMethodData.GOVPAY.getPaymentMethod(), dataPaymentMethod);
    }

    @Test
    @DisplayName("Get Data Payment Method - PayPal")
    void getPayPalData() {

        String dataPaymentMethod = transformer.getDataPaymentMethod(PaymentMethodReadable.PAYPAL.getPaymentMethod());

        assertEquals(PaymentMethodData.PAYPAL.getPaymentMethod(), dataPaymentMethod);
    }

    @Test
    @DisplayName("Get Data Payment Method - Unrecognised")
    void getUnrecognisedData() {

        String dataPaymentMethod = transformer.getDataPaymentMethod("invalid");

        assertEquals("UNRECOGNISED_PAYMENT_METHOD", dataPaymentMethod);
    }
}
