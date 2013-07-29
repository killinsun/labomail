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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import preference.PreferenceLoader;
import net.miginfocom.swing.MigLayout;

/** 画面左側のメールリスト */
public class MailListPanel extends JPanel {
	
	// メール本文をsetTextするためメールビューの参照を保持
	private MailViewPanel mailView;
	private DefaultListModel<MailObject> listModel;
	private ArrayList<MailObject> mailArrayList;
	private JList<MailObject> mailJList;
	JComboBox<DBStrategy> comboBox;
	JLabel stateLabel;
	JButton changeModeBtn;

	private MailDB db;
	private MailImap imap;

	/** メールの取得先 */
	private GetMailState getMailState;
	
	private ListSelectAction listSelectAction = new ListSelectAction();

	
	public MailListPanel(MailViewPanel mailView) {
		
		db = new MailDB(true);
		
		String[] pref = null;
		try {
			// 設定ファイルからGmailのユーザ情報読み込み
			pref = PreferenceLoader.getPreferences();
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			JOptionPane.showMessageDialog(null, "設定ファイルの読み込みに失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
		}
		imap = new MailImap(pref[0], pref[1], pref[4], Integer.parseInt(pref[5]));
		
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
		updateMailList();

		mailJList = new JList<>(listModel);
		mailJList.setCellRenderer(new TextImageRenderer());
		mailJList.addListSelectionListener(listSelectAction);
		
		listScrollPane.setViewportView(mailJList);
		
		JPanel statusPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
		stateLabel = new JLabel(getMailState.getStateText());
		statusPanel.add(stateLabel, "c");
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
	private void updateMailList() {
		
		// 初期化
		listModel.setSize(0);
		
		// 選択されている方法でメールを取得
		List<MailObject> mails = new ArrayList<>();
		try {
			mails = getMailState.getMailList();
		} catch (SQLException | MessagingException | IOException e) {
			JOptionPane.showMessageDialog(
					null, "メールの取得に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalStateException e) {
			JOptionPane.showMessageDialog(
					null, "IMAPサーバーに接続できません。設定を確認してください。",
					"エラー", JOptionPane.ERROR_MESSAGE);
		}
		
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
		 List<MailObject> getMailList() throws SQLException, MessagingException, IOException, IllegalStateException;
		 Icon getIcon();
		 String getStateText();
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

		@Override
		public String getStateText() {
			return "送受信リスト";
		}
		
	}
	
	/** IMAPから取得 */
	class ImapState implements GetMailState {
		
		Icon icon = new ImageIcon("data/sent2.png");

		public ImapState() {
			if(!imap.isConnect()) {
				imap.connect();
			}
		}
		
		@Override
		public void changeState() {
			getMailState = new DBState();
		}

		@Override
		public List<MailObject> getMailList() throws MessagingException, IOException, IllegalStateException {
			return imap.getMail();
		}

		@Override
		public Icon getIcon() {
			return icon;
		}

		@Override
		public String getStateText() {
			return "Gmail";
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
			
			// 一時的にリスナーを無効に
			mailJList.removeListSelectionListener(listSelectAction);

			System.out.println("ComboBox value changed!");
			
			// 並び替え方法を変更
			db.setStrategy(comboBox.getItemAt(comboBox.getSelectedIndex()));
			// メールリストを更新
			updateMailList();

			mailJList.addListSelectionListener(listSelectAction);
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
			
			// 一時的にリスナーを無効に
			mailJList.removeListSelectionListener(listSelectAction);
			
			// メール取得方法を変更
			getMailState.changeState();
			// アイコンを変更
			changeModeBtn.setIcon(getMailState.getIcon());
			// テキスト変更
			stateLabel.setText(getMailState.getStateText());
			
			// メールリスト更新
			updateMailList();
			mailJList.addListSelectionListener(listSelectAction);
		}
		
	}
	
	/** 更新ボタン */
	class UpdateBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			mailJList.removeListSelectionListener(listSelectAction);
			
			// メールリスト更新
			updateMailList();
			mailJList.addListSelectionListener(listSelectAction);
		}	
	}
	
}
