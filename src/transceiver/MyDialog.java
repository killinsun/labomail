package transceiver;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyDialog extends JFrame {

	public MyDialog(Frame owner, boolean modal, String message, String title) {

		System.out.println("b");

		setTitle(title);
		JLabel label = new JLabel(message);
		getContentPane().add(label);
		setUndecorated(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		System.out.println("d");
	}
}
