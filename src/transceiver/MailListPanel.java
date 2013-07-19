package transceiver;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class MailListPanel extends JPanel {
	
	private MailViewPanel mailView;
	private DefaultListModel<MailObject> model;
	private ArrayList<MailObject> mailList;
	private JList<MailObject> jList;

	private MailDB db;

	public MailListPanel(MailViewPanel mailView) {
		
		this.mailView = mailView;
		
		setLayout(new MigLayout("", "[]", "[50][][grow]"));
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		JScrollPane listScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		model = new DefaultListModel<>();
		mailList = new ArrayList<>();

		try {
			db = new MailDB(true);
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
								Timestamp.valueOf(rSet.getString("date")),
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

		jList = new JList<>(model);
		jList.setCellRenderer(new TextImageRenderer());
		jList.addMouseListener(new ListClickAction());
		jList.addKeyListener(new ListKeyAction());
		
		listScrollPane.setViewportView(jList);
		
		JPanel showStatusPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
		showStatusPanel.add(new JLabel("送受信リスト"));
		showStatusPanel.add(new JLabel(new ImageIcon("data/not_send2.png")));
		add(showStatusPanel,"grow, wrap");
		
		JComboBox<Strategy> comboBox = new JComboBox<>();
		comboBox.addItem(new NewestFirstStrategy());
		comboBox.addItem(new OldestFirstStrategy());
		add(comboBox, "grow, wrap");

		add(listScrollPane, "grow");

	}
	
	class ListClickAction extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {

			System.out.println(e.getSource().getClass());
			
			JList<MailObject> list = (JList<MailObject>) e.getSource();
			
			System.out.println(list.getSelectedValue() + "\n");
			
			mailView.setText(list.getSelectedValue().getData());
		}
	}
	
	class ListKeyAction extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			
			switch (key) {
			case KeyEvent.VK_ENTER:
				mailView.setText(mailList.get(jList.getSelectedIndex()).getData());
				break;

			default:
				break;
			}
		}
	}

	
}
