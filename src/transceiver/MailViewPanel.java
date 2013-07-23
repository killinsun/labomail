package transceiver;

import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;


public class MailViewPanel extends JPanel {

	private JTextPane mailTextPane;
	private JPanel metaDataPanel;
	private JScrollPane scrollPane;
	
	private JLabel from, to, subject, date;
	
	public MailViewPanel() {

		setLayout(new MigLayout("", "[grow]", "[][grow]"));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		metaDataPanel = new JPanel(new MigLayout("wrap 2", "[100][]", "[]"));
		metaDataPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		metaDataPanel.add(new JLabel("FROM:"), "r");
		metaDataPanel.add(from = new JLabel(), "");
		metaDataPanel.add(new JLabel("TO:"), "r");
		metaDataPanel.add(to = new JLabel(), "");
		metaDataPanel.add(new JLabel("SUBJECT:"), "r");
		metaDataPanel.add(subject = new JLabel(), "");
		metaDataPanel.add(new JLabel("DATE:"), "r");
		metaDataPanel.add(date = new JLabel(), "");
		add(metaDataPanel, "grow, wrap");
		
		JPanel textPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		mailTextPane = new JTextPane();
		mailTextPane.setText("");
		mailTextPane.setEditable(false);
		textPanel.add(mailTextPane, "grow");
		
		scrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(mailTextPane);
		add(scrollPane, "grow");
		
	}

	public void setMetaData(MailObject mailObject) {
		
		from.setText(mailObject.getFrom());
		to.setText(mailObject.getTo());
		subject.setText(mailObject.getSubject());
		date.setText(mailObject.getDate().toString());
	}
	
	public void setText(String text) {
		
		// TODO: メールを開いた際、末尾までスクロールしてしまう。
		mailTextPane.setText(text);
		JViewport view = scrollPane.getViewport();
		view.setView(mailTextPane);
		view.setViewPosition(new Point(0, 0));
	}
	
}
