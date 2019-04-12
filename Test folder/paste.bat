@echo off
set rokuClip="C:\Test folder\clip.txt"
for /F "usebackq tokens=*" %%a in (%rokuClip%) do (
	if defined file.Source (set file.Name=%%a)
	if defined operationType if not defined file.Source (set file.Source=%%a)
	if not defined operationType (set operationType=%%a)
)
if %operationType%==0 (
	echo nothing to paste>"C:\Test folder\read.txt"
	exit
)
::set file.Source=^"%file.Source%^"
echo %file.Source%> "C:\Test folder\read.txt"

set file.Destination=%1
if %operationType% gtr 10 (set file.Destination=^"%file.Destination:"=%\%file.Name%^")

echo %file.Destination%>> "C:\Test folder\read.txt"

>>"C:\Test folder\read.txt" echo %operationType%

if %operationType%==1 (
	copy %file.Source% %file.Destination%
	set file.Destination=^"%file.Destination:"=%\%file.Name%^"
)
if %operationType%==11 (xcopy /s /i %file.Source% %file.Destination%)

if %operationType%==2 (
	move %file.Source% %file.Destination%
	>%rokuClip% echo 0
	set file.Destination=^"%file.Destination:"=%\%file.Name%^"
)
if %operationType%==22 (
	move %file.Source% %file.Destination%
	>%rokuClip% echo 0
)

java -cp "%CLASSPATH%" RokuLog %operationType% %file.Source% %file.Destination%