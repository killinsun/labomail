package transceiver;

import java.awt.Container;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SampleDialog extends JDialog {

	public SampleDialog(final Frame owner) {
		super(owner);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				init(owner);
			}
		});
	}
	
	private void init(Frame owner) {
		setTitle("ダイアログのサンプル");
		setBounds(64, 64, 256, 256);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		// ダイアログに表示するコンポーネントを設定
		Container c = getContentPane();
		c.add(new JLabel("ラベル"));		
	}
}
