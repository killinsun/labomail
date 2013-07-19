package TopView;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface Strategy {

	ResultSet getMail(MailDB db) throws SQLException;
}

class NewestFirstStrategy implements Strategy {

	@Override
	public ResultSet getMail(MailDB db) throws SQLException {

		return db.executeQuery("");
	}

}

class OldestFirstStrategy implements Strategy {

	@Override
	public ResultSet getMail(MailDB db) throws SQLException {
		
		return db.executeQuery("");
	}
	
}

