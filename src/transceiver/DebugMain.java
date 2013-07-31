package transceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugMain {

	public static void main(String[] args) {
		
		String text = "<html hoge> \nl;adjfd \n</html>";
		String regex = "<html.*>.*</html>";
		System.out.println("text ======");
		System.out.println(text);
		System.out.println("regex =====");
		System.out.println(regex);
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE |  Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		
		System.out.println("result ====");
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}
}
