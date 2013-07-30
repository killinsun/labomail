package DustBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import transceiver.MailObject;

import net.miginfocom.swing.MigLayout;
import dbHelper.DbHelper;

public class Dustbox_main extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;


	final String[] clmTitle = {
			"",
			"from",
			"to",
			"件名",
	"内容"};
	public 
	Dustbox_tablemodel model = new Dustbox_tablemodel(clmTitle);

	final JPanel[] pane ={
			new JPanel(),
			new JPanel()};

	final JButton[] button ={
			new JButton("削除"),
			new JButton("元に戻す")};
	final JTable table = new JTable(model);

	public Dustbox_main(){

		this.setLayout(new MigLayout(
				"wrap 1",
				"[grow]",
				"[][grow]"));
		pane[0].setLayout(new MigLayout(
				"",
				"[grow][grow][grow]",
				"[]"));
		for(JButton b:button){
			b.addActionListener(this);
			pane[0].add(b,"c");
		}
		this.add(pane[0]);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.getTableHeader().setReorderingAllowed(false);
		DefaultTableColumnModel columnModel = (DefaultTableColumnModel)table.getColumnModel();
		TableColumn tblcolumn = columnModel.getColumn(0);
		tblcolumn.setPreferredWidth(5);
		tblcolumn.setResizable(false);
		tblcolumn = columnModel.getColumn(1);
		tblcolumn.setPreferredWidth(100);
		tblcolumn = columnModel.getColumn(2);
		tblcolumn.setPreferredWidth(100);
		tblcolumn = columnModel.getColumn(3);
		tblcolumn.setPreferredWidth(150);
		tblcolumn = columnModel.getColumn(4);
		tblcolumn.setPreferredWidth(500);


		JScrollPane scrpane = new JScrollPane(table);
		scrpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane[1].setLayout(new MigLayout(
				"",
				"[grow]",
				"[grow]"));
		pane[1].add(scrpane,"grow");
		this.add(pane[1],"grow");

		try{
			DbHelper db = new DbHelper();
			db.execute("insert into mastertbl values(null,1,1,'a','aa','aaa','aaaa',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,2,2,'b','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,3,2,'c','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,1,'d','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,2,'e','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,3,'f','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,3,'g','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,2,'h','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,2,'i','','','',datetime('now','localtime'),'');");
			db.execute("insert into mastertbl values(null,4,1,'j','','','',datetime('now','localtime'),'');");
			db.close();

		}catch(SQLException e1){
			e1.printStackTrace();
		}
		MailObject[] defmail = MailObject.createMailObjects("SELECT * FROM mastertbl where mboxid=4");
		for(int i=0;i<defmail.length;i++){
			model.add(defmail[i]);
		}

	}
	public void actionPerformed(ActionEvent e) {
		try {
			ArrayList<Integer> rowcount = new ArrayList<>();
			for(int i = 0;i < model.getRowCount();i++){
				if((boolean)model.getValueAt(i, 0)){
					rowcount.add(i);
				}
			}
			if(e.getSource() == button[0]){
				DbHelper db = new DbHelper();
				for(int i=rowcount.size()-1;i>=0;i--){
					int selectRow = rowcount.get(i);
					db.execute("delete from mastertbl where id='"+model.getmailID(selectRow)+"'" );
					model.removeRow(selectRow);
				}
				db.close();
			}else if(e.getSource() == button[1]){
				int id;
				ResultSet rs;
				DbHelper dbGetId;
				DbHelper dbUpdate;
				for(int i=rowcount.size()-1;i>=0;i--){
					dbGetId = new DbHelper();
					rs = dbGetId.executeQuery("select * from mastertbl where id='"+model.getmailID(rowcount.get(i))+"'");
					id=rs.getInt(3);
					System.out.println("boxid="+rs.getInt(2));
					dbGetId.close();
					dbUpdate = new DbHelper();
					dbUpdate.execute("update mastertbl" +
							" set MBOXID="+id+",BOXID=1 where id='"+model.getmailID(rowcount.get(i))+"'" );
					dbUpdate.close();
					model.removeRow(rowcount.get(i));
					rs.close();
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
