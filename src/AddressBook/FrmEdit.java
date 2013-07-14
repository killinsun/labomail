package AddressBook;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dbHelper.DbHelper;

import net.miginfocom.swing.MigLayout;

public class FrmEdit extends JFrame implements ActionListener {

	//コンポーネントの準備
	JButton faceButton = new JButton();
	ImageIcon icon = new ImageIcon("data/faceIcon/default.jpg");
	JLabel nameLabel = new JLabel("名前:");
	JLabel furiganaLabel = new JLabel("フリガナ:");
	JLabel kubunLabel = new JLabel("区分:");
	JLabel pcMailLabel = new JLabel("メールアドレス(PC):");
	JLabel phoneMailLabel = new JLabel("メールアドレス(モバイル):");
	JLabel telLabel = new JLabel("電話番号:");
	JLabel memoLabel = new JLabel("メモ");

	JTextField name = new JTextField();
	JTextField furigana = new JTextField();
	JTextField pcMail = new JTextField();
	JTextField phoneMail = new JTextField();
	JTextField tel = new JTextField();
	JTextArea memo = new JTextArea();

	String[] stabData = {"家族","学校"};
	JComboBox kubun = new JComboBox(stabData);

	JButton commit = new JButton("編集");
	JButton cancel = new JButton("キャンセル");

	public FrmEdit(){
		this.setLayout(new MigLayout("", "[][][]", "[][][]"));	

		faceButton.setIcon(icon);
		faceButton.addActionListener(this);
		this.add(faceButton,"span 1 3");

		this.add(nameLabel,"grow");
		name.setPreferredSize(new Dimension(200, 20));
		this.add(name,"wrap");
		this.add(furiganaLabel,"grow");
		furigana.setPreferredSize(new Dimension(200, 20));
		this.add(furigana,"wrap");
		this.add(kubunLabel,"");

		kubun.setPreferredSize(new Dimension(80, 30));
		this.add(kubun,"wrap");

		this.add(pcMailLabel,"");
		pcMail.setPreferredSize(new Dimension(230, 20));
		this.add(pcMail,"span,wrap");

		this.add(phoneMailLabel,"");
		phoneMail.setPreferredSize(new Dimension(230, 20));
		this.add(phoneMail,"span,wrap");

		this.add(telLabel,"");
		tel.setPreferredSize(new Dimension(230, 20));
		this.add(tel,"span,wrap");

		this.add(memoLabel,"");
		memo.setPreferredSize(new Dimension(230,180));
		this.add(memo,"span,wrap");

		commit.addActionListener(this);
		cancel.addActionListener(this);
		this.add(commit,"span 2,right");
		this.add(cancel,"");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		String delim = ",";
		switch(command){
		//現状では新規追加。後で直す。
		case "編集":
			String sql ="insert into addresstable values(' " +
					name.getText() + "','" +
					furigana.getText() + "','" +
					kubun.getSelectedItem() +"','" +
					pcMail.getText() + "','" +
					phoneMail.getText() + "','" +
					tel.getText() + "','" +
					memo.getText() +"');";

			DbHelper dbhelper = new DbHelper();
			System.out.println(sql);
			dbhelper.execSql(sql);
			break;
		case "キャンセル":
			System.out.println("キャンセル");
			break;
		}
		System.out.println(command);
	}


}
