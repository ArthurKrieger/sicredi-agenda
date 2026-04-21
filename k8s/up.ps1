# up.ps1 - Script corrigido

# 1. REMOVE TODAS AS IMAGENS ANTIGAS
Write-Host "Removendo imagens antigas..."
docker rmi agenda:local agenda:latest docker-agenda:latest -f 2>$null

# 2. COMPILA
cd ..
Write-Host "Compilando..."
.\gradlew clean build -x test
if ($LASTEXITCODE -ne 0) { exit 1 }

# 3. BUILD DOCKER
Write-Host "Buildando imagem..."
docker build -t agenda:local .
if ($LASTEXITCODE -ne 0) { exit 1 }

# 4. VERIFICA
Write-Host "Imagens disponíveis:"
docker images | findstr agenda

# 5. SOBE KUBERNETES
cd k8s
Write-Host "Subindo Kubernetes..."

# Delete tudo primeiro
kubectl delete -f agenda/ -f localstack/ -f postgres/ 2>$null

# Postgres
kubectl apply -f postgres/postgres-secret.yaml
kubectl apply -f postgres/postgres-pvc.yaml
kubectl apply -f postgres/postgres-statefulset.yaml
kubectl apply -f postgres/postgres-service.yaml
kubectl rollout status statefulset/postgres --timeout=120s

# LocalStack
kubectl apply -f localstack/localstack-deployment.yaml
kubectl apply -f localstack/localstack-service.yaml
kubectl rollout status deployment/localstack --timeout=120s

# SNS
kubectl apply -f localstack/sns-init-job.yaml

# Agenda
kubectl apply -f agenda/agenda-service.yaml
kubectl apply -f agenda/agenda-ingress.yaml
kubectl apply -f agenda/agenda-deployment.yaml
kubectl rollout status deployment/agenda --timeout=120s

Write-Host "Pronto!"
kubectl get pods