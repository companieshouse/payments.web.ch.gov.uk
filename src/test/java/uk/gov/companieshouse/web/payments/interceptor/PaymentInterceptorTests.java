package uk.gov.companieshouse.web.payments.interceptor;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.HandlerMapping;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.Payment;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

    @ExtendWith(MockitoExtension.class)
    public class PaymentInterceptorTests {

        private static String paymentId = "paymentId";
        private static String total = "total";
        private Map<String, String> pathVariables = new HashMap<>();
        private List<Payment> payments = new ArrayList<>();
        private PaymentSummary paymentSummary = new PaymentSummary();

        @Mock
        private HttpServletRequest httpServletRequest;

        @Mock
        private HttpServletResponse httpServletResponse;

        @Mock
        private PaymentService paymentService;

        @InjectMocks
        private PaymentInterceptor paymentInterceptor;

        @Before
        public void setUp() {
            paymentInterceptor = new PaymentInterceptor();
        }

        @Test
        @DisplayName("Test the interceptor returns true with a valid payment Id and payment "
                + "summary from the api")
        void preHandleValidPaymentId() throws Exception {

            pathVariables.put("paymentId", paymentId);
            paymentSummary.setTotal(total);
            paymentSummary.setPayments(payments);
            when(httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                    .thenReturn(pathVariables);
            when(paymentService.getPaymentSummary(paymentId)).thenReturn(paymentSummary);

            boolean result = paymentInterceptor.preHandle(httpServletRequest, httpServletResponse,
                    null);
            assertTrue(result);
        }

        @Test
        @DisplayName("Test the interceptor returns false with an invalid payment Id")
        void preHandleEmptyPaymentId() throws Exception {

            pathVariables.put("paymentId", null);
            paymentSummary.setTotal(total);
            paymentSummary.setPayments(payments);
            when(httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                    .thenReturn(pathVariables);
            boolean result = paymentInterceptor.preHandle(httpServletRequest, httpServletResponse,
                    null);
            assertFalse(result);
        }

        @Test
        @DisplayName("Test the interceptor returns false with an invalid PaymentSummary api call")
        void preHandleNonMatchingPaymentId() throws Exception {

            pathVariables.put("paymentId", paymentId);
            paymentSummary.setTotal(total);
            paymentSummary.setPayments(payments);
            when(httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                    .thenReturn(pathVariables);
            when(paymentService.getPaymentSummary(paymentId)).thenThrow(ServiceException.class);
            boolean result = paymentInterceptor.preHandle(httpServletRequest, httpServletResponse,
                    null);
            assertFalse(result);
        }

        @Test
        @DisplayName("Test the interceptor returns false with a null payment")
        void preHandleNullPayment() throws Exception {

            pathVariables.put("paymentId", paymentId);
            paymentSummary = null;
            when(httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                    .thenReturn(pathVariables);
            boolean result = paymentInterceptor.preHandle(httpServletRequest, httpServletResponse,
                    null);
            assertFalse(result);
        }


    }
