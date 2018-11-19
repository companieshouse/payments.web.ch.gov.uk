package uk.gov.companieshouse.web.payments.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger("payments.web.ch.gov.uk");

    @Autowired
    private PaymentService paymentService;

    /**
     * Ensure requests are authenticated for the existence of a payment id and if it matches an
     * entry in the database.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        final Map<String, String> pathVariables = (Map) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final String paymentId = pathVariables.get("paymentId");
        if (paymentId == null) {
            final Map<String, Object> debugMap = new HashMap<String, Object>();
            debugMap.put("path_variables", pathVariables);
            LOGGER.debugRequest(request, "PaymentInterceptor error: no payment id", debugMap);

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("error");
            return false;
        }

        PaymentSummary payment = null;
        try {
            payment = paymentService.getPaymentSummary(paymentId);
        } catch (ServiceException serviceException) {
            final Map<String, Object> errorMap = new HashMap<String, Object>();
            errorMap.put("payment_id", paymentId);
            LOGGER.errorRequest(request, serviceException, errorMap);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("error");
            return false;
        }

        if (payment == null) {
            final Map<String, Object> debugMap = new HashMap<String, Object>();
            debugMap.put("payment_id", paymentId);
            LOGGER.debugRequest(request, "PaymentInterceptor not found: payment not found",
                    debugMap);

            response.sendRedirect("error");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        /**
         * create a map of useful properties for logging exit points within this
         * interceptor
         */
        final Map<String, Object> debugMap = new HashMap<String, Object>();
        debugMap.put("payment_id", paymentId);
        debugMap.put("request_method", request.getMethod());


        /**If all of the above conditions above are met then the request is
         * authorised
         */
        LOGGER.debugRequest(request, "PaymentInterceptor authorised", debugMap);
        return true;
    }
}
