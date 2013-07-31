package senderView;

import java.util.ArrayList;

import javax.activation.FileDataSource;

import Util.UndoTextField;

public class UtilsForThisPackage {

	//インスタンス生成の禁止
	private UtilsForThisPackage(){};

	/* ArrayList<JTextArea>のインスタンスから内容のString配列を生成 */
	public static String[] toStringArray(ArrayList<UndoTextField> array){
		String[] retArray = new String[array.size()];
		for(int i=0; i<array.size(); i++){
			retArray[i] = array.get(i).getText();
		}
		return retArray;
	}

	/* ArrayList<JTextArea>のインスタンスから内容のString配列を生成 */
	public static String[] toStringArraySqueezeNull(ArrayList<UndoTextField> array){
		/* nullでない個数を数える */
		int notEmpty = 0;
		ArrayList<UndoTextField> tmp = new ArrayList<UndoTextField>();
		for(UndoTextField test : array){
			if(test != null){
				tmp.add(test);
				notEmpty++;
			}
		}

		/* nullでない要素を配列に移す */
		String[] retArray = new String[notEmpty];
		for(int i=0; i<notEmpty; i++){
			retArray[i] = tmp.get(i).getText();
		}

		//握りつぶせてるといいな
		return retArray;
	}

	/* 添付ファイルの穴抜けを握りつぶせ！！！ */
	public static ArrayList<FileDataSource> squeezeNull(ArrayList<FileDataSource> files){
		ArrayList<FileDataSource> notIncludeNull = new ArrayList<FileDataSource>();
		for(FileDataSource test : files){
			if(test != null){
				notIncludeNull.add(test);
			}
		}
		return notIncludeNull;
	}

	/* 文字列型以外のArrayListを文字列配列に変換 */
	public static String[] listToArray(ArrayList<?> list){
		String[] array = new String[list.size()];
		for(int i=0; i<list.size(); i++){
			array[i] = list.get(i).toString();
		}
		return array;
	}
}
