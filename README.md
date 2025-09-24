# GRUPA_W_CHMURZE_BACKEND

A Spring Boot 3 REST API for a simple To-Do application backed by MongoDB, containerized with Docker, deployable to Kubernetes (AKS), and provisionable via Azure Bicep.

## Overview
- **Language/Runtime**: Java 17, Spring Boot 3.4
- **Persistence**: MongoDB (via Spring Data MongoDB)
- **Docs/UI**: OpenAPI/Swagger UI (springdoc)
- **Container**: Docker (multi-stage build)
- **Kubernetes**: Deployment + Service manifests
- **Infra as Code**: Azure Bicep for ACR + AKS at subscription scope

## Repository Structure
- `src/main/java/com/example/ToDoApp` — Spring Boot app code
  - `controller` — REST endpoints for users and tasks
  - `service` — business logic
  - `model` — MongoDB document models
  - `config` — Swagger and CORS
- `src/main/resources/application.yaml` — app configuration
- `Dockerfile` — multi-stage container build
- `docker-compose.yaml` — local container run with env file
- `kube/` — Kubernetes manifests (Deployment, Service, Secret, ConfigMap)
- `infra/` — Bicep templates for ACR and AKS

## Prerequisites
- Java 17 (or Docker for containerized build)
- Maven 3.9+
- Docker 24+
- kubectl 1.30+
- Azure CLI (for Bicep deploy) and `az bicep` installed

## Environment Variables
The application requires a MongoDB connection string via `MONGO_URI`:
- `src/main/resources/application.yaml` uses `${MONGO_URI}`.
- For local/dev, create an `.env` file (used by docker-compose):

```
MONGO_URI=mongodb+srv://<user>:<pass>@<cluster-host>/<db>?retryWrites=true&w=majority
```

Warning: Do not commit real credentials. The sample Kubernetes secret currently contains a plaintext example — replace it in your environment before deploying.

## Build and Run (Local)
- Run with Maven:
```
./mvnw spring-boot:run
```
Or package and run:
```
./mvnw clean package -DskipTests
java -jar target/GRUPA_W_CHMURZE_BACKEND-0.0.1-SNAPSHOT.jar
```
Ensure `MONGO_URI` is exported in your shell if not using Docker:
```
export MONGO_URI=...
```

## Docker
Build the image (multi-stage):
```
docker build -t grupawchmurze/backend:local .
```
Run with docker-compose (uses `.env`):
```
docker compose up --build
```
The service will be available at `http://localhost:8080`.

Image reference used in manifests:
- `grupawchmurzebackendacrregestry.azurecr.io/grupa-w-chmurze-back-end:v3`
Update this if you publish to a different registry.

## API Endpoints
Base URL: `http://localhost:8080`

- Users
  - `GET /users/` — list users
  - `GET /users/{id}` — get user by id
  - `POST /users` — create user (body: `{ email, password }`)
  - `DELETE /users/{id}` — delete user

- Tasks
  - `GET /tasks/user/{userId}` — list tasks for user
  - `POST /tasks/user/{userId}` — add task to user (body: `{ id, title, completed }`)
  - `DELETE /tasks/user/{userId}/task/{taskId}` — delete task from user
  - `PUT /tasks/{userId}/tasks` — update a task for user (body: `{ id, title, completed }`)

- Swagger UI
  - Available at `/swagger-ui/index.html`

CORS is configured via `WebConfig` to allow common dev origins; adjust as needed.

## Kubernetes Deployment (AKS or any K8s)
Manifests in `kube/`:
- `grupa-w-kubie.yaml` — `Deployment` and `Service` (LoadBalancer)
- `secrets.yaml` — `Secret` (MONGO_URI) and `ConfigMap`

Steps:
```
# 1) Set/replace secret value before applying
# Edit kube/secrets.yaml and set stringData.MONGO_URI appropriately

# 2) Create namespace if desired
kubectl create namespace todo || true

# 3) Apply manifests
kubectl apply -n todo -f kube/secrets.yaml
kubectl apply -n todo -f kube/grupa-w-kubie.yaml

# 4) Wait for external IP
kubectl get svc -n todo grupa-w-chmurze-back-end-service -w
```
Update the container image in `kube/grupa-w-kubie.yaml` to match your registry and tag.

## Azure: Provision ACR + AKS via Bicep
Bicep files in `infra/`:
- `main.bicep` (subscription-level): creates RG, then deploys `main.resources.bicep` to RG
- `main.resources.bicep`: creates ACR (Basic) and AKS, and assigns `AcrPull` to AKS kubelet
- `params.json`: example parameters (contains an SSH key sample)

Deploy:
```
# Log in and select subscription
az login
az account set --subscription <SUBSCRIPTION_ID>

# Deploy subscription-scoped Bicep (creates resource group, ACR, AKS)
az deployment sub create \
  --location <azure-region> \
  --template-file infra/main.bicep
```
Outputs include resource group name, ACR name, and AKS name.

Connect kubectl:
```
az aks get-credentials -g <RESOURCE_GROUP> -n <AKS_NAME>
```

Push image to ACR:
```
# Log in to ACR
az acr login -n <ACR_NAME>

# Tag and push
docker tag grupawchmurze/backend:local <ACR_NAME>.azurecr.io/grupa-w-chmurze-back-end:v3
docker push <ACR_NAME>.azurecr.io/grupa-w-chmurze-back-end:v3
```

Deploy manifests (ensure image matches your ACR):
```
kubectl apply -f kube/secrets.yaml
kubectl apply -f kube/grupa-w-kubie.yaml
```

## Configuration Notes
- `MONGO_URI` must point to a reachable MongoDB instance; the app uses Spring Data MongoDB default database from URI.
- CORS: `WebConfig` allows `http://4.231.122.88:5001` and `http://localhost:5500` plus wildcard mapping; refine for production.
- Actuator is included; expose endpoints as needed via `management.*` properties.

## Development Tips
- Use `springdoc-openapi` for interactive docs at `/swagger-ui/index.html`.
- Prefer creating users before assigning tasks.
- Mongo relations are via `@DBRef` from `User` to `Task`.

## Cleaning Up
```
kubectl delete -f kube/grupa-w-kubie.yaml
kubectl delete -f kube/secrets.yaml
```

