# Build stage
FROM ghcr.io/graalvm/native-image-community:25 AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -Pnative -DskipTests native:compile

# Runtime stage
FROM debian:bookworm-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        zlib1g \
        ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /workspace/target/authorization .

USER 65532:65532

EXPOSE 8080

ENTRYPOINT ["./authorization"]
