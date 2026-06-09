#!/usr/bin/env bash

set -e

# Configuration Definitions
# POD_NAME="spring-app-pod"
# IMAGE_NAME="localhost/spring-app:latest"
ENV_FILE=".env"

# 1. Verification Checklist
if [ ! -f "$ENV_FILE" ]; then
    echo "❌ Error: $ENV_FILE configuration file missing!"
    exit 1
fi

if [ ! -f "Dockerfile" ]; then
    echo "❌ Error: Dockerfile missing!"
    exit 1
fi

# Extract and export variables temporarily to use within this script execution
export $(grep -v '^#' "$ENV_FILE" | xargs)

# 2. Build Image
echo "🛠️ Compiling and building fresh Podman image..."
podman build -t "${IMAGE_NAME}" .

# 3. Environment Clean-up
echo "🔄 Tearing down existing pod structures..."
podman pod rm -f "${POD_NAME}" 2>/dev/null || true

# 4. Infrastructure Assembly
echo "📦 Initializing Podman Pod (Exposing app port 8080 only)..."
podman pod create --name "${POD_NAME}" -p ${AUTHORIZATION_SERVER_PORT}:8080

echo "🗄️ Injecting database infrastructure container..."
podman run -d \
    --pod "${POD_NAME}" \
    --name "${DB_CONTAINER}" \
    --env-file "$ENV_FILE" \
    postgres:16-alpine

echo "🚀 Launching Spring Boot context container..."
podman run -d \
    --pod "${POD_NAME}" \
    --name "${APP_CONTAINER}" \
    --env-file "$ENV_FILE" \
    -e SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/${POSTGRES_DB}" \
    -e SPRING_DATASOURCE_USERNAME="${POSTGRES_USER}" \
    -e SPRING_DATASOURCE_PASSWORD="${POSTGRES_PASSWORD}" \
    "${IMAGE_NAME}"

echo "🎉 Architecture deployment complete!"
echo "------------------------------------------------"
echo "Monitor Live System Logs:  podman logs -f ${POD_NAME}"
echo "Review Running Topology:   podman pod ps"
echo "------------------------------------------------"


