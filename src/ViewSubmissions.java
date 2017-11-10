import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewSubmissions extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JList<Object> list;
	private JTextField student_ID;
	private JFileChooser fileChooser;
	private File directory;
	
	Map<String, String> student_assignments = new HashMap<>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String ta_id = "ID";
					String ta_Name = "NAME";
					ViewSubmissions frame = new ViewSubmissions(ta_id, ta_Name);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public ViewSubmissions(String ta_id, String ta_Name) throws MalformedURLException, RemoteException, NotBoundException {
		
		FileAppInterface fileAppInterface = (FileAppInterface) Naming.lookup(FileAppInterface.LOOKUP);
		student_assignments = fileAppInterface.listSubmissions();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 669, 438);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_ID = new JLabel(ta_id);
		lbl_ID.setBounds(457, 41, 120, 14);
		contentPane.add(lbl_ID);

		JLabel lbl_Name = new JLabel(ta_Name);
		lbl_Name.setBounds(457, 78, 186, 14);
		contentPane.add(lbl_Name);
		
		JLabel lbl_NewLable = new JLabel("All the Submitted Assignments");
		lbl_NewLable.setBounds(221, 11, 202, 14);
		contentPane.add(lbl_NewLable);
		
		list = new JList<Object>(student_assignments.keySet().toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setBounds(10, 103, 176, 285);
		list.setValueIsAdjusting(true);
		contentPane.add(list);
//		contentPane.add(new JScrollPane(list));

		JLabel lblEnterStudentId = new JLabel("Enter Student ID: ");
		lblEnterStudentId.setBounds(262, 239, 134, 14);
		contentPane.add(lblEnterStudentId);
		
		student_ID = new JTextField();
		student_ID.setBounds(406, 236, 164, 20);
		contentPane.add(student_ID);
		student_ID.setColumns(10);
		
		String downloadAssignment_info = "<html> <p>Download Student Assingment:</p> \n"
				+ "<p>Enter the student ID in the TextField Below</p> \n"
				+ "<p>Click the 'Download Assignment Button', and Save the assignment</p></html>";

		JLabel lbl_info = new JLabel(downloadAssignment_info);
		lbl_info.setBounds(274, 111, 323, 114);
		contentPane.add(lbl_info);
			
		JButton btnDownloadAssignment = new JButton("Download Assignment");
		btnDownloadAssignment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String studentID = student_ID.getText().toString();
				String checkID = "";
				String result = "";
				try {
					//If JTextFile is 'null'
					if (studentID.equals("")) {
						JOptionPane.showMessageDialog(null, "Please Enter an ID", "ID Error" , 0);
					}
					
					//1st Check if student ID is valid
					checkID = fileAppInterface.checkSubmissionID(studentID);
					if(!studentID.equals("") && checkID.equals("fail")){ //if not valid
						JOptionPane.showMessageDialog(null, "Invalid ID Entered", "ID Error" , 0);
					}
					
					//valid student id --> Processed with the Assignment Download.
					else if(checkID.equals("success")){
//						JOptionPane.showMessageDialog(null, "Valid ID Entered", "ID Correct" , 1);
						
						// file chooser
						String userDir = System.getProperty("user.home");
						fileChooser = new JFileChooser(userDir+"/Desktop");
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fileChooser.setAcceptAllFileFilterUsed(false);

						if (fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
							directory = fileChooser.getSelectedFile(); // Get the Selected File
							System.out.println(directory);
							
							result = fileAppInterface.downloadSubmission(studentID, directory.toString());
							if(result.equals("success")) {
								JOptionPane.showMessageDialog(null, "Assignment Downloaded Successfully", "Download Message" , 1);
							}else if(result.equals("fail")){
								JOptionPane.showMessageDialog(null, "Assignment Downloading Failed", "Download Message" , 0);
							}
//							contentPane.repaint();
						}//if -> fileChooser
					}//else if -> "success"
					
				} catch(Exception e1) {
					e1.printStackTrace();
				}
				contentPane.setVisible(false);
				contentPane.setVisible(true);
			}
		});
		btnDownloadAssignment.setBounds(406, 267, 164, 23);
		contentPane.add(btnDownloadAssignment);
		
		JLabel lblAssignmentSubmittedBy = new JLabel("Assignments Submitted by: ");
		lblAssignmentSubmittedBy.setBounds(10, 78, 203, 14);
		contentPane.add(lblAssignmentSubmittedBy);
						
	}//end viewSubmissions() Constructor
}//end of viewSubmissions.class
