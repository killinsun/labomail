package Util;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;


/************** 項目 ****************/
/*	コンポーネント情報取得系		*/
/*	文字列操作系					*/
/************************************/


public class MyUtils {

	//インスタンスの生成阻止
	private MyUtils(){}


	/************ コンポーネント情報取得系 ************/

	/* イベントのソースからテキスト属性(ラベル名)を割り出す */
	public static String getText(MouseEvent e){
		String source = e.getSource().toString();
		source = source.substring(source.indexOf("text=")+5);
		String text = source.substring(0, indexOf(source, ",", "]"));
		return text;
	}

	/* それっぽいSwingコンポーネントのソースからテキスト属性を割り出す */
	public static String getText(Object e){
		String source = e.toString();
		source = source.substring(source.indexOf("text=")+5);
		String text = source.substring(0, indexOf(source, ",", "]"));
		return text;
	}

	/* DefaultListModelのソースからテキストを割り出す */
	public static String getText(DefaultListModel e){
		String source = e.toString();
		source = source.substring(source.indexOf("text=")+5);
		String text = source.substring(0, indexOf(source, ",", "]")-1);
		return text;
	}

	/* Component#.setNameであらかじめ設定した名前を取得 */
	public static String getName(MouseEvent e){
		return ((JLabel)e.getComponent()).getName();
	}

	/* ArrayList<JTextArea>のインスタンスから内容のString配列を生成 */
	public static String[] toStringArray(ArrayList<UndoTextArea> array){
		String[] retArray = new String[array.size()];
		for(int i=0; i<array.size(); i++){
			retArray[i] = array.get(i).getText();
		}
		return retArray;
	}


	/************ 文字列操作系 ************/

	/* 複数の検索文字列を検索対象から検索し、一番初めに見つかった位置を返す */
	public static int indexOf(String target, String... search){
		int found = -1;
		for(int i=0; i<search.length; i++){
			int tmp = target.indexOf(search[i]);
			if(tmp > found){
				found = tmp;
			}
		}

		return found;
	}

	/* 文字列配列の内容を任意のデリミタで結合 */
	public static String joinStringArray(String[] array, char delimiter){
		StringBuilder joined = new StringBuilder();
		for(int i=0; i<array.length-1; i++){
			joined.append(array[i]+delimiter);
		}
		joined.append( array[array.length-1] + delimiter );
		return joined.toString();
	}

	/* ファイルサイズの書式最適化 */
	private static int count = 0;
	public static Object[] getAdequateByte(long fileSize){
		if(fileSize >= 1024 && count < 5){
			getAdequateByte(fileSize/1000);
			count++;
		}
		Object[] formatFileSize = new Object[2];
		formatFileSize[0] = fileSize / (1000^count);
		formatFileSize[1] = new String[]{"B", "KB", "MB", "GB", "Over"}[count];
		count = 0;
		return formatFileSize;
	}

}
