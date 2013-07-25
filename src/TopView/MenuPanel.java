package TopView;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import preference.XmlPreferencePanel;

import net.miginfocom.swing.MigLayout;
import senderView.MailSenderPanel;
import AddressBook.PaneAddress;
import DustBox.Dustbox_main;

/** 画面上部のメニュー */
public class MenuPanel extends JPanel {
	
	// マップ
	HashMap<String, IconSet> iconMap;
	HashMap<String, JPanel> panelMap;
	
	// 一意に対応付けさせるための文字列
	private final String newMailStr = "新規作成";
	private final String receiveStr = "受信BOX";
	private final String sentStr = "送信BOX";
	private final String notSendStr = "未送信BOX";
	private final String trushStr = "ゴミ箱";
	private final String addressBookStr = "アドレス帳";
	private final String optionStr = "設定";

	// アイコンたち
	JLabel newMail, receiveBox, sentBox, notSendBox, trush, addressBook, option;
	
	// アイコンクリックで表示する各種JPanel
	PaneAddress paneAddress;
	Dustbox_main paneDustbox;
	/** デバッグ用 */
	JPanel dummyPanel;

	public MenuPanel() {

		// レイアウト設定
		this.setLayout(new MigLayout("", "[]40[]40[]40[]40[]40[]", "[grow]"));
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// アイコンマップの設定
		iconMap = new HashMap<>();
		iconMap.put(newMailStr, new IconSet(
				new ImageIcon("data/menuIcon/newMail.png"), 
				new ImageIcon("data/menuIcon/newMail2.png")));
		iconMap.put(receiveStr, new IconSet(
				new ImageIcon("data/menuIcon/receive.png"), 
				new ImageIcon("data/menuIcon/receive2.png")));
		iconMap.put(sentStr, new IconSet(
				new ImageIcon("data/menuIcon/sent.png"), 
				new ImageIcon("data/menuIcon/sent2.png")));
		iconMap.put(notSendStr, new IconSet(
				new ImageIcon("data/menuIcon/notSend.png"), 
				new ImageIcon("data/menuIcon/notSend2.png")));
		iconMap.put(trushStr, new IconSet(
				new ImageIcon("data/menuIcon/trush.png"), 
				new ImageIcon("data/menuIcon/trush2.png")));
		iconMap.put(addressBookStr, new IconSet(
				new ImageIcon("data/menuIcon/address.png"), 
				new ImageIcon("data/menuIcon/address2.png")));
		iconMap.put(optionStr, new IconSet(
				new ImageIcon("data/menuIcon/option.png"), 
				new ImageIcon("data/menuIcon/option2.png")));

		// 各種アイコンの設定と追加 
		JLabel newMail = new JLabel(newMailStr, iconMap.get(newMailStr).getDefault(), JLabel.CENTER);
		newMail.setName(newMailStr);
		JLabel receiveBox = new JLabel(receiveStr, iconMap.get(receiveStr).getDefault(), JLabel.CENTER);
		receiveBox.setName(receiveStr);
		JLabel sentBox = new JLabel(sentStr, iconMap.get(sentStr).getDefault(), JLabel.CENTER);
		sentBox.setName(sentStr);
		JLabel notSendBox = new JLabel(notSendStr, iconMap.get(notSendStr).getDefault(), JLabel.CENTER);
		notSendBox.setName(notSendStr);
		JLabel trush = new JLabel(trushStr, iconMap.get(trushStr).getDefault(), JLabel.CENTER);
		trush.setName(trushStr);
		JLabel addressBook = new JLabel(addressBookStr, iconMap.get(addressBookStr).getDefault(), JLabel.CENTER);
		addressBook.setName(addressBookStr);
		JLabel option = new JLabel(optionStr, iconMap.get(optionStr).getDefault(), JLabel.CENTER);
		option.setName(optionStr);
		
		List<JLabel> menuIcons = Arrays.asList(newMail, receiveBox, sentBox, notSendBox, trush, addressBook, option);
		for (JLabel menuIcon : menuIcons) {
			menuIcon.setHorizontalTextPosition(JLabel.CENTER);
			menuIcon.setVerticalTextPosition(JLabel.BOTTOM);
			menuIcon.addMouseListener(new MenuIconsAction());
			add(menuIcon, "");
		}
		
		// パネル設定
		paneAddress = new PaneAddress();
		paneAddress.setName(addressBookStr);
		paneDustbox = new Dustbox_main();
		paneDustbox.setName(trushStr);
		dummyPanel = new DummyPanel();
		dummyPanel.setName(optionStr);

		panelMap = new HashMap<>();
		List<JPanel> panels = Arrays.asList(paneAddress, paneDustbox, dummyPanel);
		for (JPanel panel : panels) {
			panelMap.put(panel.getName(), panel);
		}
	}
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
			
			// 対応するパネルを表示
			
			Component comp = e.getComponent();
			String name = comp.getName();
			JPanel panel = panelMap.get(name);

			System.out.println(name + " was called!");
			if(panel == null) {
				return;
			}
			if(!TopView.showTab(comp)) {
				TopView.addTab(name, panel);
			}
		}
	}

}
