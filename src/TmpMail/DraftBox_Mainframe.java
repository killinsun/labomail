package TmpMail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import senderView.MailSenderPanel;
import DustBox.Dustbox_main;
import dbHelper.DbHelper;

public class DraftBox_Mainframe extends JPanel implements ActionListener {


	/************ メンバ変数 ************/

	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> delTempRow = new ArrayList<>();
	private ArrayList<Object[]> delTempData = new ArrayList<>();

	// DATABASE
	private DbHelper dh = new DbHelper();
	private HashMap<Object, Object> map = new HashMap<>();
	private int hashID = 0;

	// カラムタイトル設定
	final String[] clmTitle = { "", "from", "件名", "内容" };

	// DraftBox_Tableをインスタンス化
	public DraftBox_Table tmpmodel = new DraftBox_Table(clmTitle);

	// パネルを配列にセット
	final JPanel[] pane = {
			new JPanel(),// 配列0
			new JPanel() };// 配列1

	// 配列にボタン設定
	final JButton[] button = { new JButton("編集する"), new JButton("ゴミ箱へ") };

	// テーブルクラス
	final JTable table = new JTable(tmpmodel);

	/************************************/

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

		/* テーブル設定 */
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
		// データ更新

		try {
			String sql = "SELECT * FROM mastertbl WHERE MBOXID=3 ;";
			ResultSet rs = dh.executeQuery(sql);

			while (rs.next()) {
				String MFROM = rs.getString("MFROM");
				String SUBJECT = rs.getString("SUBJECT");
				String DATA = rs.getString("DATA");
				tmpmodel.add(MFROM, SUBJECT, DATA);
				map.put(hashID++, rs.getInt("ID"));
			}
			dh.close();
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	/************ リスナー ************/

	public void actionPerformed(ActionEvent e) {
		/** 再編集が押されたら **/
		if (e.getSource() == button[0]) {
			try {
				// Dustbox_main dbox = new Dustbox_main();// DustBox
				ArrayList<Integer> rowcount = new ArrayList<>();// 行数格納
				for (int i = 0; i < tmpmodel.getRowCount(); i++) {// getrowcountで行数を得る
					if ((boolean) tmpmodel.getValueAt(i, 0)) {// tmpmodelが行[i],列[0]を取得し、trueならrowcountにiを格納
						rowcount.add(i);
					}
				}
				int sendcount = 0;

				for (int i = 0; i < rowcount.size(); i++) {// rowcountの配列の長さが、iより上ならループ
					int selectRow = rowcount.get(i) - sendcount;// selectRowにrowcountの列番号を格納
					String sql = "SELECT * FROM mastertbl WHERE MBOXID=3 AND ID = "
							+ map.get((Object) selectRow).toString() + ";";
					ResultSet rs = dh.executeQuery(sql);
					// プット
					//String to = rs.getString("MTO");

					// 読み込み
					JFrame f = new JFrame();
					f.setSize(700, 500);
					f.setLocationRelativeTo(null);
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					String []to = {rs.getString("MTO")};
					String []bcc = {""};
					String subject = rs.getString("SUBJECT");
					String detail = rs.getString("DATA");


					f.add(new MailSenderPanel(to, bcc, subject, detail));
					f.setVisible(true);

				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		/** ゴミ箱へ **/
		else if (e.getSource() == button[1]) {
			/** ゴミ箱へ送る処理 **/
			try {
				Dustbox_main dbox = new Dustbox_main();// DustBox
				ArrayList<Integer> rowcount = new ArrayList<>();// 行数格納
				for (int i = 0; i < tmpmodel.getRowCount(); i++) {// getrowcountで行数を得る
					if ((boolean) tmpmodel.getValueAt(i, 0)) {// tmpmodelが行[i],列[0]を取得し、trueならrowcountにiを格納
						rowcount.add(i);
					}
				}
				int sendcount = 0;

				for (int i = 0; i < rowcount.size(); i++) {// rowcountの配列の長さが、iより上ならループ
					int selectRow = rowcount.get(i) - sendcount;// selectRowにrowcountの列番号を格納
					String sql = "SELECT * FROM mastertbl WHERE MBOXID=3 ;";
					ResultSet rs = dh.executeQuery(sql);
					System.out.println("--------チェックをつけたデータのID----------");
					System.out.println(map.get((Object) selectRow));
					// プット

					while (rs.next()) {
						int selectID = rs.getInt("ID");
						map.put(hashID++, selectID);
					}
					rs.close();

					// データをゴミ箱用に更新
					String sqlupdate = "UPDATE mastertbl SET MBOXID = 4 ,BOXID = 3 WHERE ID = "
							+ map.get((Object) selectRow).toString() + ";";
					dh.execute(sqlupdate);

					System.out.println("---データ更新---");
				}

				dh.close();

				// リフレッシュ
				System.out.println("---リフレッシュ---");
				tmpmodel.setRowCount(0);
				String sql = "SELECT * FROM mastertbl WHERE MBOXID=3 ;";
				ResultSet rs = dh.executeQuery(sql);

				// テーブル再設置
				System.out.println("---再設置---");
				while (rs.next()) {
					String MFROM = rs.getString("MFROM");
					String SUBJECT = rs.getString("SUBJECT");
					String DATA = rs.getString("DATA");
					tmpmodel.add(MFROM, SUBJECT, DATA);
				}
				rs.close();

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
