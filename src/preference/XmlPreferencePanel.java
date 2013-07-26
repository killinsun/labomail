package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import net.miginfocom.swing.MigLayout;

import org.xml.sax.SAXException;

public class XmlPreferencePanel extends JPanel implements AccessMemberFields, ActionListener{

	/************ メンバ変数 ************/

	private PreferencesListener xmlWriter;
	private JTextField txtAcName;
	private JTextField txtSmtpPort;
	private JTextField txtImapServer;
	private JTextField txtImapPort;
	private JTextField txtSmtpServer;
	private JTextField txtAcMailAddr;
	private JPasswordField txtAcPassword;
	private JButton btnApply;

	/************************************/

	@Override
	public boolean allTextAreaIsNotEmpty(){
		boolean notEmpty = true;
		if(txtAcName.getText().equals("")) notEmpty = false;
		if(txtSmtpServer.getText().equals("")) notEmpty = false;
		if(txtSmtpPort.getText().equals("")) notEmpty = false;
		if(txtImapServer.getText().equals("")) notEmpty = false;
		if(txtImapPort.getText().equals("")) notEmpty = false;
		if(txtAcMailAddr.getText().equals("")) notEmpty = false;
		if(txtAcPassword.getText().equals("")) notEmpty = false;
		return notEmpty;
	}

	@Override
	public String[] getTexts() {
		return new String[]{
				txtAcName.getText(),
				txtSmtpServer.getText(),
				txtSmtpPort.getText(),
				txtImapServer.getText(),
				txtImapPort.getText(),
				txtAcMailAddr.getText(),
				txtAcPassword.getText()
		};
	}

	/************************************/

	public XmlPreferencePanel() {

		//初期値を設定
		xmlWriter = new PreferencesListener(this);

		this.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][]"));

		JLabel label = new JLabel("アカウント名");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label, "center");

		txtAcName = new JTextField();
		this.add(txtAcName, "grow, wrap");
		txtAcName.setColumns(10);

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
		btnApply.addActionListener(xmlWriter);
		btnApply.addActionListener(this);
		this.add(btnApply, "span 2, grow");


		/* テキストエリアへの設定読み込み */
		String[] prefs = null;
		try{ prefs = PreferenceLoader.getPreferences(); }
		catch(ParserConfigurationException | SAXException | IOException e){ e.printStackTrace(); }
		if(prefs != null){
			txtAcName.setText(prefs[0]);
			txtSmtpServer.setText(prefs[1]);
			txtSmtpPort.setText(prefs[2]);
			txtImapServer.setText(prefs[3]);
			txtImapPort.setText(prefs[4]);
			txtAcMailAddr.setText(prefs[5]);
			txtAcPassword.setText(prefs[6]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//XMLの書き込み完了を通知
		JOptionPane.showMessageDialog(null, "設定を書き込みました", "完了", JOptionPane.INFORMATION_MESSAGE);
	}

}
