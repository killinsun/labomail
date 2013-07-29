package transceiver;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

public class DebugMain_POP3 {

	public static void main(String[] args) throws MessagingException, IOException {
		
		MailPop3 pop3 = new MailPop3("laboaiueo@gmail.com", "labolabo", "pop.gmail.com", 995);
		pop3.connect();
		
		int count = pop3.getMessageCount();
		System.out.println(count);
		
		List<MailObject> mails = pop3.getMail(count - 5, count);
		for (MailObject m : mails) {
			System.out.println(m.toString());
		}
		
		
	}
}
