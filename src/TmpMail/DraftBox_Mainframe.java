package TmpMail;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class DraftBox_Mainframe extends JFrame implements ActionListener {

	
	
	private JPanel pane = new JPanel(new MigLayout("wrap1","[grow]", ""));
private JPanel subpane1 = new JPanel(new MigLayout("wrap3","[grow]",""));
private JPanel subpane2 = new JPanel(new MigLayout("wrap1","[grow]",""));
private Checkbox check = new Checkbox();
	private Table table = new Table();

	
	public DraftBox_Mainframe() {
		setTitle("下書き");
		setSize(800, 600);
		subpane1.add(new JButton("ボタン1"),"c");
		subpane1.add(new JButton("ボタン2"),"c");
		subpane1.add(new JButton("ボタン3"),"c");
		subpane2.add(table,"grow");
		pane.add(subpane1,"grow");
		pane.add(subpane2,"grow");
		
		
		add(pane);
		setVisible(true);
	}
	public void table(){
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

	public static void main(String[] args) {
	DraftBox_Mainframe jf = new DraftBox_Mainframe();
	
	}

}
