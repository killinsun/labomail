package senderView;

import java.io.File;

import javax.swing.JFileChooser;

public class OpenFileDialog {

	//インスタンス生成の禁止
	private OpenFileDialog(){}

	//ファイルダイアログを表示して選択したFileオブジェクトを返す
	public static File fileOpen() {

		//ファイルダイアログを生成
		JFileChooser jChooser = new JFileChooser();
		//戻り値の初期値を設定
		File file = null;

		// ファイル選択ダイアログを表示
		int selected = jChooser.showOpenDialog(null);

		// 「開く」ボタン押下時
		if(selected == JFileChooser.APPROVE_OPTION) {
			// 選択したファイルを取得
			file = jChooser.getSelectedFile();
		}

		//nullかFileオブジェクトを返す
		return file;
	}
}
