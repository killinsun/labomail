package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
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

public class PreferencesListener implements ActionListener{

	/************************************/

	//呼び出し元フレーム
	private AccessMemberFields calledFrame;

	/* 呼び出し元フレームの受け取り */
	public PreferencesListener(AccessMemberFields calledFrame){
		this.calledFrame = calledFrame;
	}


	/************ リスナー ************/

	@Override
	public void actionPerformed(ActionEvent e) {
		if(calledFrame.allTextAreaIsNotEmpty()){
			try {
				//入力された設定をxmlに書き込む
				writeXmlPreferences(calledFrame.getTexts());
			} catch (FileNotFoundException | ParserConfigurationException | TransformerException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}else{
			//エラーメッセージを表示する
			JOptionPane.showMessageDialog(null, "空の項目があります", "エラー", JOptionPane.ERROR_MESSAGE);
		}
	}

	/************ xmlファイルへの書き込み ************/

	/**
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public void writeXmlPreferences(String[] prefs) throws ParserConfigurationException, FileNotFoundException, TransformerException {
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

			smtpServer.appendChild(doc.createTextNode(prefs[0]));
			smtpPort.appendChild(doc.createTextNode(prefs[1]));
			imapServer.appendChild(doc.createTextNode(prefs[2]));
			imapPort.appendChild(doc.createTextNode(prefs[3]));
			acMailAddr.appendChild(doc.createTextNode(prefs[4]));
			acPassword.appendChild(doc.createTextNode(prefs[5]));

			root.appendChild(acMailAddr);
			root.appendChild(acPassword);
			root.appendChild(smtpServer);
			root.appendChild(smtpPort);
			root.appendChild(imapServer);
			root.appendChild(imapPort);
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
