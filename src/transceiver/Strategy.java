package transceiver;

import java.sql.SQLException;


public interface Strategy {

	String getMailSql() throws SQLException;
	@Override
	public String toString();
}

class NewestFirstStrategy implements Strategy {

	@Override
	public String getMailSql() throws SQLException {

		return "select * from master where boxid in (1, 2) order by date desc";
	}

	@Override
	public String toString() {
		return "新しい順";
	}
}

class OldestFirstStrategy implements Strategy {

	@Override
	public String getMailSql() throws SQLException {
		
		return "select * from master where boxid in (1, 2) order by date asc";
	}
	
	@Override
	public String toString() {
		return "古い順";
	}
	
}

