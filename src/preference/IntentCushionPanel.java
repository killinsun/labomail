package preference;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IntentCushionPanel extends JPanel implements IntentSupporter, ActionListener {

	/************ メンバ変数 ************/

	private JPanel intentPanel;
	private CardLayout intent;

	private JButton btnPrev;
	private int current = -1;

	/************************************/

	class InfoListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			FrmSoftwareCredit sInfo = new FrmSoftwareCredit();
			sInfo.setSize(400, 500);
			sInfo.setLocationRelativeTo(null);
			sInfo.setVisible(true);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			Component comp = (Component)e.getSource();
			comp.setCursor(cursor);
		}
	}

	/************************************/


	public IntentCushionPanel(){

		/* 初期設定 */
		intent = new CardLayout();
		btnPrev = new JButton(" ← ");

		/* 画面設定 */
		this.setLayout(new BorderLayout());

		//最初はSelectServicePanelに遷移
		intentPanel = new JPanel();
		intentPanel.setLayout(intent);
		addPanel(new SelectServicePanel(this));
		this.add(intentPanel, BorderLayout.CENTER);


		/* インテントを指示するボタンを配置 */
		btnPrev.setVisible(false);
		btnPrev.addActionListener(this);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(btnPrev, BorderLayout.WEST);

		//上部に配置
		this.add(topPanel, BorderLayout.NORTH);


		/* このソフトウェアのインフォメーションを表示するアイコンを配置 */
		JLabel info = new JLabel(new ImageIcon("data/info.png"));
		info.addMouseListener(new InfoListener());

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(info, BorderLayout.EAST);

		//下部に配置
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	@Override
	public void addPanel(JPanel newPanel) {
		//必要なときに「サッ」とでてくるかっこいいやつ
		btnPrev.setVisible(true);

		//パネルをカードに追加
		intentPanel.add(newPanel);
		intent.next(intentPanel);
		current++;
	}

	@Override
	public void prev(){
		//無いページにいくのを阻止
		if(current < 1){
			return;
		}

		//ページを一つ前のカードに戻す
		btnPrev.setVisible(false);
		intent.previous(intentPanel);
		intentPanel.remove(current--);//GCさんに早く回収してもらえますように
	}
	@Override //今回nextさんはなかったことになった、すまんnextさん。
	public void next(){}


	/************ アクションリスナー ************/

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case " ← ":
			prev();
			break;
		default:
			throw new RuntimeException("Unknown ActionCommand in IntentCushionPanel");
		}
	}

}
