package DustBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

public class Dustbox_main extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private String[] clmTitle = {
			"",
			"from",
			"件名",
			"内容"};
	private Dustbox_tablemodel model = new Dustbox_tablemodel(clmTitle);
	private JPanel[] pane ={
			new JPanel(),
			new JPanel()};
	private JButton[] button ={
			new JButton("元に戻す"),
			new JButton("設定"),
			new JButton("削除")};
	private JTable table = new JTable(model);

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

		JScrollPane scrpane = new JScrollPane(table);
	    scrpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane[1].setLayout(new MigLayout(
				"",
				"[grow]",
				"[grow]"));
		pane[1].add(scrpane,"grow");
		this.add(pane[1],"grow");
		
		
	}
	public void actionPerformed(ActionEvent e) {
        model.add("hoge","fuga","foo");
        for(boolean b:model.cbox){
        	System.out.println(b);
        }
    }

}
