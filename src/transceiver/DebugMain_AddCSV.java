package transceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class DebugMain_AddCSV {

	public static void main(String[] args) {

		MailDB db = new MailDB(true);
		
		try {
			db.initTables();
			db.csvImport(new File("mails.csv"));
		} catch (SQLException | FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

}
