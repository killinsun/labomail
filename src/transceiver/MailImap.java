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

/** IMAP操作クラス */
public class MailImap {

	protected String user;
	protected String passwd;
	protected String host;
	protected int port;
	
	protected Session session;
	protected Store store;
	protected boolean connect = false;
	
	public MailImap(String user, String passwd, String host, int port) {
		this.setUser(user, passwd, host, port);
	}
	
	public void setUser(String user, String passwd, String host, int port) {
		this.user = user;
		this.passwd = passwd;
		this.host = host;
		this.port = port;
	}
	
	public void connect() {
		
		session = Session.getInstance(System.getProperties(), null);
		try {
			store = session.getStore("imaps");
			store.connect(host, port, user, passwd);
			connect = true;
		} catch (MessagingException e) {
			System.err.println(e.getMessage());
			connect = false;
		} 
		
	}
	
	public boolean isConnect() {
		return connect;
	}
	
	public int getMailCount() throws MessagingException {
		
		/** Gmailの受信BOX */
		String target_folder = "INBOX";

		Folder folder = store.getFolder(target_folder);
		return folder.getMessageCount();
	}

	/** メールを取得する。ファッキンスロウ（くそ遅い）なのでThread化させたい */
	public List<MailObject> getMail(int start, int end) throws MessagingException, IOException, IllegalStateException {
		
		/** Gmailの受信BOX */
		String target_folder = "INBOX";
		
		ArrayList<MailObject> mails = new ArrayList<>();

//		sess.setDebug(true);

		// ターゲット(受信BOX)を取得
		Folder fol = store.getFolder(target_folder);
		if(fol.exists()){
			for(Folder f : fol.list()){
				System.out.println(f.getName());
			}
			fol.open(Folder.READ_ONLY);
			int count = 1;

			if(start <= 1) {
				start = 1;
			}
			Message[] messages = fol.getMessages(start, end);
			
			for(Message m : messages){
				Address[] mfrom = m.getFrom();
				Address[] mto = m.getReplyTo();
				
				// MailObjectに整形
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
	
	/** メールの本文を取得。MIME形式の場合、Stringに変換。 */
	protected String getText(Object content) throws MessagingException, IOException {
		
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
