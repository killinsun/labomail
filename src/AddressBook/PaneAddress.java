package AddressBook;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbHelper.DbHelper;

import net.miginfocom.swing.MigLayout;

//paneAddress ver0.8.5
public class PaneAddress extends JPanel implements ActionListener,ListSelectionListener{

	//HashMap<key=name><Value=ID>
	//keyを元に値を検索するので、今回はIDを検索するために名前を検索する
	//重複が発生したら、keyの後ろに適当な乱数を生成して貼り付けるとどうにかなるかもしれない
	HashMap<String,Integer> dataMap = new HashMap<String,Integer>();

	protected String gettedName;
	protected String gettedFurigana;
	protected String gettedKubun;
	protected String gettedPcMail;
	protected String gettedPhoneMail;
	protected String gettedTel;
	protected String gettedMemo;
	

	//コンポーネントの準備
	DefaultListModel<String> listModel = new DefaultListModel();
	private JPanel rightPanel = new JPanel();
	private JLabel imageLabel = new JLabel();
	private JLabel furiganaLabel = new JLabel();
	private JLabel nameLabel = new JLabel();
	private JLabel addressLabel1 = new JLabel();
	private JLabel addressLabel2 = new JLabel();
	private JLabel address1 = new JLabel();
	private JLabel address2 = new JLabel();
	private JLabel phoneLabel = new JLabel();
	private JLabel phoneNum = new JLabel();
	private JLabel memoLabel = new JLabel();
	private JTextField memoField = new JTextField();
	private JButton editButton = new JButton("編集");
	private JButton addButton = new JButton("+");
	private JList list = new JList(listModel);


	public PaneAddress() {
		this.setLayout(new MigLayout("", "[][][grow]", "[grow][]"));

		DbHelper dh = new DbHelper();
		//縦型タブ

		this.add(new JLabel("dammy"));
		list.addListSelectionListener(this);
		this.add(list,"flowy,width 200,height 500");
		updateList();
		rightPanelAdding();

		this.add(rightPanel);
		addButton.addActionListener(this);
		add(addButton, "cell 1 0");


	}

	void rightPanelAdding(){
		/*
		 * There is including of stab.
		 * 
		 */
		ImageIcon imI = new ImageIcon("data/debugFace.jpg");
		imageLabel.setIcon(imI);
		furiganaLabel.setText(gettedFurigana);
		nameLabel.setText(gettedName);
		nameLabel.setFont(new Font("", Font.BOLD, 24));
		addressLabel1.setText("PCメール:");
		addressLabel2.setText("携帯:");
		phoneLabel.setText("電話番号:");
		address1.setText(gettedPcMail);
		address2.setText(gettedPhoneMail);
		phoneNum.setText(gettedTel);
		memoLabel.setText("メモ:");
		memoField.setPreferredSize(new Dimension(400, 150));
		memoField.setText(gettedMemo);
		memoField.setEnabled(false);
		editButton.addActionListener(this);
		rightPanel.setLayout(new MigLayout("","[grow][grow]","[grow][grow][][][][][]"));
		rightPanel.add(imageLabel,"span 1 2");
		rightPanel.add(furiganaLabel,"wrap,left,bottom");
		rightPanel.add(nameLabel,"wrap, left, top");
		rightPanel.add(addressLabel1);
		rightPanel.add(address1,"wrap");
		rightPanel.add(addressLabel2);
		rightPanel.add(address2,"wrap");
		rightPanel.add(phoneLabel);
		rightPanel.add(phoneNum,"wrap 30");
		rightPanel.add(memoLabel,"wrap");
		rightPanel.add(memoField,"span,grow,wrap 20");
		rightPanel.add(editButton,"span,r,b");

	}
	public void updateList(){
		listModel.clear();
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( "select id,name from addresstable" );
			//  getDataModelCol = [id][name]
			while( rs.next() ) {
				listModel.addElement(rs.getString(2));
				//Jlistに登録するだけでなく、Mapに格納することで、後に検索できるようにする
				dataMap.put(rs.getString(2),rs.getInt(1));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command){
		case "編集":
			FrmEdit frmEdit = new FrmEdit();
			frmEdit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frmEdit.setLocationRelativeTo(null);
			frmEdit.setSize(400,500);
			frmEdit.setTitle("編集");
			frmEdit.setVisible(true);
			break;
		case "+":
			FrmEdit frmAdd= new FrmEdit();
			frmAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frmAdd.setLocationRelativeTo(null);
			frmAdd.setSize(400,500);
			frmAdd.setTitle("追加");
			frmAdd.setVisible(true);
			break;


		}
		updateList();


	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()){ 
			System.out.println("list changed"); 
			try {
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:labomailer.db");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery( "select * from addresstable where id="+dataMap.get(list.getSelectedValue()));
				System.out.println("select * from addresstable where id='"+dataMap.get(list.getSelectedValue())+"'");
				//id,名前,フリガナ,区分,PCMail,PhoneMail,Tel,Memo
				while( rs.next() ) {
					gettedName = rs.getString(2);
					gettedFurigana = rs.getString(3);
					gettedKubun = rs.getString(4);
					gettedPcMail = rs.getString(5);
					gettedPhoneMail = rs.getString(6);
					gettedTel = rs.getString(7);
					gettedMemo = rs.getString(8);
					
				}
				conn.close();
				rightPanelAdding();
			} catch (ClassNotFoundException | SQLException error) {
				error.printStackTrace();
			}

		}
	}

}
