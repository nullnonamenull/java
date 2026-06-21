FROM eclipse-temurin:25-jre

RUN apt-get update && apt-get install -y \
    pandoc \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/doc-forge-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]