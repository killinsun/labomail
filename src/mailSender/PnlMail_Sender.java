package mailSender;


import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.activation.FileDataSource;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

/************** 項目 ****************/
/*	メンバ変数						*/
/*	リスナー						*/
/*	メール送信操作					*/
/*	テキストコンポーネントの処理	*/
/************ 外部機能 **************/
/*	定型句挿入機能					*/
/*		: AutoInsert.java			*/
/*********** 既知の問題 *************/
/*	・なぜかバイナリファイルが		*/
/*		添付できない				*/
/* 	（おそらくエンコード問題）		*/
/*	・なぜかテキストコンポを		*/
/*	フォーカスしてから添付すると、	*/
/*	添付ファイル名が表示されない	*/
/************************************/


public class PnlMail_Sender extends JPanel implements Runnable, GetResult<PnlMail_Sender>, MouseListener {

	/************ メンバ変数 ************/

//	private JTextArea txtTo;
	private ArrayList<JTextArea> ccList;
	private JPanel pnlCC;
//	private JTextArea txtCC;
	private ArrayList<JTextArea> bccList;
	private JPanel pnlBCC;
//	private JTextArea txtBCC;
	private JTextArea txtSubject;
	private JPanel pnlAttach;
	private JScrollPane scrollPane;
	private UndoTextPane txtDetail;
	private JProgressBar progressBar;

	//インナークラスからのアクセス用(thisの退避)
	private JPanel pnlSender;

	//添付ファイル
	private ArrayList<FileDataSource> attachFileList;

	//設定から取得
	private String smtpServer;
	private String myMailAddr;
	private String myName;

	/************************************/


	public PnlMail_Sender() {
		/* 設定から取得、値を設定 */
		smtpServer = "172.16.19.213";//スタブ
		myMailAddr = "mailtest@yotu.centos.jp";
		myName = "MailTest";

		/* 初期値設定 */
		ccList = new ArrayList<JTextArea>();
		bccList = new ArrayList<JTextArea>();
		attachFileList = new ArrayList<FileDataSource>(){
			//添付ファイル無しのメール用対策
			@Override
			public FileDataSource get(int index) {
				try{
					return super.get(index);
				} catch(IndexOutOfBoundsException e) {
					return null;
				}
			}
		};
		pnlSender = this;


		/* コンポーネント設定 */

		/* タブ生成 */
		this.setLayout(new MigLayout("", "[][][][][]", "[][][][][][][grow]"));

		for(int i=0; i<19; i++) this.add(new JLabel("　　"), "");
		this.add(new JLabel("　　"), "wrap");


		/* あて先 */
		JLabel lblTo = new JLabel("　宛先　　");
		lblTo.setFont(new Font("MS UI Gothic", Font.PLAIN, 14));
		this.add(lblTo, "");


		JTextArea txtTo = new JTextArea();
		txtTo.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		ccList.add(txtTo);
		this.add(txtTo, "span 12, grow, wrap");


		/* 各欄 */
		pnlCC = new JPanel();
		pnlCC.setLayout(new BoxLayout(pnlCC, BoxLayout.Y_AXIS));

		this.add(new JLabel("　Cc:　　"), "");
		JTextArea txtCC = new JTextArea();
		txtCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		ccList.add(txtCC);
		pnlCC.add(txtCC);
		this.add(pnlCC, "span 12, grow");

		//CC用「＋」アイコン設定
		JLabel lblPlusCC = new JLabel("（ ＋ ）CC");
		lblPlusCC.addMouseListener(this);
		this.add(lblPlusCC, "wrap");


		pnlBCC = new JPanel();
		pnlBCC.setLayout(new BoxLayout(pnlBCC, BoxLayout.Y_AXIS));

		this.add(new JLabel("　Bcc:　　"), "");
		JTextArea txtBCC = new JTextArea();
		txtBCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		bccList.add(txtBCC);
		pnlBCC.add(txtBCC);
		this.add(pnlBCC, "span 12, grow");

		//BCC用「＋」アイコン設定
		JLabel lblPlusBCC = new JLabel("（ ＋ ）BCC");
		lblPlusBCC.addMouseListener(this);
		this.add(lblPlusBCC, "wrap");

		this.add(new JLabel("　件名　　"), "");
		txtSubject = new JTextArea();
		txtSubject.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		this.add(txtSubject, "span 10, grow, wrap");

		//添付したファイルを表示するパネル
		pnlAttach = new JPanel();
		pnlAttach.setLayout(new BoxLayout(pnlAttach, BoxLayout.Y_AXIS));
		this.add(pnlAttach, "span 10, grow, wrap");

		//プログレスバーの設置
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		this.add(progressBar, "cell 0 5, span 3, grow");


		/*「ファイル添付」アイコン、「定型文」アイコン設定 */
		JLabel attachment = new JLabel("（添付）");
		attachment.addMouseListener(this);
		this.add(attachment, "cell 16 5, span 1, grow");

		//余白
		this.add(new JLabel(""), "cell 16 5, span 1, grow");

		JLabel lblTemplate = new JLabel("（定型文）");
		lblTemplate.addMouseListener(this);
		this.add(lblTemplate, "span 1, grow, wrap");


		/* 「内容」テキストエリアを設定 */
		scrollPane = new JScrollPane();
		this.add(scrollPane, "cell 0 6 18 2, height 500, grow, wrap");

		txtDetail = new UndoTextPane();
		txtDetail.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		AutoInsert inserter = new AutoInsert(txtDetail);
		txtDetail.addKeyListener(inserter);
		txtDetail.addCaretListener(inserter);
		scrollPane.setViewportView(txtDetail);


		//「送信」アイコン、「一時保存」アイコン設定
		JLabel lblsubmit = new JLabel("（送信）");
		lblsubmit.addMouseListener(this);
		this.add(lblsubmit, "cell 16 8, span 1, grow");
		this.add(new JLabel("（一時保存）"), "span 1, grow");

	}


