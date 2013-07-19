package TopView;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class MailImap {

	String user;
	String passwd;
	
	public MailImap(String user, String passwd) {
		this.user = user;
		this.passwd = passwd;
	}
	
	public void name() throws MessagingException, IOException {
		
		String host = "imap.gmail.com";
		int port = 993;
		String target_folder = "INBOX";

		Properties props = System.getProperties();
		Session sess = Session.getInstance(props, null);
//		sess.setDebug(true);

		Store st = sess.getStore("imaps");
		st.connect(host, port, user, passwd);
		Folder fol = st.getFolder(target_folder);
		if(fol.exists()){
			for(Folder f : fol.list()){
				System.out.println(f.getName());
			}
			fol.open(Folder.READ_ONLY);
			for(Message m : fol.getMessages()){
				System.out.printf("%s - %d\n", m.getSubject(), m.getSize());
				System.out.println(m.getContent());
			}
			fol.close(false);
		}else{
			System.out.printf("%s is not exist.", target_folder);
		}
		st.close();

	}
}
