package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.miginfocom.swing.MigLayout;

import org.xml.sax.SAXException;

public class GmailPreferencePanel extends JPanel implements ActionListener{

	/************ メンバ変数 ************/

	private JTextField txtAcMailAddr;
	private JPasswordField txtAcPassword;
	private JButton btnApply;

	/************************************/

	public boolean allTextAreaIsNotEmpty(){
		boolean notEmpty = true;
		if(txtAcMailAddr.getText().equals("")) notEmpty = false;
		if(txtAcPassword.getText().equals("")) notEmpty = false;
		return notEmpty;
	}

	/************************************/

	public GmailPreferencePanel() {

		/*** コンポーネントを生成 ***/

		this.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][]"));

		JLabel label_1 = new JLabel("メールアドレス");
		this.add(label_1, "center");

		txtAcMailAddr = new JTextField();
		this.add(txtAcMailAddr, "grow, wrap");
		txtAcMailAddr.setColumns(10);

		JLabel label_2 = new JLabel("パスワード");
		this.add(label_2, "center");

		txtAcPassword = new JPasswordField();
		this.add(txtAcPassword, "grow, wrap");
		txtAcPassword.setColumns(10);

		this.add(new JLabel(""), "height 10, wrap");

		btnApply = new JButton("決定");
		btnApply.addActionListener(this);
		this.add(btnApply, "span 2, grow");


		/* テキストエリアへの設定読み込み */
		String[] prefs = null;
		try{ prefs = PreferenceLoader.getPreferences(); }
		catch(ParserConfigurationException | SAXException | IOException e){ e.printStackTrace(); }
		if(prefs != null){
			txtAcMailAddr.setText(prefs[0]);
			txtAcPassword.setText(prefs[1]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//全項目が入力されていることを確認
		if(!allTextAreaIsNotEmpty()){
			JOptionPane.showMessageDialog(null, "入力されていない項目があります", "エラー", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//XMLに書き込む内容を配列にまとめる
		String[] property = new String[]{
				txtAcMailAddr.getText(),
				txtAcPassword.getText(),
				"smtp.gmail.com",
				"587",
				"imap.gmail.com",
				"993"
		};

		try {
			//XMLに設定とメールサービス属性を書き込み
			PreferencesWriter.writeXmlPreferences(property, "Gmail");
			//XMLの書き込み完了を通知
			JOptionPane.showMessageDialog(null, "設定を書き込みました", "完了", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException | ParserConfigurationException | TransformerException e1) {
			e1.printStackTrace();
		}

	}

}
