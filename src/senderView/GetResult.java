package senderView;


public interface GetResult {

	/*
	 * setResultメソッドが呼び出されることを明示し(強制はされていません)、
	 * ここから画面を呼び出します。
	 * 自クラスに値として結果を戻す必要がないならばnullを指定してください。
	 *
	 * また第２引数はsetResultでの識別ナンバーとして使用してください。
	 * 分岐がない場合は使用しないでかまいません。
	 */
	void startForResult(GetResult receiveClass, int callNumber);


	//呼び出し先から結果を受けるメソッド
	void setResult(Object retValue, int callNumber);

}

/*
 * このインターフェースはAndroidの
 * 「startActivityForResultメソッド」を
 * 参考にしています。
 */