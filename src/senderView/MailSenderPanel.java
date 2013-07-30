package senderView;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.xml.parsers.ParserConfigurationException;

import net.miginfocom.swing.MigLayout;

import org.xml.sax.SAXException;

import preference.PreferenceLoader;
import Util.MyUtils;
import Util.UndoTextField;
import Util.UndoTextPane;
import dbHelper.DbHelper;

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
/* 	（おそらくエンコード問題？）	*/
/*	Gmailモジュール添付メソッドでの	*/
/*	不明な送り先エラー				*/
/************************************/


public class MailSenderPanel extends JPanel implements Runnable, GetResult, MouseListener, KeyListener {

	/************ メンバ変数 ************/

	//「ccList」は宛先欄を含む
	private ArrayList<UndoTextField> ccList;
	private JPanel pnlCC;
	private ArrayList<UndoTextField> bccList;
	private JPanel pnlBCC;
	private UndoTextField txtSubject;
	private JPanel pnlAttach;
	private UndoTextPane txtDetail;
	private JProgressBar progressBar;

	//定型文用リスナーインスタンス
	private AutoInsert inserter;

	//インナークラスからのアクセス用(thisの退避)
	private JPanel pnlSender;

	//添付ファイル
	private ArrayList<FileDataSource> attachFileList;

	//設定から取得
	private String myMailAddr;
	private String myPassword;
	private String smtpServer;
	private String smtpPort;

	/************************************/


