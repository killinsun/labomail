package senderView;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


public class AutoInsert implements CaretListener, KeyListener, GetResult {

	/************ メンバ変数 ************/

	//定型句挿入先エリア
	private JTextComponent textComp;

	//挿入先カーソルポイント
	private int cursorPoint;

	/********** インナークラス **********/


	/************************************/

	public AutoInsert(JTextComponent textComp){
		this.textComp = textComp;
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		try {
			cursorPoint = textComp.getCaretPosition();
		} catch(NullPointerException nullpo) {
			cursorPoint = 0;
		}
	}

	@Override public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if((e.getModifiersEx()&InputEvent.CTRL_DOWN_MASK)!=0 && e.getKeyCode()==KeyEvent.VK_SPACE){
			//挿入句画面を起動
			startForResult(null, 0);
		}
	}
	@Override public void keyReleased(KeyEvent e) {}


	/**** 呼び出しと結果のコールバック ****/

	@Override
	public void startForResult(GetResult receiveClass, int callNumber) {
		Inserter insert = new Inserter(this);
		insert.setSize(900, 500);
		insert.setLocationRelativeTo(null);
		insert.setVisible(true);
	}

	@Override
	public void setResult(Object retValue, int callNumber) {
		Document doc = textComp.getDocument();
		try {
			doc.insertString(cursorPoint, (String)retValue, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
