

/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mitra.creatiocrm.headers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.MessageContext;

public class BPMCSRFCookie extends AbstractMediator {

    private static final Log log = LogFactory.getLog(BPMCSRFCookie.class);
    private static String COOKIE_NAME = "set-cookie";
    private static String COOKIE_NAME_CAMELCASE = "Set-Cookie";
    private static String COOKIE_BPMCSRF = "BPMCSRF=";
    private static String AUTH_COMPLETE_PROPERTY = "authCompleteCookie";
    private static String BPMSESSIONID = "BPMSESSIONID";
    private static String USERNAME = "UserName";
    private static String BPMLOADER = "BPMLOADER";
    private static String ASPXAUTH = ".ASPXAUTH";
    private static String BPMCSRF = "BPMCSRF";

    public boolean mediate(MessageContext context) {

        String allCookies = "";
        Map<String, String> cookiesMap = new HashMap<String, String>();
        Object headers = ((Axis2MessageContext) context)
                .getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        Map<String, String> headerMap = (Map) headers;
        if (headerMap.containsKey(COOKIE_NAME_CAMELCASE)) {
            String val = headerMap.get(COOKIE_NAME_CAMELCASE);
            headerMap.remove(COOKIE_NAME_CAMELCASE);
            headerMap.put(COOKIE_NAME, val);
        }
        if (headerMap.containsKey(COOKIE_NAME)) {
            String cookies = headerMap.get(COOKIE_NAME);
            String authCookiesVal = cookies.split(";")[0];
            if (cookies.contains(COOKIE_BPMCSRF)) {
                context.setProperty(BPMCSRF, authCookiesVal.split(":")[1]);
            }
            cookiesMap.put(authCookiesVal.split("=")[0], cookies.split(";")[0]);
            headerMap.remove(COOKIE_NAME);
        }
        Map<String, List<String>> excessHeadersMap = (Map) ((Axis2MessageContext) context).getAxis2MessageContext().getProperty("EXCESS_TRANSPORT_HEADERS");
        if (excessHeadersMap.containsKey(COOKIE_NAME_CAMELCASE)) {
            List<String> val = excessHeadersMap.get(COOKIE_NAME_CAMELCASE);
            excessHeadersMap.remove(COOKIE_NAME_CAMELCASE);
            excessHeadersMap.put(COOKIE_NAME, val);
        }
        if (excessHeadersMap.containsKey(COOKIE_NAME)) {
            List<String> cookies = excessHeadersMap.get(COOKIE_NAME);
            for (String cookie : cookies) {
                String authCookiesVal = cookie.split(";")[0];
                cookiesMap.put(authCookiesVal.split("=")[0], cookie.split(";")[0]);
                if (cookie.contains(COOKIE_BPMCSRF)) {
                    context.setProperty(BPMCSRF, authCookiesVal.split("=")[1]);
                }
            }
        }
        allCookies = getCookieVal(cookiesMap, BPMSESSIONID, allCookies);
        allCookies = getCookieVal(cookiesMap, USERNAME, allCookies);
        allCookies = getCookieVal(cookiesMap, BPMLOADER, allCookies);
        allCookies = getCookieVal(cookiesMap, ASPXAUTH, allCookies);
        allCookies = getCookieVal(cookiesMap, BPMCSRF, allCookies);
        context.setProperty(AUTH_COMPLETE_PROPERTY, allCookies);
        return true;
    }

    private String getCookieVal(Map<String, String> cookieMap, String cookieName, String totalCookies) {
        if (cookieMap.containsKey(cookieName)) {
            if (totalCookies.isEmpty()) {
                totalCookies = cookieMap.get(cookieName);
            } else {
                totalCookies = totalCookies + "; " + cookieMap.get(cookieName);
            }
        }
        return totalCookies;
    }
}
