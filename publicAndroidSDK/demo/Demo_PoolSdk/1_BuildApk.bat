@echo off
cd /d %~dp0
"%ANT_ROOT%\bin\ant" clean release -f build.xml
pause