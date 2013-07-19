package transceiver;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface Strategy {

	ResultSet getMail(MailDB db) throws SQLException;
	@Override
	public String toString();
}

class NewestFirstStrategy implements Strategy {

	@Override
	public ResultSet getMail(MailDB db) throws SQLException {

		return db.executeQuery("");
	}

	@Override
	public String toString() {
		return "新しい順";
	}
}

class OldestFirstStrategy implements Strategy {

	@Override
	public ResultSet getMail(MailDB db) throws SQLException {
		
		return db.executeQuery("");
	}
	
	@Override
	public String toString() {
		return "古い順";
	}
	
}

