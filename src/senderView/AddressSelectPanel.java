package senderView;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;

import AddressBook.PaneAddress;
import Util.MyUtils;
import dbHelper.DbHelper;

/************************************/
/*									*/
/*		「！！完成しました！！」	*/
/*									*/
/************ 既知のバグ ************/
/*	下からアドレスを追加			*/
/*	しようとしてはいけません。		*/
/************************************/


public class AddressSelectPanel extends PaneAddress implements MouseListener {


	/************ メンバ変数 ************/

	//呼び出し元クラスの参照
	private GetResult receiveClass;
	private int callNumber;

	//このパネルを格納するフレームの参照(disposeに使用)
	private JFrame superFrame;

	//宛先追加用ボタン
	private JButton addButton;

	//スーパークラスのリストを元に独自リストを生成
	private JList<JCheckBox> nameList;
	private DefaultListModel<JCheckBox> listModel;
	private int[] checkList;	//0:選択なし, 1:PCメール選択, 2:PHONEメール選択. 3:両方選択

	//名前でDBのPCアドレス、Phoneメールを格納
	private String[][] addressDimension;


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

/*
			 ∧＿∧
			( ･ω･) ＜ ほっと一息
			( つ旦O
			と＿)＿)
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
			listModel.addElement(nameBox);
		}
		nameList = new JList<JCheckBox>(listModel);


		/* アドレスクエリの発行 */
		addressDimension = getAddressQuery();

		/* チェック状態を表す配列を初期設定 */
		checkList = new int[listModel.size()];
		for(int i=0; i<checkList.length; i++){
			checkList[i] = 0;
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
			ArrayList<String> addressList = new ArrayList<String>();
			for(int i=0; i<checkList.length; i++){
				//checkList要素は０～２でなければならない
				assert 0<=checkList[i] && checkList[i]<=2;
				if(checkList[i] != 0){
					//JListは一つのコンポーネントであり、まとまりである。
					String[] addressOfName;
					//追加するアドレスを判別
					switch (checkList[i]) {
					case 1:
						addressOfName = new String[1];
						addressOfName[0] = addressDimension[0][i];
						break;
					case 2:
						addressOfName = new String[1];
						addressOfName[0] = addressDimension[1][i];
						break;
					case 3:
						addressOfName = new String[2];
						addressOfName[0] = addressDimension[0][i];
						addressOfName[1] = addressDimension[1][i];
						break;
					default:
						addressOfName = new String[1];
						addressOfName[0] = "unknow check in AddressSelectPanel";
						break;
					}

					//割り出したアドレスを最終結果に追加
					for(String added : addressOfName){
						addressList.add(added);
					}
				}
			}
			String[] addList = addressList.toArray(new String[addressList.size()]);

			//呼び出し元に結果をコールバックする
			receiveClass.setResult(addList, callNumber);
			break;

		default:
			throw new RuntimeException("Unkown Click Event");
		}

		//親フレームの画面を閉じる
		superFrame.dispose();
	}


	// PCとPhoneのアドレスカラムを文字列配列で取得
	@SuppressWarnings({ "unchecked", "rawtypes" })//気にしないでよし
	private String[][] getAddressQuery(){
		ArrayList[] addressList = new ArrayList[2];
		addressList[0] = new ArrayList<String>();
		addressList[1] = new ArrayList<String>();
		DbHelper helper = new DbHelper();
		String addressQuery =
				"SELECT pcmail, phonemail" +
				" FROM " + DbHelper.ADDRESS_TABLE;
		ResultSet rs = helper.executeQuery(addressQuery);
		boolean succeedFlag = true;
		try {
			while(rs.next()){
				addressList[0].add(rs.getString("pcmail"));
				addressList[1].add(rs.getString("phonemail"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
			succeedFlag = false;
		} finally {
			try {
				rs.close();
				helper.close();
				if(!succeedFlag) return null;
			} catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		String[][] retValue = {
				UtilsForThisPackage.listToArray(addressList[0]),
				UtilsForThisPackage.listToArray(addressList[1])
		};
		return retValue;
	}

	/* チェックボックスリスナー */
	@Override
	public void mouseClicked(MouseEvent e) {

		/* クリックされた座標からインデックスを割り出す */
		Point p = e.getPoint();
		int index = nameList.locationToIndex(p);

		/* チェックボックス状態の書き換え、記録 */
//		JCheckBox checkBox = listModel.getElementAt(index);
//		if(checkBox.isSelected()){
//			checkBox.setSelected(false);
//			checkList[index] = false;
//		}else{
//			checkBox.setSelected(true);
//			checkList[index] = true;
//		}
		//ポップアップの出現するクリック位置条件を指定
		if(
				(e.getX() - nameList.indexToLocation(index).getX()) > 50 ||
				e.getY() - nameList.indexToLocation(index).getY() > 50)
		{
			return;
		}

		//ポップアップメニューに表示する(PC|Phone)メールアドレスを取得
		DbHelper helper = new DbHelper();
		String name = MyUtils.getText(listModel.getElementAt(index));
		ResultSet rs = helper.executeQuery(
				"select PCMAIL, PHONEMAIL from addresstable" +
				" where NAME='"+name+"';");
		JCheckBox address1 = null;
		JCheckBox address2 = null;
		try{
			rs.next();
			if(!rs.getString("PCMAIL").equals("")) address1 = new JCheckBox(rs.getString("PCMAIL"));
			if(!rs.getString("PHONEMAIL").equals("")) address2 = new JCheckBox(rs.getString("PHONEMAIL"));
		} catch (SQLException e2) {
			e2.printStackTrace();
		} finally {
			//リソースのクローズ
			try{ helper.close(); rs.close(); }
			catch(SQLException e1){ e1.printStackTrace(); }
		}

		if(address1!=null) address1.addActionListener(new GetResultNumber());
		if(address2!=null) address2.addActionListener(new GetResultNumber());

		/* ポップアップメニューを表示 */
		JPopupMenu pop = new JPopupMenu();
		if(address1!=null) pop.add(address1);
		if(address2!=null) pop.add(address2);
		pop.show(this, e.getX(), e.getY());

		/* 再描画 */
		nameList.repaint();

	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}

	class GetResultNumber implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedAddress = e.getActionCommand();
			int[] pointXY = new int[2];
			pointXY[0] = -1;
			pointXY[1] = -1;
			boolean got = false;
			for(int i=0; i<addressDimension[0].length && !got; i++){
				for(int j=0; j<addressDimension.length; j++){
					if(selectedAddress.equals(addressDimension[j][i])){
						pointXY[0] = i;
						pointXY[1] = j;
						got = true;
					}
				}
			}
			//結果を戻す
			if(checkList[pointXY[0]] != 0){
				if((checkList[pointXY[0]]-1) == pointXY[1]+1){
					checkList[pointXY[0]] = pointXY[1] + 1;
				}else{
					checkList[pointXY[0]] += pointXY[1] + 1;
				}
			}else{
				checkList[pointXY[0]] = pointXY[1] + 1;
			}
		}
	}

	/****************************************/

}
