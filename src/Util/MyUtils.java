package Util;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class MyUtils {
	//インスタンスの生成阻止
	private MyUtils(){}

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

	/* Component#.setNameであらかじめ設定した名前を取得 */
	public static String getName(MouseEvent e){
		return ((JLabel)e.getComponent()).getName();
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

	/* ArrayList<JTextArea>のインスタンスから内容のString配列を生成 */
	public static String[] toStringArray(ArrayList<JTextArea> array){
		String[] retArray = new String[array.size()];
		for(int i=0; i<array.size(); i++){
			retArray[i] = array.get(i).getText();
		}
		return retArray;
	}
}
