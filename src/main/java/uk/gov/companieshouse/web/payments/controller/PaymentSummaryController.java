package uk.gov.companieshouse.web.payments.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.payments.exception.ServiceException;
import uk.gov.companieshouse.web.payments.model.AvailablePaymentMethods;
import uk.gov.companieshouse.web.payments.model.PaymentSummary;
import uk.gov.companieshouse.web.payments.model.PaymentMethodChoice;
import uk.gov.companieshouse.web.payments.service.externalpayment.ExternalPaymentService;
import uk.gov.companieshouse.web.payments.service.payment.PaymentService;
import uk.gov.companieshouse.web.payments.transformer.PaymentMethodTransformer;
import uk.gov.companieshouse.web.payments.util.PaymentStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping({"/payments/{paymentId}/pay", "/payments/{paymentId}/pay/api-key"})
public class PaymentSummaryController extends BaseController {

    private static final String PAYMENT_SUMMARY_VIEW = "payments/paymentSummary";
    private static final String PAYMENT_METHOD_CHOICE_VIEW = "payments/paymentMethodChoice";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ExternalPaymentService externalPaymentService;

    @Autowired
    private PaymentMethodTransformer paymentMethodTransformer;

    @Override
    protected String getTemplateName() {
        return PAYMENT_SUMMARY_VIEW;
    }

    @GetMapping
    public String getPaymentSummary(@PathVariable String paymentId,
                                    @RequestParam(value = "summary", required = false, defaultValue = "true") Boolean summary,
                                     Model model,
                                     HttpServletRequest request) {

        PaymentSummary paymentSummary;
        Boolean isAPIKey = request.getRequestURI().contains("api-key");
        try {
            paymentSummary = paymentService.getPayment(paymentId, isAPIKey);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        if (StringUtils.equals(paymentSummary.getStatus(), (PaymentStatus.PAYMENT_STATUS_PAID.paymentStatus()))) {
            LOGGER.errorRequest(request, "payment session status is paid, cannot pay again");
            return  ERROR_VIEW;
        }

        // Set the available payment methods using the first resource that is to be paid for
        // If there is more than one resource and they have different available payment methods this error will be found in the API.
        AvailablePaymentMethods availablePaymentMethods = new AvailablePaymentMethods();
        availablePaymentMethods.setAvailablePaymentMethods(paymentSummary.getPayments().get(0).getPaymentMethods());

        // If there is only one payment method do not display screen to choose payment type
        if (availablePaymentMethods.getAvailablePaymentMethods().size() == 1) {
            if (summary.equals(false)) {
                PaymentMethodChoice paymentMethodChoice = new PaymentMethodChoice();
                paymentMethodChoice.setSelectedPaymentMethod(availablePaymentMethods.getAvailablePaymentMethods().get(0));

                // If summary query parameter is set to false do not display summary screen and return ExternalPayment URL
                return postExternalPayment(paymentId, request, paymentMethodChoice, null, null);
            } else {
                // If no summary query parameter is set then show summary screen
                model.addAttribute("paymentSummary", paymentSummary);
                model.addAttribute("chosenPaymentMethod", availablePaymentMethods.getAvailablePaymentMethods());
                return getTemplateName();
            }
        } else {
            if (summary.equals(false)) {
                // If there is more than one payment method then the screen can not be skipped
                LOGGER.errorRequest(request, "A parameter cannot be passed to skip the summary screen if there is more than one payment method available");
                return ERROR_VIEW;
            } else {
                // If there is more than one available payment method a screen must be displayed to choose which payment method the user wants to use
                model.addAttribute("availablePaymentMethods", availablePaymentMethods.getAvailablePaymentMethods());
                model.addAttribute("paymentMethods", new PaymentMethodChoice());
                return PAYMENT_METHOD_CHOICE_VIEW;
            }
        }
    }

    @PostMapping
    public String postExternalPayment(
            @PathVariable String paymentId,
            HttpServletRequest request,
            @ModelAttribute("paymentMethods") @Valid PaymentMethodChoice paymentMethodChoice,
            BindingResult bindingResult,
            Model model) {

        Boolean isAPIKey = request.getRequestURI().contains("api-key");

        // Generate payments summary again to repopulate available payment methods
        PaymentSummary paymentSummary;
        try {
            paymentSummary = paymentService.getPayment(paymentId, isAPIKey);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        AvailablePaymentMethods availablePaymentMethods = new AvailablePaymentMethods();
        availablePaymentMethods.setAvailablePaymentMethods(paymentSummary.getPayments().get(0).getPaymentMethods());

        // Only validate page if there is more than one payment method available to be chosen
        if (availablePaymentMethods.getAvailablePaymentMethods().size() > 1) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("availablePaymentMethods", availablePaymentMethods.getAvailablePaymentMethods());
                return PAYMENT_METHOD_CHOICE_VIEW;
            }
        } else {
            // If there is only one payment method set it as the selected payment method
            paymentMethodChoice.setSelectedPaymentMethod(availablePaymentMethods.getAvailablePaymentMethods().get(0));
        }

        String journeyUrl;

        // Patch chosen Payment Method for payment session.
        try {
            paymentService.patchPayment(paymentId, paymentMethodTransformer.getDataPaymentMethod(paymentMethodChoice.getSelectedPaymentMethod()), isAPIKey);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        // Post to API to start external journey with chosen Payment provider.
        try {
            journeyUrl = externalPaymentService.createExternalPayment(paymentId, isAPIKey);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + journeyUrl;
    }
}
