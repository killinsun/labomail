package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import net.miginfocom.swing.MigLayout;

public class SelectServicePanel extends JPanel implements ActionListener {
	public SelectServicePanel(){
		this.setLayout(new MigLayout("", "[]", "[][]"));

		//ヒント（ToolTip）の表示を即時にします
		ToolTipManager.sharedInstance().setInitialDelay(0);

		JButton gmailIntentButton = new JButton("Ｇ ｍ ａ ｉ ｌ");
		gmailIntentButton.setToolTipText("<html>Gmail用のアカウント設定を行ないます<br>ほとんどの設定は自動で設定されます");
		gmailIntentButton.addActionListener(this);
		this.add(gmailIntentButton, "height 70, width 300, center, wrap");

		JButton otherIntentButton = new JButton("そ　の　他");
		otherIntentButton.setToolTipText("アカウント、サーバーの設定を全て手動で行います");
		otherIntentButton.addActionListener(this);
		this.add(otherIntentButton, "height 70, width 300, center, wrap");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame prefView = new JFrame();
		prefView.setSize(400, 300);
		prefView.setLocationRelativeTo(null);

		/* 呼び出したボタンによって呼び出す画面を分岐させる */

		switch (e.getActionCommand()) {
		case "Ｇ ｍ ａ ｉ ｌ": prefView.add(new GmailPreferencePanel()); break;
		case "そ　の　他": prefView.add(new OriginalPreferencePanel()); break;
		default: throw new RuntimeException("Unknown ActionCommand\n\tin PreferenceSelection");
		}

		prefView.setVisible(true);
	}

}
