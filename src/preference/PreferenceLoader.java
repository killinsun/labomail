package preference;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PreferenceLoader {

	//インスタンス生成の禁止
	private PreferenceLoader(){}

	/**
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	/* 設定内容をXMLから取得 */
	public static String[] getPreferences() throws ParserConfigurationException, SAXException, IOException{
		//固定長の配列を確保
		String[] prefs;

		/* XMLを読み込むためのDOMインスタンス */
		File xmlReader = new File("prefs.xml");

		//内容が存在するか確認
		if(xmlReader.exists()){
			prefs = new String[6];
		}else{
			return null;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlReader);

		/* 内容取得開始 */
		NodeList root = doc.getChildNodes();
		for(int i=0; i<root.getLength(); i++){
			Node child = root.item(i);
			NodeList paramNode = child.getChildNodes();
			for(int j=0; j<paramNode.getLength(); j++){
				Node value = paramNode.item(j);
				prefs[j] = value.getFirstChild().getNodeValue();
			}
		}

		//設定内容が順に入った配列を返す
		return prefs;
	}

	//テストメソッド、試してみてください。
	public static void main(String[] args){
		String[] prefs = null;
		try {
			prefs = getPreferences();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		for(String view : prefs){
			System.out.println(view);
		}
	}
}
