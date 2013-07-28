package TmpMail;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import DustBox.Dustbox_main;

import net.miginfocom.swing.MigLayout;

public class Driver extends JFrame{

	private JPanel pane = new JPanel(new MigLayout("","",""));
	private DraftBox_Mainframe box = new DraftBox_Mainframe();
	private Dustbox_main dbox = new Dustbox_main();	
	public Driver(){
			setTitle("test");
			setSize(800,600);
			pane.add(box,"r");
			pane.add(dbox,"l");
			add(pane);
			setVisible(true);
			
		
		
	}
	
	public static void main(String[] args) {
		Driver jf = new Driver();
	}
}
