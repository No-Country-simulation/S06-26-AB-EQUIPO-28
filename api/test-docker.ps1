function Write-Success { Write-Host $args[0] -ForegroundColor Green }
function Write-Info { Write-Host $args[0] -ForegroundColor Cyan }
function Write-Warning { Write-Host $args[0] -ForegroundColor Yellow }
function Write-Error-Custom { Write-Host $args[0] -ForegroundColor Red }

# ============ VERIFICACIONES PREVIAS ============
Write-Info "Verificando requisitos..."

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error-Custom "[X] Maven no está instalado"
    exit 1
}

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Error-Custom "[X] Docker no está instalado"
    exit 1
}

if (-not (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
    Write-Error-Custom "[X] Docker Compose no está instalado"
    exit 1
}

Write-Success " [OK] Todos los requisitos están presentes`n"

# ============ FUNCIONES DE UTILIDAD ============
function Test-ServiceHealth {
    param (
        [string]$Name,
        [string]$Url,
        [string]$ExpectedContent
    )
    try {
        $response = Invoke-WebRequest -Uri $Url -Method Get -TimeoutSec 5 -UseBasicParsing -ErrorAction Stop
        if ($response.StatusCode -eq 200 -and $response.Content -match $ExpectedContent) {
            return $true
        }
    } catch { }
    return $false
}

# ============ BUILD MAVEN ============
Write-Warning "Compilando proyecto Maven..."
$mavenStart = Get-Date
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Error-Custom "[X] Build Maven falló"
    exit 1
}

$mavenTime = ((Get-Date) - $mavenStart).TotalSeconds
Write-Success "Build Maven completado en $([math]::Round($mavenTime, 2)) segundos`n"

# ============ BUILD DOCKER ============
Write-Warning "Levantando infraestructura Docker..."
$dockerBuildStart = Get-Date

Write-Info "Limpiando sesión anterior..."
docker-compose down

# Levantar todo forzando la reconstrucción de la app
Write-Info "Iniciando contenedores..."
docker-compose up -d --build

if ($LASTEXITCODE -ne 0) {
    Write-Error-Custom "[X] Docker Compose falló"
    exit 1
}

$dockerBuildTime = ((Get-Date) - $dockerBuildStart).TotalSeconds
Write-Success "Contenedores iniciados en $([math]::Round($dockerBuildTime, 2)) segundos`n"

# ============ VERIFICACIONES DE SALUD ============
Write-Info "Esperando a que los servicios estén listos..."

# 1. Verificar PostgreSQL
$pgRetries = 0
$pgIsReady = $false
Write-Info "-> Verificando PostgreSQL..."

while ($pgRetries -lt 5) {
    $pgHealth = docker-compose exec -T postgres_db pg_isready -U postgres -d app_bit_docker_db 2>&1
    if ($pgHealth -match "accepting connections") {
        Write-Success "  [OK] PostgreSQL está listo y aceptando conexiones"
        $pgIsReady = $true
        break
    }
    $pgRetries++
    Start-Sleep -Seconds 3
}

if (-not $pgIsReady) {
    Write-Error-Custom "  [X] PostgreSQL falló tras varios intentos o no está configurado correctamente."
}

# 2. Verificar la API de Spring Boot
$appRetries = 0
$appIsReady = $false
Write-Info "-> Verificando App Bit API..."

while ($appRetries -lt 15) {
    $healthStatus = docker inspect --format="{{if .State.Health}}{{.State.Health.Status}}{{else}}unknown{{end}}" app_bit_api
    if ($healthStatus -eq "healthy") {
        Write-Success "  [OK] ¡La API está 100% lista y respondiendo!"
        $appIsReady = $true
        break
    }

    $appRetries++
    Write-Warning "  Esperando a que el servidor web levante... ($appRetries/15) - Estado actual: $healthStatus"
    Start-Sleep -Seconds 5
}

if (-not $appIsReady) {
    Write-Error-Custom "  [!] La API aún no responde. Es posible que siga ejecutando migraciones o haya un error."
}

# ============ RESUMEN FINAL ============
Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "           ¡SETUP COMPLETADO!                " -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

Write-Info "`nEndpoints Disponibles:"
Write-Info "  • API Base: http://localhost:8080"
Write-Info "  • Scalar: http://localhost:8080/scalar"
Write-Info "  • Actuator Health: http://localhost:8080/actuator/health"
Write-Info "  • Base de Datos: localhost:5433 (Usuario: postgres / BD: app_bit_docker_db)"

Write-Host "`nEstado de los Contenedores:"
docker-compose ps

Write-Host "`n"
$response = Read-Host "¿Deseas ver los logs en vivo de la API ahora? (S/N)"
if ($response -match "^[sS]$") {
    Write-Info "Mostrando logs... (Presiona Ctrl+C para salir)"
    docker-compose logs -f java-app
} else {
    Write-Info "Script finalizado. Usa 'docker-compose logs -f java-app' para ver los logs más tarde."
}