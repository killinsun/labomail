package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import net.miginfocom.swing.MigLayout;

public class SelectServicePanel extends JPanel implements ActionListener {

	//呼び出し元のインテント用パネル
	private IntentSupporter callby;
	public SelectServicePanel(IntentSupporter callby){

		//初期設定
		this.callby = callby;

		this.setLayout(new MigLayout("", "[]", "[][]"));

		//ヒント（ToolTip）の表示を即時にします
		ToolTipManager.sharedInstance().setInitialDelay(0);

		JButton gmailIntentButton = new JButton("Ｇ ｍ ａ ｉ ｌ");
		gmailIntentButton.setActionCommand("Gmail");
		gmailIntentButton.setToolTipText("<html>Gmail用のアカウント設定を行ないます<br>ほとんどの設定は自動で設定されます");
		gmailIntentButton.addActionListener(this);
		this.add(gmailIntentButton, "height 70, width 300, center, wrap");

		JButton otherIntentButton = new JButton("そ　の　他");
		otherIntentButton.setActionCommand("none");
		otherIntentButton.setToolTipText("アカウント、サーバーの設定を全て手動で行います");
		otherIntentButton.addActionListener(this);
		this.add(otherIntentButton, "height 70, width 300, center, wrap");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		/* 呼び出したボタンによって遷移する画面を分岐させる */

		switch (e.getActionCommand()) {
		case "Gmail":
			GmailPreferencePanel prefsView = new GmailPreferencePanel();
			callby.addPanel(prefsView);
			break;
		case "none":
			callby.addPanel(new OriginalPreferencePanel());
			break;
		default:
			throw new RuntimeException("Unknown ActionCommand\n\tin PreferenceSelection");
		}

	}

}
