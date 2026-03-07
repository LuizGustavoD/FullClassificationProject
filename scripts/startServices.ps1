# iniciar tudo
# powershell -ExecutionPolicy Bypass -File .\scripts\startServices.ps1

# exemplo: subir sem frontend
# powershell -ExecutionPolicy Bypass -File .\scripts\startServices.ps1 -SkipFrontend



[CmdletBinding(SupportsShouldProcess = $true)]
param(
	[switch]$SkipPredict,
	[switch]$SkipMail,
	[switch]$SkipJava,
	[switch]$SkipFrontend
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Get-RepoRoot {
	$scriptsDir = $PSScriptRoot
	return Split-Path -Parent $scriptsDir
}

function Test-CommandAvailable {
	param(
		[Parameter(Mandatory = $true)]
		[string]$CommandName
	)

	return [bool](Get-Command -Name $CommandName -ErrorAction SilentlyContinue)
}

function Convert-ToSingleQuotedLiteral {
	param(
		[Parameter(Mandatory = $true)]
		[string]$Value
	)

	return $Value.Replace("'", "''")
}

function Start-ServiceShell {
	param(
		[Parameter(Mandatory = $true)]
		[string]$Name,
		[Parameter(Mandatory = $true)]
		[string]$WorkingDirectory,
		[Parameter(Mandatory = $true)]
		[string]$Command
	)

	if (-not (Test-Path -LiteralPath $WorkingDirectory)) {
		Write-Warning "[$Name] Pasta nao encontrada: $WorkingDirectory"
		return
	}

	$safeDir = Convert-ToSingleQuotedLiteral -Value $WorkingDirectory
	$launchScript = "Set-Location -LiteralPath '$safeDir'; Write-Host '[$Name] Rodando: $Command' -ForegroundColor Cyan; $Command"

	if ($PSCmdlet.ShouldProcess($Name, "Iniciar em nova janela PowerShell")) {
		Start-Process -FilePath 'powershell.exe' -ArgumentList @(
			'-NoExit',
			'-ExecutionPolicy',
			'Bypass',
			'-Command',
			$launchScript
		) | Out-Null

		Write-Host "[$Name] Inicializado." -ForegroundColor Green
	}
}

$repoRoot = Get-RepoRoot

Write-Host 'Iniciando servicos do projeto...' -ForegroundColor Yellow
Write-Host "Repositorio: $repoRoot"

$checks = @(
	@{ Name = 'python'; RequiredBy = @('predictConnection', 'mailService') },
	@{ Name = 'npm'; RequiredBy = @('frontend') },
	@{ Name = 'powershell'; RequiredBy = @('script launcher') }
)

foreach ($check in $checks) {
	if (-not (Test-CommandAvailable -CommandName $check.Name)) {
		Write-Warning "Comando '$($check.Name)' nao encontrado. Impacta: $($check.RequiredBy -join ', ')."
	}
}

if (-not $SkipPredict) {
	Start-ServiceShell -Name 'PredictConnection (Flask :5000)' `
		-WorkingDirectory (Join-Path $repoRoot 'predictConnection') `
		-Command 'python app.py'
}

if (-not $SkipMail) {
	Start-ServiceShell -Name 'MailService (Flask :5001)' `
		-WorkingDirectory (Join-Path $repoRoot 'mailService') `
		-Command 'python app.py'
}

if (-not $SkipJava) {
	Start-ServiceShell -Name 'Project API (Spring Boot :8080)' `
		-WorkingDirectory (Join-Path $repoRoot 'project') `
		-Command '.\mvnw.cmd spring-boot:run'
}

if (-not $SkipFrontend) {
	Start-ServiceShell -Name 'Frontend (Vite :5173)' `
		-WorkingDirectory (Join-Path $repoRoot 'frontend\img-vision-hub') `
		-Command 'npm run dev -- --port 5173'
}

Write-Host ''
Write-Host 'Servicos experimentais nao iniciados automaticamente:' -ForegroundColor DarkYellow
Write-Host '- streetPreviewConnection (app.py vazio)'
Write-Host '- translateConnection (app.py sem inicializacao de servidor)'
Write-Host ''
Write-Host 'Dica: use os parametros -SkipPredict -SkipMail -SkipJava -SkipFrontend para controle fino.' -ForegroundColor DarkGray
