@echo off
setlocal

set "ROOT=%~dp0"

pushd "%ROOT%"
if not exist "out" mkdir "out"
if errorlevel 1 exit /b 1

powershell -NoProfile -Command "$sources = Get-ChildItem -LiteralPath 'src' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }; & javac --release 25 -Xlint:all -d 'out' $sources; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; & java -cp 'out' test.BibliotecaTest; exit $LASTEXITCODE"
set "RESULT=%ERRORLEVEL%"
popd
exit /b %RESULT%
