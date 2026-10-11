@echo off
echo ========================================================
echo Starting Blood Donor Availability Management System...
echo ========================================================

java -cp "bin;lib/mysql-connector-j-8.3.0.jar" com.blooddonor.Main

if %ERRORLEVEL% neq 0 (
    echo.
    echo Application exited or encountered an error.
    pause
)
