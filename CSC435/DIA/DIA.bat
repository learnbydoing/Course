rem % gradeagent.bat version 1.0
rem % Run this batch file to grade the DIA assignment
rem % You may need to add TIMEOUT 3 after each step to let the agents settle.
rem % If so, provide a gradeagent.bat file for me to run in the dir with the
rem % java source.

rem % Compile the source:

javac *.java
rem % HostServer.java,  NameServer.java, DIAdb.java

% Run the programs:

start java NameServer
rem pause
rem % start java DIAdb
start java HostServer
rem pause
start java HostServer 45051
rem pause
% Create five agents on two hostservers:
start iexplore http://localhost:45050
rem pause
start iexplore http://localhost:45050
rem pause
start iexplore http://localhost:45050
rem pause

start iexplore http://localhost:45051
rem pause
start iexplore http://localhost:45051
rem pause

% Connect to the nameserver:

start iexplore http://localhost:48060
