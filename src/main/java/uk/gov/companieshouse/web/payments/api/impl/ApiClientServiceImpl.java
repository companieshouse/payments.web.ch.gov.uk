package uk.gov.companieshouse.web.payments.api.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.sdk.manager.ApiClientManager;
import uk.gov.companieshouse.web.payments.api.ApiClientService;

@Component
public class ApiClientServiceImpl implements ApiClientService {
    @Value("${paymentPrivilegeKey}") // via application property
    private String paymentPrivilegeKey;

    @Override
    public ApiClient getPublicApiClient() {
        return ApiClientManager.getSDK(paymentPrivilegeKey);
    }

    @Override
    public InternalApiClient getPrivateApiClient() {
        return ApiClientManager.getPrivateSDK();
    }

}