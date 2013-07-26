package senderView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;

import AddressBook.PaneAddress;
import Util.MyUtils;
import dbHelper.DbHelper;
//killinsun

public class AddressSelectPanel extends PaneAddress implements MouseListener {


	/************ メンバ変数 ************/

	//呼び出し元クラスの参照
	private GetResult receiveClass;
	private int callNumber;

	//このパネルを格納するフレームの参照(disposeに使用)
	private JFrame superFrame;

	//データベースのコネクション
	private DbHelper dh; //killinsun

	//宛先追加用ボタン
	private JButton addButton;

	//スーパークラスのリストを元に独自リストを生成
	private JList<JCheckBox> nameList;
	private DefaultListModel<JCheckBox> listModel;
	private boolean[] checkList;


	/************ インナークラス ************/

	class MyCellRenderer extends JCheckBox implements ListCellRenderer<JCheckBox> {

		@Override
		public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index,
				boolean isSelected, boolean cellHasFocus) {

			/* 項目の値を読み出して改めて表示する */
			JCheckBox checkBox = (JCheckBox)value;
			setText(checkBox.getText());

			setSelected(checkBox.isSelected());
			return this;
		}

	}

	/****************************************/

	public AddressSelectPanel(GetResult receiveClass, int callNumber, JFrame superFrame){

		//スーパーコンストラクタを呼び出し
		super();

		//呼び出し元の参照を受け取り
		this.receiveClass = receiveClass;
		this.callNumber = callNumber;

		//こちらからdispose()を行なうための参照を受け取る
		this.superFrame = superFrame;

		//初期値設定
		dh = new DbHelper();

/*
		try {
			//DBファイルのチェック
			if( !new File("labomailer.db").exists() ){
				throw new FileNotFoundException("DBファイルが見つかりません");
			}
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
		} catch (ClassNotFoundException e) {
			JOptionPane.showConfirmDialog(null, "データベースの接続に失敗しました\n" + e.fillInStackTrace(),
					"エラー", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showConfirmDialog(null, "データベースの接続に失敗しました\n" + e.fillInStackTrace(),
					"エラー", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
		} catch (FileNotFoundException e) {
			  labomailer.dbファイルとテーブルでも作りましょうかね。
			 ∧＿∧
			( ･ω･) ＜ ほっと一息
			( つ旦O
			と＿)＿)
			e.printStackTrace();
		}

	}
*/

		//親クラスのボタンを取り除く
		super.remove(0);
		super.remove(1);
		//初期設定
		listModel = new DefaultListModel<JCheckBox>();

		/* スーパークラスのJListの項目を受け取って新しく構成 */
		String tmpNames = super.list.getModel().toString().replaceAll(" ", "");
		String[] names = tmpNames.substring(1, tmpNames.length()-1).split(",");
		for(String listLabel : names){
			JCheckBox nameBox = new JCheckBox(listLabel);
			nameBox.setBackground(Color.WHITE);
			listModel.addElement(nameBox);
		}
		nameList = new JList<JCheckBox>(listModel);

		/* チェック状態を表す配列を初期設定 */
		checkList = new boolean[listModel.size()];
		for(int i=0; i<checkList.length; i++){
			checkList[i] = false;
		}

		/* チェックボックス機能を有効化 */
		nameList.setCellRenderer(new MyCellRenderer());
		nameList.addMouseListener(this);

		//リストを追加
		this.add(nameList, "flowy,width 200,height 500");


		/* ボタンを追加 */
		addButton = new JButton("挿入");
		addButton.addActionListener(this);
		this.add(addButton, "cell 0 1 3 1, grow");

	}


	/************ リスナー群 ************/

	/* 「挿入」ボタンクリック時 */
	@Override
	public void actionPerformed(ActionEvent e){
		switch (e.getActionCommand()) {
		case "挿入":
			ArrayList<String> addListArray = new ArrayList<String>();
			for(int i=0; i<checkList.length; i++){
				if(checkList[i]){
					//JListは一つのコンポーネントであり、まとまりである。
					String nameOfListNumber = MyUtils.getText(nameList.getModel().getElementAt(i));
					addListArray.add( getAddressQuery(nameOfListNumber) );
				}
			}
			String[] addList = addListArray.toArray(new String[addListArray.size()]);

			receiveClass.setResult(addList, callNumber);
			break;

		default:
			throw new RuntimeException("Unkown Click Event");
		}

		//親フレームの画面を閉じる
		superFrame.dispose();

	}

	// 指定のNameカラムのアドレスを選択
	private String getAddressQuery(String name){
		//戻り値用変数の宣言
		String address = "not found";

		try {
			ResultSet rs = dh.executeQuery("SELECT pcmail FROM addresstable WHERE name='"+name+"';");
			rs.next();
			address = rs.getString("pcmail");
			dh.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return address;
	}

	/* リスト要素クリック時 */
	@Override
	public void valueChanged(ListSelectionEvent e){
		System.out.println("act");
	}

	/* チェックボックスリスナー */
	@Override
	public void mouseClicked(MouseEvent e) {
		/* クリックされた座標からインデックスを割り出す */
		Point p = e.getPoint();
		int index = nameList.locationToIndex(p);

		/* チェックボックス状態の書き換え、記録 */
		JCheckBox checkBox = listModel.getElementAt(index);
		if(checkBox.isSelected()){
			checkBox.setSelected(false);
			checkList[index] = false;
		}else{
			checkBox.setSelected(true);
			checkList[index] = true;
		}

		/* 再描画 */
		nameList.repaint();

	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}

	/****************************************/

}
