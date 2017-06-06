
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Delete gmails v0.1 by Sihyung Lee");
		
		try {
			EmailService es = new EmailService();
			es.openEmailSession();
			es.printFolderList_and_AskWhichFolderToDelete();			
			es.moveToTrashFolder(null);
			es.expungeTrashFolder();
			es.closeEmailSession();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
