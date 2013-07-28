package preference;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class FrmSoftwareCredit extends JFrame {
	public FrmSoftwareCredit(){

		/* 初期設定 */
		this.setTitle("Credit");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		getContentPane().setBackground(Color.LIGHT_GRAY);

		//アイコンを設置
		JLabel softIcon = new JLabel(new ImageIcon("data/creditIcon.jpg"));
		this.add(softIcon, BorderLayout.NORTH);

		//明示文を設置
		String creadit =
				"\nLabo Mailer for Gmail\n\n" +
				"Version: 1.0\n" +
				"Release: 2013-07-31\n\n" +
				"Created by FJB Labo Members\n" +
				"And FJB Graphic Menbers";

		JTextArea txtCredit = new JTextArea(creadit);
		txtCredit.setFont(new Font("ＭＳ Ｐゴシック", Font.BOLD, 15));
		txtCredit.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		txtCredit.setEditable(false);
		txtCredit.setBackground(Color.LIGHT_GRAY);
		this.add(txtCredit, BorderLayout.CENTER);
	}
}
