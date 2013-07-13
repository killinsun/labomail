package TopView;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import AddressBook.FrmAddress;

public class TopView extends JFrame {

	public static JTabbedPane tabbedPane = new JTabbedPane();
	
	public TopView() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("FJB-Mailer");
		setSize(800,600);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JPanel menuPanel = new MenuPanel();
		getContentPane().add(menuPanel, "c, wrap");
		
		JPanel mailAndViewPanel = new MailListViewPanel();
		
		tabbedPane.addTab("Top", mailAndViewPanel);
		
		getContentPane().add(tabbedPane, "grow");
		
	}
//	TopViewで生成されたタブのコンポーネントに対してアクセスできるようにメソッド作ったけど
//	もっと賢いやり方あるかな？
//	使い方
//	別のクラスから　TopView.topViewAddTab(タブの名前,追加したいパネル);
//	そのまんまや。

	public static void topViewAddTab(String tabName,Component compName){
		tabbedPane.addTab(tabName,compName);
	}

	public static void main(String[] args) {

		TopView frame = new TopView();
		frame.setVisible(true);
	}
	
	

}
