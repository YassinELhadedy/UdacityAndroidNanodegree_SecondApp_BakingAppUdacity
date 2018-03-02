package com.nextbit.yassin.bakingapp.infrastructure.service;


import com.nextbit.yassin.bakingapp.domain.service.RetrofitClient;
import com.nextbit.yassin.bakingapp.domain.service.SOService;



public class ApiUtils {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}