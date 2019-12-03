/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.warp10.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class QueryParams {

    private final String SELECTOR = "selector";
    private final String START_DATE = "start";
    private final String STOP_DATE = "stop";
    private final String NOW = "now";
    private final String TIMESTAMP = "timespan";

    private Map<String, String> parameters = new HashMap<String, String>();

    private String className = "~.*";

    private Date startDate;

    private Date endDate;

    public static QueryParams builder() {
        QueryParams queryParams = new QueryParams();
        return queryParams;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        query.append(getDateFilter());
        query.append("&").append(SELECTOR).append("=");
        query.append(className).append(getLabelParameters()).append("&format=json&showattr=true&dedup=true");
        return query.toString();
    }

    private String getLabelParameters() {
        return "{" + parameters.entrySet().stream().map(entry -> entry.getKey() + '~' + entry.getValue()).collect(Collectors.joining(",")) + "}";
    }

    private String getDateFilter() {
        StringBuilder filter = new StringBuilder();
        if (startDate !=null && endDate != null) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            filter.append(START_DATE).append("=").append(df.format(startDate)).append("&").append(STOP_DATE).append("=").append(df.format(endDate));
        } else {
            filter.append(NOW).append("=now").append("&").append(TIMESTAMP).append("=-10");
        }

        return filter.toString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
