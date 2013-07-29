package senderView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/* senderView.MailSenderPanel*メール送信操作*用インターフェース */
public interface Smtp_Interface {
	void sendMail(String[] to, String[] bcc, String subject, String detail) throws AddressException, MessagingException, UnsupportedEncodingException;
	void sendMail(String[] to, String[] bcc, String subject, String detail, ArrayList<FileDataSource> fileList) throws MessagingException, UnsupportedEncodingException;
}
