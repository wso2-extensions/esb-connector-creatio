# Creatio REST EI Connector

The Creatio REST Connector allows you to work with below Creatio API functionalities.

Contact CRUD operation
Account CRUD operation
Case CRUD operation

## Compatibility

| Connector version | Supported Creatio REST API version | Supported WSO2 ESB/EI version |
| ------------- | ------------- | ------------- |
| [1.0.0]| v32.0 | EI 6.5.0, EI 6.6.0, EI 7.0.X, EI 7.1.0 |

## Getting started

## Building From the Source

Follow the steps given below to build the Creatio REST connector from the source code:

1. Get a clone or download the source from [Github](https://github.com/wso2-extensions/esb-connector-creatio/).
2. Run the following Maven command from the `esb-connector-creatiocrm` directory: `mvn clean install`.
3. The Creatio connector zip file is created in the `esb-connector-creatiocrm/target` directory

#### Deploy connector with mediator implemented CAR file 

1. Add connector creatiocrm-connector-1.1.0.zip to Integration Studio inorder to work with EI tooling, see [Working with Connectors via Tooling](https://docs.wso2.com/display/EI650/Working+with+Connectors+via+Tooling) and create the mediation

2. Import the Creatio server certificate to the EI client keystore using either the following commands or the EI Management Console.

```
    keytool -importcert -file <certificate_file> -keystore <EI>/repository/resources/security/client-truststore.jks -alias "creatio"
```

3. Deploy the car app to <EI-HOME>/micro-integrator/repository/deployment/server/carbonapps folder in WSO2 EI

4. Start the WSO2 EI instance like this.

```
	./micro-integrator.sh
```

5. Send the sample request to created mediation using Creatio connector.

## Headers

Headers : 
Content-Type: application/json
Content-Length :

6. ## Authentication Operation

Provide the Creatio Hostname and Admin user credentials under username and password

```
    <creatiocrm.init>
        <hostName>${Creatio hostname}</hostName>
        <password>${Admin passowrd}</password>
        <username>${Admin username}</username>
        <timeout>${timeout Value}</timeout>
    </creatiocrm.init>
    <creatiocrm.authSession/>
```
## Contact Operations

1. Create Contact

Creatio Contact create operation used in EI mediation. It will be used payload to create contact in Creatio

```
    <creatiocrm.contactCreate/>
```

2. Get Contact

Ex:
```
    <creatiocrm.contactGet>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.contactGet>
```

3. Patch Contact

```
    <creatiocrm.contactUpdate>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.contactUpdate>
```

4. Delete Contact

```
    <creatiocrm.contactDelete>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.contactDelete>
```

## Case Operations

    Body Format:
```
    {
        "Number": String,
        "RegisteredOn": Datetime,
        "Subject": String,
        "Symptoms": String,
        "Notes": String,
        "AccountId": String,
        "ContactId": String,
        "SolutionRemains": Double
    }
```
    Body Example:
```
    {
        "Number": "SR00000045",
        "RegisteredOn": "2021-07-16T11:00:00Z",
        "Subject": "Consultation on functionality",
        "Symptoms": "Comparison of different DBMS in accordance with the customer's needs",
        "Notes": "",
        "AccountId": "ff7e089f-1fe9-4ca9-bc30-2d76ad39d178",
        "ContactId": "00b34750-2feb-4545-b233-153502326f3c",
        "SolutionRemains": 0.0
    }
```

1. Create case
```
   <creatiocrm.caseCreate/>
```

2. Get case

```
    <creatiocrm.caseGet>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.caseGet>
```
3. Patch case

```
    <creatiocrm.caseUpdate>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.caseUpdate>
```

4. Delete case

```
    <creatiocrm.caseDelete>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.caseDelete>
```
## Account Operations


    Body Format:
```
    {
        "Name": String,
        "ProcessListeners": integer,
        "Code": String,
        "Phone": Timestamp,
        "AdditionalPhone": String,
        "Fax": String,
        "Web": String,
        "Address": String,
        "Notes":String
    }
```
    Sample Request:
```
    {
        "Name": "API Test",
        "ProcessListeners": 0,
        "Code": "73",
        "Phone": "+1 206 480 3801",
        "AdditionalPhone": "+1 206 480 4495",
        "Fax": "",
        "Web": "www.infocom-global.com",
        "Address": "48 Pilgrim Street",
        "Notes": ""
    }
```

1. Account Contact

```
   <creatiocrm.accountCreate/>
```

2. Get Account

```
    <creatiocrm.accountGet>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.accountGet>
```
    
3. Patch Account

```
    <creatiocrm.accountUpdate>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.accountUpdate>
```

4. Delete Account

```
    <creatiocrm.accountDelete>
        <id>{$ctx:uri.var.id}</id>
    </creatiocrm.accountDelete>
```
