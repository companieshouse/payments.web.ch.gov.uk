package uk.gov.companieshouse.web.payments.interceptor;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.payments.session.SessionService;

@Component
public class UserDetailsInterceptor implements HandlerInterceptor {

  private static final String USER_EMAIL = "userEmail";

  private static final String SIGN_IN_KEY = "signin_info";
  private static final String USER_PROFILE_KEY = "user_profile";
  private static final String EMAIL_KEY = "email";
  private static final String SUMMARY_PARAM = "summary";

  @Autowired
  private SessionService sessionService;

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      @Nullable ModelAndView modelAndView) throws Exception {
    boolean urlContainsAPIKey = false;

    if (request.getRequestURI() != null) {
      urlContainsAPIKey = request.getRequestURI().contains("api-key");
    }

    if (modelAndView != null && (request.getMethod().equalsIgnoreCase("GET") ||
        (request.getMethod().equalsIgnoreCase("POST") &&
            !modelAndView.getViewName().startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)))
        && !urlContainsAPIKey) {
      Map<String, Object> sessionData = sessionService.getSessionDataFromContext();
      Map<String, Object> signInInfo = (Map<String, Object>) sessionData.get(SIGN_IN_KEY);
        // These details should only be added when the summary screen is NOT skipped
        // i.e. there is no summary parameter or the summary parameter is set to true.
        // This is due to Spring adding these attributes as query parameters when
        // redirecting in the postExternalPayment method of the
        // PaymentSummaryController.
        // This prevents exposing the users email address in the govpay logs.
        if (signInInfo != null
                && (request.getParameter(SUMMARY_PARAM) == null
                || Boolean.valueOf(request.getParameter(SUMMARY_PARAM)) != false)) {
            Map<String, Object> userProfile = (Map<String, Object>) signInInfo
                .get(USER_PROFILE_KEY);
            modelAndView.addObject(USER_EMAIL, userProfile.get(EMAIL_KEY));
      }
    }
  }
}
