package mailSender;


//違和感があっても、実際の流れを汲み取っているためです。
public class StringArray_ReturnalClass {
	private PnlMail_Sender receiveClass;

	public StringArray_ReturnalClass(PnlMail_Sender receiveClass){
		this.receiveClass = receiveClass;
		set();
	}

	public void set(){
		receiveClass.setResult(new String[]{"udonge.like@gmail.com", "x12j007@chiba-fjb.ac.jp"});
	}
}
