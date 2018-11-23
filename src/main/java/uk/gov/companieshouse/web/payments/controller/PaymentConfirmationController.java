package uk.gov.companieshouse.web.payments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.service.PaymentService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/payments/{paymentId}/confirm")
public class PaymentConfirmationController extends BaseController {

    private static final String PAYMENT_CONFIRM_VIEW = "payments/paymentConfirmation";

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
        try {
            model.addAttribute("paymentSummary", paymentService.getPaymentSummary(paymentId));
            model.addAttribute("paymentID", paymentId);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }
}
