/*
 * Copyright (c) 2022, WSO2 LLC (https://www.wso2.com) All Rights Reserved.
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

package org.wso2.carbon.connector.creatiocrm.headers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class BPMCSRFCookie which to derive the HTTP headers from the Creatio Authentication response
 * Multiple Set-cookie HTTP headers contains Authentication information for rest of the API calls and process those and assign
 * to relevant context value in order to use in the Creatio connector implementation
 */
public class BPMCSRFCookie extends AbstractMediator {

    private static final String COOKIE_NAME = "set-cookie";
    private static final String COOKIE_NAME_CAMELCASE = "Set-Cookie";
    private static final String COOKIE_BPMCSRF = "BPMCSRF=";
    private static final String AUTH_COMPLETE_PROPERTY = "authCompleteCookie";
    private static final String BPMSESSIONID = "BPMSESSIONID";
    private static final String USERNAME = "UserName";
    private static final String BPMLOADER = "BPMLOADER";
    private static final String ASPXAUTH = ".ASPXAUTH";
    private static final String BPMCSRF = "BPMCSRF";

    /**
     * Set the authentication cookie values to context reading from the HTTP Headers
     *
     * @param context - messageContext
     * @return boolean - true or false
     */
    public boolean mediate(MessageContext context) {

        String allCookies = "";
        Map<String, String> cookiesMap = new HashMap<String, String>();
        Object headers = ((Axis2MessageContext) context)
                .getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        Map<String, String> headerMap = (Map) headers;

        if (headerMap.containsKey(COOKIE_NAME_CAMELCASE) || headerMap.containsKey(COOKIE_NAME)) {
            String cookie = headerMap.get(COOKIE_NAME_CAMELCASE);
            if (cookie.isEmpty()) {
                cookie = headerMap.get(COOKIE_NAME);
            }
            String authCookiesVal = cookie.split(";")[0];
            if (cookie.contains(COOKIE_BPMCSRF)) {
                context.setProperty(BPMCSRF, authCookiesVal.split(":")[1]);
            }
            cookiesMap.put(authCookiesVal.split("=")[0], cookie.split(";")[0]);
        }

        Map<String, List<String>> excessHeadersMap = (Map) ((Axis2MessageContext) context).getAxis2MessageContext().getProperty("EXCESS_TRANSPORT_HEADERS");
        if (excessHeadersMap.containsKey(COOKIE_NAME_CAMELCASE) || excessHeadersMap.containsKey(COOKIE_NAME)) {
            List<String> cookies = excessHeadersMap.get(COOKIE_NAME_CAMELCASE);
            if (cookies == null) {
                cookies = excessHeadersMap.get(COOKIE_NAME);
            }
            if (cookies != null) {
                for (String cookie : cookies) {
                    String authCookiesVal = cookie.split(";")[0];
                    cookiesMap.put(authCookiesVal.split("=")[0], cookie.split(";")[0]);
                    if (cookie.contains(COOKIE_BPMCSRF)) {
                        context.setProperty(BPMCSRF, authCookiesVal.split("=")[1]);
                    }
                }
            }
        }
        allCookies = loadAllCookies(cookiesMap, allCookies);
        context.setProperty(AUTH_COMPLETE_PROPERTY, allCookies);
        return true;
    }

    /**
     * Get the total cookie values for Authentication
     *
     * @param cookiesMap
     * @param allCookies
     * @return String value with the all cookie information
     */
    private String loadAllCookies(Map<String, String> cookiesMap, String allCookies) {

        allCookies = getCookieVal(cookiesMap, BPMSESSIONID, allCookies);
        allCookies = getCookieVal(cookiesMap, USERNAME, allCookies);
        allCookies = getCookieVal(cookiesMap, BPMLOADER, allCookies);
        allCookies = getCookieVal(cookiesMap, ASPXAUTH, allCookies);
        allCookies = getCookieVal(cookiesMap, BPMCSRF, allCookies);
        return allCookies;
    }

    /**
     * Extract individual cookie values
     *
     * @param cookieMap
     * @param cookieName
     * @param totalCookies
     * @return total cookies
     */
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
