FROM openjdk:17-slim

RUN apt-get update && apt-get install -y \
    ca-certificates \
    gnupg \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

COPY zscaler.crt /usr/local/share/ca-certificates/zscaler.crt
RUN update-ca-certificates && \
    keytool -importcert -noprompt \
        -trustcacerts \
        -alias zscaler \
        -file /usr/local/share/ca-certificates/zscaler.crt \
        -keystore "$JAVA_HOME/lib/security/cacerts" \
        -storepass changeit

WORKDIR /app

COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
