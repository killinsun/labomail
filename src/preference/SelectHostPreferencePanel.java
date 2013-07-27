package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectHostPreferencePanel extends JPanel implements ActionListener {
	public SelectHostPreferencePanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton gmailIntentButton = new JButton("Gmail");
		gmailIntentButton.addActionListener(this);
		this.add(gmailIntentButton);

		JButton otherIntentButton = new JButton("その他");
		otherIntentButton.addActionListener(this);
		this.add(otherIntentButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame prefView = new JFrame();
		prefView.setSize(400, 300);
		prefView.setLocationRelativeTo(null);

		/* 呼び出したボタンによって呼び出す画面を分岐させる */
		switch (e.getActionCommand()) {
		case "Gmail": prefView.add(new GmailPreferencePanel()); break;
		case "その他": prefView.add(new OriginalPreferencePanel()); break;
		default: throw new RuntimeException("Unknown ActionCommand\n\tin PreferenceSelection");
		}

		prefView.setVisible(true);
	}

}
