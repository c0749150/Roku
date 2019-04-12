@echo off
>"C:\Test folder\clip.txt" echo 2
echo %1>>"C:\Test folder\clip.txt"
>>"C:\Test folder\clip.txt" echo %~nx1
::echo %~nx1>>"C:\Test folder\clip.txt"
::echo|set /p=%1>>"C:\Test folder\clip.txt"