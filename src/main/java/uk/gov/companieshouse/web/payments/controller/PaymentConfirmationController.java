package uk.gov.companieshouse.web.payments.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/payments/{paymentId}/confirm")
public class PaymentConfirmationController extends BaseController {

    private static final String PAYMENT_CONFIRM_VIEW = "payments/paymentConfirmation";
    public static final String PAYMENT_PAID = "paid";

    @Autowired
    private PaymentService paymentService;

    @Override
    protected String getTemplateName() {
        return PAYMENT_CONFIRM_VIEW;
    }

    @GetMapping
    public String getPaymentSummary(@PathVariable String paymentId,
                                     Model model,
                                     HttpServletRequest request) {

        PaymentSummary paymentSummary;

        try {
            paymentSummary = paymentService.getPayment(paymentId);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        if (!StringUtils.equals(paymentSummary.getStatus(), PAYMENT_PAID)) {
            LOGGER.errorRequest(request, "payment not complete. Incorrect status: " + paymentSummary.getStatus());
            return ERROR_VIEW;
        }

        model.addAttribute("paymentSummary", paymentSummary);
        model.addAttribute("paymentID", paymentId);

        return getTemplateName();
    }
}
