@echo off
rem Compila o projeto e gera o JAR executavel (Windows).
rem Uso: build.bat      (precisa do JDK: javac e jar no PATH)
rem Se a classe principal mudar de nome, edite MAIN_CLASS abaixo.
setlocal
if "%MAIN_CLASS%"=="" set "MAIN_CLASS=locadora.ui.JanelaPrincipal"

cd /d "%~dp0"
set "MAIN_FONTE=src\%MAIN_CLASS:.=\%.java"
if not exist "%MAIN_FONTE%" (
    echo Classe principal nao encontrada: %MAIN_FONTE%
    exit /b 1
)

if exist bin rmdir /s /q bin
mkdir bin
rem -sourcepath src: o javac compila tambem tudo o que a classe principal usa.
javac -encoding UTF-8 -sourcepath src -d bin "%MAIN_FONTE%"
if errorlevel 1 exit /b 1

echo Main-Class: %MAIN_CLASS%> manifest.txt
jar cfm Locadora.jar manifest.txt -C bin .
if errorlevel 1 exit /b 1
del manifest.txt
echo Gerado: Locadora.jar   (execute com: java -jar Locadora.jar)
