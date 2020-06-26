FROM maven:3-openjdk-8-slim AS builder
WORKDIR /build
ENV MAVEN_REPOSITORY_URL="http://repository.aws.chdev.org:8081/"
CMD env | grep MAVEN

COPY pom.xml ./
RUN mvn verify --fail-never

COPY src ./src
RUN mvn package -DskipTests=true

# Runtime image
FROM gcr.io/distroless/java:8-debug
WORKDIR /app

COPY --from=builder /build/target/payments.web.ch.gov.uk-unversioned.jar payments.web.ch.gov.uk.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=3056", "/app/payments.web.ch.gov.uk.jar"]