	/************ リスナー ************/


	@Override
	public void mouseClicked(MouseEvent e) {
		switch (MyUtils.getLabel(e)) {

		case "（ ＋ ）CC":
			//結果を返してほしいメソッド
			startForResult(this);
			break;

		case "（ ＋ ）BCC":

			break;

		case "（添付）":
			//ファイルダイアログの表示
			File file = new File("");
			file = OpenFileDialog.fileOpen();
			if(file != null){
				//FileDataSourceを取得(添付ファイル用の型)
				this.attachFileList.add(new FileDataSource(file));

				//「xxxxxxB」という形から「xxxKB」という形に直す
				Object[] formattedFileSize = MyUtils.getAdequateByte(file.length());
				//テラバイトを超えるとメール的に厳しいので弾く
				if(formattedFileSize.equals("Over")){
					JOptionPane.showMessageDialog(null, "1024GBを超えるファイルは添付できません", "確認", JOptionPane.OK_OPTION);
					attachFileList.remove(attachFileList.size()-1);//テストしようがねえゾーン
					return;
				}

				//添付ファイル内容をパネルに設定
				String fileInfo = String.format("%s(%s%s)",
						file.getName(),	formattedFileSize[0], formattedFileSize[1]);
				pnlAttach.add(new JLabel("　　"+fileInfo+"　　"));
			}
			//後で添付したファイルを削除できるようにしよう、<ArrayList>.remove(選択インデックス)？
			break;

		case "（定型文）":
			//定型文挿入画面を表示
			new AutoInsert(txtDetail).startForResult(null);
			break;

		case "（送信）":
			//別スレッドでメール送信
			new Thread(this).start();
			break;

		default:
			//予期せぬケース
			throw new RuntimeException("This Click is ignore case");
		}
	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}


	/**********************************/


	/************ メール送信操作 ************/

	@Override
	public void run() {

		/* ユーザビリティを考慮した分岐 */

		if(ccList.get(0).getText().equals("")){
			//「宛先」が空なら中止
			JOptionPane.showMessageDialog(null, "送信先が空です", "警告", JOptionPane.ERROR_MESSAGE);
			return;
		}else if(txtSubject.getText().equals("")){
			//件名が空なら確認
			int confirm = JOptionPane.showConfirmDialog(null, "件名が空ですが、よろしいですか？", "確認", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION){
				return;
			}
		}else if(txtDetail.getText().equals("")){
			//内容が空なら確認
			int confirm = JOptionPane.showConfirmDialog(null, "内容が空ですが、よろしいですか？", "確認", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION){
				return;
			}
		}

		if(JOptionPane.showConfirmDialog(null, "送信しますか？", "確認", JOptionPane.YES_NO_OPTION) == 0){
			//送信していいか確認
			setAllEditable(false);
			{
				//プログレスバーを始動
				progressBar.setVisible(true);
				progressBar.setIndeterminate(true);

				//メール送信
				PlainSmtp_Helper sender = new PlainSmtp_Helper(smtpServer, myMailAddr, myName);
				System.out.println("send");
			}
		}

		//終了処理
		setAllEditable(true);
		progressBar.setIndeterminate(false);
		progressBar.setVisible(false);

		//全て正しく送信できた場合
		clearAllText();
		JOptionPane.showMessageDialog(null, "送信しました", "送信完了", JOptionPane.INFORMATION_MESSAGE);
	}




