#!/bin/bash

# Start script for payments.web.ch.gov.uk

PORT=8080
exec java -jar -Dserver.port="${PORT}" -XX:MaxRAMPercentage=80 "payments.web.ch.gov.uk.jar"
