package Util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

public class UndoTextArea extends JTextArea implements UndoableEditListener, KeyListener {

	// Undo,Redoを簡単に実装できる凄いやつ
	UndoManager uManager = new UndoManager();

	public UndoTextArea() {
		Document doc = this.getDocument();
		doc.addUndoableEditListener(this);
		this.addKeyListener(this);
	}

	public UndoTextArea(String text){
		super(text);
		Document doc = this.getDocument();
		doc.addUndoableEditListener(this);
		this.addKeyListener(this);
	}

	// 編集内容を消去する
	public void clearHistory() {
		uManager.discardAllEdits();
	}

// UndoableEditListener ---------------------------------------

	// テキストの内容が変わったとき
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		// 編集内容を記録
		uManager.addEdit(e.getEdit());
	}

// KeyListener -------------------------------------------------

	@Override
	public void keyTyped(KeyEvent e) {
	}

	// キー入力時
	@Override
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if( e.isControlDown() && (uManager.canUndo() || uManager.canRedo()) ) {

			// C-z でUndo実行
			if(key == KeyEvent.VK_Z && uManager.canUndo()) {
				uManager.undo();
				e.consume();
			}

			// C-y でRedo実行
			if(key == KeyEvent.VK_Y && uManager.canRedo()) {
				uManager.redo();
				e.consume();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

// ButtonEvent --------------------------------------------------

	public void undo() {
		// Undoが可能なら実行
		if(uManager.canUndo()) {
			uManager.undo();
		}
	}

	public void redo() {
		// Redoが可能なら実行
		if(uManager.canRedo()) {
			uManager.redo();
		}
	}
}
