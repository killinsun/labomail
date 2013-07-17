package AddressBook;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

	JButton commit = new JButton("登録");
	JButton cancel = new JButton("キャンセル");
	PaneAddress paneAddress = new PaneAddress();

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

		/*
		 * なんかこのままだとコードが汚い
		 */
		case "登録":

			try {
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
				Statement stmt = conn.createStatement();

				ResultSet rs = stmt.executeQuery("select count(*) from addresstable where name = '" + name.getText() +"';");
				rs.next();
				System.out.println( rs.getInt( 1 ) );
				String sql;
				if(rs.getInt(1) == 0){
					sql ="insert into addresstable values(" +
							"null,'" +
							name.getText() + "','" +
							furigana.getText() + "','" +
							kubun.getSelectedItem() +"','" +
							pcMail.getText() + "','" +
							phoneMail.getText() + "','" +
							tel.getText() + "','" +
							memo.getText() +"');";
				}else{
					sql ="update addresstable set " +
							"name ='"+ name.getText() + "'," +
							"furigana ='" + furigana.getText() + "'," +
							"kubun ='" + kubun.getSelectedItem() + "'," +
							"pcmail ='" + pcMail.getText() + "'," +
							"phonemail ='" + phoneMail.getText() + "'," +
							"tel ='" + tel.getText() + "'," +
							"memo ='" + memo.getText() +"' where name = '" + name.getText() +"';";
				}
				stmt.execute(sql);
				conn.close();
				paneAddress.updateList();

			} catch (ClassNotFoundException | SQLException error) {
				error.printStackTrace();
			}
			break;
		case "キャンセル":
			System.out.println("キャンセル");
			break;
		}
		System.out.println(command);
	}


}
