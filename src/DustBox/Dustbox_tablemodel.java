package DustBox;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class Dustbox_tablemodel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

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
	public void add(String f,String t,String s,String c){
		Object[] data = {
				new Boolean(false),
				t,
				f,
				s,
				c};
		this.addRow(data);
	}
}
