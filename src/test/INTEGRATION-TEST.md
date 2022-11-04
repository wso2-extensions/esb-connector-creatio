## Integration tests for WSO2 EI Kafka connector

### Pre-requisites:

- Maven 3.x
- Java 1.8

### Tested Platform:

- Ubuntu 16.04
- WSO2 EI 6.5.0, EI 7.0.0

STEPS:

1. Download EI 6.5.0 by navigating the following the URL: https://wso2.com/integration/.

2. Get a clone or download the source from [Github](https://github.com/wso2-extensions/esb-connector-creatio).

3. Compress the EI zip and place it in "{CONNECTOR_HOME}/esb-connector-creatio-crm/repository/"

4. Update the Creatio server hostname, admin user credentials ine below tags in artifacts of the test/resources/artifacts/ESB/config/proxies/creatio location
   a.) <replace-with-creatio-host-name> Replace with Creatio Hostname
   b.) <replace-with-creatio-password>  Replace with Creatio Admin Username
   c.) <replace-with-creatio-username>  Replace with Creatio Admin password
4. Go to "{CONNECTOR_HOME}/esb-connector-creatio-crm/" and type "mvn clean install -Dskip-tests=false" to test and build.
