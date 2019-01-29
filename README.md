# Companies House Payments Web Service
The Companies House Web Service for handling payments. This application is written using the [Spring Boot](http://projects.spring.io/spring-boot/) Java framework.

- Retrieves Payments data via the [SDK Manager](https://github.com/companieshouse/sdk-manager-java).
- Displays web screens required for payments.

### Requirements
In order to run this Web App locally you will need to install:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)
- [Payments API](https://github.com/companieshouse/payments.api.ch.gov.uk)

### Getting Started

1. [Configure your service](#configuration) if you want to override any of the defaults.
1. Run `make`
1. Run `./start.sh`


### Configuration

Key                | Description
-------------------|------------------------------------
`PAYMENTS_WEB_PORT`|The port of the Payments Web service
`HUMAN_LOG`        |For human readable logs


### Web Pages

Page           | Address
---------------|-----------------------------
Payment Summary| `/payments/<payment_id>/pay`
