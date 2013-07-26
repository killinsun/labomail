package TmpMail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import dbHelper.DbHelper;

import net.miginfocom.swing.MigLayout;

public class DraftBox_Mainframe extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> delTempRow = new ArrayList<>();
	private ArrayList<Object[]> delTempData = new ArrayList<>();

	// カラムタイトル設定
	final String[] clmTitle = { "", "from", "件名", "内容" };
	// DraftBox_Tableをインスタンス化
	public DraftBox_Table model = new DraftBox_Table(clmTitle);
	// パネルを配列にセット
	final JPanel[] pane = { new JPanel(),// 配列0
			new JPanel() };// 配列1

	// 配列にボタン設定
	final JButton[] button = { 
			new JButton("再編集(またはそのまま送信)"), 
			new JButton("ゴミ箱へ") };
	// テーブルクラス
	final JTable table = new JTable(model);

	public DraftBox_Mainframe() {

		// レイアウト設定
		this.setLayout(new MigLayout("wrap 1", "[grow]", "[][grow]"));

		// 配列0のパネルをセット
		pane[0].setLayout(new MigLayout("", "[grow][grow][grow]", "[]"));
		// ボタン配列を0から順に設置（センター）
		for (JButton b : button) {
			b.addActionListener(this);
			pane[0].add(b, "c");
		}
		this.add(pane[0]);
		/**
		 * テーブル設定
		 */
		// AUTO_RESIZE_LAST_COLUMN:最後の列だけをサイズ自動調整
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		// ユーザーが列順序ををいじれないようにする
		table.getTableHeader().setReorderingAllowed(false);
		DefaultTableColumnModel columnModel = (DefaultTableColumnModel) table
				.getColumnModel();
		TableColumn tblcolumn = columnModel.getColumn(0);
		// テーブルの横幅設定　check部分のリサイズできないようにする
		tblcolumn.setPreferredWidth(5);
		tblcolumn.setResizable(false);
		tblcolumn = columnModel.getColumn(1);
		tblcolumn.setPreferredWidth(200);
		tblcolumn = columnModel.getColumn(2);
		tblcolumn.setPreferredWidth(200);
		/**
		 * スクロールパネル
		 */
		// スクロールパネルインスタンス化
		JScrollPane scrpane = new JScrollPane(table);
		// 必要な時にスクロールバー(縦)を出す
		scrpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// 常にスクロールバー(横)を表示させない
		scrpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// 配列1のパネル設置
		pane[1].setLayout(new MigLayout("", "[grow]", "[grow]"));
		pane[1].add(scrpane, "grow");
		this.add(pane[1], "grow");

	}

	public void actionPerformed(ActionEvent e) {
		/**
		 * アクション処理
		 */
		// もし配列0(再編集)のボタンが押されたら
		if (e.getSource() == button[0]) {
			// 再編集(or送信)用処理
		}
		// もし配列1(ゴミ箱へ)のボタンが押されたら
		if (e.getSource() == button[1]) {
			//ゴミ箱へ送る処理
		}
	}
}
