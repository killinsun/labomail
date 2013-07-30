package transceiver;

import java.io.IOException;

import javax.mail.MessagingException;

public class DebugMain_Imap {
	
	public static void main(String[] args) throws MessagingException, IllegalStateException, IOException {
		
		MailImap imap = new MailImap("laboaiueo@gmail.com", "labolabo", "imap.gmail.com", 993);
		imap.connect();
		int index;
		System.out.println(index = imap.getMailCount());
		
		for (MailObject m : imap.getMail(index - 5, index)) {
			System.out.println(m.toString());
		}
	}
}
