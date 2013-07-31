package senderView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TemplateDB_Helper {
	public TemplateDB_Helper(String user, String pass){
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
		TemplateDB_Helper main = new TemplateDB_Helper("labo", "");
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
