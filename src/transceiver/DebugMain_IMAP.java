package transceiver;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.mail.MessagingException;

public class DebugMain_IMAP {

	public static void main(String[] args) throws MessagingException, IOException {

		Scanner scan = new Scanner(System.in);
		System.out.print("user:");
		String user = scan.nextLine();
		System.out.print("password:");
		String passwd = scan.nextLine();
		
		MailImap imap = new MailImap(user, passwd);
		List<MailObject> mails = imap.getMail();
		
		for (MailObject mailObject : mails) {
			mailObject.getFrom();
		}
	}
}
