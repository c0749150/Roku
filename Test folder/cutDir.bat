@echo off
>"C:\Test folder\clip.txt" echo 22
echo %1>>"C:\Test folder\clip.txt"
>>"C:\Test folder\clip.txt" echo %~nx1