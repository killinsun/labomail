package Main;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import AddressBook.FrmAddress;

public class Main extends JFrame{
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("LaboMail");
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setSize(720, 500);
		mainFrame.setLocationRelativeTo(null);
		
		//Create Forms Instance.
		FrmAddress address = new FrmAddress();

		//Create MainTabPanel
		JTabbedPane  mainTabPanel = new JTabbedPane();
		
		mainTabPanel.add(address);
		mainTabPanel.setTitleAt(0,"アドレス帳");
		
		mainFrame.add(mainTabPanel);
		mainFrame.setVisible(true);
		
		

	}

}
