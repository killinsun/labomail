package mailSender;

// E => 実装側クラスの型
public interface GetResult<E> {

	/*
	 * setResultメソッドが呼び出されることを明示(!=強制)、ここから画面を呼び出す。
	 * 自クラスに値として結果を戻す必要がないならばnullを指定してください。
	 */
	void startForResult(E receiveClass);


	//呼び出し先から結果を受けるメソッド
	void setResult(Object retValue);

}