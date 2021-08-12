package uk.gov.companieshouse.web.payments.api.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.sdk.manager.ApiClientManager;
import uk.gov.companieshouse.web.payments.api.ApiClientService;

@Component
public class ApiClientServiceImpl implements ApiClientService {

    @Override
    public ApiClient getPublicApiClient() {
        System.out.println("in getPublicAPIClient");
        return ApiClientManager.getSDK();
    }

    @Override
    public InternalApiClient getPrivateApiClient() {
        return ApiClientManager.getPrivateSDK();
    }

}