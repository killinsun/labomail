package transceiver;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class TransceiverPanel extends JPanel {
	
	MailDB db;
	
	public TransceiverPanel() {
		
		this.setLayout(new MigLayout("", "[grow][grow]", "[grow]"));
		
		MailViewPanel mailView = new MailViewPanel();
		MailListPanel mailList = new MailListPanel(mailView);
		
		this.add(mailList, "width 280::, grow, dock west");
		this.add(mailView, "grow");

	}
	
}
