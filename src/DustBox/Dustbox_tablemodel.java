package DustBox;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import transceiver.MailObject;

public class Dustbox_tablemodel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> mailID = new ArrayList<>();
	private MailObject[] dbmail;

	public Dustbox_tablemodel(String[] clmTitle) {
		super(null,clmTitle);
		this.reload();
		}
	public boolean isCellEditable(int row,int col){
		if(col==0){
			return true;
		}else{
			return false;
		}
	}
	public Class getColumnClass(int col){
		return getValueAt(0, col).getClass();
	}
	public void add(MailObject mailobj){
		mailID.add(mailobj.getId());
		Object[] data = {
				new Boolean(false),
				mailobj.getFrom(),
				mailobj.getToCC()[0],
				mailobj.getSubject(),
				mailobj.getData()};
		this.addRow(data);
	}
	public int getmailID(int row){
		return mailID.get(row);
	}
	public void reload(){
		dbmail = MailObject.createMailObjects("SELECT * FROM mastertbl where mboxid=4");
			this.setRowCount(0);
			for(int i=0;i<dbmail.length;i++){
				this.add(dbmail[i]);
			}
	}

}
