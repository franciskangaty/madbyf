# Build stage
FROM ghcr.io/graalvm/native-image-community:25 AS build

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

COPY src ./src

# Build a fully static native executable
RUN ./mvnw -B -DskipTests -Pnative \
    -Dquarkus.native.additional-build-args=--static \
    native:compile

# Runtime stage
FROM scratch

WORKDIR /app

COPY --from=build /build/target/authorization /app/authorization

EXPOSE 8080

ENTRYPOINT ["/app/authorization"]
