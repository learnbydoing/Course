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
rem % start java DIAdb
start java HostServer
start java HostServer 45051

% Create five agents on two hostservers:
start iexplore http://localhost:45050
start iexplore http://localhost:45050
start iexplore http://localhost:45050

start iexplore http://localhost:45051
start iexplore http://localhost:45051

% Connect to the nameserver:

start iexplore http://localhost:48060