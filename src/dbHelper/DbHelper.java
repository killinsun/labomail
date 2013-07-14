package dbHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper{


	static final String createAddressTbl = "" +
			"CREATE TABLE addresstable " +
			"( ID integer," +
			"NAME varchar(20) not null, " +
			"FURIGANA varchar(40) not null, " +
			"PCMAIL varchar(100), " +
			"PHONEMAIL varchar(100), " +
			"PHONENUM varchar(13), " +
			"MEMO varchar(255) " +
			")";
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
