package transceiver;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;


public class MailViewPanel extends JPanel {

	private JTextPane mailTextPane;
	private JPanel metaDataPanel;
	
	public MailViewPanel() {

		setLayout(new MigLayout("", "[grow]", "[][grow]"));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		metaDataPanel = new JPanel(new MigLayout("wrap 2", "[100][]", "[]"));
		metaDataPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		metaDataPanel.add(new JLabel("FROM:"), "r");
		metaDataPanel.add(new JLabel("hogehoge@foo.com"), "");
		metaDataPanel.add(new JLabel("TO:"), "r");
		metaDataPanel.add(new JLabel("nullpo_ga@bar.ne.jp"), "");
		metaDataPanel.add(new JLabel("SUBJECT:"), "r");
		metaDataPanel.add(new JLabel("けんめい"), "");
		metaDataPanel.add(new JLabel("DATE:"), "r");
		metaDataPanel.add(new JLabel("xx/xx/xx 00:00:00 (XX時間前)"), "");
		add(metaDataPanel, "grow, wrap");
		
		JPanel textPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		mailTextPane = new JTextPane();
		mailTextPane.setText("てすと\nhoge\n\nふぉおふぉお");
		mailTextPane.setEditable(false);
		textPanel.add(mailTextPane, "grow");
		add(mailTextPane, "grow");

	}

	public void setMetaData() {
		
	}
	
	public void setText(String text) {
		
	}
	
}
