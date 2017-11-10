import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileApp extends UnicastRemoteObject implements FileAppInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// SERVER_ASSIGNMENT_STORAGE --> Folder where student assignments will be saved
	String SERVER_ASSIGNMENT_STORAGE = "C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/Submitted_Assignments";

	// Key -> Student #, Value -> Password
	Map<String, String> student_login = new HashMap<>();

	// Key -> Student #, Value -> Student Name
	Map<String, String> identify_student = new HashMap<>();

	// Key -> Student #, Value -> Assignment Name
	Map<String, String> student_assignments = new HashMap<>();

	// Key -> TA #, Value -> Password
	Map<String, String> TA_login = new HashMap<>();

	// Key -> TA #, Value -> Password
	Map<String, String> identify_TA = new HashMap<>();

	// Constructor
	protected FileApp() throws RemoteException {
		super();
	}

	public static void main(String[] args) {

	}// end main()

	/*
	 * --------------------------->>>>>>> Implemented Methods
	 * <<<<<<---------------------------
	 */

	// ------------------> STUDENT METHODS <------------------------------ //

	@Override
	public synchronized String loginStudent(String id, String pass) throws RemoteException {
		populateMaps();

		String studentID = id;
		String password = pass;
		String returnStmt = "";

		if (student_login.containsKey(studentID) && student_login.get(studentID).equals(password)) {
			returnStmt = studentID + ";" + identify_student.get(studentID) + ";" + "success";
		} else {
			returnStmt = "" + ";" + "" + ";" + "fail";
		}
		return returnStmt;
	}// end loginStudent()

	@Override
	public synchronized String submitSingleFile(String id, String inputFolder, String sName) throws RemoteException {

		String studentID = id;
		String assignment = inputFolder;
		String studentName = sName;
		String returnStmt = "";

		// First check if the student has already submitted an assignment
		/*
		 * Check if the HashMap already contains the given ID if it does -> print the
		 * assignment name, and let he user know "they can not submit more than once"
		 */
		if (student_assignments.containsKey(studentID)) {

			// returnStmt = "You have already submitted your assignment! \n"
			// + "Assignment Submitted: " + student_assignments.get(studentID) + "\n"
			// + "Can NOT submit more than ONCE! Thank You!";
			returnStmt = "fail" + ";" + student_assignments.get(studentID).toString();

		} else { // if the student has not submitted any assignments, take their submission

			// Assignment Submission will be stored on the Server Side

			// Folder where the assignments will be stored
			String folderName = SERVER_ASSIGNMENT_STORAGE;

			String fileName = studentName.replaceAll("\\s+", "");

			System.out.println("file Name --> " + fileName);

			String zipFolderName = fileName.concat(".zip"); // Give a Name to the "ZIP" Folder.

			String filePath = assignment; // inputPath of the file

			File file = new File(filePath);

			try {

				// Need a FileOutputStream to write the file to the ZipOutputStream
				FileOutputStream file_output_stream = new FileOutputStream(folderName + "/" + zipFolderName);

				ZipEntry zip_entry = new ZipEntry(file.getName()); // File we want to put into the 'ZIP' folder
				ZipOutputStream zip_output_stream = new ZipOutputStream(file_output_stream);// Use 'zip_output_stream'
																							// compresses the file, and
																							// uses 'file_output_stream'
																							// to write the file to the
																							// "ZIP" Folder
				zip_output_stream.putNextEntry(zip_entry); // Put the zip_entry, into the zip_output_stream.

				byte[] buffer = Files.readAllBytes(Paths.get(filePath));// read the file, to a byte 'buffer'
				zip_output_stream.write(buffer, 0, buffer.length);
				zip_output_stream.closeEntry();
				zip_output_stream.close();

				student_assignments.put(studentID, zipFolderName);
				System.out.println(student_assignments);

				// Write the studentID, and Assignment Name to a 'student_assignment.txt' File
				File list_assignments = new File(
						"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/student_assignments.txt");
				try {

					PrintWriter pw = new PrintWriter(new FileOutputStream(list_assignments));

					for (Map.Entry<String, String> pair : student_assignments.entrySet()) {
						pw.println(pair.getKey() + ":" + pair.getValue());
					}
					pw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("File could not be submitted!!");
			}

			returnStmt = "success" + ";" + "";

		} // end of if/else block

		return returnStmt;
	}// end submitAssignment()

	// ------------------> TA METHODS <------------------------------ //

	public synchronized String loginTA(String id, String pass) throws RemoteException {
		populateMaps();

		String TA_ID = id;
		String password = pass;
		String returnStmt = "";

		if (TA_login.containsKey(TA_ID) && TA_login.get(TA_ID).equals(password)) {
			returnStmt = TA_ID + ";" + identify_TA.get(TA_ID) + ";" + "success";
		} else {
			returnStmt = "" + ";" + "" + ";" + "fail" + ";" + "";
		}
		return returnStmt;
	}// end loginTA()

	public synchronized HashMap<String, String> listSubmissions() throws RemoteException {
		return (HashMap<String, String>) student_assignments;
	}// end of listSubmissions()

	public synchronized String checkSubmissionID(String sID) throws RemoteException {

		String studentID = sID;
		String returnStmt = null;

		if (!student_assignments.containsKey(studentID)) {
			returnStmt = "fail";
		}

		else if (student_assignments.containsKey(studentID)) {
			returnStmt = "success";
		}
		return returnStmt;
	}// end checkSubmissionID()

	public synchronized String downloadSubmission(String sID, String outputFolder) throws RemoteException {
		System.out.println(student_assignments);
		
		String studentID = sID;
		String returnStmt = null;

		String NAME_of_file_to_retrive = student_assignments.get(studentID).split("\\.zip")[0];
		System.out.println("File to be downloaded: " + NAME_of_file_to_retrive);

		String zipFile_to_retrive = SERVER_ASSIGNMENT_STORAGE + "/" + NAME_of_file_to_retrive + ".zip";
		System.out.println("Location of file to be downloaded from: " + zipFile_to_retrive);

		byte[] buffer = new byte[1024];

		String get_output_dir = outputFolder;

		// ---------> 2nd Get the Output Directory
		String our_outputDir = get_output_dir + "/" + NAME_of_file_to_retrive + "_" + studentID;
		System.out.println(our_outputDir);

		// ---------> 3nd Download the File
		try {

			// create output directory is not exists
			File folder = new File(our_outputDir);
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile_to_retrive));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			
			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(our_outputDir + "/" + fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();			
			}
			zis.close();
			
			// After download remove student from the list
			student_assignments.remove(studentID);
			File list_assignments = new File(
					"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/student_assignments.txt");
			try {
				//Re-Write HashMap to the "student_assignments.txt"
				PrintWriter pw = new PrintWriter(new FileOutputStream(list_assignments));

				for (Map.Entry<String, String> pair : student_assignments.entrySet()) {
					pw.println(pair.getKey() + ":" + pair.getValue());
				}
				pw.close();
				System.out.println("File Re-Written");
			} catch (Exception e) {
				e.printStackTrace();
			}		
			System.out.println(student_assignments);
			
			
			returnStmt = "success";

		} catch (Exception e) {
			e.printStackTrace();
			returnStmt = "fail";
		}

		return returnStmt;
	}// end downloadSubmission()

	// ---------------> POPULAT THE HASH MAPS <----------------- //

	public void populateMaps() {
		// Read file login.txt, and populate 'login' HashMap with file contents.
		try {

			BufferedReader br = new BufferedReader(new FileReader(
					"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/student_login.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String pairs[] = line.split(":");
				student_login.put(pairs[0], pairs[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Read file identify_student.txt, and populate 'identify_student' HashMap with
		// file contents.
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/identify_student.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String pairs[] = line.split(":");
				identify_student.put(pairs[0], pairs[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Read file student_assignments.txt, and populate 'student_assignments' HashMap
		// with file contents.
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/student_assignments.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String pairs[] = line.split(":");
				student_assignments.put(pairs[0], pairs[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Read file TA_login.txt, and populate the 'TA_login' HashMap with file
		// contents.
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/TA_login.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String pairs[] = line.split(":");
				TA_login.put(pairs[0], pairs[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Read file identify_TA.txt, and populate the 'identify_TA' HashMap with file
		// contents.
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"C:/Users/Daljit Sohi/Desktop/RMI FileApp - Good/RMI GUI/src/Server Files/identify_TA.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String pairs[] = line.split(":");
				identify_TA.put(pairs[0], pairs[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end of populate maps

}// end FileApp.class
