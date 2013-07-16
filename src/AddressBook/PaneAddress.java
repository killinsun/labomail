/*
 *------やるべき事リスト------- 
 *☆一番左の縦型のタブっぽいの
 *★アドレス帳追加画面 
 *★追加画面で作成したデータをcsv形式に
 *☆csvから左側のリスト読み込み
 *☆50音ボタンでリスト位置を読み込みし直せるようにする
 */
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

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dbHelper.DbHelper;

import net.miginfocom.swing.MigLayout;

//paneAddress ver0.8
public class PaneAddress extends JPanel implements ActionListener{

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

	public PaneAddress() {
		this.setLayout(new MigLayout("", "[][][grow]", "[grow][]"));

		DbHelper dh = new DbHelper();
		//縦型タブ

		this.add(new JLabel("dammy"));

		JList list = new JList(listModel);
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
		furiganaLabel.setText("ヤマダタロウ");
		nameLabel.setText("山田太郎");
		nameLabel.setFont(new Font("", Font.BOLD, 24));
		addressLabel1.setText("メールアドレス:");
		addressLabel2.setText("メールアドレス2:");
		phoneLabel.setText("電話番号:");
		address1.setText("aaaa@aaa.com");
		address2.setText("bbbbb@bbb.com");
		phoneNum.setText("080-xxxx-xxxx");
		memoLabel.setText("メモ:");
		memoField.setPreferredSize(new Dimension(400, 150));
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
			ResultSet rs = stmt.executeQuery( "select name from addresstable" );
			while( rs.next() ) {
				System.out.println( rs.getString( 1 ) );
				listModel.addElement(rs.getString( 1 ));
			}



		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}
