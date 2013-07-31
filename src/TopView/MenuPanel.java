package TopView;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;
import preference.SelectServicePanel;
import senderView.MailSenderPanel;
import AddressBook.PaneAddress;
import DustBox.Dustbox_main;

/** 画面上部のメニュー */
public class MenuPanel extends JPanel {

	// マップ
	HashMap<String, IconSet> iconMap;
	HashMap<String, Class<? extends JComponent>> panelMap;

	// 一意に対応付けさせるための文字列
	private final String NEW_MAIL_IDENT = "新規作成";
	private final String RECEIVE_IDENT = "受信BOX";
	private final String SENT_IDENT = "送信BOX";
	private final String NOT_SEND_IDENT = "未送信BOX";
	private final String TRUSH_IDENT = "ゴミ箱";
	private final String ADDRESSBOOK_IDENT = "アドレス帳";
	private final String OPTION_IDENT = "設定";

	// アイコンたち
	JLabel newMail, receiveBox, sentBox, notSendBox, trush, addressBook, option;

	// あとでインスタンス生成するため、各JComponentのクラスを保持
	Class<? extends JComponent> paneAddress, senderView, optionPanel, paneDustBox, dummyPanel;

	public MenuPanel() {

		// レイアウト設定
		this.setLayout(new MigLayout("", "[]40[]40[]40[]40[]40[]", "[grow]"));
		this.setBorder(new BevelBorder(BevelBorder.RAISED));

		// アイコンマップの設定
		iconMap = new HashMap<>();
		iconMap.put(NEW_MAIL_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/newMail.png"),
				new ImageIcon("data/menuIcon/newMail2.png")));
		iconMap.put(RECEIVE_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/receive.png"),
				new ImageIcon("data/menuIcon/receive2.png")));
		iconMap.put(SENT_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/sent.png"),
				new ImageIcon("data/menuIcon/sent2.png")));
		iconMap.put(NOT_SEND_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/notSend.png"),
				new ImageIcon("data/menuIcon/notSend2.png")));
		iconMap.put(TRUSH_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/trush.png"),
				new ImageIcon("data/menuIcon/trush2.png")));
		iconMap.put(ADDRESSBOOK_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/address.png"),
				new ImageIcon("data/menuIcon/address2.png")));
		iconMap.put(OPTION_IDENT, new IconSet(
				new ImageIcon("data/menuIcon/option.png"),
				new ImageIcon("data/menuIcon/option2.png")));

		// 各種アイコンの設定と追加
		JLabel newMail = new JLabel(NEW_MAIL_IDENT, iconMap.get(NEW_MAIL_IDENT).getDefault(), JLabel.CENTER);
		newMail.setName(NEW_MAIL_IDENT);
		JLabel receiveBox = new JLabel(RECEIVE_IDENT, iconMap.get(RECEIVE_IDENT).getDefault(), JLabel.CENTER);
		receiveBox.setName(RECEIVE_IDENT);
		JLabel sentBox = new JLabel(SENT_IDENT, iconMap.get(SENT_IDENT).getDefault(), JLabel.CENTER);
		sentBox.setName(SENT_IDENT);
		JLabel notSendBox = new JLabel(NOT_SEND_IDENT, iconMap.get(NOT_SEND_IDENT).getDefault(), JLabel.CENTER);
		notSendBox.setName(NOT_SEND_IDENT);
		JLabel trush = new JLabel(TRUSH_IDENT, iconMap.get(TRUSH_IDENT).getDefault(), JLabel.CENTER);
		trush.setName(TRUSH_IDENT);
		JLabel addressBook = new JLabel(ADDRESSBOOK_IDENT, iconMap.get(ADDRESSBOOK_IDENT).getDefault(), JLabel.CENTER);
		addressBook.setName(ADDRESSBOOK_IDENT);
		JLabel option = new JLabel(OPTION_IDENT, iconMap.get(OPTION_IDENT).getDefault(), JLabel.CENTER);
		option.setName(OPTION_IDENT);

		List<JLabel> menuIcons = Arrays.asList(newMail, receiveBox, sentBox, notSendBox, trush, addressBook, option);
		for (JLabel menuIcon : menuIcons) {
			menuIcon.setHorizontalTextPosition(JLabel.CENTER);
			menuIcon.setVerticalTextPosition(JLabel.BOTTOM);
			menuIcon.addMouseListener(new MenuIconsAction());
			add(menuIcon, "");
		}

		// パネル設定
		paneAddress = PaneAddress.class;
		senderView = MailSenderPanel.class;
		// TODO: JscrollPane消しちゃったから新規作成画面の方でなんとかしてください＞＜ (to:あいやくん)
//		senderView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		optionPanel = SelectServicePanel.class;
		paneDustBox = Dustbox_main.class;
		dummyPanel = DummyPanel.class;

		panelMap = new HashMap<>();
		panelMap.put(NEW_MAIL_IDENT, senderView);
		panelMap.put(TRUSH_IDENT, paneDustBox);
		panelMap.put(ADDRESSBOOK_IDENT, paneAddress);
		panelMap.put(OPTION_IDENT, optionPanel);
	}

//
// MouseListener ----------------------------------------------------------------------------------------------------
//

	class MenuIconsAction extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {

			// マウスカーソルを手の形に変える
			Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			JLabel label = (JLabel)e.getSource();
			label.setCursor(cursor);

			// アイコンに変化をつける
			label.setIcon(iconMap.get(label.getName()).getOnMouse());
		}

		public void mouseExited(MouseEvent e) {
			// アイコンをもとに戻す
			JLabel label = (JLabel)e.getSource();
			label.setIcon(iconMap.get(label.getName()).getDefault());
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			/* 対応するパネルを表示 */

			Component comp = e.getComponent();
			String name = comp.getName();
			Class<? extends JComponent> panel = panelMap.get(name);

			System.out.println(name + " was called!");
			if(panel == null) {
				return;
			}
			if(!TopView.showTab(name)) {
				try {
					// インスタンス生成と追加
					JComponent newComponent = panel.newInstance();
					TopView.addTab(name, newComponent);
				} catch (InstantiationException | IllegalAccessException e1) {
					JOptionPane.showMessageDialog(null, "タブが開けません", "エラー", JOptionPane.ERROR_MESSAGE);
				}
			}

		}


	}

}
