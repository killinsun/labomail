package senderView;

import java.sql.SQLException;

public class Test_GetAddressQuery {

	public static void main(String[] args) throws SQLException{
		int[][] interger = new int[3][2];
		int count = 0;
		for(int i=0; i<interger.length; i++){
			for(int j=0; j<interger[0].length; j++){
				System.out.println(i+", "+j+" : " + count);
			}
		}
		System.out.println(interger.length);
	}

}
