package senderView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
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
			conn = DriverManager.getConnection("jdbc:sqlite:a.db",user,pass);
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

	public static void main(String[] args) {
		DB main = new DB("labo", "");
		String[] monthSentence = main.getMonthSentence(1);
		for(String word : monthSentence){
			System.out.println(word);
		}

		System.out.println("---------------------\n");
		String[] center = main.getCenterSentence();
		for(String word : center){
			System.out.println(word);
		}

		System.out.println("---------------------\n");
		String[] end = main.getEndSentence();
		for(String word : end){
			System.out.println(word);
		}
	}
}


public class Inserter extends JFrame implements ActionListener {

	private GetResult calledForm;
	private JList<String> firstSyntax;
	private JList<String> secondSyntax;
	private JList<String> thirdSyntax;
	private DefaultComboBoxModel defaultcom = null
	private String[] tuki = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"}; 
	private String[][] stab = {
			{ "aaaだから、", "bbbなので、", "cccだけど、" },
			{ "aaaを、", "bbbが、", "cccで、" },
			{ "aaaする。", "bbbだった。", "cccかもしんない。"},
	};

	public Inserter(GetResult calledForm) {
		//結果の格納先を設定
		this.calledForm = calledForm;

		getContentPane().setLayout(new MigLayout("", "[200,grow][200,grow][400,grow]", "[grow][25]"));

		/* 1列目 */
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 0,grow");

		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[0][i]);
		}
		firstSyntax = new JList<String>(model);
		firstSyntax.setSelectedIndex(0);
		scrollPane.setViewportView(firstSyntax);


		/* 2列目 */
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 1 0,grow");

		model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[1][i]);
		}
		secondSyntax = new JList<String>(model);
		secondSyntax.setSelectedIndex(0);
		scrollPane_1.setViewportView(secondSyntax);


		/* 3列目 */
		JScrollPane scrollPane_2 = new JScrollPane();
		getContentPane().add(scrollPane_2, "cell 2 0, grow, wrap");

		model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[2][i]);
		}
		thirdSyntax = new JList<String>(model);
		thirdSyntax.setSelectedIndex(0);
		scrollPane_2.setViewportView(thirdSyntax);


		/* 挿入ボタン */
		JButton button = new JButton("挿入");
		button.addActionListener(this);
		getContentPane().add(button, "span 2, grow");
		
		/*コンボボックス*/
		defaultcom = new DefaultComboBoxModel(tuki);
		JComboBox com = new JComboBox(defaultcom);
		getContentPane().add(com,"span 1, grow");
		com.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StringBuilder template = new StringBuilder();
		template.append(firstSyntax.getSelectedValue());
		template.append(secondSyntax.getSelectedValue());
		template.append(thirdSyntax.getSelectedValue());
		String tuki;
//		if(com.getSelectedIndex() == -1){
//			tuki = 
//		}
		calledForm.setResult(template.toString(), 0);
		this.dispose();
	}

}