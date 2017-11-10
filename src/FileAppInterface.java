import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface FileAppInterface extends Remote{
	
	public static final String LOOKUP = "file_service";
	
	//------------------>  			ALL	STUDENT METHODS 				<------------------------------ //
	public String loginStudent(String id, String password) throws RemoteException;
	public String submitSingleFile(String id, String inputFolder, String sName) throws RemoteException;
	
	
	//------------------>  				ALL TA METHODS				<------------------------------ //
	public String loginTA(String id, String pass) throws RemoteException;
	public HashMap<String, String> listSubmissions() throws RemoteException;
	public String downloadSubmission(String id, String outputFolder) throws RemoteException;
	public String checkSubmissionID(String id) throws RemoteException;
}//end FileAppInterface.interface
