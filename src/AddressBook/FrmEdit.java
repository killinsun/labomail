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
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dbHelper.DbHelper;

import net.miginfocom.swing.MigLayout;
import Util.Util;


public class FrmEdit extends JFrame implements ActionListener {
	private String nameAndSuffix;
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

	JComboBox<String> kubun = new JComboBox<String>();
	DefaultComboBoxModel<String> kubunModel = new DefaultComboBoxModel<String>();

	JButton commit = new JButton("登録");
	JButton cancel = new JButton("キャンセル");
	JButton kubunAddButton = new JButton("+");

	PaneAddress paneAddress;
	FrmAddKubun frmAddKubun;
	DbHelper dh;
	public FrmEdit(){

	}

	public FrmEdit(PaneAddress pane,String[] gettedData,int gettedID){
		this.gettedID = gettedID;
		dh = new DbHelper();

		//
		paneAddress = pane;
		//

		this.setLayout(new MigLayout("", "[][][][grow]", "[][][]"));	
		/*
		 * gettedData adding;
		 */

		name.setText(gettedData[0]);
		furigana.setText(gettedData[1]);
		setKubun();

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
		this.add(name,"wrap,span");
		this.add(furiganaLabel,"grow");
		furigana.setPreferredSize(new Dimension(200, 20));
		this.add(furigana,"wrap,span");
		this.add(kubunLabel,"");

		kubun.setPreferredSize(new Dimension(80, 30));
		this.add(kubun,"");
		this.add(kubunAddButton,"left,wrap");
		kubunAddButton.addActionListener(this);

		this.add(pcMailLabel,"");
		pcMail.setPreferredSize(new Dimension(250, 20));
		this.add(pcMail,"span 3,wrap");

		this.add(phoneMailLabel,"");
		phoneMail.setPreferredSize(new Dimension(250, 20));
		this.add(phoneMail,"span 3,wrap");

		this.add(telLabel,"");
		tel.setPreferredSize(new Dimension(250, 20));
		this.add(tel,"span 3,wrap");

		this.add(memoLabel,"");
		memo.setPreferredSize(new Dimension(250,180));
		this.add(memo,"span 3,wrap");

		commit.addActionListener(this);
		cancel.addActionListener(this);
		this.add(commit,"span 3,right");
		this.add(cancel,"");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command){
		case "登録":

			try {
				String sql = "select count(*) from addresstable where id = " + gettedID +";";
				ResultSet rs = dh.executeQuery(sql);
				rs.next();
				System.out.println( rs.getInt( 1 ) );
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
				dh.execute(sql);
				dh.close();


			} catch (SQLException error) {
				error.printStackTrace();
			}
			paneAddress.updateList();
			dispose();
			break;
		case "キャンセル":
			dispose();
			break;
			//区分の追加
		case "+":
			frmAddKubun = new FrmAddKubun(this);
			frmAddKubun.setSize(250,100);
			frmAddKubun.setTitle("区分の追加");
			frmAddKubun.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			frmAddKubun.setLocationRelativeTo(null);
			frmAddKubun.setVisible(true);
			break;
			//FaceIcon
		case "":
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int selected = filechooser.showOpenDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION){
				//GetFileSection
				File file = filechooser.getSelectedFile();
				nameAndSuffix= file.getName(); //名前と拡張子
				Util util = new Util();
				String suffix = util.getSuffix(nameAndSuffix); //拡張子

				if(!suffix.equals("jpg")){
					JFrame errorFrame = new JFrame();
					errorFrame.setTitle("拡張子エラー");
					JOptionPane.showMessageDialog(errorFrame, "拡張子はJPEGのものを選択してください。");
					break;
				}

				//Resize section
				srcPath = file.getAbsolutePath();
				dstPath = "data/faceIcon/" + nameAndSuffix;
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
	protected void setKubun(){
		kubunModel.removeAllElements();
		System.out.println("ぬるぽ");
		DbHelper dh = new DbHelper();
		ResultSet rs = dh.executeQuery("SELECT kubun FROM kubuntable");
		try {
		System.out.println("ぬるぽ2");
			while(rs.next()){
				kubunModel.addElement(rs.getString(1));
			}
			dh.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		kubun.setModel(kubunModel);
		kubun.validate();
		kubun.repaint();
	}


}