	public MailSenderPanel() {

		/* 設定から取得、値を設定 */
		try {
			String[] prefs = PreferenceLoader.getPreferences();
			if(prefs == null){
				JOptionPane.showMessageDialog(null, "アカウント情報が登録されていません\n設定画面にて登録してください", "エラー", JOptionPane.ERROR_MESSAGE);
				//タブを閉じる
				return;
			}
			myMailAddr = prefs[0];
			myPassword = prefs[1];
			smtpServer = prefs[2];
			smtpPort = prefs[3];

		} catch (ParserConfigurationException | SAXException | IOException e1) {
			JOptionPane.showMessageDialog(null, "アカウント設定情報の取得に失敗しました\n設定をしていない場合は設定してください", "エラー", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}

		/* 初期値設定 */
		ccList = new ArrayList<UndoTextField>();
		bccList = new ArrayList<UndoTextField>();
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
		//インナークラス用に参照を退避
		pnlSender = this;


		/*** コンポーネント設定 ***/

		/* グリッドっぽい「あいまいレイアウト」生成 */
		this.setLayout(new MigLayout("", "[][][][][]", "[][][][][][][grow]"));

		for(int i=0; i<19; i++) this.add(new JLabel("　　"), "");
		this.add(new JLabel("　　"), "wrap");


		/*** 各欄 ***/

		{/*「宛先」欄、「CC」欄共同のパネルを生成 */

			//宛先欄、CC欄を含むパネル「pnlCC」を生成
			pnlCC = new JPanel();
			pnlCC.setLayout(new BoxLayout(pnlCC, BoxLayout.Y_AXIS));

			//宛先欄とCC欄
			JLabel lblTo = new JLabel("<html>　宛先　　<br><br>　ＣＣ　　");
			lblTo.setHorizontalAlignment(SwingConstants.LEFT);
			lblTo.setFont(new Font("ＭＳ Ｐゴシック", Font.BOLD, 15));
			this.add(lblTo, "top, center");

			//宛先テキストエリア
			UndoTextField txtTo = new UndoTextField();
			txtTo.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			ccList.add(txtTo);
			pnlCC.add(txtTo);
			/* 謎の参照失踪事件記念所
			 * this.add(txtTo, "span 12, grow");
			 */

			//CCテキストエリア
			UndoTextField txtCC = new UndoTextField();
			txtCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			ccList.add(txtCC);
			pnlCC.add(txtCC);
			this.add(pnlCC, "span 12, grow");

			//CC用(宛先用)アイコン設定
			JLabel lblPlusCC = new JLabel(new ImageIcon("data/senderIcon/Plus.png"));
			lblPlusCC.setName("addCC");
			lblPlusCC.addMouseListener(this);
			lblPlusCC.addMouseListener(new MouseOverListener());
			this.add(lblPlusCC, "top, wrap");

		}


		{/* 「BCC」欄のパネルを生成 */
			pnlBCC = new JPanel();
			pnlBCC.setLayout(new BoxLayout(pnlBCC, BoxLayout.Y_AXIS));

			JLabel label = new JLabel("　Ｂｃｃ　　");
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setFont(new Font("ＭＳ Ｐゴシック", Font.BOLD, 15));
			this.add(label, "");

			UndoTextField txtBCC = new UndoTextField();
			txtBCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			bccList.add(txtBCC);
			pnlBCC.add(txtBCC);
			this.add(pnlBCC, "span 12, grow");

			//BCC用「＋」アイコン設定
			JLabel lblPlusBCC = new JLabel(new ImageIcon("data/senderIcon/Plus.png"));
			lblPlusBCC.setName("addBCC");
			lblPlusBCC.addMouseListener(this);
			lblPlusBCC.addMouseListener(new MouseOverListener());
			this.add(lblPlusBCC, "top, wrap");


			JLabel label_1 = new JLabel("　件名　　");
			label_1.setHorizontalAlignment(SwingConstants.LEFT);
			label_1.setFont(new Font("ＭＳ Ｐゴシック", Font.BOLD, 15));
			this.add(label_1, "");
			txtSubject = new UndoTextField();
			txtSubject.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			this.add(txtSubject, "span 10, grow, wrap");
		}


		//添付したファイルを表示するパネル
		pnlAttach = new JPanel();
		pnlAttach.setLayout(new BoxLayout(pnlAttach, BoxLayout.Y_AXIS));
		this.add(pnlAttach, "cell 1 4 5 0, grow, wrap");

		//プログレスバーの設置
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		this.add(progressBar, "cell 0 5, span 3, grow");


		/*「ファイル添付」アイコン */
		JLabel attachment = new JLabel(new ImageIcon("data/senderIcon/Attach.png"));
		attachment.setName("attachment");
		attachment.addMouseListener(this);
		attachment.addMouseListener(new MouseOverListener());
		this.add(attachment, "cell 16 5, span 1, grow");

		/*「定型文」アイコン設定 */
		JLabel lblTemplate = new JLabel(new ImageIcon("data/senderIcon/Template.png"));
		lblTemplate.setName("template");
		lblTemplate.addMouseListener(this);
		lblTemplate.addMouseListener(new MouseOverListener());
		this.add(lblTemplate, "cell 18 5, span 1, grow, wrap");


		/*「内容」テキストエリアを設定 */
		JScrollPane scrollPane = new JScrollPane();
		txtDetail = new UndoTextPane();
		//定型文用リスナー
		inserter = new AutoInsert(txtDetail);
		txtDetail.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		txtDetail.addKeyListener(inserter);
		txtDetail.addKeyListener(this);
		txtDetail.addCaretListener(inserter);

		scrollPane.setViewportView(txtDetail);
		this.add(scrollPane, "cell 0 6 19 2, height 150, grow, wrap");


		//「送信」アイコン設定
		JLabel lblSubmit = new JLabel(new ImageIcon("data/senderIcon/Send.png"));
		lblSubmit.setName("send");
		lblSubmit.addMouseListener(this);
		lblSubmit.addMouseListener(new MouseOverListener());
		this.add(lblSubmit, "cell 18 8, span 1, grow");

	}

	/* 他画面からの遷移に使用 */
	public MailSenderPanel(String[] to, String[] bcc, String subject, String detail){
		//引数なしコンストラクタを呼び出し
		this();

		/* 引数の内容を設定 */
		addAddressToAreaAndList(to, "CC");
		addAddressToAreaAndList(bcc, "BCC");

		/* 引数の内容を設定 */
		txtSubject.setText(subject);
		txtDetail.setText(detail);

		/* 内容を可視化 */
		this.validate();
	}


	/************ リスナー ************/


	@Override
	public void mouseClicked(MouseEvent e) {
		switch (MyUtils.getName(e)){

		case "addCC":
			//結果を返してほしいメソッドをコールナンバー指定無しで呼び出し
			startForResult(this, 0x1);
			break;

		case "addBCC":
			//コールナンバー:0x2でsetResultへの結果を期待して呼び出し
			startForResult(this, 0x2);
			break;

		case "attachment":
			//ファイルダイアログの表示
			File file = new File("");
			file = MyUtils.fileOpen();
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
				JLabel lblFileInfo = new JLabel("　　"+fileInfo+"　　　");
				lblFileInfo.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
				addCloseIcon(lblFileInfo, attID);
				attID++;
				pnlAttach.add(lblFileInfo);

				//更新内容を表示
				this.validate();
			}
			//後で添付したファイルを削除できるようにしよう、<ArrayList>.remove(選択インデックス)？
			break;

		case "template":
			//定型文挿入画面を表示
			inserter.startForResult(null, 0);
			break;

		case "send":
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


	/* マウスカーソルが上に乗ったときに指に切り替える */
	class MouseOverListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			Component comp = (Component)e.getSource();
			comp.setCursor(cursor);
		}
	}

