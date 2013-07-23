package transceiver;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

public class MailImap {

	String user;
	String passwd;
	String host;
	int port;
	
	Session session;
	Store store;
	
	
	public MailImap(String user, String passwd) {
		this.user = user;
		this.passwd = passwd;
		host = "imap.gmail.com";
		port = 993;
		
		session = Session.getInstance(System.getProperties(), null);
		try {
			store = session.getStore("imaps");
			store.connect(host, port, user, passwd);

		} catch (MessagingException e) {
			System.err.println(e.getMessage());
		} 
	}
	
	public List<MailObject> getMail() throws MessagingException, IOException {
		
		String target_folder = "INBOX";
		
		ArrayList<MailObject> mails = new ArrayList<>();

//		sess.setDebug(true);

		Folder fol = store.getFolder(target_folder);
		if(fol.exists()){
			for(Folder f : fol.list()){
				System.out.println(f.getName());
			}
			fol.open(Folder.READ_ONLY);
			int count = 1;
			int msgCount = fol.getMessageCount();
			Message[] messages = fol.getMessages(msgCount - 4, msgCount);
			for(Message m : messages){
				Address[] mfrom = m.getFrom();
				Address[] mto = m.getReplyTo();
				mails.add(new MailObject(
						count++, 
						1,
						1,
						mfrom[0].toString(),
						mto[0].toString(), 
						m.getSubject(), 
						getText(m.getContent()), 
						new Timestamp(m.getReceivedDate().getTime()), 
						""));
				System.out.println(m.getSubject());
			}
			fol.close(false);
		}else{
			System.out.printf("%s is not exist.", target_folder);
		}
//		store.close();

		return mails;
	}
	
	private String getText(Object content) throws MessagingException, IOException {
		
		String text = null;
	    StringBuffer sb = new StringBuffer();
	
	    if (content instanceof String) {
	        sb.append((String) content);
	    } else if (content instanceof Multipart) {
	        Multipart mp = (Multipart) content;
	        for (int i = 0; i < mp.getCount(); i++) {
	            BodyPart bp = mp.getBodyPart(i);
	            sb.append(getText(bp.getContent()));
	        }
	    }
	
	    text = sb.toString();
	    return text;
	}
}
