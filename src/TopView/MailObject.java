package TopView;

import java.util.Date;

enum Status {
	RECEIVE,
	SENT,
	NOT_SEND,
	OTHER,
}

public class MailObject {

	private String from;
	private String subject;
	private Date date;
	private String text;
	Status status;

	public MailObject() {
		this.from = "from";
		this.subject = "subject";
		this.date = new Date();
		this.text = "text";
		this.status = Status.OTHER;
	}
	
	public MailObject(String from, String subject, Date date, String text) {
		this.from = from;
		this.subject = subject;
		this.date = date;
		this.text = text;
		this.status = status.OTHER;
	}
	
	public MailObject(String from, String subject, Date date, String text, Status status) {
		this.from = from;
		this.subject = subject;
		this.date = date;
		this.text = text;
		this.status = status;
	}
	
	public String getListViewText() {
		
		return from + "\n" + subject + "\n" + date;
	}

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	public Date getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return from + "\n" + subject + "\n" + date;
	}
	
}
