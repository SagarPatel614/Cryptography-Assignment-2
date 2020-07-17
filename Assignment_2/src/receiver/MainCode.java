package receiver;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import connection.Connection;


public class MainCode {
	
	static RC4 rc4;
	Connection con = new Connection();
	
	private static byte[] SC = {48,48,48,48};
	private static byte[][] msgBlocks;
	private static int blocks = 0;
	
	public MainCode() {
		
	}
	
	public MainCode(byte[] keyword) {
		rc4 = new RC4(keyword);
	}
	
	//Method to print whole message
	public void printMessage() {
		byte[] message = new byte[1000];
		
		for(int i = 0; i < blocks; i++) {
			System.arraycopy(msgBlocks[i], 0, message, i*msgBlocks[i].length, msgBlocks[i].length);
		}
		String msg = new String(message, StandardCharsets.UTF_8);
		System.out.println("The message received is: \n" + msg);
	}
	
	//Method to store message blocks
	public static void storeMessage(final byte[] msgBlock, final byte[] counter) {
		//remove padding if any
		byte[] finalBlock = removePadding(msgBlock);
		blocks++;
		//copy message blocks in an array according to their counter value
		System.arraycopy(finalBlock, 0, msgBlocks[counter[3]-48], 0, finalBlock.length);
	}
	
	//Method to remove padding
	protected static byte[] removePadding(byte[] message) {
		int length = message.length - 1;
		
		while (length >= 0 && message[length] == 0)
	    {
	        --length;
	    }
		byte[] originalBlock = null;
		System.arraycopy(message, 0, originalBlock, 0, length+1);

		//return unpadded block
		return originalBlock;
	}
	
	//Method to check the hash value
	public static void checkHash(byte[] senderSC, byte[] dataBlock, byte[] receivedHash) throws NoSuchAlgorithmException, IOException {
		
		//local variable for new hash block and hash
		byte[] hashBlock = new byte[256];
		byte[] newHash;
	    
		//combining SC and Data block
		System.arraycopy(senderSC, 0, hashBlock, 0, senderSC.length);
		System.arraycopy(dataBlock, 0, hashBlock, senderSC.length, dataBlock.length);
	    
	    //calculate hash value of the hash block
		MessageDigest md = MessageDigest.getInstance("MD5");
		newHash = md.digest(hashBlock);
		
		if(newHash.equals(receivedHash)) {
			//send answer to sender
			//con.correctMsg(true);
			// but here we assume it matches always
			storeMessage(dataBlock, senderSC);
		} else {
			//send message if it does not match
			//con.correctMsg(false);
		}
	}
	
	//Method to decrypt the data blocks and hash values
	@SuppressWarnings("null")
	public static void decrypt(byte[] senderSC, byte[] cipherText) throws NoSuchAlgorithmException, IOException {
		byte[] decryptedBlock = new byte[268];
		byte[] dataBlock = new byte[252], hashBlock = new byte[16];
		
		//check the SC
		SC = rc4.calculateState(senderSC, SC);
		//decrypt whole block
		decryptedBlock = rc4.decrypt(cipherText);
		
		//separate plain text data
		System.arraycopy(decryptedBlock, 0, dataBlock, 0, 252);
		//separate hash block
		System.arraycopy(decryptedBlock, dataBlock.length, hashBlock, 0, hashBlock.length);
		
		//check hash value
		checkHash(senderSC, dataBlock, hashBlock);
		
	}
	
	//Method to get SC and Ciphered Blocks
	@SuppressWarnings("null")
	public static boolean getMessage(byte[] message) throws IOException {
		byte[] receivedBlock = message;
		byte[] senderSC = new byte[4];
		byte[] cipherMsg = new byte[268];
		//receivedBlock = con.receiveMessage();
		System.out.println("Message block recieved.");
		//separate SC
		System.arraycopy(receivedBlock, 0, senderSC, 0, 4);
		//separate cipher block
		System.arraycopy(receivedBlock, senderSC.length, cipherMsg, 0, cipherMsg.length);
		
		try {
			decrypt(senderSC, cipherMsg);
			return true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
		
	}

}
