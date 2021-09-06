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

### Building a Docker container image

This project uses jib-maven-plugin to build Docker container images. To build a container image, run the following
command on the command line:

```bash
mvn compile jib:dockerBuild -Dimage=169942020521.dkr.ecr.eu-west-1.amazonaws.com/local/payments.web.ch.gov.uk:latest
```