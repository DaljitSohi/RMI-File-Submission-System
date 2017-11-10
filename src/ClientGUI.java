import java.awt.EventQueue;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.awt.event.ActionEvent;

public class ClientGUI extends UnicastRemoteObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6579246193057390001L;
	
	private JFrame frame;
//	private JFrame submitFiles;
	
	private JLabel lblId, lblPassword, lblStudentOrTa;
	private JTextField getID;
	private JPasswordField passwordField;
	private JComboBox<?> comboBox;
	private JButton btnLogin;
	
	Map<String, String> studentAssignments = new HashMap<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() throws RemoteException, MalformedURLException, NotBoundException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	private void initialize() throws RemoteException, MalformedURLException, NotBoundException {
		
		FileAppInterface fileAppInterface = (FileAppInterface)Naming.lookup(FileAppInterface.LOOKUP);
		
		frame = new JFrame("File App");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
//		submitFiles = new JFrame("Submit Files");
//		submitFiles.setBounds(100, 100, 450, 300);s
//		submitFiles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		submitFiles.getContentPane().setLayout(null);
		
		//Log in User
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String result;
				int boxOption = comboBox.getSelectedIndex();
				String userID = getID.getText();
				String password = passwordField.getText();
				try {
					//Student Login
					if(boxOption == 0) {
						result = fileAppInterface.loginStudent(userID, password);
						//Server Sends Back
						//result[0] -> The Student ID 
						//result[1] -> Student Name 
						//result[2] -> "success" 
						String[] successResult = result.split("\\;");
						if(successResult[2].equals("success")) {
							String sID = successResult[0];
							String sName = successResult[1];
							getID.setText(null);
							passwordField.setText(null);
							
							//Start the submission
							SubmitAssignment submit = new SubmitAssignment(sID, sName);
							submit.setVisible(true);
						}
						else if(successResult[2].equals("fail")) {
							JOptionPane.showMessageDialog(null, "Incorrect ID, or Password", "Login Fail", 0);
						}
					}else if(boxOption == 1) {
						result = fileAppInterface.loginTA(userID, password);
						//Server Sends Back
						//result[0] -> The TA ID 
						//result[1] -> TA Name 
						//result[2] -> "success" 
						String[] successResult = result.split("\\;");
						if(successResult[2].equals("success")) {
							String taID = successResult[0];
							String taName = successResult[1];
							getID.setText(null);
							passwordField.setText(null);
							
							//View student submission
							ViewSubmissions viewSubmissions = new ViewSubmissions(taID, taName);
							viewSubmissions.setVisible(true);
						}
						else if(successResult[2].equals("fail")) {
							JOptionPane.showMessageDialog(null, "Incorrect ID, or Password", "Login Fail", 0);
						}
					}
					
				} catch(Exception e1) {e1.printStackTrace();}				
			}//end actionPerformed()
		});
		btnLogin.setBounds(225, 183, 89, 23);
		frame.getContentPane().add(btnLogin);
		
		// Get User ID
		getID = new JTextField();
		getID.setBounds(200, 95, 170, 20);
		getID.setColumns(10);
		frame.getContentPane().add(getID);

		// Get Password
		passwordField = new JPasswordField();
		passwordField.setBounds(200, 139, 170, 20);
		frame.getContentPane().add(passwordField);

		// ID Label
		lblId = new JLabel("Enter ID:");
		lblId.setBounds(68, 98, 74, 14);
		frame.getContentPane().add(lblId);

		// Password Label
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(68, 142, 85, 14);
		frame.getContentPane().add(lblPassword);

		String[] studentORta = {
				"Student",
				"Teacher Assistant (TA)"
				};
		comboBox = new JComboBox<Object>(studentORta);
		comboBox.setBounds(200, 48, 170, 20);
		frame.getContentPane().add(comboBox);

		lblStudentOrTa = new JLabel("Student or TA:");
		lblStudentOrTa.setBounds(40, 51, 89, 14);
		frame.getContentPane().add(lblStudentOrTa);
	}

}
