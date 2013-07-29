package transceiver;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;


public class MailPop3 {

	private String user;
	private String passwd;
	private String host = "pop.gmail.com";
	private int port = 995;

	private String protocol = "pop3s";
	private Store store;
	private String folderName = "INBOX";


	public MailPop3(String user, String passwd, String host, int port) {
		this.setUser(user, passwd, host, port);
	}
	
	public void setUser(String user, String passwd, String host, int port) {
		this.user = user;
		this.passwd = passwd;
		this.host = host;
		this.port = port;		
	}
	
	public void connect() throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty(
				"mail.pop3.socketFactory.class", 
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		Session session = Session.getDefaultInstance(props, null);
		URLName urln = new URLName(protocol, host, port, null, user, passwd);

		store = session.getStore(urln);
		store.connect();
	}
	
	public void close() throws MessagingException, IllegalStateException {
		store.close();
	}
	
	public void setFolder(String folderName) { 
		this.folderName = folderName;
	}
	
	public int getMessageCount() throws MessagingException, IllegalStateException {
		Folder folder = store.getFolder(folderName);
		folder.open(Folder.READ_ONLY);
		
		return folder.getMessageCount();
	}
	
	public List<MailObject> getMail(int start, int end) throws MessagingException, IllegalStateException {
		
		List<MailObject> mails = new ArrayList<>();

		Folder folder  = null;

		try {
			folder = store.getFolder(folderName);
			folder.open(Folder.READ_ONLY);

			// メッセージの数を取得
			int totalMessages = folder.getMessageCount();

			// メッセージを取得
			Message[] messages = folder.getMessages(start, end);

			for (int i = start; i < end; i++) {
				
				System.out.println(messages[i].getSubject());
				
				// MailObjectに整形
				try {
					Address[] mfrom = messages[i].getFrom();
					Address[] mto = messages[i].getReplyTo();
					String subject = messages[i].getSubject();
					String text = getText(messages[i].getContent());
					
					if(subject == null) {
						subject = "";
					}
					if(text == null) {
						text = "";
					}
					
					mails.add(new MailObject(
							messages[i].getMessageNumber(), 
							1,
							1,
							mfrom[0].toString(),
							mto[0].toString(),
							subject, 
							text, 
							new Timestamp(messages[i].getSentDate().getTime()), 
							""));
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}

			}
			
			// フォルダーを閉じます
			folder.close(false);
		
		} 
		catch (MessagingException e) {
			throw e;
		} 
		
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
