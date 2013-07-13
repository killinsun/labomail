package TopView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class MailListViewPanel extends JPanel {
	
	private JTextPane mailTextPane;
	
	public MailListViewPanel() {

		this.setLayout(new MigLayout("", "[][grow]", "[grow]"));
		
		JPanel leftSidePanel = new JPanel(new MigLayout("", "[]", "[50][grow]"));
		leftSidePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		JScrollPane listScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		DefaultListModel<MailObject> model = new DefaultListModel<>();
		ArrayList<MailObject> mailList = new ArrayList<>();
		mailList.add(new MailObject("hoge@foo.com", "ほげぇ", new Date(), "本文だよん"));
		mailList.add(new MailObject("moge@ppp.co.jp", "もげぇ", new Date(), "本文\nなのです"));
		mailList.add(new MailObject("poge@feaw.ne.jp", "ぽげぇ", new Date(), "本文\nである"));
		mailList.add(new MailObject("fage@geaw.ac.jp", "ふぁげぇ", new Date(), "本文\n\n\nかもしれない"));
		mailList.add(new MailObject("piyo@pipipi.com", "ぴよ", new Date(), "本文だった", Status.RECEIVE));
		mailList.add(new MailObject("moge@mogeru.com", "もげ", new Date(), "本\n文\nの\nよ\nう\nな\nも\nの\n", Status.SENT));
		mailList.add(new MailObject());
		
		for(MailObject mail : mailList) {
			model.addElement(mail);
		}

		JList<MailObject> jList = new JList<>(model);
		jList.setCellRenderer(new TextImageRenderer());
		jList.addMouseListener(new ListClickAction());
		
		listScrollPane.setViewportView(jList);
		
		JPanel showStatusPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
		showStatusPanel.add(new JLabel("送受信リスト"));
		showStatusPanel.add(new JLabel(new ImageIcon("data/not_send2.png")));
		leftSidePanel.add(showStatusPanel,"grow, wrap");

		leftSidePanel.add(listScrollPane, "grow");
		this.add(leftSidePanel, "grow");
		
		JPanel textPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		textPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		mailTextPane = new JTextPane();
		textPanel.add(mailTextPane, "grow");
		this.add(textPanel, "grow");

	}
	
	class ListClickAction extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {

			System.out.println(e.getSource().getClass());
			
			JList<MailObject> list = (JList<MailObject>) e.getSource();
			
			System.out.println(list.getSelectedValue() + "\n");
			
			mailTextPane.setText(list.getSelectedValue().getText());
		}
	}
	
}
