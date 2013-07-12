/*
 *------やるべき事リスト------- 
 *◯一番左の縦型のタブっぽいの
 *◯アドレス帳追加画面 
 *◯追加画面で作成したデータをcsv形式に
 *◯csvから左側のリスト読み込み
 *◯50音ボタンでリスト位置を読み込みし直せるようにする
 */
package AddressBook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class FrmAddress extends JPanel {

	//コンポーネントの準備
	DefaultListModel listModel = new DefaultListModel();
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
	private JButton editButton = new JButton();

	public FrmAddress() {
		JPanel panel = new JPanel();
		//[tab][list][label1][label2]
		this.setLayout(new MigLayout("", "[][][grow]", "[grow]"));
		
		//縦型タブ
		
		this.add(new JLabel("dammy"));
		listModel.addElement("test1");
		JList list = new JList(listModel);
		this.add(list,"h 500,w 200");

		/*
		 * There is including of stab.
		 * 
		 */
		JPanel rightPanel = new JPanel();
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
		this.add(rightPanel);
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
			rightPanel.add(memoField,"span,grow,wrap");

	}

}