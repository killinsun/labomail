package transceiver;

import java.sql.SQLException;


/** Strategyパターン。実行するSQL文を切り替える。 */
public interface DBStrategy {

	String getMailSql() throws SQLException;
	@Override
	public String toString();
}


/** 新しい順のStrategy */
class NewestFirstStrategy implements DBStrategy {

	@Override
	public String getMailSql() throws SQLException {

		return "select * from master where boxid in (1, 2) order by date desc";
	}

	@Override
	public String toString() {
		return "新しい順";
	}
}


/** 古い順のStrategy */
class OldestFirstStrategy implements DBStrategy {

	@Override
	public String getMailSql() throws SQLException {
		
		return "select * from master where boxid in (1, 2) order by date asc";
	}
	
	@Override
	public String toString() {
		return "古い順";
	}
	
}
