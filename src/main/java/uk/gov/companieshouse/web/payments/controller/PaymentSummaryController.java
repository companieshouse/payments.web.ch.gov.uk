package uk.gov.companieshouse.web.payments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.service.externalpayment.ExternalPaymentService;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.util.PaymentMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/payments/{paymentId}/pay")
public class PaymentSummaryController extends BaseController {

    private static final String PAYMENT_SUMMARY_VIEW = "payments/paymentSummary";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ExternalPaymentService externalPaymentService;

    @Override
    protected String getTemplateName() {
        return PAYMENT_SUMMARY_VIEW;
    }

    @GetMapping
    public String getPaymentSummary(@PathVariable String paymentId,
                                     Model model,
                                     HttpServletRequest request) {
        try {
            model.addAttribute("paymentSummary", paymentService.getPayment(paymentId));
        } catch (ServiceException e) {
         LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postExternalPayment(@PathVariable String paymentId, HttpServletRequest request) {
        String journeyUrl;

        /**
         * Patch chosen Payment Method for payment session.
         */
        try {
            String paymentMethod = PaymentMethod.GOV_PAY.getPaymentMethod();
            paymentService.patchPayment(paymentId, paymentMethod);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        /**
         * Post to API to start external journey with chosen Payment provider.
         */
        try {
            journeyUrl = externalPaymentService.createExternalPayment(paymentId);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + journeyUrl;
    }
}
