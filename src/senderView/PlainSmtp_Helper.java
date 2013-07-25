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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class PlainSmtp_Helper {

	/************ メンバ変数 ************/

	private String smtpServer;
	private String myMailAddress;
	private String myName;

	/************************************/

	/* 選択アカウントの基本設定 */
	public PlainSmtp_Helper(String smtpServer, String accountMailAddress, String accountName){
		this.smtpServer = smtpServer;
		this.myMailAddress = accountMailAddress;
		this.myName = accountName;
	}

	/* SMTP送信モジュール */
	public void sendMail(String[] to, String[] bcc, String subject, String detail) throws MessagingException, UnsupportedEncodingException{

		//smtpサーバ情報を設定
		Properties prop = new Properties();
		prop.put("mail.smtp.host", smtpServer);
		prop.put("mail.host", smtpServer);

		Session session = Session.getDefaultInstance(prop, null);
		MimeMessage msg = new MimeMessage(session);

		//Toモードでtoに送信
		msg.setRecipients(Message.RecipientType.TO, to[0]);

		//CCモードで他のあて先を送信する
		for(int i=1; i<to.length; i++){
			msg.setRecipients(Message.RecipientType.CC, to[i]);
		}

		//BCC宛先の追加
		for(int i=0; i<bcc.length; i++){
			msg.setRecipients(Message.RecipientType.BCC, bcc[i]);
		}

		//送信先、送信者名
		InternetAddress from = new InternetAddress(myMailAddress, myName);
		msg.setFrom(from);

		//件名と内容をエンコード指定して設定
		msg.setSubject(subject, "ISO-2022-JP");
		msg.setText(detail, "ISO-2022-JP");

		//送信
		Transport.send(msg);
	}

	/* 複数添付ファイル付きSMTP送信モジュール */
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
		Properties prop = new Properties();
		prop.put("mail.smtp.host", smtpServer);
		prop.put("mail.host", smtpServer);

		Session session = Session.getDefaultInstance(prop, null);
		MimeMessage msg = new MimeMessage(session);

		//添付内容を設定
		msg.setContent(mp);

		//Toモードでtoに送信
		msg.setRecipients(Message.RecipientType.TO, to[0]);

		//CCモードで残りを送信
		for(int i=1; i<to.length; i++){
			msg.setRecipients(Message.RecipientType.CC, to[i]);
		}

		//BCC宛先の追加
		for(int i=0; i<bcc.length; i++){
			msg.setRecipients(Message.RecipientType.BCC, bcc[i]);
		}

		//送信先、送信者名
		InternetAddress from = new InternetAddress(myMailAddress, myName);
		msg.setFrom(from);

		//件名をエンコード指定して設定
		msg.setSubject(subject, "ISO-2022-JP");

		//送信
		Transport.send(msg);

	}

}