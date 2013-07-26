package senderView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

class DB {
	boolean connect(String user,String pass)
	{
		//JDBCドライバのロード
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		//データベースに接続

		try {
			conn = DriverManager.getConnection("jdbc:sqlite:a.db",user,pass);
			pstm = conn.createStatement();
		} 
		catch (Exception e) {
			System.err.println();
			return false;
		}
		return true;
	}
	//データベース切断用
	//戻り値 true:成功 false:失敗
	boolean close()
	{
		try
		{
			pstm.close();
			conn.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	ResultSet Month(String a){
		try {
			String k;
			k=String.format("select * from Month where month='%s'",a);
			ResultSet rset = pstm.executeQuery(k);
			return rset;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
	ResultSet center(){
		try {
			String c;
			c=String.format("select * from Sentence_center");
			ResultSet rset = pstm.executeQuery(c);
			return rset;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
	ResultSet end(){
		try {
			String e;
			e=String.format("select * from Sentence_end");
			ResultSet rset = pstm.executeQuery(e);
			return rset;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
	//データの戻らないSQLの実行
	//sql SQL文
	//戻り値 true:実行成功 false:実行失敗
	boolean execute(String sql)
	{
		try {
			return pstm.execute(sql);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	//データが戻るSQLの実行
	//sql SQL文
	//戻り値 selectの結果 null:実行失敗
	ResultSet query(String sql)
	{
		try {
			return pstm.executeQuery(sql);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	Connection conn;    //データベース接続子
	Statement pstm;        //SQL実行用作業領域
}		

public class Inserter extends JFrame implements ActionListener {
	public static void main(String[] algs){
	DB a = new DB();
	if(a.connect("labo","")){
		System.out.println("成功");
	}
	else{
		System.out.println("失敗");
		System.exit(0);
	}
	}
	StringBuilder sb = new StringBuilder();
	
	private GetResult calledForm;
	private JList<String> firstSyntax;
	private JList<String> secondSyntax;
	private JList<String> thirdSyntax;
	private String[][] stab = {
//			{ "新緑の候、","初春の候、", "頌春の候、", "厳冬の候、", "厳寒の候、", "中冬の候、", "寒冷の候、", "麗春の候、", "大寒の候、", "酷寒のみぎり、","寒さ厳しき季節、",
//			"余寒の候、","晩冬の候、","向春の候、","解氷の候、","梅花の候、","余寒なお厳しき折、",
//			"春色のなごやかな季節、","春寒の候、","春光天地に満ちて快い時候、","孟春の候、","春雨降りやまぬ候、","浅春のみぎり、","春寒しだいに緩むころ、","冬の名残まだ去りやらぬ時候、","春寒の候、","春分の季節、","早春の候、",
//			"陽春の候、","春暖の候、","軽暖の候、","麗春の候、","春暖快適の候、","桜花爛漫の候、","花信相次ぐ候、","春眠暁を覚えずの候、","春たけなわの今日この頃、",
//			"仲春四月、","新緑の候、","薫風の候、","初夏の候、","立夏の候、","暮春の候、","老春の候、","晩春の候、","軽春の候、","惜春のみぎり、","若葉の鮮やかな季節、",
//			"梅雨の候、","初夏の候、","短夜の候、","五月雨の候、","長雨の候、","薄暑の候、","向夏の候、","麦秋の候、","向暑のみぎり、","若鮎おどる季節、",
//			"猛暑の候、","酷暑の候、","炎暑の候、","猛夏の候、","大暑の候、","暑さ厳しき折から、","三伏のみぎり、","甚暑のみぎり、","炎熱のみぎり、","灼熱の候、",
//			"残暑の候、","残炎の候、","残夏の候、","暮夏の候、","季夏の候、","新涼の候、","秋暑厳しき候、","晩夏のみぎり、","処暑のみぎり、","残暑厳しき折から、",
//			"初秋の候、","中秋の候、","錦秋の候、","寒露の候、","黄葉の候、","秋雨の候、","金風の候、","秋晴れの候、","菊薫る候、","秋たけなわの候、","紅葉の季節、","秋冷の心地よい季節、",
//			"初秋の候、","錦秋の候、","寒露の候、","黄葉の候、","秋雨の候、","金風の候、","秋晴れの候、","菊薫る候、","秋たけなわの候、","紅葉の季節、","秋冷の心地よい季節、",
//			"晩秋の候、","暮秋の候、","向寒の候、","深冷の候、","菊花の候、","紅葉の候、","初霜の候、","氷雨の候、","枯れ葉舞う季節、",
//			"寒冷の候、","師走の候、","初冬の候、","寒気の候、","霜気の候、","心せわしい年の暮れ、","歳末ご多忙の折、","歳晩の候、","季冬の候、","霜寒の候、"},
//			
//			{"貴社ますますご盛栄のこととお慶び申し上げます。", 
//			"貴社ますますご清祥のこととお慶び申し上げます。",
//			"貴社いよいよご清栄のこととお慶び申し上げます。",
//				"貴社いよいよご清祥のこととお慶び申し上げます。",
//				"貴社ますますご繁栄のこととお慶び申し上げます。",
//				"貴社いよいよご隆盛のこととお慶び申し上げます。",
//				"貴社ますます御隆昌にてお慶び申し上げます。",
//				"貴店ますますご発展のこととお慶び申し上げます。",
//				"貴行ますますご清栄のことお慶び申し上げます。",
//				"ますます御健勝のこととお慶び申し上げます。",
//				"時下ますますご清祥の段、お慶び申し上げます。",
//			},
//			
//			{"平素は格別のご高配を賜り、厚く御礼申し上げます。",
//			"日頃は大変お世話になっております。",
//	     		"平素は格別のお引き立てをいただき、厚く御礼申し上げます。",
//				"平素は格別のお引き立てを賜り、ありがたく厚く御礼申し上げます。",
//				"平素は当店を御利用いただき御厚情のほど、心より御礼申し上げます。",
//				"毎々格別のご愛顧鵜を賜り、厚く御礼申し上げます。",
//				"平素はひとかたならぬ御愛顧を賜り、厚く御礼申し上げます。",
//				"平素はひとかたならぬ御愛顧を賜り、ありがとうございます。",
//				"平素は格別のご厚誼にあずかり、厚く御礼申し上げます。",
//				"日頃は格別のお引き立てをいただき、ありがたく御礼申し上げます。",
//				"毎度格別のお引き立てを賜り、厚く御礼申し上げます。",
//			},
			
	       { sb.getString( 3 ) },
			
			{ sb.getString( 5 ) },
			
			{ sb.getString( 7 ) }
	};

	public Inserter(GetResult calledForm) {
		//結果の格納先を設定
		this.calledForm = calledForm;

		getContentPane().setLayout(new MigLayout("", "[200,grow][200,grow][400,grow]", "[grow][25]"));

		/* 1列目 */
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 0,grow");

		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[0][i]);
		}
		firstSyntax = new JList<String>(model);
		firstSyntax.setSelectedIndex(0);
		scrollPane.setViewportView(firstSyntax);


		/* 2列目 */
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 1 0,grow");

		model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[1][i]);
		}
		secondSyntax = new JList<String>(model);
		secondSyntax.setSelectedIndex(0);
		scrollPane_1.setViewportView(secondSyntax);


		/* 3列目 */
		JScrollPane scrollPane_2 = new JScrollPane();
		getContentPane().add(scrollPane_2, "cell 2 0, grow, wrap");

		model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[2][i]);
		}
		thirdSyntax = new JList<String>(model);
		thirdSyntax.setSelectedIndex(0);
		scrollPane_2.setViewportView(thirdSyntax);


		/* 挿入ボタン */
		JButton button = new JButton("挿入");
		button.addActionListener(this);
		getContentPane().add(button, "span 3, grow");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StringBuilder template = new StringBuilder();
		template.append(firstSyntax.getSelectedValue());
		template.append(secondSyntax.getSelectedValue());
		template.append(thirdSyntax.getSelectedValue());

		calledForm.setResult(template.toString(), 0);
		this.dispose();
	}

}
