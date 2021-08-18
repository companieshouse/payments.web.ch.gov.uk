package uk.gov.companieshouse.web.payments.api.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.sdk.manager.ApiClientManager;
import uk.gov.companieshouse.web.payments.api.ApiClientService;

@Component
public class ApiClientServiceImpl implements ApiClientService {
    @Value("${paymentPrivilegeKey}")
    private String paymentPrivilegeKey;

    @Override
    public ApiClient getPublicApiClient() {
        return ApiClientManager.getSDK();
    }

    @Override
    public ApiClient getPublicApiClientWithKey() {
        return ApiClientManager.getSDK(paymentPrivilegeKey);
    }

    @Override
    public InternalApiClient getPrivateApiClient() {
        return ApiClientManager.getPrivateSDK();
    }

    @Override
    public InternalApiClient getPrivateApiClientWithKey() {
        return ApiClientManager.getPrivateSDK(paymentPrivilegeKey);
    }

}