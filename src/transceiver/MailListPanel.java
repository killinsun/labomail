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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

/** 画面左側のメールリスト */
public class MailListPanel extends JPanel {
	
	// メール本文をsetTextするためメールビューの参照を保持
	private MailViewPanel mailView;
	private DefaultListModel<MailObject> listModel;
	private ArrayList<MailObject> mailArrayList;
	private JList<MailObject> mailJList;
	JComboBox<DBStrategy> comboBox;
	JButton changeModeBtn;

	private MailDB db;
	private MailImap imap;

	/** メールの取得先 */
	private GetMailState getMailState;

	
	public MailListPanel(MailViewPanel mailView) {
		
		db = new MailDB(true);
		// TODO: 現在IMAP取得先は固定
		imap = new MailImap("laboaiueo@gmail.com", "labolabo");
		
		this.mailView = mailView;
		
		setLayout(new MigLayout("", "[]", "[50][][grow][]"));
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		JScrollPane listScrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		listModel = new DefaultListModel<>();
		mailArrayList = new ArrayList<>();
		
		// DBからメール取得
		getMailState = new DBState();
		try {
			updateMailList();
		}
		catch (SQLException | MessagingException | IOException e) {
			System.err.println(e.getMessage());
		}

		mailJList = new JList<>(listModel);
		mailJList.setCellRenderer(new TextImageRenderer());
		mailJList.addListSelectionListener(new ListSelectAction());
		
		listScrollPane.setViewportView(mailJList);
		
		JPanel statusPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
		statusPanel.add(new JLabel("送受信リスト"), "c");
		changeModeBtn = new JButton(new ImageIcon("data/not_send2.png"));
		changeModeBtn.addActionListener(new ModeChangeBtnAction());
		statusPanel.add(changeModeBtn);
		add(statusPanel,"grow, wrap");
		
		// コンボボックスに「新しい順」「古い順」の項目追加
		// TODO: 現在並び替えはDBからの取得のみ対応
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
	
	/** メールリスト更新 */
	private void updateMailList() throws SQLException, MessagingException, IOException {
		
		// 初期化
		listModel.setSize(0);
		
		// 選択されている方法でメールを取得
		List<MailObject> mails = getMailState.getMailList();
		
		for (MailObject mailObject : mails) {
			listModel.addElement(mailObject);
		}
		
		validate();
	}
	
	/** メールビュー更新 */
	private void updateMailView(MailObject mailObject) {
		
		mailView.setMetaData(mailObject);
		mailView.setText(mailObject.getData());
	}
	
//
// GetMailState -----------------------------------------------------------------------------------
//
	
	/** Stateパターン */
	interface GetMailState {
		 void changeState();
		 List<MailObject> getMailList() throws SQLException, MessagingException, IOException;
		 Icon getIcon();
	}
	
	/** DBから取得 */
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
	
	/** IMAPから取得 */
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
	
	/** メールリストのアイテム選択時の処理 */
	class ListSelectAction implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			// 選択されたメールを表示
			updateMailView(mailJList.getSelectedValue());
		}
		
	}
	
	
//
// ComboBox Action ------------------------------------------------------------------------------------
//

	/** コンボボックス選択時の処理 */
	class comboBoxChangedAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("ComboBox value changed!");
			
			// 並び替え方法を変更
			db.setStrategy(comboBox.getItemAt(comboBox.getSelectedIndex()));
			try {
				// メールリストを更新
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

	/** 状態遷移ボタン */
	class ModeChangeBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// メール取得方法を変更
			getMailState.changeState();
			// アイコンを変更
			changeModeBtn.setIcon(getMailState.getIcon());
			try {
				// メールリスト更新
				updateMailList();
			} catch (SQLException | MessagingException | IOException e1) {
				System.err.println(e1.getMessage());
			}
		}
		
	}
	
	/** 更新ボタン */
	class UpdateBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// メールリスト更新
				updateMailList();
			} catch (SQLException | MessagingException | IOException e1) {
				System.err.println(e1.getMessage());
			}
		}
		
	}
}
