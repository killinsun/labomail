package TmpMail;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class DraftBox_Table extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	// Boolean型のリスト
	ArrayList<Boolean> cbox = new ArrayList<>();

	// チェックボックス設置
	public DraftBox_Table(String[] clmTitle) {
		super(null, clmTitle);
	}

	public boolean isCellEditable(int row, int col) {// 列幅の編集設定
		// チェックボックスにチェックされていないなら
		if (col == 0) {
			// trueだから編集不可
			return true;
		} else {
			return false;
		}
	}

	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}

	public void add(String f, String s, String c) {
		Object[] data = { new Boolean(false), f, s, c };
		// 行を追加
		this.addRow(data);

	}
}
