package DustBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

public class Dustbox_main extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> delTempRow = new ArrayList<>();
	private ArrayList<Object[]> delTempData = new ArrayList<>();

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
			new JButton("元に戻す"),
			new JButton("設定")};
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


	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button[0]){
			ArrayList<Integer> rowcount = new ArrayList<>();
			for(int i = 0;i < model.getRowCount();i++){
				if((boolean)model.getValueAt(i, 0)){
					rowcount.add(i);
				}
			}
			int delcount = 0;
			for(int i=0;i<rowcount.size();i++){
				int selectRow = rowcount.get(i)-delcount;
				Object[] data = {
						false,
						model.getValueAt(selectRow,1),
						model.getValueAt(selectRow,2),
						model.getValueAt(selectRow,3)
				};
				delTempRow.add(selectRow);
				delTempData.add(data);
				model.removeRow(selectRow);
				delcount++;
			}
		}else if(e.getSource() == button[1]){
			for(int i=0;i<delTempRow.size();i++){
				model.insertRow(delTempRow.get(i),delTempData.get(i));
			}
			delTempRow.clear();
			delTempData.clear();
		}else if(e.getSource() == button[2]){

		}
	}
}
