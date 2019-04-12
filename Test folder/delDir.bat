@echo off
java -cp "%CLASSPATH%" RokuLog 4 %1
rmdir /q /s %1