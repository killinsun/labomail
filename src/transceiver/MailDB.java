package transceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MailDB {

	Connection connection;
	Statement statement;
	/** SQLのコンソール出力 */
	boolean debug;
	
	DBStrategy strategy;
	
	public MailDB(boolean debug) {
		
		this.debug = debug;
		strategy = new NewestFirstStrategy();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:blacky_test.db");
			statement = connection.createStatement();
			
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	private void execute(String sql) throws SQLException {
		
		if(debug) {
			System.out.println("exec => " + sql);
		}
		statement.execute(sql);
	}
	
	private ResultSet executeQuery(String sql) throws SQLException {

		if(debug) {
			System.out.println("exec => " + sql);
		}
		ResultSet rSet = statement.executeQuery(sql);
		return rSet;
	}

	
	public void initTables() throws SQLException {
		
		try {
			execute("drop table master");
		} catch (SQLException e) {
			System.out.println("MASTER doesn't exist.");
		}
		
		try {
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
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	
	public void csvImport(File path) throws FileNotFoundException, SQLException {
		
		Scanner scan = new Scanner(path);
		
		try {
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
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void setStrategy(DBStrategy s) {
		this.strategy = s;
	}
	
	public ResultSet getAllMails() throws SQLException {
		
		return executeQuery("select * from master");
	}
	
	public ResultSet getMails() throws SQLException {
		
		return executeQuery(strategy.getMailSql());
	}
	
	public List<MailObject> getMailObjects() throws SQLException {
		
		List<MailObject> mails = new ArrayList<>();
		ResultSet rSet = getMails();
		while (rSet.next()) {
			mails.add(
					new MailObject(
							rSet.getInt("id"), 
							rSet.getInt("mboxid"), 
							rSet.getInt("boxid"), 
							rSet.getString("mfrom"), 
							rSet.getString("mto"), 
							rSet.getString("subject"),
							rSet.getString("data"),
							Timestamp.valueOf(rSet.getString("date")),
							rSet.getString("path")
							)
					);
		}
		return mails;
	}
}
