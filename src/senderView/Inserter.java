package senderView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
class DB {
	public DB(String user, String pass){
		//JDBCドライバのロード
		//データベースに接続
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:Template.db",user,pass);
			pstm = conn.createStatement();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			System.out.println("例外が発生しました" + e1);
		}
	}

	//ArrayList<String>をString[]に変換
	private String[] listToArray(ArrayList<String> array){
		String[] retValues = new String[array.size()];
		for(int i=0; i<array.size(); i++){
			retValues[i] = array.get(i);
		}
		return retValues;
	}

	//データベース切断用
	//戻り値 true:成功 false:失敗
	boolean close(){
		try
		{
			pstm.close();
			conn.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	//月文のクエリを発行
	public String[] getMonthSentence(int month){
		String k = String.format("select * from Month where month=%d", month);
		ResultSet rset = null;
		try { rset = pstm.executeQuery(k);}
		catch (SQLException e1) { e1.printStackTrace(); }

		ArrayList<String> words = new ArrayList<String>();
		try {
			while(rset.next()){
				//テーブルデータの出力
				words.add(String.format("%s",rset.getString("template")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rset.close(); }
			catch (SQLException e) { e.printStackTrace(); }
		}
		return listToArray(words);
	}

	public String[] getCenterSentence(){
		//戻り値
		ArrayList<String> list = new ArrayList<>();
		try {
			String c = String.format("select * from Sentence_center");
			ResultSet rset = pstm.executeQuery(c);
			while(rset.next()){
				list.add(rset.getString("template"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listToArray(list);
	}

	public String[] getEndSentence(){
		ArrayList<String> list = new ArrayList<>();
		try {
			String e = String.format("select * from Sentence_end");
			ResultSet rset = pstm.executeQuery(e);
			while(rset.next()){
				list.add(rset.getString("template"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return listToArray(list);
	}

	Connection conn;    //データベース接続子
	Statement pstm;        //SQL実行用作業領域

	//	public static void main(String[] args) {
	//		DB main = new DB("labo", "");
	//		String[] monthSentence = main.getMonthSentence(1);
	//		for(String word : monthSentence){
	//			System.out.println(word);
	//		}
	//
	//		System.out.println("---------------------\n");
	//		String[] center = main.getCenterSentence();
	//		for(String word : center){
	//			System.out.println(word);
	//		}
	//
	//		System.out.println("---------------------\n");
	//		String[] end = main.getEndSentence();
	//		for(String word : end){
	//			System.out.println(word);
	//		}
	//	}
}


public class Inserter extends JFrame implements ActionListener {

	private DB db = null;
	private GetResult calledForm;
	private JList<String> firstSyntax;
	DefaultListModel<String> fSyntaxModel = new DefaultListModel<String>();
	private JList<String> secondSyntax;
	DefaultListModel<String> sSyntaxModel = new DefaultListModel<String>();
	private JList<String> thirdSyntax;
	DefaultListModel<String> tSyntaxModel = new DefaultListModel<String>();
	private JComboBox<String> com;
	private DefaultComboBoxModel<String> defaultcom = null;
	private String[] tuki = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
	private String[] seasonWords = null;

	public Inserter(GetResult calledForm) {
		//結果の格納先を設定
		this.calledForm = calledForm;

		db = new DB("labo", "");

		getContentPane().setLayout(new MigLayout("", "[200,grow][200,grow]", "[25][grow][grow][25]"));

		/*コンボボックス*/
		defaultcom = new DefaultComboBoxModel<String>(tuki);
		com = new JComboBox<String>(defaultcom);
		getContentPane().add(com,"cell 0 0, grow, wrap");
		com.addActionListener(this);

		/* 1列目 */
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 1,grow");

		seasonWords = db.getMonthSentence(1);
		for (String word : seasonWords) {
			fSyntaxModel.addElement(word);
		}
		firstSyntax = new JList<String>(fSyntaxModel);
		firstSyntax.setSelectedIndex(0);
		scrollPane.setViewportView(firstSyntax);


		/* 2列目 */
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 1 1,grow");
		String[] str1 = db.getCenterSentence();
		for (String word : str1) {
			sSyntaxModel.addElement(word);
		}
		secondSyntax = new JList<String>(sSyntaxModel);
		secondSyntax.setSelectedIndex(0);
		scrollPane_1.setViewportView(secondSyntax);

		/* 3列目 */
		JScrollPane scrollPane_2 = new JScrollPane();
		getContentPane().add(scrollPane_2, "cell 0 2 2 1, grow, wrap");

		String[] str2 = db.getEndSentence();
		for (String word : str2) {
			tSyntaxModel.addElement(word);
		}
		thirdSyntax = new JList<String>(tSyntaxModel);
		thirdSyntax.setSelectedIndex(0);
		scrollPane_2.setViewportView(thirdSyntax);

		/* 挿入ボタン */
		JButton button = new JButton("挿入");
		button.addActionListener(this);
		getContentPane().add(button, "span 2, grow");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 季語の取得
		if(e.getActionCommand().equals("comboBoxChanged")){
			int idx=com.getSelectedIndex()+1;
			seasonWords = db.getMonthSentence(idx);
			// JListへ挿入
			fSyntaxModel.clear();
			for (String word : seasonWords) {
				fSyntaxModel.addElement(word);
			}
			firstSyntax.setModel(fSyntaxModel);
		} else if (e.getActionCommand().equals("挿入")) {
			StringBuilder template = new StringBuilder();
			template.append(firstSyntax.getSelectedValue());
			template.append(secondSyntax.getSelectedValue());
			template.append(thirdSyntax.getSelectedValue());
			calledForm.setResult(template.toString(), 0);
			this.dispose();
		}

	}

}