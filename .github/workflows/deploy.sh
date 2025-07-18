#!/bin/bash

set -e

# Check for required argument (Docker image name with tag)
if [ -z "$1" ]; then
  echo "Usage: ./deploy.sh <docker-image>"
  exit 1
fi

IMAGE_NAME="$1"
CONTAINER_NAME="weindependent-backend-container"
APP_PORT=8080

echo "[+] Pulling latest image: $IMAGE_NAME"
sudo docker pull "$IMAGE_NAME"

echo "[+] Stopping and removing old container (if exists)"
sudo docker stop "$CONTAINER_NAME" || true
sudo docker rm "$CONTAINER_NAME" || true

echo "[+] Starting new container from image"
sudo docker run -d \
  --name "$CONTAINER_NAME" \
  -p $APP_PORT:$APP_PORT \
  -v /home/ubuntu/We-In-Dependent-Backend/weindependent-app/src/main/resources/application.yaml:/config/application.yaml \
  "$IMAGE_NAME" \
  --spring.config.location=file:/config/application.yaml

echo "[✓] Deployment complete — $CONTAINER_NAME is running on port $APP_PORT"