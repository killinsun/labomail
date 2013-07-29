package preference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PreferencesWriter {

	//インスタンス生成の禁止
	private PreferencesWriter(){}

	/************ xmlファイルへの書き込み ************/

	/**
	 * このメソッドは引数を
	 * ・メールアドレス
	 * ・パスワード
	 * ・SMTPサーバー
	 * ・SMTPポート
	 * ・IMAPサーバー
	 * ・IMAPポート
	 * の順に書き込みます
	 *
	 * また第二引数はメールサービス（Gmail等）の判別用として
	 * ・メールサービス
	 * に書き込まれます
	 */

	/**
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public static void writeXmlPreferences(String[] prefs, String ident) throws ParserConfigurationException, FileNotFoundException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation dom = builder.getDOMImplementation();
		Document doc = dom.createDocument("", "アカウント設定", null);
		Element root = doc.getDocumentElement();

		{/* 書き込み情報の構築*/
			Element acMailAddr = doc.createElement("メールアドレス");
			Element acPassword = doc.createElement("パスワード");
			Element smtpServer = doc.createElement("SMTPサーバー");
			Element smtpPort = doc.createElement("SMTPポート");
			Element imapServer = doc.createElement("IMAPサーバー");
			Element imapPort = doc.createElement("IMAPポート");
			Element mailServiceIdent = doc.createElement("メールサービス");

			acMailAddr.appendChild(doc.createTextNode(prefs[0]));
			acPassword.appendChild(doc.createTextNode(prefs[1]));
			smtpServer.appendChild(doc.createTextNode(prefs[2]));
			smtpPort.appendChild(doc.createTextNode(prefs[3]));
			imapServer.appendChild(doc.createTextNode(prefs[4]));
			imapPort.appendChild(doc.createTextNode(prefs[5]));
			mailServiceIdent.appendChild(doc.createTextNode(ident));

			root.appendChild(acMailAddr);
			root.appendChild(acPassword);
			root.appendChild(smtpServer);
			root.appendChild(smtpPort);
			root.appendChild(imapServer);
			root.appendChild(imapPort);
			root.appendChild(mailServiceIdent);
		}

		/* 設定情報の書き込み */
		TransformerFactory transFact  = TransformerFactory.newInstance();
		Transformer transformer = transFact.newTransformer();

		DOMSource source = new DOMSource(doc);
		File xml = new File("prefs.xml");
		FileOutputStream out = new FileOutputStream(xml);
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}

}
