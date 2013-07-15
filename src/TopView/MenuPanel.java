package TopView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import AddressBook.PaneAddress;

public class MenuPanel extends JPanel implements ActionListener {

	JButton newMail = new JButton("新規作成");
	JButton trush = new JButton("ゴミ箱");
	JButton notSend = new JButton("未送信");
	JButton address = new JButton("アドレス帳");
	JButton option = new JButton("設定");
	JButton newFolder = new JButton("フォルダ作成");
	PaneAddress paneAddress;

	public MenuPanel() {

		this.setLayout(new MigLayout("", "[]20[]20[]20[]20[]20[]", "[grow]"));

		List<JButton> buttons = Arrays.asList(newMail, trush, notSend, address, option, newFolder);
		for (JButton button : buttons) {
			ImageIcon icon = new ImageIcon("data/menuicon.png");
			button.setIcon(icon);
			button.setHorizontalTextPosition(JButton.CENTER);
			button.setVerticalTextPosition(JButton.BOTTOM);
			button.addActionListener(this);
			add(button, "");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String buttonEvent = e.getActionCommand();
		switch(buttonEvent){
		case "新規作成":
			System.out.println("newMail!!");
			break;
		case "ゴミ箱":
			System.out.println("trush!");
			break;
		case "未送信":
			System.out.println("notSend");
			break;
		case "アドレス帳":
			/*
			 * 複数タブが生成されるのを防止する
			 */
			if(paneAddress == null){
				paneAddress = new PaneAddress();
				TopView.topViewAddTab("アドレス帳", paneAddress);
			}
			break;
		case "設定":
			System.out.println("option!");
			break;
		case "フォルダ作成":
			System.out.println("newFolder!");
			break;
		}

	}
}
