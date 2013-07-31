package senderView;

import javax.swing.JScrollPane;

public class SenderScrollPane extends JScrollPane {
	public SenderScrollPane(){
		super();
		this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		this.setViewportView(new MailSenderPanel());
	}
}
