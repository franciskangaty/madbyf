#!/usr/bin/env bash

set -euo pipefail

if [ ! -f .env ]; then
    echo ".env file not found"
    exit 1
fi

set -a
source .env
set +a

echo "Removing existing pod if present..."

podman pod rm -f "$POD_NAME" 2>/dev/null || true

echo "Creating volume..."

podman volume exists postgres_data || \
    podman volume create postgres_data

echo "Creating pod..."

podman pod create \
    --name "$POD_NAME" \
    -p "${AUTHORIZATION_SERVER_PORT}:8080"

echo "Starting PostgreSQL..."

podman run -d \
    --name "authorization-db" \
    --pod "$POD_NAME" \
    -e POSTGRES_DB="$AUTHORIZATION_POSTGRES_DB" \
    -e POSTGRES_USER="$AUTHORIZATION_POSTGRES_USER" \
    -e POSTGRES_PASSWORD="$AUTHORIZATION_POSTGRES_PASSWORD" \
    -v postgres_data:/var/lib/postgresql/data \
    "$DB_IMAGE"

echo "Starting Spring Boot application..."

podman run -d \
    --name "authorization-app" \
    --pod "$POD_NAME" \
    --env-file .env \
    "$APP_IMAGE"

echo "Waiting for containers..."

sleep 5

echo
echo "Pod status:"
podman pod ps

echo
echo "Containers:"
podman ps --pod

echo
echo "Generating Kubernetes manifest..."

podman generate kube "$POD_NAME" > "$KUBE_OUTPUT"

echo "Generated: $KUBE_OUTPUT"

echo
echo "Application URL:"
echo "http://localhost:${AUTHORIZATION_SERVER_PORT}"