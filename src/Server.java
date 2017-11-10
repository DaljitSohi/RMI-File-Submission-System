import java.rmi.Naming;

public class Server {

	public static void main(String[] args) throws Exception {
		
		FileApp fileApp = new FileApp();
		Naming.rebind(FileAppInterface.LOOKUP, fileApp);
		System.out.println("Server Started................");
	
	}//end main() Method
}//end Server.class
