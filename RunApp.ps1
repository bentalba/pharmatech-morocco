# PharmaTech Morocco - Run App Script
# This script sets up the environment and runs the Android app

Write-Host "========================================"  -ForegroundColor Cyan
Write-Host "PharmaTech Morocco - App Launcher" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set up Java
$javaHome = "C:\Program Files\Android\Android Studio\jbr"
if (Test-Path $javaHome) {
    $env:JAVA_HOME = $javaHome
    $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "[OK] Java configured: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "[ERROR] Java not found at expected location" -ForegroundColor Red
    Write-Host "  Please install Android Studio or set JAVA_HOME manually" -ForegroundColor Yellow
    exit 1
}

# Set up Android SDK
$androidSdk = "$env:LOCALAPPDATA\Android\Sdk"
if (Test-Path $androidSdk) {
    $env:ANDROID_HOME = $androidSdk
    $env:ANDROID_SDK_ROOT = $androidSdk
    $env:PATH = "$androidSdk\platform-tools;$androidSdk\emulator;$env:PATH"
    Write-Host "[OK] Android SDK configured: $androidSdk" -ForegroundColor Green
} else {
    Write-Host "[ERROR] Android SDK not found" -ForegroundColor Red
    Write-Host "  Please install Android SDK via Android Studio" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Checking for Android emulators..." -ForegroundColor Yellow

# Check for running emulators
$adbPath = "$androidSdk\platform-tools\adb.exe"
$devicesOutput = & $adbPath devices 2>&1 | Select-Object -Skip 1 | Where-Object { $_ -match "device$" }

if ($devicesOutput) {
    Write-Host "[OK] Emulator is running" -ForegroundColor Green
} else {
    Write-Host "[WARNING] No emulator detected" -ForegroundColor Red
    Write-Host ""
    Write-Host "Available emulators:" -ForegroundColor Yellow
    $emulatorPath = "$androidSdk\emulator\emulator.exe"
    & $emulatorPath -list-avds 2>&1
    Write-Host ""
    Write-Host "To start an emulator:" -ForegroundColor Yellow
    Write-Host "  emulator -avd <emulator_name>" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Or use Android Studio: Tools > Device Manager > Start Emulator" -ForegroundColor Cyan
    Write-Host ""
    $response = Read-Host "Continue anyway? (y/n)"
    if ($response -ne "y") {
        exit 0
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Building and Installing App..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Build and install the app
Write-Host "Running: gradlew.bat installDebug" -ForegroundColor Yellow
Write-Host "This may take several minutes on first build..." -ForegroundColor Cyan
Write-Host ""
.\gradlew.bat installDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "[SUCCESS] App installed successfully!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "The app should now be installed on your emulator." -ForegroundColor Green
    Write-Host "Look for PharmaTech Morocco in the app drawer." -ForegroundColor Green
    Write-Host ""
    
    # Try to launch the app
    Write-Host "Attempting to launch app..." -ForegroundColor Yellow
    & $adbPath shell am start -n com.pharmatech.morocco/.MainActivity 2>&1 | Out-Null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] App launched!" -ForegroundColor Green
    } else {
        Write-Host "[INFO] Could not auto-launch. Please launch manually from emulator." -ForegroundColor Yellow
    }
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "[ERROR] Build failed" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please check the error messages above." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "  1. Missing google-services.json (see FIREBASE_SETUP_GUIDE.md)" -ForegroundColor Cyan
    Write-Host "  2. Missing API keys in gradle.properties" -ForegroundColor Cyan
    Write-Host "  3. Network connection required for first build" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

