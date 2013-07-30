package dbHelper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper{

	Connection conn; 
	Statement stmt;

	/* テーブル名 */

	public static final String ADDRESS_TABLE = "addresstable";
	public static final String MASTER_TABLE = "mastertbl";
	public static final String MBOX_TABLE = "boxtbl";

	//テーブルの作成
	static final String createAddressTbl = "" + //createAddrrssTblの中身
			"CREATE TABLE addresstable " +
			"(" +
			"	ID integer primary key," +
			"	NAME varchar(20) not null, " +
			"	FURIGANA varchar(40) not null, " +
			"	KUBUN varchar(20) not null, " +
			"	PCMAIL varchar(100), " +
			"	PHONEMAIL varchar(100), " +
			"	TEL varchar(13), " +
			"	MEMO varchar(255), " +
			"	FACEICON varchar(255)" +
			")";
	static final String createKubunTbl =""+
			"CREATE TABLE kubuntable " +
			"( KUBUNID integer primary key,"+
			"KUBUN varchar(40) not null)";

	static final String createMasterTbl = //createMasterTblの中身
			"CREATE TABLE mastertbl" +
			"(" +
			"	ID integer primary key autoincrement," +
			"	MBOXID varchar(1) not null," +
			"	BOXID varchar(1) not null," +
			"	MFROM varchar(100) not null," +
			"	MTO varchar(100) not null," +
			"	SUBJECT varchar(255)," +
			"	DATA varchar(255) not null," +
			"	DATE timestamp," +
			"	PATH varchar(255) not null" +
			")";

	static final String createBoxTbl = //createBoxTblの中身
			"CREATE TABLE boxtbl" +
			"(" +
			"	BOXID integer primary key," +
			"	BOX varchar(20) not null" +
			")";

	public DbHelper() {
		try{
			Class.forName("org.sqlite.JDBC");
			//データベースチェック
			File f = new File("labomailer.db");
			if(!f.exists()){
				initTables();
			}
		} catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	private void initTables() throws SQLException{
		execute(createMasterTbl);
		execute(createBoxTbl);
		execute(createAddressTbl);
		execute(createKubunTbl);
		System.out.println("Database Created");
	}

	public void execute(String sql){
		System.out.println("DB is opened");
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
			stmt = conn.createStatement();
			stmt.execute(sql);
			System.out.println("sql run accepted:"+sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public ResultSet executeQuery(String sql){
		System.out.println("DB is opened");
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
			stmt = conn.createStatement();
			System.out.println("sql run accepted:"+sql);
			return stmt.executeQuery(sql);
		} catch (SQLException e ) {
			e.printStackTrace();
			return null;
		}
	}
	

	public void close() throws SQLException{
		stmt.close();
		conn.close();
		System.out.println("DB is closed");
	}
}
