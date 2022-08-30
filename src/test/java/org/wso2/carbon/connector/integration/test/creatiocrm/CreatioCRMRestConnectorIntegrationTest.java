
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

package org.wso2.carbon.connector.integration.test.creatiocrm;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatioCRMRestConnectorIntegrationTest extends ConnectorIntegrationTestBase {
    private final Map<String, String> eiRequestHeadersMap = new HashMap<String, String>();

    private final Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        String connectorName = System.getProperty("connector_name") + "-connector-" +
                System.getProperty("connector_version") + ".zip";
        addCertificatesToEIKeyStore("client-truststore.jks", "wso2carbon");
        init(connectorName);
        eiRequestHeadersMap.put("Accept-Charset", "UTF-8");
        eiRequestHeadersMap.put("Content-Type", "application/json");
    }

    @Test(groups = {"wso2.ei"}, description = "Authentication for REST APIs")
    public void authenticationWithMandatoryParameters() throws Exception {
        String methodName = "postAuth";
        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, null);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertTrue(esbRestResponse.getHeadersMap().containsKey("BPMCSRF"));
        Assert.assertTrue(esbRestResponse.getHeadersMap().containsKey("Cookie"));
    }

    @Test(groups = {"wso2.ei"}, description = "Creatio Contact integration test for CRUD API call with Auth data")
    public void contactTestWithAuth() throws Exception {

        String methodName = "postAuth";
        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, null);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        methodName = "postContact";
        esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, "esb_contactPost_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
        String cId = esbRestResponse.getBody().getString("Id");

        methodName = "getContact";
        String endpoint = getProxyServiceURLHttp(methodName) + "?contactId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "GET",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        eiRequestHeadersMap.put("Accept-Charset", "UTF-8");
        eiRequestHeadersMap.put("Content-Type", "application/json");

        methodName = "updateContact";
        endpoint = getProxyServiceURLHttp(methodName) + "?contactId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "PUT",
                apiRequestHeadersMap, "esb_contactUpdate_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);

        methodName = "deleteContact";
        endpoint = getProxyServiceURLHttp(methodName) + "?contactId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "DELETE",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    @Test(groups = {"wso2.ei"}, description = "Creatio Contact integration test for CRUD API call without Auth data")
    public void contactTestWithoutAuth() throws Exception {

        String methodName = "postContact";
        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, "esb_contactPost_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
        String cId = esbRestResponse.getBody().getString("Id");

        methodName = "getContact";
        String endpoint = getProxyServiceURLHttp(methodName) + "?contactId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "GET",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        eiRequestHeadersMap.put("Accept-Charset", "UTF-8");
        eiRequestHeadersMap.put("Content-Type", "application/json");

        methodName = "updateContact";
        endpoint = getProxyServiceURLHttp(methodName) + "?contactId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "PUT",
                apiRequestHeadersMap, "esb_contactUpdate_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);

        methodName = "deleteContact";
        endpoint = getProxyServiceURLHttp(methodName) + "?contactId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "DELETE",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    @Test(groups = {"wso2.ei"}, description = "Creatio Case integration test for CRUD API call with Auth data")
    public void caseTestWithAuth() throws Exception {

        String methodName = "postAuth";
        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, null);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Map<String, List<String>> list = esbRestResponse.getHeadersMap();
        eiRequestHeadersMap.get(esbRestResponse.getHeadersMap().get("BPMCSRF"));
        eiRequestHeadersMap.get(esbRestResponse.getHeadersMap().get("set-cookie"));
        eiRequestHeadersMap.get(esbRestResponse.getHeadersMap().get("Cookie"));

        methodName = "postCase";

        esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, "esb_casePost_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);

        String cId = esbRestResponse.getBody().getString("Id");

        methodName = "getCase";
        String endpoint = getProxyServiceURLHttp(methodName) + "?caseId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "GET",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        methodName = "updateCase";
        endpoint = getProxyServiceURLHttp(methodName) + "?caseId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "PUT",
                apiRequestHeadersMap, "esb_caseUpdate_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);

        methodName = "deleteCase";
        endpoint = getProxyServiceURLHttp(methodName) + "?caseId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "DELETE",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    @Test(groups = {"wso2.ei"}, description = "Creatio Case integration test for CRUD API call without Auth data")
    public void caseTestWithoutAuth() throws Exception {

        String methodName = "postCase";

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, "esb_casePost_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);

        String cId = esbRestResponse.getBody().getString("Id");

        methodName = "getCase";
        String endpoint = getProxyServiceURLHttp(methodName) + "?caseId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "GET",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        methodName = "updateCase";
        endpoint = getProxyServiceURLHttp(methodName) + "?caseId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "PUT",
                apiRequestHeadersMap, "esb_caseUpdate_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);

        methodName = "deleteCase";
        endpoint = getProxyServiceURLHttp(methodName) + "?caseId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "DELETE",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    @Test(groups = {"wso2.ei"}, description = "Creatio Account integration test for CRUD API call with Auth data")
    public void accountTestWithAuth() throws Exception {

        String methodName = "postAuth";
        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, null);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Map<String, List<String>> list = esbRestResponse.getHeadersMap();
        eiRequestHeadersMap.get(esbRestResponse.getHeadersMap().get("BPMCSRF"));
        eiRequestHeadersMap.get(esbRestResponse.getHeadersMap().get("set-cookie"));
        eiRequestHeadersMap.get(esbRestResponse.getHeadersMap().get("Cookie"));

        methodName = "postAccount";
        esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, "esb_accountPost_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);

        String cId = esbRestResponse.getBody().getString("Id");
        methodName = "getAccount";
        String endpoint = getProxyServiceURLHttp(methodName) + "?accountId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "GET", eiRequestHeadersMap);

        methodName = "updateAccount";
        endpoint = getProxyServiceURLHttp(methodName) + "?accountId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "PUT",
                apiRequestHeadersMap, "esb_accountUpdate_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);

        methodName = "deleteAccount";
        endpoint = getProxyServiceURLHttp(methodName) + "?accountId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "DELETE",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    @Test(groups = {"wso2.ei"}, description = "Creatio Account integration test for CRUD API call without Auth data")
    public void accountTestWithoutAuth() throws Exception {

        String methodName = "postAccount";
        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(getProxyServiceURLHttp(methodName),
                "POST", eiRequestHeadersMap, "esb_accountPost_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);

        String cId = esbRestResponse.getBody().getString("Id");
        methodName = "getAccount";
        String endpoint = getProxyServiceURLHttp(methodName) + "?accountId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "GET", eiRequestHeadersMap);

        methodName = "updateAccount";
        endpoint = getProxyServiceURLHttp(methodName) + "?accountId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "PUT",
                apiRequestHeadersMap, "esb_accountUpdate_optional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);

        methodName = "deleteAccount";
        endpoint = getProxyServiceURLHttp(methodName) + "?accountId=" + cId;
        esbRestResponse = sendJsonRestRequest(endpoint, "DELETE",
                apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }
}
