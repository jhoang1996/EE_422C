//Justin Hoang
//jah7399

package chatServer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerMain extends JFrame implements ActionListener {
	
	static int SERVER_PORT = 3000; 
	
	static ServerSocket server;
	static Socket socket;
	JPanel ChatPanel;
	JTextField NewMsg;
	JTextArea ChatHistoryBox;
	JButton SendButton;
	DataInputStream dis;
	DataOutputStream dos;
	
	JLabel PortLabel; 
	JTextField PortField;

	String ClientName = null;
	
	HashMap<String, Socket> userMap= new HashMap<String, Socket>();
	
	private static final int maxClientsCount = 10;
	private static final clientThread[] threads = new clientThread[maxClientsCount];	
	
	public ServerMain() throws UnknownHostException, IOException 
	{
		// Set up Port field label
		PortLabel  = new JLabel("Host Port: ", JLabel.LEFT);
		PortLabel.setLocation(20,15);
		PortLabel.setSize(100,30);		
		
		// Set up Port text box
		PortField = new JTextField();
		PortField.setBounds(90, 18, 50, 25);	
		PortField.setText(Integer.toString(SERVER_PORT));    // Assign port 3000
		PortField.setEnabled(false);  // This is read-only field
		
		// Set up chat history text field		
		ChatHistoryBox = new JTextArea();
		ChatHistoryBox.setBounds(20, 70, 445, 270);	
		ChatHistoryBox.setBackground(Color.lightGray);
		ChatHistoryBox.setEditable(false);
	
		// Set up Main chat window		
		
		ChatPanel = new JPanel();
		this.setSize(500, 400);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ChatPanel.setLayout(null);
		this.add(ChatPanel);

		// Add fields to chat window	
		ChatPanel.add(PortLabel);
		ChatPanel.add(PortField);
		ChatPanel.add(ChatHistoryBox);
		//ChatPanel.add(NewMsg);
		//ChatPanel.add(SendButton);
		this.setTitle("Server");

		ChatHistoryBox.setText("SERVER -->  Waiting for client...\n");
		
	  
	    while (true) {
	      try {
	        socket = server.accept();
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String inputText = dis.readUTF();
	    	  
			String[] words = inputText.split("#");	
			      
			// If first token indicates it is a user login 
			if (words[0].equals("LOGIN") )
			{
				ClientName = words[1];
				userMap.put(ClientName, socket);
				
				String welcome ="FROM#SERVER# Hello "+ ClientName + ".  Welcome to our chat room.\n";				//ChatHistoryBox.setText(ChatHistoryBox.getText() + '\n' + "SERVER -->  Welcome to our chat room...");
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(welcome);	
				
		        Iterator<Entry<String, Socket>> itr = 
		        		userMap.entrySet().iterator();
        
		        // Let new login know who is online
		        while(itr.hasNext())
		        {
		        	Map.Entry<String, Socket> entry = itr.next();
		        	String name = entry.getKey();
		        	if (!name.equals(ClientName)) {
		        		String users = "ONLINE USER#" + name;	        	
	        	
		        		DataOutputStream out = new DataOutputStream(
		        				socket.getOutputStream());
		        		out.writeUTF(users);	
		        	}
		        } 		        		      
				
				// Keep track of users and their sockets
				userMap.put(ClientName, socket);
								
				
				// Create a new thread for new user
		        int i = 0;
		        for (i = 0; i < maxClientsCount; i++) {
		          if (threads[i] == null) {
		            (threads[i] = new clientThread(ClientName, socket, threads)).start();
		            break;
		          }
		        }
		        
		        if (i == maxClientsCount) {
		          PrintStream os = new PrintStream(socket.getOutputStream());
		          os.println("Server too busy. Try later.");
		          os.close();
		          socket.close();
		        }				
			}			
			else
			{				
				//ChatHistoryBox.setText(ChatHistoryBox.getText() + '\n' + ClientName +" -->  "
				//		+ inputText);
			}			
			
	      } catch (IOException e) {
	        System.out.println(e);
	      }
	    }	
		
		
	}
		
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if ((e.getSource() == SendButton) && (NewMsg.getText() != "")) 
		{
			ChatHistoryBox.setText(ChatHistoryBox.getText() + '\n' + "ME:"
					+ NewMsg.getText());
			try 
			{
				DataOutputStream dos = new DataOutputStream(
							socket.getOutputStream());
				dos.writeUTF(NewMsg.getText());
			} 
			catch (Exception e1) 
			{
				try 
				{
					Thread.sleep(3000);
					System.exit(0);
				} 
				catch (InterruptedException e2) 
				{
					e2.printStackTrace();
				}
			}
			NewMsg.setText("");
		}
	}
	
	
	class clientThread extends Thread 
	{
		private DataInputStream is = null;
		private DataOutputStream os = null;
		private Socket clientSocket = null;
		private final clientThread[] threads;
		private int maxClientsCount;
		private String userName;

		public clientThread(String name, Socket clientSocket, clientThread[] threads) 
		{
			this.userName = name;
			this.clientSocket = clientSocket;
			this.threads = threads;
			maxClientsCount = threads.length;
		}
	  
		public void run() 
		{
		    int maxClientsCount = this.maxClientsCount;
		    clientThread[] threads = this.threads;
		    
		    String text = "SERVER --> Client " + userName + " just connected...\n" ;

			ChatHistoryBox.append(text);	    
		    
		    try {
		      is = new DataInputStream(clientSocket.getInputStream());
		      os = new DataOutputStream(clientSocket.getOutputStream());

		      
		      for (int i = 0; i < maxClientsCount; i++) 
		      {
		          if (threads[i] != null && threads[i] != this) 
		          {	        	  
			        String newUser = "NEW USER#" + userName +"#" + 
			        			" just entered the chat room.!\n";			        	  
				             	      
		            threads[i].os.writeUTF(newUser);
		            try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		          }
		      }
		      
		      while (true) 
		      {
		    	  DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
		    	  String line = dis.readUTF();
		    	  
		    	  //ChatHistoryBox.append("SERVER -->  Client " + line + '\n');	   	
		    	  
		    	  String[] words = line.split("#");		    	  
		    	  
		    	  if (words[0].equals("FROM USER")) 
		    	  {		    		  
		    		  String fromUserName = words[1];
		    		  
		    		  if (fromUserName.equals(userName)) 
		    		  {
		    			  // 3rd token should be TO USER
		    			  
		    			  if (words[2].equals("TO USER"))
		    			  {
		    				  String toUser = words[3];
		    				  
		    				  String message = words[4];
		    				  
		    				  Iterator<Entry<String, Socket>> itr = 
		    			        		userMap.entrySet().iterator();
		    	        
		    				  // Find the TO USER and his socket
		    				  while(itr.hasNext())
		    				  {
		    					  Map.Entry<String, Socket> entry = itr.next();
		    					  String name = entry.getKey();
		    					  if (name.equals(toUser)) {
		    						  Socket s = entry.getValue();
		    						  
		    						  String fowardMsg = "FROM#" + fromUserName + "#" + message;
		        	  		        	
		    						  DataOutputStream out = new DataOutputStream(
		    			        				s.getOutputStream());
		    						  out.writeUTF(fowardMsg);	
		    						  out.flush();
		    						  
		    						  break;
		    					  }
		    				  } 		    				  		    				  		    				  
		    			  }
		    		  }		    		  
		    	  }
		    	  else if (words[0].equals("LOGOUT") )
		    	  {
		    		  String signedOutUserName = words[1];			    		  
		    		  
				      for (int i = 0; i < maxClientsCount; i++) 
				      {
				    	  // Tell others this user just signed off
				          if (threads[i] != null && threads[i] != this) 
				          {	        	  
					        String newUser = "LOGOUT#" + signedOutUserName +"#" + 
					        			" just signed of.\n";			        	  
						             	      
				            threads[i].os.writeUTF(newUser);
				            
				            try {
								TimeUnit.SECONDS.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				          }
				      }
				      		    		  
		    		  // Now remove the user if this is his thread   		  
		    		  if (signedOutUserName.equals(userName)) 
		    		  {		    	    			  
		    			  String goodbye ="FROM#SERVER# Goodbye. ";
		    			  DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
		    			  dos.writeUTF(goodbye);
		    			  
		    			  userMap.remove(userName);
		    			  
		    			  try {
							TimeUnit.SECONDS.sleep(1);
		    			  } catch (InterruptedException e) {
							e.printStackTrace();
		    			  }
		    			  
		    		      is.close();
		    		      os.close();
		    		      clientSocket.close();	
		    		      
		    			    
		    		      String text1 = "SERVER --> Client " + userName + " just signed off...\n" ;
		    		      ChatHistoryBox.append(text1);	 
		    		      
		    		      for (int i = 0; i < maxClientsCount; i++) 
		    		      {
		    		          if (threads[i] == this) {
		    		            threads[i] = null;
		    		          }
		    		      }		    		      		    		      
		    		  }          	  
		    	  }
		      }
		    } 
		    catch (IOException e) 
		    {
		    }
		}
	}  
	  
	  

	public static void main(String[] args) throws UnknownHostException,
		IOException 
	{
		server = new ServerSocket(SERVER_PORT, 1, InetAddress.getLocalHost());	
		new ServerMain();
	}
	
}
