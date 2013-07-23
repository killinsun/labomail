package transceiver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	JComboBox<DBStrategy> comboBox;
	JButton changeModeBtn;

	private MailDB db;
	private MailImap imap;
	
	private GetMailState getMailState;

	public MailListPanel(MailViewPanel mailView) {
		
		db = new MailDB(true);
		imap = new MailImap("laboaiueo@gmail.com", "labolabo");
		
		this.mailView = mailView;
		
		setLayout(new MigLayout("", "[]", "[50][][grow][]"));
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		JScrollPane listScrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		listModel = new DefaultListModel<>();
		mailArrayList = new ArrayList<>();
		
		getMailState = new DBState();

		try {
			updateMailList();
		}
		catch (SQLException | MessagingException | IOException e) {
			System.err.println(e.getMessage());
		}

		mailJList = new JList<>(listModel);
		mailJList.setCellRenderer(new TextImageRenderer());
		mailJList.addMouseListener(new ListClickAction());
		mailJList.addKeyListener(new ListKeyAction());
		
		listScrollPane.setViewportView(mailJList);
		
		JPanel statusPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
		statusPanel.add(new JLabel("送受信リスト"), "c");
		changeModeBtn = new JButton(new ImageIcon("data/not_send2.png"));
		changeModeBtn.addActionListener(new ModeChangeBtnAction());
		statusPanel.add(changeModeBtn);
		add(statusPanel,"grow, wrap");
		
		comboBox = new JComboBox<>();
		comboBox.addItem(new NewestFirstStrategy());
		comboBox.addItem(new OldestFirstStrategy());
		comboBox.addActionListener(new comboBoxChangedAction());
		add(comboBox, "grow, wrap");

		add(listScrollPane, "grow, wrap");
		
		JButton updateBtn = new JButton("更新");
		updateBtn.addActionListener(new UpdateBtnAction());
		add(updateBtn, "r, wrap");

	}
	
	private void updateMailList() throws SQLException, MessagingException, IOException {
		
		listModel.setSize(0);
		
		List<MailObject> mails = getMailState.getMailList();
		
		for (MailObject mailObject : mails) {
			listModel.addElement(mailObject);
		}
		
		validate();
	}
	
	private void updateMailView(MailObject mailObject) {
		
		mailView.setMetaData(mailObject);
		mailView.setText(mailObject.getData());
	}
	
//
// GetMailState -----------------------------------------------------------------------------------
//
	
	interface GetMailState {
		 void changeState();
		 List<MailObject> getMailList() throws SQLException, MessagingException, IOException;
		 Icon getIcon();
	}
	
	class DBState implements GetMailState {

		Icon icon = new ImageIcon("data/not_send2.png");
		
		@Override
		public void changeState() {
			getMailState = new ImapState();
		}

		@Override
		public List<MailObject> getMailList() throws SQLException {
			return db.getMailObjects();
		}

		@Override
		public Icon getIcon() {
			return icon;
		}
		
	}
	
	class ImapState implements GetMailState {
		
		Icon icon = new ImageIcon("data/sent2.png");

		@Override
		public void changeState() {
			getMailState = new DBState();
		}

		@Override
		public List<MailObject> getMailList() throws MessagingException, IOException {
			return imap.getMail();
		}

		@Override
		public Icon getIcon() {
			return icon;
		}
		
	}
	
//
// Jlist Action ------------------------------------------------------------------------------------
//
	
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
	
//
// ComboBox Action ------------------------------------------------------------------------------------
//

	class comboBoxChangedAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("ComboBox value changed!");
			db.setStrategy(comboBox.getItemAt(comboBox.getSelectedIndex()));
			try {
				updateMailList();
			} catch (SQLException | MessagingException | IOException e1) {
				System.err.println(e1.getMessage());
			}
			validate();
		}
		
	}
	
//
// Buttons Action --------------------------------------------------------------------------------------
//

	class ModeChangeBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			getMailState.changeState();
			changeModeBtn.setIcon(getMailState.getIcon());
			try {
				updateMailList();
			} catch (SQLException | MessagingException | IOException e1) {
				System.err.println(e1.getMessage());
			}
		}
		
	}
	
	class UpdateBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				updateMailList();
			} catch (SQLException | MessagingException | IOException e1) {
				System.err.println(e1.getMessage());
			}
		}
		
	}
}
