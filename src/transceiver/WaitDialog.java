package transceiver;

import java.awt.Container;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/** Swing用のスレッドクラス。Swingはもともとシングルスレッドで動作する。 */
public class WaitDialog extends SwingWorker<Object, Object> {

	private JDialog dialog;
	private boolean flag;
	
	public WaitDialog(Frame owner, boolean modal, String message, String title) {
		this.dialog = new MyDialog(owner, modal, message, title);
		this.flag = true;
	}
	
	public WaitDialog(JDialog d) {
		this.dialog = d;
		this.flag = false;
	}

	/** 非同期処理 */
	@Override
	protected Object doInBackground() throws Exception {

		while(!dialog.isVisible()) {
			Thread.sleep(100);
		}
		
		while (flag) {
			// フラグが変わるまで無限ループ
			Thread.sleep(100);
		}
		
		System.out.println("hoge");
		
		return null;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/** 非同期処理が終了したあとに実行 */
	@Override
	protected void done() {
		dialog.dispose();
	}
	
}

