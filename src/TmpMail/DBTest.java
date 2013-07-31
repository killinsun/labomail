package TmpMail;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.naming.ldap.Rdn;
import javax.swing.JFrame;

import dbHelper.DbHelper;

public class DBTest {
	DbHelper dh = new DbHelper();
	String sql = "SELECT *FROM mastertbl WHERE MBOXID=3 AND ID = 5;";
	ResultSet rs = dh.executeQuery(sql);

	public DBTest() {

		try {
			while (rs.next()) {
				System.out.println("-------------" + "\n"+ 
								   "BOXID="+rs.getString("MBOXID") + "\n" + 
						           "ID="+ rs.getString("ID") + "\n" + 
						           "MTO="+rs.getString("MTO")+ "\n"+
						           "SUBJECT="+ rs.getString("SUBJECT") + "\n" + 
						           "DATA="+ rs.getString("DATA")
						           );
				String a = rs.getString("MTO");
				System.out.println("ストリングで:"+a);
			}
			rs.close();
			dh.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	/**
	 * アップデート文チェック
	 */
		
		String sql = "SELECT * FROM mastertbl WHERE MBOXID=4 ;";
		ResultSet rs = dh.executeQuery(sql);
		try {
			while (rs.next()) {
				System.out.println("------更新後------------" + "\n"+
								   "MBOXID:" + rs.getString("MBOXID")+"\n"+
								   "ID="+rs.getString("ID")+"\n"+
				 				"MFROM="+ rs.getString("MFROM") + "\n" + 
				 				"MTO="+rs.getString("MTO")+ "\n"+
				 				"SUBJECT="+ rs.getString("SUBJECT") + "\n" + 
		           "DATA="+ rs.getString("DATA"));
			}
			rs.close();
			dh.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DBTest db = new DBTest();
	}
}
