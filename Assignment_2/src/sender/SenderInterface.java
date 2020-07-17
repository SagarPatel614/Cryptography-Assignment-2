package sender;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import connection.Connection;


public class SenderInterface {
	
	static RC4 rc4;
	static MainCode main;
	static Connection con = new Connection();
	
	public static void main(String arg[]) throws Exception {
		
		String key, msg;
		String choice;
		byte[] keyword = null;
		byte[] plaintext = null;
		
		
		
		//Request key from user
		System.out.print("\nEnter key: ");
		Scanner scanKey = new Scanner(System.in);
		key = scanKey.next();				//get users key				
				
		//Request message from user
		System.out.print("\nEnter message (for test 1000-byte only): ");
		Scanner scanMsg = new Scanner(System.in);
		msg = scanMsg.nextLine();				//get users message
	
		//Request case from user
		System.out.print("\nTest Cases:\n1. Case 1 - order is 0,1,2,3\n2. Case 2 - order is 1,0,3,2\n3. Case 3 - order is 3,2,1,0");
		System.out.print("\nEnter choice (1/2/3): ");
		Scanner scanCase = new Scanner(System.in);
		choice = scanCase.next();				//get users choice
		
		//Close the scanner objects
		scanKey.close();
		scanMsg.close();
		scanCase.close();
		
		try {
			keyword = key.getBytes("UTF-8");
			plaintext = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		main = new MainCode(keyword);
		main.divideMessage(plaintext);
		main.calculateHash();
		main.encrypt();
		
		switch(choice) {
			case "1":
				int[] sequence = {0,1,2,3};
				System.out.print("\nSending Message... ");
				main.sendMessage(sequence);
				break;
			case "2":
				int[] sequence1 = {1,0,3,2};
				System.out.print("\nSending Message... ");
				main.sendMessage(sequence1);
				break;
			case "3":
				int[] sequence2 = {3,2,1,0};
				System.out.print("\nSending Message... ");
				main.sendMessage(sequence2);
				break;
			default:
		}
		System.out.print("\n\nMessage Sent!! ");		
	
	}
}
