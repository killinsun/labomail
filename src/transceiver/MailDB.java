package transceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;


public class MailDB {

	Connection connection;
	Statement statement;
	/** SQLのコンソール出力 */
	boolean debug;
	
	Strategy strategy;
	
	public MailDB(boolean debug) {
		
		this.debug = debug;
		strategy = new NewestFirstStrategy();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:blacky_test.db");
			// 自動commitを無効にする（トランザクション処理のため）
			connection.setAutoCommit(false);
			statement = connection.createStatement();
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	public void execute(String sql) throws SQLException {
		
		if(debug) {
			System.out.println("exec => " + sql);
		}
		statement.execute(sql);
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		
		if(debug) {
			System.out.println("exec => " + sql);
		}
		return statement.executeQuery(sql);
	}

	
	public void initTables() throws SQLException {
		
		try {
			execute("drop table master");
		} catch (SQLException e) {
			System.out.println("MASTER doesn't exist.");
		}
		
		// テーブル生成
		execute("create table master("
				+ "id integer not null primary key autoincrement,"
				+ "mboxid int not null,"
				+ "boxid int not null,"
				+ "mfrom text,"
				+ "mto text,"
				+ "subject text,"
				+ "data text,"
				+ "date timestamp default (datetime('now','localtime')),"
				+ "path text"
				+ ")");
		connection.commit();
	}

	
	public void testMethod() throws SQLException {
		
		Statement st = connection.createStatement();
		
		execute("drop table hoge");
		execute("create table hoge(num int, str text)");
		execute("insert into hoge values(1, 'hoge')");
		execute("insert into hoge values(2, 'foo')");
		
		ResultSet rSet = executeQuery("select * from hoge");
		while (rSet.next()) {
			System.out.print(rSet.getInt("num"));
			System.out.println(":".concat(rSet.getString("str")));
		}
		
		connection.commit();
		st.close();
	}
	
	public void csvImport(File path) throws FileNotFoundException, SQLException {
		
		Scanner scan = new Scanner(path);
		
		// 一行目を無視
		scan.nextLine();
		while (scan.hasNext()) {
			
			int i = 0;
			String[] line = scan.nextLine().split(",");
			// ゴリ押し
			execute(String.format(
					"insert into "
					+ "master(id, mboxid, boxid, mfrom, mto, subject, data, date, path) "
					+ "values(%s, %s, %s, '%s', '%s', '%s', '%s', '%s', '%s')",
					line[i++], line[i++], line[i++], line[i++], line[i++], 
					line[i++], line[i++], line[i++], line[i++]));
			connection.commit();
		}
	}
	
	public void setStrategy(Strategy s) {
		this.strategy = s;
	}
	
	public ResultSet getAllMails() throws SQLException {
		
		return executeQuery("select * from master");
	}
	
	public List<MailObject> getMailObjects() {
		
		return null;
	}
}
