//Justin Hoang
//jah7399
package chatClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientMain extends JFrame implements ActionListener 
{
	static int SERVER_PORT = 3000;
	
	static Socket serverSocket;
	JPanel ChatPanel;   
	JTextArea chatHistoryBox;	
	JTextField newMsg;
	JList onlineUserList;
		
	//JTextArea ChatBox;
	JButton SendButton;
	JButton LoginButton;
	JButton LogoutButton;
	
	String UserName;
	JTextField ClientName;


	// Constructor
	@SuppressWarnings("unchecked")
	public ClientMain() throws UnknownHostException, IOException 
	{				
		// Set up "Enter User Name:" label
		JLabel userNameLabel = new JLabel("Enter Username: ", JLabel.CENTER);	
		userNameLabel.setLocation(20,18);
		userNameLabel.setSize(100,25);		
		
		// Set up user name box
		ClientName = new JTextField();
		ClientName.setBounds(125, 19, 100, 25);	
		
		// Set up Sign In button
		LoginButton = new JButton("Sign In");		
		LoginButton.setBounds(265, 18, 100, 25);
		LoginButton.addActionListener(this);   // Attach listener
		
		// Set up chat history text field
		chatHistoryBox = new JTextArea();		
		chatHistoryBox.setBounds(20, 75, 340, 300);
		
		JLabel usersLabel = new JLabel("Online Users:");
		
		usersLabel.setLocation(370, 50);
		usersLabel.setSize(100,30);
		
		// Set up Sign Out button
		LogoutButton = new JButton("Sign Out");		
		LogoutButton.setBounds(370, 18, 100, 25);
		LogoutButton.setEnabled(false);
		LogoutButton.addActionListener(this);   // Attach listener
		
		// Set up chat history text field
		chatHistoryBox = new JTextArea();		
		chatHistoryBox.setBounds(20, 75, 340, 300);
		chatHistoryBox.setEditable(false);
		
		JLabel usersLabel1 = new JLabel("Online Users:");
		
		usersLabel1.setLocation(370, 50);
		usersLabel1.setSize(100,30);		
		
		// set up online users list
		DefaultListModel listModel = new DefaultListModel();
		//listModel.addElement("All");
		onlineUserList = new JList(listModel);
		onlineUserList.setBounds(370, 75, 100, 300);
						
		// Set up new message field
		newMsg = new JTextField();
		newMsg.setBounds(20, 385, 342, 30);	
		
		// Set up "Send" button
		SendButton = new JButton("Send");
		SendButton.setBounds(370, 385, 100, 30);
		SendButton.setEnabled(false);	    // Disabled until login
		SendButton.addActionListener(this); // Attach listener
			
		// Set up Main chat window
		ChatPanel = new JPanel();		
		this.setSize(510, 470);
		this.setVisible(true);	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ChatPanel.setLayout(null);
		this.add(ChatPanel);	
		
		// Add fields to chat window
		ChatPanel.add(userNameLabel);
		ChatPanel.add(ClientName);
		ChatPanel.add(LoginButton); 
		ChatPanel.add(LogoutButton);
		
		ChatPanel.add(chatHistoryBox);
		
		ChatPanel.add(usersLabel1);
		ChatPanel.add(onlineUserList);

		ChatPanel.add(newMsg);
		ChatPanel.add(SendButton);
		
		this.setTitle("Client");		

		serverSocket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);

		chatHistoryBox.setText("Connected to Server on port 3000...\n");

		// loop while waiting for incoming messages
		while (true) 
		{
			try 
			{
				DataInputStream dis = new DataInputStream(serverSocket.getInputStream());
				String in = dis.readUTF();
								
				String[] words = in.split("#");	
							
				if (words[0].equals("FROM"))
				{
					String msgOwner = words[1];
					String textMsg = words[2];
		
					
					String text = "[" + msgOwner + " > me] : " + textMsg + '\n';
					chatHistoryBox.append(text);
				}
				else if (words[0].equals("NEW USER"))
				{
					String newUser = words[1];
					DefaultListModel listModel1 = (DefaultListModel) onlineUserList.getModel();
					listModel1.addElement(newUser);
					onlineUserList.setModel(listModel1);

					String text = "[SERVER > me] : User " + newUser + 
							" has just came online.\n";
					chatHistoryBox.append(text);					
				}
				else if (words[0].equals("ONLINE USER"))
				{
					DefaultListModel listModel1 = (DefaultListModel) onlineUserList.getModel();
					String userName = words[1];
					listModel1.addElement(userName);			
			
					onlineUserList.setModel(listModel1);				
				}
				else if (words[0].endsWith("LOGOUT")) 
				{
					String signoutUser = words[1];
					DefaultListModel listModel1 = (DefaultListModel) onlineUserList.getModel();
					listModel1.removeElement(signoutUser);
					onlineUserList.setModel(listModel1);

					String text = "[SERVER > me] : User " + signoutUser + 
							" has just signed out.\n";
					chatHistoryBox.append(text);						
				}
			} 
			catch (Exception e1) 
			{
				chatHistoryBox.setText(chatHistoryBox.getText() + '\n'
						+ "Message sending fail:Network Error");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// If user just clicked Send button
		if ((e.getSource() == SendButton) && (newMsg.getText() != "")) 
		{
			String sendMsg = newMsg.getText();
			
			// Get names being hi-lighted in the online users list
			@SuppressWarnings("unchecked")
			List<String> selectedUsers = (List<String>)onlineUserList.getSelectedValuesList();
					
			try 
			{
				if (selectedUsers.size() == 0)
				{
					String text = "[Me > NOBODY] : " + sendMsg + '\n';
					chatHistoryBox.append(text);					
				}
				else
				{
					for (int i = 0; i < selectedUsers.size(); i++) {
						String user = selectedUsers.get(i);
						user.trim();
						
						String text = "[Me > " + user + "] : " + sendMsg + '\n';
						chatHistoryBox.append(text);
					
						String outMsg = 						
							"FROM USER#"+ UserName + "#" + "TO USER#" + user + "#" + sendMsg;
						
						DataOutputStream dos = new DataOutputStream(
								serverSocket.getOutputStream());
						dos.writeUTF(outMsg);
						dos.flush();
						
						TimeUnit.SECONDS.sleep(1);
					}
				}
			} 
			catch (Exception e1) 
			{;
			
				chatHistoryBox.setText(chatHistoryBox.getText() + '\n'
						+ "Message sending fail:Network Error");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
			newMsg.setText("");
		}
		else if ((e.getSource() == LoginButton)) 
		{
			// Sign In button was pressed.  Get user name
			UserName = ClientName.getText();		
			
			// If valid user name, enable Send message button
			if (UserName != "") 
			{
				this.setTitle(UserName);
				
				// disable login fields so cann't sign in again
				ClientName.setEnabled(false);
				LoginButton.setEnabled(false);
				
				// enable logout
				LogoutButton.setEnabled(true);
				
				// enable Send button so chat can start
				SendButton.setEnabled(true);				
				
				try 
				{
					DataOutputStream dos = new DataOutputStream(
							serverSocket.getOutputStream());
					dos.writeUTF("LOGIN#"+ UserName);	
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		else if ((e.getSource() == LogoutButton)) 
		{
			try 
			{
				DataOutputStream dos = new DataOutputStream(
						serverSocket.getOutputStream());
				dos.writeUTF("LOGOUT#"+ UserName);	
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}			
		}
	}

	
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		ClientMain chatForm = new ClientMain();
	}
}
