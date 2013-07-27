package senderView;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Crunchify.com
 * @modifer Aiya000
 */

public class GmailSmtp_Helper implements Smtp_Interface {

	/************ メンバ変数 ************/

	//自分のアドレスを設定してください（送信元として適用されます）
	private String myMailAddress;
	//自分のアドレスのパスワードを設定してください（認証に使用されます）
	private String myPassword;

	/************************************/

	/* 選択アカウントの基本設定 */
	public GmailSmtp_Helper(String smtpServer, String accountMailAddress, String accountPassword, String port){
		this.myMailAddress = accountMailAddress;
		this.myPassword = accountPassword;
	}


	@Override /* Gmail用SMTP送信モジュール */
	public void sendMail(String[] to, String[] bcc, String subject, String detail) throws AddressException, MessagingException {

		/* プロパティの取得と設定 */
		Properties props = System.getProperties();
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		/* セッションの取得 */
		Session session = Session.getDefaultInstance(props, null);

		/* メッセージの取得と設定 */
		MimeMessage msg = new MimeMessage(session);

		//宛先の追加
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[0]));

		//残りの宛先の追加
		for(int i=1; i<to.length; i++){
			if(to[i].equals("")) continue; //謎エラー防止(なぜかtoが1つでも2と認識される)
			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(to[i]));
		}

		//BCC宛先の追加
		for(int i=0; i<bcc.length; i++){
			if(bcc[i].equals("")) continue;
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
		}

		//件名の設定
		msg.setSubject(subject, "ISO-2022-JP");

		//内容の設定
		msg.setText(detail, "ISO-2022-JP");

		/* メールの送信 */
		Transport transport = session.getTransport("smtp");

		transport.connect("smtp.gmail.com", myMailAddress, myPassword);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
	}

	@Override /* 複数添付ファイル付きSMTP送信モジュール */
	public void sendMail(String[] to, String[] bcc, String subject, String detail, ArrayList<FileDataSource> fileList) throws MessagingException, UnsupportedEncodingException{

		//メール内容と添付ファイル内容を生成
		MimeBodyPart[] mimePart = new MimeBodyPart[fileList.size()+1];

		//マルチポートを生成
		Multipart mp = new MimeMultipart();

		//内容記述用
		mimePart[0] = new MimeBodyPart();
		mimePart[0].setText(detail, "ISO-2022-JP");
		mp.addBodyPart(mimePart[0]);

		//添付ファイル用
		for(int i=1; fileList.get(0)!=null && i-1<fileList.size(); i++){
			//ファイルリストのファイルを取り出す
			FileDataSource file = fileList.get(i-1);
			//添付ファイル用のパートを作成
			mimePart[i] = new MimeBodyPart();

			//データを添付
			mimePart[i].setDataHandler(new DataHandler(file));

			//ファイル名を設定
			mimePart[i].setFileName(file.getName());

			//添付を実施
			mp.addBodyPart(mimePart[i]);
		}

		//smtpサーバ情報を設定
		Properties props = System.getProperties();
//		props.put("mail.smtp.host", smtpServer);
//		props.put("mail.host", smtpServer);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage msg = new MimeMessage(session);

		//添付内容を設定
		msg.setContent(mp);

		//Toモードでtoに送信
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[0]));

		//CCモードで残りを送信
		for(int i=1; i<to.length; i++){
			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(to[i]));
		}

		//BCC宛先の追加
		for(int i=0; i<bcc.length; i++){
			if(bcc[i].equals("")) continue;
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
		}

		//送信先、送信者名
		InternetAddress from = new InternetAddress(myMailAddress, myMailAddress);
		msg.setFrom(from);

		//件名をエンコード指定して設定
		msg.setSubject(subject, "ISO-2022-JP");

		//送信
//		Transport.send(msg);
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.gmail.com", myMailAddress, myPassword);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();

	}

}