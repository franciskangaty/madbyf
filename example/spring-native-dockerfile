# --- Stage 1: Build the Native Executable ---
FROM ghcr.io/graalvm/native-image-community:25 AS builder

WORKDIR /build

# Copy build wrappers and configuration files first
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Optimisation: Pre-download Maven dependencies using a cache mount.
# This prevents downloading the internet on every single commit.
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the native application using a cache mount for the build tools
# Replace 'authorization-server' if your pom.xml specifies a custom artifactId/finalName
RUN --mount=type=cache,target=/root/.m2 ./mvnw -Pnative native:compile -DskipTests -B

# --- Stage 2: Ultra-lightweight Runtime ---
# We use Google's distroless base because it contains glibc (needed for GraalVM)
# but strips out the shell, package managers, and vulnerabilities.
FROM gcr.io/distroless/base-debian12

WORKDIR /workspace

# Copy the native binary from the builder stage
COPY --from=builder /build/target/authorization-server /workspace/application

# Expose the default application port
EXPOSE 8080

# Run the native binary directly
ENTRYPOINT ["/workspace/application"]
