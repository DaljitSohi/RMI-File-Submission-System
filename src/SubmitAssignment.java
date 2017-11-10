import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

public class SubmitAssignment extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lbl_Name;
	private JLabel lbl_id;
	private JButton btnSubmitAssignment;
	private JFileChooser fileChooser;

	private File file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String studentID = "ID";
					String studentName = "NAME";
					SubmitAssignment frame = new SubmitAssignment(studentID, studentName);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public SubmitAssignment(String studentID, String studentName)
			throws MalformedURLException, RemoteException, NotBoundException {

		FileAppInterface fileAppInterface = (FileAppInterface) Naming.lookup(FileAppInterface.LOOKUP);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 513, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lbl_Name = new JLabel(studentID);
		lbl_Name.setBounds(212, 32, 186, 14);
		contentPane.add(lbl_Name);

		lbl_id = new JLabel(studentName);
		lbl_id.setBounds(29, 32, 120, 14);
		contentPane.add(lbl_id);

		// file chooser
		String userDir = System.getProperty("user.home");
		fileChooser = new JFileChooser(userDir+"/Desktop");
	
		btnSubmitAssignment = new JButton("Submit File");
		btnSubmitAssignment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int ret = fileChooser.showDialog(null, "Select File"); // Show Dialog
					String inputFile = null;
					String result= "";
					if (ret == JFileChooser.APPROVE_OPTION) {
						file = fileChooser.getSelectedFile(); // Get the Selected File

						inputFile = file.toString();
						System.out.println(inputFile);

						result = fileAppInterface.submitSingleFile(studentID, inputFile, studentName);
						//Server Sends Back
						//result[0] -> "fail" or "success" 
						//result[1] -> if "fail", then "Assignment Name"
						String[] successResult = result.split("\\;");
						if(successResult[0].equals("fail")) {
							String message = "You have already submitted your assignment! \n"
									+ "Assignment Submitted: " + successResult[1] + "\n"
									+ "Can NOT submit more than ONCE. \n"
									+ "Thank You.";	
							JOptionPane.showMessageDialog(null, message, "Submission", 0);
						}else {
							JOptionPane.showMessageDialog(null, "Assignment Submitted", "Submission", 1);
						}
					} // end of JFileChooser.Approve_Options 'if'
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSubmitAssignment.setBounds(156, 208, 186, 23);
		contentPane.add(btnSubmitAssignment);

		String greeting_text = "<html><p>		Greetings		</p><br>"
				+ "<p>To SUBMIT your assignment, click the 'Submit Assignment' Button<br>"
				+ "To LOGOUT, close the application.<br>" + "Thank You " + "</p></html>";
		JLabel lblGreetings = new JLabel(greeting_text);
		lblGreetings.setBounds(82, 92, 316, 105);
		contentPane.add(lblGreetings);
	}// end of submitFiles() Constructor
}// end submitFiles.class
