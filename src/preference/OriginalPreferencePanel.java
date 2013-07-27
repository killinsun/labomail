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
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.miginfocom.swing.MigLayout;

import org.xml.sax.SAXException;

public class OriginalPreferencePanel extends JPanel implements ActionListener{

	/************ メンバ変数 ************/

	private JTextField txtAcMailAddr;
	private JPasswordField txtAcPassword;
	private JTextField txtSmtpServer;
	private JTextField txtSmtpPort;
	private JTextField txtImapServer;
	private JTextField txtImapPort;
	private JButton btnApply;

	/************************************/

	public boolean allTextAreaIsNotEmpty(){
		boolean notEmpty = true;
		if(txtAcMailAddr.getText().equals("")) notEmpty = false;
		if(txtAcPassword.getText().equals("")) notEmpty = false;
		if(txtSmtpServer.getText().equals("")) notEmpty = false;
		if(txtSmtpPort.getText().equals("")) notEmpty = false;
		if(txtImapServer.getText().equals("")) notEmpty = false;
		if(txtImapPort.getText().equals("")) notEmpty = false;
		return notEmpty;
	}

	public String[] getTexts() {
		return new String[]{
				txtAcMailAddr.getText(),
				txtAcPassword.getText(),
				txtSmtpServer.getText(),
				txtSmtpPort.getText(),
				txtImapServer.getText(),
				txtImapPort.getText()
		};
	}

	/************************************/

	public OriginalPreferencePanel() {

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

		JLabel lblSmtp = new JLabel("SMTPサーバー");
		lblSmtp.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblSmtp, "center");

		txtSmtpServer = new JTextField();
		this.add(txtSmtpServer, "grow, wrap");
		txtSmtpServer.setColumns(10);

		JLabel lblSmtp_1 = new JLabel("SMTPポート");
		this.add(lblSmtp_1, "center");

		txtSmtpPort = new JTextField();
		this.add(txtSmtpPort, "grow, wrap");
		txtSmtpPort.setColumns(10);

		JLabel lblImap = new JLabel("IMAPサーバー");
		this.add(lblImap, "center");

		txtImapServer = new JTextField();
		this.add(txtImapServer, "grow, wrap");
		txtImapServer.setColumns(10);

		JLabel lblImap_1 = new JLabel("IMAPポート");
		this.add(lblImap_1, "center");

		txtImapPort = new JTextField();
		this.add(txtImapPort, "grow, wrap");
		txtImapPort.setColumns(10);

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
			txtSmtpServer.setText(prefs[2]);
			txtSmtpPort.setText(prefs[3]);
			txtImapServer.setText(prefs[4]);
			txtImapPort.setText(prefs[5]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			//XMLに設定内容を書き込む
			PreferencesWriter.writeXmlPreferences(getTexts(), "none");

			//XMLの書き込み完了を通知
			JOptionPane.showMessageDialog(null, "設定を書き込みました", "完了", JOptionPane.INFORMATION_MESSAGE);

		} catch (FileNotFoundException | ParserConfigurationException | TransformerException e1) {
			e1.printStackTrace();
		}
	}

}
