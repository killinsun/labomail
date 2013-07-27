package DustBox;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import transceiver.MailObject;

public class Dustbox_tablemodel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	ArrayList<Integer> boxID = new ArrayList<>();
	ArrayList<Boolean> cbox = new ArrayList<>();
	public Dustbox_tablemodel(String[] clmTitle) {
		super(null,clmTitle);
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
	public void add(MailObject mObj){
		Object[] data = {
				new Boolean(false),
				mObj.getFrom(),
				mObj.getToCC(),
				mObj.getSubject(),
				mObj.getData()};
		this.addRow(data);
	}
}
