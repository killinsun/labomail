package TmpMail;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class Table extends JPanel {
	
	//テーブル部分
	String [] cl = {"ID","name"};
	String [][] td = {
			{"1","hoge"}
			};
	private DefaultTableModel tb = new DefaultTableModel(td,cl);
	private JTable jt = new JTable(tb);
	private JScrollPane jsc = new JScrollPane(jt);


	public Table() {

		this.setLayout(new MigLayout("","[grow]","[grow]"));
		this.add(jsc,"grow");
	}


}
