import javax.mail.*;
import javax.mail.internet.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class EmailService {
	private String HOST = "imap.gmail.com";
	private String USERNAME = "xxxxx@gmail.com";
	private String PASSWORD = "enter your password here";
	private Properties properties;
	private Store store;
	public Folder folder_to_delete;
	public Folder trash;
	
	public EmailService() throws MessagingException {
	    this.properties = new Properties();
	    this.properties.put("mail.imap.host", HOST);
	    this.properties.put("mail.imap.port", "993");
	    this.properties.put("mail.imap.starttls.enable", "true");
	  }
		
	public void openEmailSession() throws MessagingException, InterruptedException {
		System.out.println("Openning email session");
		
	    Session emailSession = Session.getInstance(this.properties);
	    //emailSession.setDebug(true);		// for printing out all the debug messages
	    this.store = emailSession.getStore("imaps");
	    this.store.connect(HOST, USERNAME, PASSWORD);
	    	    
	    this.trash = this.store.getFolder("[Gmail]/Trash");	    
	    this.trash.open(Folder.READ_WRITE);
	    
	    System.out.println("[DONE]");
	  }
	
	
	public ArrayList<Folder> validFolders = new ArrayList<Folder>();
	public void printFolderList_and_AskWhichFolderToDelete() {
		int selection = 0;
		
		try {
			validFolders.clear();
			Folder[] folders = this.store.getDefaultFolder().list();
			for(Folder folder:folders)
				try {
					System.out.print(">> |" + folder.getName() + "| (" + folder.getMessageCount() + " mails)");
					validFolders.add(folder);
					System.out.println("\t\t\t[" + validFolders.size() + "]");
				}
				catch (Exception e) {
					//e.printStackTrace();
				}
			
			System.out.println();
			Scanner reader = new Scanner(System.in); 
			while(selection < 1 || selection > validFolders.size()) {
				System.out.print("Which folder would you like to expunge? Enter [1-" + validFolders.size() + "]: ");
				selection = reader.nextInt();			
			}
			reader.close();
			System.out.println();
			this.folder_to_delete = validFolders.get(selection-1);
			System.out.println("Your selection: " + this.folder_to_delete.getName());
			this.folder_to_delete.open(Folder.READ_WRITE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeEmailSession() throws MessagingException, IOException {
		System.out.println();
		System.out.println("Closing email session");
		
		this.folder_to_delete.close(true);
	    this.trash.close(true);
	    this.store.close();
	    
	    System.out.println("[DONE]");
	  }
	
	public Message[] getUserMessages(Folder folder) throws MessagingException, IOException {
	    Message[] messages = folder.getMessages();
	    return messages;
	  }
	
	public void showMessagesInFolder(Folder folder) throws IOException, MessagingException {
		Message[] messages = this.getUserMessages(folder);
		for (Message message :messages) {
			System.out.println(message.getSubject());
			Address[] addresses = message.getFrom();
			for(Address addr:addresses) {
				System.out.println("  [" + addr.toString() + "]");
			}
		}
	}
	
	String[] exclude_signature_sender = {
			/* family members */
			"dad@gmail.com", "mom@gmail.com", "Son",			
			/* academic advisors */
			"advisor1@gmail.com", "My Advisor"
		}; 
	public void moveToTrashFolder(Folder from) throws IOException, MessagingException {
		if (from == null) from = this.folder_to_delete;
		
		Message[] messages = this.getUserMessages(from);
		ArrayList<Message> messages_to_remove = new ArrayList<Message>();
		
		System.out.println();
		System.out.println("Inspecting the inbox folder: a total of " + messages.length + " emails");
		int i=0;
		
		for (Message message :messages) {	
			//System.out.println(message.getSubject());
			
			Address[] addresses_from = message.getFrom();
			Address[] addresses_to = message.getRecipients(Message.RecipientType.TO);
			ArrayList<String> addresses = new ArrayList<String>();
			for(Address addr: addresses_from) addresses.add(addr.toString());
			for(Address addr: addresses_to) addresses.add(addr.toString());
			
			boolean exclude = false;
			for(String addr:addresses) {	
				//System.out.println("\t\t>>" + addr);
				for (String exclude_sender: exclude_signature_sender) {
					if (addr.contains(exclude_sender)) {
						exclude = true;
						//System.out.println("[EXCLUDE]");
						break;
					}
				}
				if (exclude) break;
			}
			if (!exclude) {
				messages_to_remove.add(message);				
				if (messages_to_remove.size() > 10) {
					Message list[] = new Message[messages_to_remove.size()];
					list = messages_to_remove.toArray(list);
					from.copyMessages(list, this.trash);
					
					System.out.print(".");
					i += messages_to_remove.size();					
					if (i % 50 == 0) System.out.println();
					
					messages_to_remove.clear();
				}
			}
		}
		if (messages_to_remove.size() > 0) {
			Message list[] = new Message[messages_to_remove.size()];
			list = messages_to_remove.toArray(list);
			from.copyMessages(list, this.trash);
			System.out.println(".");
			i += messages_to_remove.size();
			messages_to_remove.clear();
		}
		
		
		System.out.println("A total of " + i + " mails were moved to the trash folder");
		System.out.println("[DONE]");
	}
	
	public void expungeTrashFolder() throws IOException, MessagingException {		
		Message[] messages = this.getUserMessages(this.trash);
		
		System.out.println();
		System.out.println("Expunging the trash folder: a total of " + messages.length + " emails");
		int i=0;
		for (Message message :messages) {			
			message.setFlag(Flags.Flag.DELETED, true);
			i++;
			if (i % 10 == 0) System.out.print(".");
			if (i % 500 == 0) System.out.println();
			if (i > 100) break;
		}
		if (i>=10) System.out.println();
		System.out.println("[DONE]");
	}
}
