@echo off
java -cp "%CLASSPATH%" RokuLog 4 %1
del /q /f %1