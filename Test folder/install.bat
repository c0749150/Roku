@echo off
rem  set __COMPAT_LAYER=RunAsInvoker
REGEDIT.EXE  /S  "%~dp0\Roku context menu config.reg"
reg.exe add HKEY_CLASSES_ROOT\*\shell\Roku\Shell\cmd3\command /ve /f /t REG_EXPAND_SZ /d "\"%%JAVA_HOME%%\bin\java.exe\" -cp \"%%CLASSPATH%%\" RokuLog 0 \"%%1\"
reg.exe add HKEY_CLASSES_ROOT\Directory\shell\Roku\Shell\cmd3\command /ve /f /t REG_EXPAND_SZ /d "\"%%JAVA_HOME%%\bin\java.exe\" -cp \"%%CLASSPATH%%\" RokuLog 0 \"%%1\"

reg.exe add HKEY_CLASSES_ROOT\*\shell\Roku\Shell\cmd6\command /ve /f /t REG_EXPAND_SZ /d "\"%%JAVA_HOME%%\bin\java.exe\" -cp \"%%CLASSPATH%%\" RokuExtract \"%%1\"
reg.exe add HKEY_CLASSES_ROOT\Directory\shell\Roku\Shell\cmd6\command /ve /f /t REG_EXPAND_SZ /d "\"%%JAVA_HOME%%\bin\java.exe\" -cp \"%%CLASSPATH%%\" RokuExtract \"%%1\"