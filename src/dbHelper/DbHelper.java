package dbHelper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper{


	static final String createAddressTbl = "" +
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
	public DbHelper(){
		File f = new File("labomailer.db");
		if(!f.exists()){
			try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
			Statement stmt = conn.createStatement();

			stmt.execute(createAddressTbl);
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