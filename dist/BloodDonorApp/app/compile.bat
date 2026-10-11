@echo off
echo ========================================================
echo Compiling Blood Donor Availability Management System...
echo ========================================================

if not exist bin mkdir bin

javac -encoding UTF-8 -cp "lib/mysql-connector-j-8.3.0.jar" -d bin src/com/blooddonor/model/*.java src/com/blooddonor/db/*.java src/com/blooddonor/dao/*.java src/com/blooddonor/ui/*.java src/com/blooddonor/Main.java

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] Compilation completed successfully! Class files generated in 'bin' folder.
) else (
    echo [ERROR] Compilation failed. Please check errors above.
)
pause
