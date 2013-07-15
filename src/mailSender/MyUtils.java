package mailSender;

import java.awt.event.MouseEvent;

public class MyUtils {
	//インスタンスの生成阻止
	private MyUtils(){}

	//イベントのソースからラベル名を割り出す
	public static String getLabel(MouseEvent e){
		String source = e.getSource().toString();
		source = source.substring(source.indexOf("text=")+5);
		String text = source.substring(0, source.indexOf(","));
		return text;
	}

	//ファイルサイズの書式最適化
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
