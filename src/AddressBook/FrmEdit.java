package AddressBook;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import Util.Util;


public class FrmEdit extends JFrame implements ActionListener {
	private String chooseFaceIcon;
	private String srcPath;
	private String dstPath ="data/faceIcon/default.jpg";
	private int gettedID;

	//コンポーネントの準備
	JButton faceButton = new JButton();
	ImageIcon icon;
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
	PaneAddress paneAddress;

	public FrmEdit(PaneAddress pane,String[] gettedData,int gettedID){
		this.gettedID = gettedID;

		//
		paneAddress = pane;
		//

		this.setLayout(new MigLayout("", "[][][]", "[][][]"));	
		/*
		 * gettedData adding;
		 */

		name.setText(gettedData[0]);
		furigana.setText(gettedData[1]);
		//Insert commboboxmodel data add.
		pcMail.setText(gettedData[3]);
		phoneMail.setText(gettedData[4]);
		tel.setText(gettedData[5]);
		memo.setText(gettedData[6]);
		dstPath = gettedData[7];

		icon = repaintIcon(dstPath);
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
		switch(command){

		/*
		 * なんかこのままだとコードが汚い
		 */
		case "登録":

			try {
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
				Statement stmt = conn.createStatement();

				ResultSet rs = stmt.executeQuery("select count(*) from addresstable where id = " + gettedID +";");
				System.out.println("select count(*) from addresstable where id = " + gettedID +";");
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
							memo.getText() +"','" +
							dstPath + "');";
				}else{
					sql ="update addresstable set " +
							"name ='"+ name.getText() + "'," +
							"furigana ='" + furigana.getText() + "'," +
							"kubun ='" + kubun.getSelectedItem() + "'," +
							"pcmail ='" + pcMail.getText() + "'," +
							"phonemail ='" + phoneMail.getText() + "'," +
							"tel ='" + tel.getText() + "'," +
							"memo ='" + memo.getText() + "'," +
							"faceicon ='" + dstPath + "' where id = " + gettedID +";";
				}
				System.out.println(sql);
				stmt.execute(sql);
				conn.close();


			} catch (ClassNotFoundException | SQLException error) {
				error.printStackTrace();
			}
			paneAddress.updateList();
			dispose();
			break;
		case "キャンセル":
			dispose();
			break;
		case "":
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int selected = filechooser.showOpenDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				chooseFaceIcon = file.getName();

				Util util = new Util();


				String suffix = util.getSuffix(chooseFaceIcon);
				if(!suffix.equals("jpg")){
					JFrame errorFrame = new JFrame();
					errorFrame.setTitle("拡張子エラー");
					JOptionPane.showMessageDialog(errorFrame, "拡張子はJPEGのものを選択してください。");
					break;
				}
				srcPath = file.getAbsolutePath();
				dstPath = "data/faceIcon/" + chooseFaceIcon;
				try {
					util.resize(srcPath,dstPath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				repaintIcon(dstPath);
				repaint();

			}
			break;
		}
		System.out.println(command);
	}

	private ImageIcon repaintIcon(String dstPath){
		icon = new ImageIcon(dstPath);
		return icon;

	}


}