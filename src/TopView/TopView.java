package TopView;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import transceiver.MailListViewPanel;
import net.miginfocom.swing.MigLayout;

public class TopView extends JFrame {

	/** 画面下部の表示域。ここに各種JPanelを追加していく。 */
	private static JTabbedPane tabbedPane = new JTabbedPane();
	
	public TopView() {

		// フレームの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("FJB-Mailer");
		setSize(800,600);
		setLocationRelativeTo(null);
		
		// レイアウト設定
		getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		/** 画面上部のメニュー項目 */
		JPanel menuPanel = new MenuPanel();
		getContentPane().add(menuPanel, "c, wrap");
		
		/** TOPの送受信画面 */
		JPanel mailAndViewPanel = new MailListViewPanel();
		
		tabbedPane.addTab("Top", mailAndViewPanel);
		
		getContentPane().add(tabbedPane, "grow");
		
	}
	


	/** 渡されたコンポーネントを追加し、表示します。 */
	public static void addTab(String tabName,Component compName){
		tabbedPane.addTab(tabName,compName);
		showTab(compName);
	}
	
	/** 渡されたタブが存在すれば、タブを切り替え表示し、trueを返します。
	 * 渡されたタブが存在しなければ、falseを返します。 */
	public static boolean showTab(Component showComponent) {
		
		// 追加されているコンポーネントすべてを取得
		Component[] comps = tabbedPane.getComponents();
		
		for (Component comp : comps) {
			if(comp == showComponent) {
				// 存在していれば表示を切り替え
				tabbedPane.setSelectedComponent(comp);
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		TopView frame = new TopView();
		frame.setVisible(true);
		
	}
	
	
}
