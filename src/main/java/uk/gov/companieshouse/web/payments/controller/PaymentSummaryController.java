package uk.gov.companieshouse.web.payments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.service.PaymentService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/payments/{paymentId}/pay")
public class PaymentSummaryController extends BaseController {

    private static final String PAYMENT_SUMMARY_VIEW = "payments/paymentSummary";

    @Autowired
    private PaymentService paymentService;

    @Override
    protected String getTemplateName() {
        return PAYMENT_SUMMARY_VIEW;
    }

    @GetMapping
    public String getPaymentSummary(@PathVariable String paymentId,
                                     Model model,
                                     HttpServletRequest request) {
        try {
            model.addAttribute("paymentSummary", paymentService.getPaymentSummary(paymentId));
        } catch (ServiceException e) {
         LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postPayment(@PathVariable String paymentId, HttpServletRequest request) {

        try {
            String externalUrl = paymentService.getExternalPaymentUrl(paymentId);
            LOGGER.info("External URL is " + externalUrl);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        // TODO Redirect to Payment Provider
        return ERROR_VIEW;
    }
}
