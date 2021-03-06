package transceiver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import dbHelper.DbHelper;


enum Status {
	RECEIVE,
	SENT,
	DRAFT,
	TRUSH,
	OTHER,
}

/** メール1件のデータを保持する */
public class MailObject {

	private int id;
	private int mboxid;
	private int boxid;
	private String mfrom;
	private String mto;
	private String subject;
	private String data;
	private Timestamp date;
	private String path;
	Status status;

	public MailObject() {
		this.id = 0;
		this.mboxid = 1;
		this.boxid = 1;
		this.mfrom = "from";
		this.mto = "to";
		this.subject = "subject";
		this.data = "text";
		this.date = new Timestamp(System.currentTimeMillis());
		this.path = "hoge.txt";
	}

	public MailObject(int id, int mboxid, int boxid, String mfrom, String mto,
			String subject, String data, Timestamp date, String path) {
		this.id = id;
		this.mboxid = mboxid;
		this.boxid = boxid;
		this.mfrom = mfrom;
		this.mto = mto;
		this.subject = subject;
		this.data = data;
		this.date = date;
		this.path = path;

		switch (boxid) {
		case 1:
			this.status = Status.RECEIVE;
			break;
		case 2:
			this.status = Status.SENT;
			break;
		case 3:
			this.status = Status.DRAFT;
			break;
		case 4:
			this.status = Status.TRUSH;
			break;

		default:
			this.status = Status.OTHER;
			break;
		}
	}

	public static MailObject[] createMailObjects(String sql){
		ArrayList<MailObject> mailObjAry = new ArrayList<>();
		DbHelper dbhelper = new DbHelper();
		ResultSet rs = dbhelper.executeQuery(sql);
		try {
			while(rs.next()){
				mailObjAry.add(new MailObject(
						rs.getInt("id"), 
						rs.getInt("mboxid"), 
						rs.getInt("boxid"), 
						rs.getString("mfrom"), 
						rs.getString("mto"), 
						rs.getString("subject"),
						rs.getString("data"),
						Timestamp.valueOf(rs.getString("date")),
						rs.getString("path")));
			}
			dbhelper.close();
			rs.close(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mailObjAry.toArray(new MailObject[]{});
	}


	public int getId() {
		return id;
	}



	public int getMboxid() {
		return mboxid;
	}



	public int getBoxid() {
		return boxid;
	}



	public String getFrom() {

		// Gmail用に整形
		int fIndex = mfrom.indexOf("<");
		int eIndex = mfrom.lastIndexOf(">");
		if(fIndex != -1 && eIndex != -1) {
			return mfrom.substring(fIndex, eIndex + 1);
		}

		return mfrom;
	}



	public String getTo() {

		int fIndex = mto.indexOf("<");
		int eIndex = mto.lastIndexOf(">");
		if(fIndex != -1 && eIndex != -1) {
			return mto.substring(fIndex, eIndex + 1);
		}

		return mto;
	}

	public String[] getToCC(){
		String[] ToCcBcc = this.getTo().split("[BCC]");
		return ToCcBcc[0].split(",");
	}


	public String[] getBcc(){
		String[] ToCcBcc = this.getTo().split("[BCC]");
		return ToCcBcc[1].split(",");
	}

	public String getSubject() {
		return subject;
	}



	public String getData() {
		return data;
	}



	public Timestamp getDate() {
		return date;
	}



	public String getPath() {
		return path;
	}



	public Status getStatus() {
		return status;
	}


	@Override
	public String toString() {

		// JList用に整形

		String from = mfrom;
		String subj = subject;

		int fIndex = from.indexOf("<");
		int eIndex = from.lastIndexOf(">");

		if(fIndex != -1 && eIndex != -1) {
			from = from.substring(fIndex, eIndex + 1);
		}
		if(from.length() > 20) {
			from = from.substring(0, 20).concat("...");
		}
		if(subj.length() > 20) {
			subj = subj.substring(0, 15).concat("...");
		}
		return from + "\n" + subj + "\n" + date;
	}

}
