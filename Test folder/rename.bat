@echo off
echo "Current name: %~nx1"
set /P new_Name="Enter new Name(with the extension): "
::echo "New name entered: %new_Name%"
ren %1 "%new_Name%"
::echo 3 %1 "%new_name%"
java -cp "%CLASSPATH%" RokuLog 3 %1 "%new_name%"
