package TopView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		
		try {
			MailDB db = new MailDB(true);
			ResultSet rSet = db.getAllMails();
			while (rSet.next()) {
				mailList.add(
						new MailObject(
								rSet.getInt("id"), 
								rSet.getInt("mboxid"), 
								rSet.getInt("boxid"), 
								rSet.getString("mfrom"), 
								rSet.getString("mto"), 
								rSet.getString("subject"),
								rSet.getString("data"),
								rSet.getString("date"),
								rSet.getString("path")
								)
						);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
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
			
			mailTextPane.setText(list.getSelectedValue().getData());
		}
	}
	
}
