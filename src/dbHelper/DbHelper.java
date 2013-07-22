package dbHelper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper{

//テーブルの作成
	static final String createAddressTbl = "" + //createAddrrssTblの中身
			"CREATE TABLE addresstable " +
			"( ID integer primary key," +
			"NAMEID varchar(20) not null, " +
			"FURIGANA varchar(40) not null, " +
			"KUBUN varchar(20) not null, " +
			"PCMAIL varchar(100), " +
			"PHONEMAIL varchar(100), " +
			"TEL varchar(13), " +
			"MEMO varchar(255), " +
			"FACEICON varchar(255)" +
			")";
	
	static final String createMasuterTbl = "" + //createMasuterTblの中身
			"CREATE TABLE masutertbl　" +
			"(ID integer primary key autoincrement," +
			"MBOXID varchar(1) not null," +
			"BOxID varchar(1) not null," +
			"MFROM varchar(100) not null," +
			"MTO varchar(100) not null," +
			"SUJECT varchar(255)," +
			"DATA varchar(255) not null," +
			"DATE varchar('TIMESTAMP')," +
			"PATH varchar(255) not null" +
			")";
	
	static final String createBoxTbl = "" + //createBoxTblの中身
			"CREATE TABLE boxtbl" +
			"(BOXID integer primary key," +
			"BOX varchar(20)　not null," +
			")";
	public DbHelper(){ //DbHelperの中身を指定
		File f = new File("labomailer.db");//ファイルの使用
		if(!f.exists()){
			try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
			Statement stmt = conn.createStatement();

			stmt.execute(createAddressTbl);
			stmt.execute(createMasuterTbl);
			stmt.execute(createBoxTbl);
			conn.close();
			System.out.println("Database created!");
			} catch(ClassNotFoundException | SQLException e){
				e.printStackTrace();
			}


		}
	}
	public void execSql(String sql){
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
			Statement stmt = conn.createStatement();

			stmt.execute(sql);
			conn.close();
			System.out.println("sql run accepted");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}




}