package DustBox;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class Dustbox_tablemodel extends DefaultTableModel {
	ArrayList<Boolean> cbox = new ArrayList<>();
    public Dustbox_tablemodel(String[] clmTitle) {
    	super(null,clmTitle);
	}

	public Class getColumnClass(int col){
        return getValueAt(0, col).getClass();
    }
    public void add(String f,String s,String c){
    	cbox.add(false);
    	Object[] data = {
    			cbox.get(cbox.size()-1),
    			f,
    			s,
    			c};
    	this.addRow(data);
    }
}
