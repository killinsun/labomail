package AddressBook;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import dbHelper.DbHelper;

import net.miginfocom.swing.MigLayout;

public class FrmAddKubun extends JFrame implements ActionListener{
	DbHelper dh;
	JTextField inputKubun = new JTextField();
	FrmEdit frmEdit = new FrmEdit();

	public FrmAddKubun(FrmEdit edit){
		frmEdit = edit;
		dh = new DbHelper();
		this.setLayout(new MigLayout("", "[][]", "[][]"));	
		
		inputKubun.setPreferredSize(new Dimension(200, 20));
		this.add(inputKubun,"span,wrap");
		
		JButton addButton = new JButton("追加");
		addButton.addActionListener(this);
		this.add(addButton,"");
		JButton cancelButton = new JButton("キャンセル");
		cancelButton.addActionListener(this);
		this.add(cancelButton,"wrap");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		String sql;
		switch(command){
		case "追加":
			sql = "INSERT INTO kubuntable VALUES(null,'"+ inputKubun.getText() + "');";
			try {
				dh.execute(sql);
				dh.close();
				frmEdit.setKubun();
			} catch (SQLException error) {
				error.printStackTrace();
			} finally{
				validate();
				dispose();
			}
			break;
		
		case "キャンセル":
			dispose();
			break;
			
		}
	}



}
