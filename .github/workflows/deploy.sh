#!/bin/bash

set -e

CONTAINER_NAME="weindependent-backend-container"
IMAGE_NAME="weindependent-backend-image"
PORT=8080

echo "[+] Pull latest code"
cd We-In-Dependent-Backend

echo "[+] Build new Docker image"
sudo DOCKER_BUILDKIT=1 docker build -t ${IMAGE_NAME} .

echo "[+] Stop and remove old container (if exists)"
sudo docker stop ${CONTAINER_NAME} || true
sudo docker rm ${CONTAINER_NAME} || true

echo "[+] Start new container on port ${PORT}"
sudo docker run -d --name ${CONTAINER_NAME} -p ${PORT}:${PORT} ${IMAGE_NAME}:latest

echo "[✓] Deployment complete. App running on port ${PORT}"