	/* UndoTextPane上でのタブ遷移を有効化 */
	@Override public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_TAB) {
			System.out.println(e.getModifiers());
			if(e.getModifiers() > 0){
				this.transferFocusBackward();
			}else{
				this.transferFocus();
			}
			e.consume();
		}
	}
	@Override public void keyReleased(KeyEvent e) {}

	/**********************************/


	/************ メール送信操作 ************/


	/* 引数としてMBOX識別番号を受け取る */
	private void pushDB(int mboxID) throws SQLException{
		//ArrayList<UndoTextArea>から内容のString[]に変換
		String[] ccArray = UtilsForThisPackage.toStringArraySqueezeNull(ccList);
		String[] bccArray = UtilsForThisPackage.toStringArraySqueezeNull(bccList);

		//データベースに挿入
		DbHelper helper = new DbHelper();
		String cc = MyUtils.joinStringArray(ccArray, ',');
		String bcc = MyUtils.joinStringArray(bccArray, ',');
		String formattedList = cc + "[BCC]" + bcc;
		System.out.println(formattedList);
		String txtMailName = txtSubject.getText().equals("") ? "NoName" : txtSubject.getText();

		String sql =
				String.format(
						"INSERT INTO" +
								" mastertbl(MBOXID, BOXID, MFROM, MTO, SUBJECT, DATA, DATE, PATH)" +
								" VALUES('%d', '1', '%s', '%s'," +
								" '%s', '%s', DATETIME('now','localtime'), 'data/send/%s.lbm');",
								mboxID, myMailAddr, formattedList, txtSubject.getText(), txtDetail.getText(),  txtMailName
						);
		helper.execute(sql);
		helper.close();
	}

	@Override
	public void run() {

		/* 宛先の有無を確認 */
		boolean exist = false;
		for(int i=0; i<ccList.size(); i++){
			if(!ccList.get(i).getText().equals("")){
				exist = true;
				break;
			}
		}

		/* ユーザビリティを考慮した分岐 */

		if(!exist){
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


		/* 送信操作 */

		//送信していいか確認
		if(JOptionPane.showConfirmDialog(null, "送信しますか？", "確認", JOptionPane.YES_NO_OPTION) == 0){
			//プログレスバーを始動、テキストエリアを編集不可化
			setWorkingMode(true);

			//メール送信
			Smtp_Interface sender = null;

			//設定情報の「メールサービス」を識別
			try {
				switch (PreferenceLoader.getPreferences()[6]) {
				case "Gmail":
					sender = new GmailSmtp_Helper(myMailAddr, myPassword);
					break;
				case "none":
					sender = new PlainSmtp_Helper(smtpServer, smtpPort, myMailAddr, myPassword);
					break;
				default:
					throw new RuntimeException("Unknown MailService in MailSenderPanel");
				}
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "メールサービスの識別に失敗しました\n設定では適切なサービスを選択してください", "エラー",  JOptionPane.ERROR_MESSAGE);
				setWorkingMode(false);
				return;
			}

			try {
				//ArrayList<UndoTextArea>から内容のString[]に変換
				String[] ccArray = UtilsForThisPackage.toStringArraySqueezeNull(ccList);
				String[] bccArray = UtilsForThisPackage.toStringArraySqueezeNull(bccList);

				//メール送信
				if(attachFileList.size() > 0){
					/* 添付ファイルがある場合 */
					ArrayList<FileDataSource> files = UtilsForThisPackage.squeezeNull(attachFileList);
					sender.sendMail(ccArray, bccArray, txtSubject.getText(), txtDetail.getText(), files);
				} else {
					/* 添付ファイルがない場合 */
					sender.sendMail(ccArray, bccArray, txtSubject.getText(), txtDetail.getText());
				}

				/* メール内容をDBに格納 */
				pushDB(2); //「送信」を表す「2」

			} catch (UnsupportedEncodingException | MessagingException | SQLException e) {
				/* 例外発生時 */
				setWorkingMode(false);
				clearAllText();
				e.printStackTrace();

				//現在のメール内容をデータベースに格納
				try{ pushDB(3); } catch(SQLException e1){ e1.printStackTrace(); }
				JOptionPane.showMessageDialog(null, "メールの送信にエラーが発生しました\nメール情報を未送信画面に保存しました", "送信に失敗しました", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//終了処理
			setWorkingMode(false);

			//全て正しく送信できた場合
			clearAllText();
			JOptionPane.showMessageDialog(null, "送信しました", "送信完了", JOptionPane.INFORMATION_MESSAGE);
		}

	}


	/************ テキストコンポーネントの処理 ************/

	//送信中の状態に遷移させる
	private void setWorkingMode(boolean flag){
		setAllEditable(!flag);
		progressBar.setIndeterminate(flag);
		progressBar.setVisible(flag);
	}

	//全てのテキストコンポーネントを(無効化|有効化)
	private void setAllEditable(boolean flag){
		for(UndoTextField txtCC : ccList){
			if(txtCC != null) txtCC.setEditable(flag);
		}
		for(UndoTextField txtBCC : bccList){
			if(txtBCC != null) txtBCC.setEditable(flag);
		}
		txtSubject.setEditable(flag);
		txtDetail.setEditable(flag);
	}

	//全てのコンポーネントの内容をクリア
	private void clearAllText(){
		for(UndoTextField txtCC : ccList){
			if(txtCC != null) txtCC.removeAll();
		}
		for(UndoTextField txtBCC : bccList){
			if(txtBCC != null) txtBCC.removeAll();
		}
		txtSubject.setText("");
		txtDetail.setText("");
		pnlAttach.removeAll();
		attachFileList.clear();
	}


	/************ 画面の呼び出しと結果の受け取り ************/

	@Override
	public void startForResult(GetResult receiveClass, int callNumber) {
		/* AddressBook.PaneAddressの継承クラスをフレームに追加 */
		JFrame address = new JFrame();
		address.setBounds(750, 100, 300, 500);
		AddressSelectPanel addressList = new AddressSelectPanel( (GetResult)this, callNumber, address );
		address.getContentPane().add(addressList);
		address.setVisible(true);
	}


	//IDの初期値 兼 インデックス
	private int attID = 0;
	private int ccID = 100;
	private int bccID = 200;
	//既に追加した全て(宛先,CC,BCC)の宛先アドレスのカウンタ
	private int toCount = 0;
	@Override
	public void setResult(Object retValue, int callNumber) {

		//コールナンバーでの分岐
		switch(callNumber){

		case 0x1:
			//配列にキャスト
			String[] toCC = (String[])retValue;

			//CC欄に受け取ったアドレスを追加
			addAddressToAreaAndList(toCC, "CC");

			//結果を可視化
			this.validate();

			break;

		case 0x2:
			//配列にキャスト
			String[] bcc = (String[])retValue;

			//BCC欄に受け取ったアドレスを追加
			addAddressToAreaAndList(bcc, "BCC");

			//結果を可視化
			this.validate();
			break;

		default:
			JOptionPane.showMessageDialog(null, "unknown callNumber for setResult", "Ramification Error", JOptionPane.OK_OPTION);
			return;
		}

	}

	private void addAddressToAreaAndList(String[] address, String mode){

		//CCもしくはBCCのテキストエリアとリストにアドレスを追加
		switch(mode){

		/* CCモード */
		case "CC":
			for(int i=0; i<address.length; i++, toCount++){
				if(toCount > 99){
					//送信先が99を超えたら例外を出す
					JOptionPane.showMessageDialog(null, "宛先が99件を超えています", "エラー", JOptionPane.OK_OPTION);
					throw new AddressLimitOverException("Send to Address is Over 99");
				}

				try{
					//既にある「宛先」欄、「CC」欄にアドレスを追加
					ccList.get(ccID-100).setText(address[i]);	//例外発生の可能性
					addCloseIcon(ccList.get(ccID-100), ccID);
					ccID++;

				} catch(IndexOutOfBoundsException e) {

					//未元なCC欄を追加、その欄にアドレスを追加
					UndoTextField txtCC = new UndoTextField(address[i]);
					txtCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
					addCloseIcon(txtCC, ccID);
					ccID++;
					ccList.add(txtCC);
					pnlCC.add(txtCC);
				}
			}
			//アドレスの追加を終了
			break;


			/* BCCモード */
		case "BCC":
			for(int i=0; i<address.length; i++, toCount++){
				if(toCount > 99){
					//送信先が99を超えたら例外を出す
					JOptionPane.showMessageDialog(null, "宛先が99件を超えています", "エラー", JOptionPane.OK_OPTION);
					throw new AddressLimitOverException("Send to Address is Over 99");
				}

				try{
					//既にある「BCC」欄にアドレスを追加
					bccList.get(bccID-200).setText(address[i]);	//例外発生の可能性
					addCloseIcon(bccList.get(bccID-200), bccID);
					bccID++;

				} catch(IndexOutOfBoundsException e) {

					//未元なCC欄を追加、その欄にアドレスを追加
					UndoTextField txtBCC = new UndoTextField(address[i]);
					txtBCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
					addCloseIcon(txtBCC, bccID);
					bccID++;
					bccList.add(txtBCC);
					pnlBCC.add(txtBCC);
				}
			}
			//アドレスの追加を終了
			break;

			//CC, BCC以外のモードを指定されたら
		default:
			throw new RuntimeException("undefined add mode");
		}
	}


	/* ×アイコンを指定テキストエリアに上乗せ */
	private void addCloseIcon(JComponent to, int id){
		to.setLayout(new BoxLayout(to, BoxLayout.PAGE_AXIS));
		JLabel xIcon = getCloseIcon(id);
		xIcon.addMouseListener(new MouseOverListener());
		xIcon.setAlignmentX(1.0f);	//アイコン位置を右揃えにする。
		to.add(xIcon);
	}

	/* アイコン取得 + 必要操作 */
	private JLabel getCloseIcon(int id){
		//アイコンを生成
		ImageIcon image = new ImageIcon("data/senderIcon/Close.png");
		int w = image.getIconWidth();
		int h = image.getIconHeight();
		JLabel icon = new JLabel(image);
		icon.setBounds(1, 3, w, h);

		//アイコンナンバー識別子を送付
		icon.addMouseListener(new IconListener(id));

		return icon;
	}

	/* アイコンクリックによる宛先欄削除用 */
	class IconListener extends MouseAdapter {
		//識別用
		private int iconID;
		public IconListener(int id){
			this.iconID = id;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(this.iconID >= 200) {
				//iconIDがbccIDであるならば
				pnlBCC.remove(bccList.get(this.iconID-200));
				bccList.set(this.iconID-200, null);

				//「BCC」欄の数が1を下回ったら再追加する（デフォルトの欄の分）
				if(pnlBCC.getComponents().length < 1){
					UndoTextField txtBCC = new UndoTextField();
					txtBCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
					bccList.add(txtBCC);
					pnlBCC.add(txtBCC);
				}

				//画面に反映
				pnlSender.validate();

			} else if(200>this.iconID && this.iconID>=100) {
				//iconIDがccIDであるならば
				pnlCC.remove(ccList.get(this.iconID-100));
				ccList.set(this.iconID-100, null);

				//「宛先」欄と「CC」欄の総数が2を下回ったら再追加する
				if(pnlCC.getComponents().length < 2){
					UndoTextField txtCC = new UndoTextField();
					txtCC.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
					ccList.add(txtCC);
					pnlCC.add(txtCC);
				}

				//画面に反映
				pnlSender.validate();

			} else if(this.iconID >= 0){
				/* iconIDが添付ファイル用であれば */
				//パネルとリストから添付ファイルを削除
				pnlAttach.getComponent(iconID).setVisible(false);
				attachFileList.set(iconID, null);
				//画面に反映
				pnlSender.validate();
			} else {
				//不明なIDエラー
				throw new RuntimeException("Unknown Icon ID");
			}
		}

	}


	/************ 送信先アドレスが99を超えたときの例外 ************/

	class AddressLimitOverException extends RuntimeException {

		public AddressLimitOverException(String message) {
			super(message);
		}

	}

	/******************************************************/

	public static void main(String[] args){
		JFrame f = new JFrame();
		f.add(new MailSenderPanel());
		f.setSize(800, 600);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}
