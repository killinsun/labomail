package preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SelectHostPreferencePanel extends JPanel implements ActionListener {
	public SelectHostPreferencePanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton gmailIntentButton = new JButton("G m a i l");
		gmailIntentButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		getcon
	}

}
