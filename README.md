# RMI-File-Submission-System
Distributed Assignment#2 - Coded with Java - An RMI File Submission System

Code By: Daljit Sohi
ID: 100520358


Files and Folders Description:

Main Java File
----------------------------------------------------------------------------------------------------------------------------------

Client Files ->>
- ClientGUI.java
  -> Allows TA's and Student's to login. 
  
- SubmitAssignment.java
  -> Allows Students to submit their Assignments. 
  
- ViewSubmissions.java
  -> Allows TA's to view all the submissions.
  
  -> All the about classes, make a call to the RMI registry, to access the needed Implementations of the methods 
    declared by FileAppInterface.java
  
- FileAppInterface.java
  -> Declaration of the FileApp object's methods, that resides in the RMI Registry.

Server Files ->>
FileAppInterface.java
  -> Declaration of the FileApp object's methods, that resides in the RMI Registry.
  
FileApp.java
  -> Containts the Implements the 'FileAppInterface.java', and defines it's methods.

Server.java
  -> Places and instance of FileApp object in the Registry.
  -> Starts the registry, and with allows for Remote Method Invocations.


Server File
----------------------------------------------------------------------------------------------------------------------------------
Stores the Assignments submitted by students
Contains nessassry files to log Students, and TAs into the System.

  
  
  
