package transceiver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;

public class DebugMain_AddMail {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Scanner scan = new Scanner(System.in);
		
		Integer mBoxID, boxID;
		String mFrom, mTo, subject, data, path;
		Timestamp date;

		System.out.print("MBOXID => ");
		mBoxID = scan.nextInt();

		System.out.print("BOXID(受信=1, 送信=2, 下書き=3, ゴミ箱=4, ユーザー定義=5~) => ");
		boxID = scan.nextInt();
		scan.nextLine();
		
		System.out.print("MFROM(default:hogehoge@hoge.com) => ");
		mFrom = scan.nextLine();
		if(mFrom.isEmpty())
			mFrom = "hogehoge@hoge.com";

		System.out.print("MTO(default:foofoo@foo.ne.jp) => ");
		mTo = scan.nextLine();
		if(mTo.isEmpty())
			mTo = "foofoo@foo.ne.jp";
		
		System.out.print("SUBJECT => ");
		subject = scan.nextLine();
		
		System.out.print("DATA => ");
		data = scan.nextLine();
		
		System.out.print("DATE => ");
		date = new Timestamp(System.currentTimeMillis());
		System.out.println(date);
		
		System.out.print("PATH => ");
		path = scan.nextLine();
		
		Class.forName("org.sqlite.JDBC");
		Connection connection = DriverManager.getConnection("jdbc:sqlite:blacky_test.db");
		Statement st = connection.createStatement();
		String sql = String.format(
				"insert into master(mboxid, boxid, mfrom, mto, subject, data, date, path) "
				+ "values(%d, %d, '%s', '%s', '%s', '%s', '%s', '%s')",
				mBoxID, boxID, mFrom, mTo, subject, data, date.toString(), path);
		System.out.println(sql);
		st.execute(sql);

		System.out.println("success");
	}

}
