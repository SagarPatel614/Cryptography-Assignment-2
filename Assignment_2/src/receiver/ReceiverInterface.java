package receiver;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import connection.Connection;


public class ReceiverInterface {
	
	static RC4 rc4;
	static MainCode main;
	static Connection con = new Connection();
	
	public static void main(String arg[]) throws Exception {
		
		String key;
		byte[] keyword = null;
		
		
		
		//Request key from user
		System.out.print("\nEnter key: ");
		Scanner scanKey = new Scanner(System.in);
		key = scanKey.next();				//get users key
		
		//Close the scanner objects
		scanKey.close();
		
		try {
			keyword = key.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//initialize RC4
		main = new MainCode(keyword);
		
		con.read();
		//print decrypted message
		main.printMessage();	
	}
}