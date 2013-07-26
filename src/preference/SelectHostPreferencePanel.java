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
		JFrame f = new JFrame();
		f.setLocationRelativeTo(null);
		f.setSize(400, 300);
		f.add(new OriginalPreferencePanel());
		f.setVisible(true);
	}

}
