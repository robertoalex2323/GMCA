param(
    [string]$DbPassword
)

if (-not $DbPassword) {
    $secure = Read-Host "Supabase DB password" -AsSecureString
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
    try {
        $DbPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    } finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

$env:DB_URL = "jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres?sslmode=require&prepareThreshold=0"
$env:DB_USERNAME = "postgres.zwwbvahmcvptypyoheen"
$env:DB_PASSWORD = $DbPassword
$env:JWT_SECRET = "gmca-secret-dev-2026-cambiar-en-produccion"

Write-Host "Starting GMCA API with Supabase pooler..." -ForegroundColor Green
.\mvnw.cmd spring-boot:run