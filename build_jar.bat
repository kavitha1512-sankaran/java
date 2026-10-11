@echo off
echo ========================================================
echo Building Standalone Executable JAR (BloodDonorSystem.jar)...
echo ========================================================

if exist build rd /s /q build
mkdir build\temp

echo 1. Copying compiled classes...
xcopy /s /e /y bin\* build\temp\ > nul

echo 2. Extracting MySQL JDBC Driver into bundle...
cd build\temp
jar -x -f ..\..\lib\mysql-connector-j-8.3.0.jar
del /q META-INF\*.SF META-INF\*.DSA META-INF\*.RSA 2>nul
cd ..\..

echo 3. Creating Fat Executable JAR...
jar -c -f BloodDonorSystem.jar -e com.blooddonor.Main -C build\temp .

echo 4. Cleaning temporary files...
rd /s /q build

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] BloodDonorSystem.jar created successfully!
    echo Anyone with Java installed can now double-click BloodDonorSystem.jar to run!
) else (
    echo [ERROR] Failed to create JAR file.
)
pause
