package transceiver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private DefaultListModel<MailObject> listModel;
	private ArrayList<MailObject> mailArrayList;
	private JList<MailObject> mailJList;
	JComboBox<Strategy> comboBox;

	private MailDB db;

	public MailListPanel(MailViewPanel mailView) {
		
		db = new MailDB(true);
		
		this.mailView = mailView;
		
		setLayout(new MigLayout("", "[]", "[50][][grow]"));
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		JScrollPane listScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		listModel = new DefaultListModel<>();
		mailArrayList = new ArrayList<>();

		try {
			updateMailList();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		mailJList = new JList<>(listModel);
		mailJList.setCellRenderer(new TextImageRenderer());
		mailJList.addMouseListener(new ListClickAction());
		mailJList.addKeyListener(new ListKeyAction());
		
		listScrollPane.setViewportView(mailJList);
		
		JPanel showStatusPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
		showStatusPanel.add(new JLabel("送受信リスト"));
		showStatusPanel.add(new JLabel(new ImageIcon("data/not_send2.png")));
		add(showStatusPanel,"grow, wrap");
		
		comboBox = new JComboBox<>();
		comboBox.addItem(new NewestFirstStrategy());
		comboBox.addItem(new OldestFirstStrategy());
		comboBox.addActionListener(new comboBoxChangedAction());
		add(comboBox, "grow, wrap");

		add(listScrollPane, "grow");

	}
	
	private void updateMailList() throws SQLException {
		
		listModel.setSize(0);
		
		ResultSet rSet = db.getMails();
		while (rSet.next()) {
			listModel.addElement(
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
		validate();
	}
	
	private void updateMailView(MailObject mailObject) {
		
		mailView.setMetaData(mailObject);
		mailView.setText(mailObject.getData());
	}
	
	class ListClickAction extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			
			updateMailView(mailJList.getSelectedValue());
		}
	}
	
	class ListKeyAction extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			
			switch (key) {
			case KeyEvent.VK_ENTER:
				updateMailView(mailJList.getSelectedValue());
				break;

			default:
				break;
			}
		}
	}

	class comboBoxChangedAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("ComboBox value changed!");
			db.setStrategy(comboBox.getItemAt(comboBox.getSelectedIndex()));
			try {
				updateMailList();
			} catch (SQLException e1) {
				System.err.println(e1.getMessage());
			}
			validate();
		}
		
	}
	
}
