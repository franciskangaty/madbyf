# Build stage
FROM ghcr.io/graalvm/native-image-community:25 AS build

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

COPY src ./src

RUN ./mvnw -B -DskipTests -Pnative native:compile

# Runtime stage
FROM gcr.io/distroless/base-debian12:nonroot

WORKDIR /app

COPY --from=build /build/target/authorization /app/authorization

EXPOSE 8080

ENTRYPOINT ["/app/authorization"]