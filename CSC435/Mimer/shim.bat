@echo on
rem This is shim.bat
rem Change this to your development directory:
cd D:\WorkSpace\GitHub\Course\CSC435\Mimer
echo "We are now in a shim called from the Web Browser"
echo Arg one is: %1
rem Change this to point to your Handler directory:
cd D:\WorkSpace\GitHub\Course\CSC435\Mimer
pause
rem have to set classpath in batch, passing as arg does not work.
rem Change this to point to your own Xstream library files:
rem set classpath=%classpath%C:\dp\435\java\mime-xml\;c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar;
set classpath=%classpath%D:\WorkSpace\GitHub\Course\CSC435\Mimer;JAVA_HOME\lib;
rem pass the name of the first argument to java:
java -Dfirstarg=%1 Handler
pause