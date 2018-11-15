package uk.gov.companieshouse.web.payments.utilities;

import org.apache.commons.lang.ArrayUtils;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("payments.web.ch.gov.uk");

    public static final String OAUTH2_IDENTITY_TYPE          = "oauth2";
    public static final String API_KEY_IDENTITY_TYPE         = "apikey";
    public static final String ADMIN_TRANSACTION_LOOKUP_ROLE = "/admin/transaction-lookup";

    private static final String ERIC_IDENTITY         = "ERIC-Identity";
    private static final String ERIC_IDENTITY_TYPE    = "ERIC-Identity-Type";
    private static final String ERIC_AUTHORISED_USER  = "ERIC-Authorised-User";
    private static final String ERIC_AUTHORISED_SCOPE = "ERIC-Authorised-Scope";
    private static final String ERIC_AUTHORISED_ROLES = "ERIC-Authorised-Roles";

    public static String getAuthorisedIdentity(HttpServletRequest request) {
        return getRequestHeader(request, AuthenticationHelper.ERIC_IDENTITY);
    }

    public static String getAuthorisedIdentityType(HttpServletRequest request) {
        return getRequestHeader(request, AuthenticationHelper.ERIC_IDENTITY_TYPE);
    }

    public static String getAuthorisedUser(HttpServletRequest request) {
        return getRequestHeader(request, AuthenticationHelper.ERIC_AUTHORISED_USER);
    }

    public static String getAuthorisedScope(HttpServletRequest request) {
        return getRequestHeader(request, AuthenticationHelper.ERIC_AUTHORISED_SCOPE);
    }

    public static String getAuthorisedCompany(HttpServletRequest request) {
        final String scope = getAuthorisedScope(request);

        // company number is last 8 digits of scope
        if (scope == null) {
            return null;
        }

        // scope should be like:
        // https://api.companieshouse.gov.uk/company/{company_number}
        if (scope.length() < 8 || !scope.contains("/")) {
            final Map<String, Object> debugMap = new HashMap<String, Object>();
            debugMap.put("scope", scope);
            LOGGER.debugRequest(request, "Invalid company scope", debugMap);

            return null;
        }
        return scope.substring(scope.lastIndexOf("/") + 1);
    }

    public static String getAuthorisedRoles(HttpServletRequest request) {
        return getRequestHeader(request, AuthenticationHelper.ERIC_AUTHORISED_ROLES);
    }

    public static String[] getAuthorisedRolesArray(HttpServletRequest request) {
        String roles = getAuthorisedRoles(request);
        if (roles == null || roles.length() == 0) {
            return new String[0];
        }

        // roles are space separated list of authorized roles
        return roles.split(" ");
    }

    public static boolean isRoleAuthorised(HttpServletRequest request, String role) {
        if (role == null || role.length() == 0) {
            return false;
        }

        String[] roles = getAuthorisedRolesArray(request);
        if (roles.length == 0) {
            return false;
        }

        return ArrayUtils.contains(roles, role);
    }

    private static String getRequestHeader(HttpServletRequest request, String header) {
        return request == null ? null : request.getHeader(header);
    }
}
