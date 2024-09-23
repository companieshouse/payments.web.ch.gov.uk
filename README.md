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

## Terraform ECS

### What does this code do?

The code present in this repository is used to define and deploy a dockerised container in AWS ECS.
This is done by calling a [module](https://github.com/companieshouse/terraform-modules/tree/main/aws/ecs) from terraform-modules. Application specific attributes are injected and the service is then deployed using Terraform via the CICD platform 'Concourse'.


Application specific attributes | Value                                | Description
:---------|:-----------------------------------------------------------------------------|:-----------
**ECS Cluster**        |payments-service                                     | ECS cluster (stack) the service belongs to
**Load balancer**      |{env}-chs-chgovuk                                            | The load balancer that sits in front of the service
**Concourse pipeline**     |[Pipeline link]({{LINK_TO_PIPELINE}}) <br> [Pipeline code]({{LINK_TO_PIPELINE_CODE}})                                  | Concourse pipeline link in shared services


### Contributing
- Please refer to the [ECS Development and Infrastructure Documentation](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/4390649858/Copy+of+ECS+Development+and+Infrastructure+Documentation+Updated) for detailed information on the infrastructure being deployed.

### Testing
- Ensure the terraform runner local plan executes without issues. For information on terraform runners please see the [Terraform Runner Quickstart guide](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/1694236886/Terraform+Runner+Quickstart).
- If you encounter any issues or have questions, reach out to the team on the **#platform** slack channel.

### Vault Configuration Updates
- Any secrets required for this service will be stored in Vault. For any updates to the Vault configuration, please consult with the **#platform** team and submit a workflow request.

### Useful Links
- [ECS service config dev repository](https://github.com/companieshouse/ecs-service-configs-dev)
- [ECS service config production repository](https://github.com/companieshouse/ecs-service-configs-production)
