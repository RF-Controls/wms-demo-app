package com.rfcontrols.wms.service.impl;

public class QueryBuilderUtils {

    public static String buildWildcardQuery(String originalQuery){
        if(originalQuery.length() < 3) return originalQuery;
        return new StringBuilder(originalQuery).append("*").toString();
    }
}