	/************ テキストコンポーネントの処理 ************/

	//全てのテキストコンポーネントを(無効化|有効化)
	private void setAllEditable(boolean flag){
		for(JTextArea txtCC : ccList){
			txtCC.setEditable(flag);
		}
		for(JTextArea txtBCC : bccList){
			txtBCC.setEditable(flag);
		}
		txtSubject.setEditable(flag);
		txtDetail.setEditable(flag);
	}

	//全てのコンポーネントの内容をクリア
	private void clearAllText(){
		for(JTextArea txtCC : ccList){
			txtCC.setText("");
		}
		for(JTextArea txtBCC : bccList){
			txtBCC.setText("");
		}
		txtSubject.setText("");
		txtDetail.setText("");
		pnlAttach.removeAll();
		attachFileList.clear();
	}


	/************ 画面の呼び出しと結果の受け取り ************/

	@Override
	public void startForResult(PnlMail_Sender receiveClass) {
		//アドレス文字列をこのクラスに設定するスタブ
		new StringArray_ReturnalClass(receiveClass);
	}


	//IDの初期値
	private int ccID = 100;
	private int bccID = 200;
	//既に追加した宛先アドレスのカウンタ
	private int toCount = 0;
	@Override
	public void setResult(Object retValue) {
		//配列にキャスト
		String[] to = (String[])retValue;

		//CC欄に受け取ったアドレスを追加
		for(int i=0; i<to.length; i++, toCount++){
			if(toCount > 99){
				//送信先が99を超えたら例外を出す
				throw new AddressLimitOverException("Send to Address is Over 99");
			}

			try{
				//既にある「宛先」欄、「CC」欄にアドレスを追加
				ccList.get(toCount).setText(to[i]);	//例外発生の可能性
				addCloseIcon(ccList.get(toCount));
				ccList.get(toCount).addMouseListener(new IconListener(ccID++));

			} catch(IndexOutOfBoundsException e) {

				//未元なCC欄を追加、その欄にアドレスを追加
				JTextArea txtCC = new JTextArea(to[i]);
				txtCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
				addCloseIcon(txtCC);
				txtCC.addMouseListener(new IconListener(ccID++));
				ccList.add(txtCC);
				pnlCC.add(txtCC);
			}
		}

		//結果を可視化
		this.validate();

	}

	/* ×アイコンを指定テキストエリアに上乗せ */
	private void addCloseIcon(JTextArea to){
		to.setLayout(new BoxLayout(to, BoxLayout.PAGE_AXIS));
		JLabel xIcon = getCloseIcon(ccID);
		xIcon.setAlignmentX(1.0f);	//アイコン位置を右揃えにする。
		to.add(xIcon);
	}

	/* アイコン取得 + 必要操作 */
	private JLabel getCloseIcon(int id){
		//アイコンを生成
		ImageIcon image = new ImageIcon("data/close.png");
		int w = image.getIconWidth();
		int h = image.getIconHeight();
		JLabel icon = new JLabel(image);
		icon.setBounds(1, 3, w, h);

		//アイコンナンバー識別子を送付
		icon.addMouseListener(new IconListener(id));

		return icon;
	}

	/* アイコンクリックによる宛先欄削除用 */
	class IconListener extends MouseAdapter{
		//識別用
		private int iconID;
		public IconListener(int id){
			this.iconID = id;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			//「宛先」であるか1番目のCC欄であるならば内容を消すだけにとどめる
			if(this.iconID==100 || this.iconID==101){
				if(this.iconID == 100){	//宛先欄の内容を削除
//					ccList.get(0).setText("");
//					ccList.get(0)..
				} else {	//1番目のCC欄の内容を削除
					ccList.get(1).setText("");
				}

			} else if(this.iconID >= 200) {
				//iconIDがbccIDであるならば
				System.out.println("BCC iconID:" + this.iconID);

			} else if(200>this.iconID && this.iconID>=0) {
				//iconIDがccIDであるならば
				pnlCC.remove(ccList.get(this.iconID-100));
				pnlSender.validate();

			} else {
				//不明なIDエラー
				throw new RuntimeException("Unknown Icon ID");
			}

		}
	}


	/************ 送信先アドレスが99を超えたときの例外 ************/

	class AddressLimitOverException extends RuntimeException {

		public AddressLimitOverException(String string) {
			super(string);
		}

	}


	/******************************************************/

}