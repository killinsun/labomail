package TopView;


enum Status {
	RECEIVE,
	SENT,
	DRAFT,
	TRUSH,
	OTHER,
}

public class MailObject {

	private int id;
	private int mboxid;
	private int boxid;
	private String mfrom;
	private String mto;
	private String subject;
//	private Date date;
	private String data;
	private String date;
	private String path;
	Status status;

	public MailObject() {
		this.id = 0;
		this.mboxid = 1;
		this.boxid = 1;
		this.mfrom = "from";
		this.mto = "to";
		this.subject = "subject";
		this.date = "xx/xx/xx 00:00:00";
		this.data = "text";
		this.path = "hoge.txt";
	}
	
	
	
	public MailObject(int id, int mboxid, int boxid, String mfrom, String mto,
			String subject, String data, String date, String path) {
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



	public int getId() {
		return id;
	}



	public int getMboxid() {
		return mboxid;
	}



	public int getBoxid() {
		return boxid;
	}



	public String getMfrom() {
		return mfrom;
	}



	public String getMto() {
		return mto;
	}



	public String getSubject() {
		return subject;
	}



	public String getData() {
		return data;
	}



	public String getDate() {
		return date;
	}



	public String getPath() {
		return path;
	}



	public Status getStatus() {
		return status;
	}



	public String getListViewText() {
		
		return mfrom + "\n" + subject + "\n" + date;
	}


	@Override
	public String toString() {
		return mfrom + "\n" + subject + "\n" + date;
	}
	
}
