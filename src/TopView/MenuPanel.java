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
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;
import AddressBook.PaneAddress;
import DustBox.Dustbox_main;

/** 画面上部のメニュー */
public class MenuPanel extends JPanel {

	// アイコンたち
	JLabel newMail;
	JLabel receiveBox;
	JLabel sentBox;
	JLabel notSendBox;
	JLabel trush;
	JLabel addressBook;
	JLabel option;
	
	HashMap<String, IconSet> iconMap;
	
	// アイコンクリックで表示する各種JPanel
	PaneAddress paneAddress;
	Dustbox_main paneDustbox;
	/** デバッグ用 */
	JPanel dummyFrame;

	public MenuPanel() {

		// レイアウト設定
		this.setLayout(new MigLayout("", "[]40[]40[]40[]40[]40[]", "[grow]"));
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// アイコンマップの設定
		iconMap = new HashMap<>();
		iconMap.put("newMail", new IconSet(
				new ImageIcon("data/menuIcon/newMail.png"), 
				new ImageIcon("data/menuIcon/newMail2.png")));
		iconMap.put("receive", new IconSet(
				new ImageIcon("data/menuIcon/receive.png"), 
				new ImageIcon("data/menuIcon/receive2.png")));
		iconMap.put("sent", new IconSet(
				new ImageIcon("data/menuIcon/sent.png"), 
				new ImageIcon("data/menuIcon/sent2.png")));
		iconMap.put("notSend", new IconSet(
				new ImageIcon("data/menuIcon/notSend.png"), 
				new ImageIcon("data/menuIcon/notSend2.png")));
		iconMap.put("trush", new IconSet(
				new ImageIcon("data/menuIcon/trush.png"), 
				new ImageIcon("data/menuIcon/trush2.png")));
		iconMap.put("addressBook", new IconSet(
				new ImageIcon("data/menuIcon/address.png"), 
				new ImageIcon("data/menuIcon/address2.png")));
		iconMap.put("option", new IconSet(
				new ImageIcon("data/menuIcon/option.png"), 
				new ImageIcon("data/menuIcon/option2.png")));

		// 各種アイコンの設定と追加
		JLabel newMail = new JLabel("新規作成", iconMap.get("newMail").getDefault(), JLabel.CENTER);
		newMail.setName("newMail");
		JLabel receiveBox = new JLabel("受信BOX", iconMap.get("receive").getDefault(), JLabel.CENTER);
		receiveBox.setName("receive");
		JLabel sentBox = new JLabel("送信BOX", iconMap.get("sent").getDefault(), JLabel.CENTER);
		sentBox.setName("sent");
		JLabel notSendBox = new JLabel("未送信BOX", iconMap.get("notSend").getDefault(), JLabel.CENTER);
		notSendBox.setName("notSend");
		JLabel trush = new JLabel("ゴミ箱", iconMap.get("trush").getDefault(), JLabel.CENTER);
		trush.setName("trush");
		JLabel addressBook = new JLabel("アドレス帳", iconMap.get("addressBook").getDefault(), JLabel.CENTER);
		addressBook.setName("addressBook");
		JLabel option = new JLabel("設定", iconMap.get("option").getDefault(), JLabel.CENTER);
		option.setName("option");
		
		List<JLabel> menuIcons = Arrays.asList(newMail, receiveBox, sentBox, notSendBox, trush, addressBook, option);
		for (JLabel menuIcon : menuIcons) {
			menuIcon.setHorizontalTextPosition(JLabel.CENTER);
			menuIcon.setVerticalTextPosition(JLabel.BOTTOM);
			menuIcon.addMouseListener(new MenuIconsAction());
			add(menuIcon, "");
		}

	}
	
	class MenuIconsAction extends MouseAdapter {
		
		@Override
		public void mouseEntered(MouseEvent e) {
			
			// マウスカーソルを手の形に変える
			Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			JLabel label = (JLabel)e.getSource();
			label.setCursor(cursor);
			
			// マウスカーソルの変化だけでなく、アイコンに変化があれば、
			// マウスが乗っかっているのがわかりやすくなりそうです。
			
			// アイコンに変化をつける（余裕あれば）
			label.setIcon(iconMap.get(label.getName()).getOnMouse());
		}
		
		public void mouseExited(MouseEvent e) {
			// アイコンをもとに戻す（余裕あれば）
			JLabel label = (JLabel)e.getSource();
			label.setIcon(iconMap.get(label.getName()).getDefault());
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			Component comp = e.getComponent();
			
			// いまいちスマートじゃない…
			if(comp == newMail) {
				// 新規作成
				System.out.println("newMail!");
			}
			else if (comp == receiveBox) {
				// 受信BOX
				System.out.println("receive!");
			}
			else if(comp == sentBox) {
				// 送信BOX
				System.out.println("sent!");
			}
			else if (comp == notSendBox) {
				// 未送信BOX
				System.out.println("notSend!");
			}
			else if (comp == trush) {
				// ゴミ箱
				System.out.println("trush!");
				
				if(!TopView.showTab(paneDustbox)) {
					paneDustbox = new Dustbox_main();
					TopView.addTab("ゴミ箱", paneDustbox);
				}
			}
			else if(comp == addressBook) {
				// アドレス帳
				System.out.println("addressBook!");
				
				if(!TopView.showTab(paneAddress)) {
					paneAddress = new PaneAddress();
					TopView.addTab("アドレス帳", paneAddress);
				}
			}
			else if (comp == option) {
				// 設定
				System.out.println("option!");
				
				if(!TopView.showTab(dummyFrame)) {
					dummyFrame = new DummyPanel();
					TopView.addTab("ダミー", dummyFrame);
				}
			}
				
		}
	}

// ↓ 一応残しておきます。そのうち消します。
	
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		String buttonEvent = e.getActionCommand();
//		switch(buttonEvent){
//		case "新規作成":
//			System.out.println("newMail!!");
//			break;
//		case "ゴミ箱":
//			System.out.println("trush!");
//			break;
//		case "未送信":
//			System.out.println("notSend");
//			break;
//		case "アドレス帳":
//			/*
//			 * 複数タブが生成されるのを防止する
//			 */
//			if(frmAddress == null){
//				frmAddress = new FrmAddress();
//				TopView.topViewAddTab("アドレス帳", frmAddress);
//			}
//			break;
//		case "設定":
//			System.out.println("option!");
//			break;
//		case "フォルダ作成":
//			System.out.println("newFolder!");
//			break;
//		}
//
//	}

}
