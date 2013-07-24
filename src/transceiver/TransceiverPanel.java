package transceiver;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class TransceiverPanel extends JPanel {
	
	MailDB db;
	
	public TransceiverPanel() {
		
		// TODO: 表示が色々と見づらいので、レイアウトを要確認。

		this.setLayout(new MigLayout("", "[grow][]", "[grow]"));
		
		MailViewPanel mailView = new MailViewPanel();
		MailListPanel mailList = new MailListPanel(mailView);
		
		this.add(mailList, "grow");
		this.add(mailView, "grow");

	}
	
}
