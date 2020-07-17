package sender;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import connection.Connection;


public class MainCode {
	
	RC4 rc4;
	Connection con = new Connection();
	
	private byte[] SC = {48,48,48,48};
	private byte[][] hash = new byte[100][16];
	private byte[][] msgBlocks = new byte[100][252];
	private byte[][] cipherBlocks = new byte[100][268];
	
	
	private static int blocks = 0;
	
	public MainCode(byte[] keyword) {
		rc4 = new RC4(keyword);
	}
	
	//method to divide message into blocks
	public void divideMessage(byte[] plaintext) {
		int length = plaintext.length;
		
		if(length < 252) {
			//if the message length is less then 252
			//send for padding and create only one block
			msgBlocks[0] = addPadding(plaintext);
			
		} else if (length >= 252) {
			//if message block is larger then 252 divide into blocks
			for(int n = 1, j = 0; n <= (length/252); n++,j++) {
				blocks++;
				//System.arraycopy(plaintext, 252*j, msgBlocks[j], 0, 252*n);
				msgBlocks[j] = Arrays.copyOfRange(plaintext, 252*j, 252*n);
			}
			//if the message is not a complete multiple of 252 
			//Then send remaining message for padding
			if((length % 252) != 0) {
				byte[] remainingMsg = Arrays.copyOfRange(plaintext, blocks*252, length-1);
				//System.arraycopy(plaintext, 252*blocks, remainingMsg, 0, length);
				msgBlocks[++blocks] = addPadding(remainingMsg);
			}	
		}
		System.out.println("Blocks made.");
	}
	
	//method to increment SC by 1
	public void incrementCounter(byte[] tempSC) {
		byte[] increment = {0,0,0,1};
		for(int i = 0; i < 4; i++) {
			tempSC[i] = (byte) (tempSC[i] + increment[i]);
		}
	}
	
	//Method to add padding
	protected byte[] addPadding(byte[] message) {
		byte[] newMessage = new byte[252];
		int l = message.length;
		System.arraycopy(message, 0, newMessage, 0, l);
		newMessage[l] = 1;
		
		for(int length = l+1; length < 252; length++) {
			newMessage[length] = 0;
		}
		
		//return padded block
		return newMessage;
	}
	
	//Method to calculate the hash value of the message blocks
	public void calculateHash() throws NoSuchAlgorithmException {
		byte[] tempSC = {48,48,48,48};
		//combine SC and data block and produce the hash
		for (int i = 0; i < blocks; ++i)
		{
			//local variable for hash block
			byte[] hashBlock = new byte[256];
		    //combining SC and Data block
			System.arraycopy(tempSC, 0, hashBlock, 0, 4);
			System.arraycopy(msgBlocks[i], 0, hashBlock, 4, 252);
		    
		    //calculate hash value of the hash block
			MessageDigest md = MessageDigest.getInstance("MD5");
			hash[i] = md.digest(hashBlock);
			incrementCounter(tempSC);
		}
		System.out.println("Hash Calculated.");
	}
	
	//Method to encrypt the data blocks and hash values
	public void encrypt() {
		byte[] encryptionBlock = new byte[268];
		for(int i = 0; i < blocks; i++) {
			//add the msg Block
			System.arraycopy(msgBlocks[i], 0, encryptionBlock, 0, msgBlocks[i].length);
			//add the hash block
			System.arraycopy(hash[i], 0, encryptionBlock, msgBlocks[i].length, hash[i].length);
			//send for encryption
			cipherBlocks[i] = rc4.encrypt(encryptionBlock);
			//System.out.println(new String(cipherBlocks[i], StandardCharsets.UTF_16));
		}
		System.out.println("Encryption done.");
	}
	
	//Method to send SC and Ciphered Blocks
	public void sendMessage(int[] sequence) throws IOException {
		byte[] finalBlock = new byte[272];
		int n = 1;
		for(int x : sequence) {
			byte[] tempSC = SC;
			//calculate correct SC for the block from the sequence
			for(int y = 1; y <= x; y++) {
				incrementCounter(tempSC);
			}
			//add SC
			System.arraycopy(tempSC, 0, finalBlock, 0, tempSC.length);
			//add ciphered block
			System.arraycopy(cipherBlocks[x], 0, finalBlock, tempSC.length, cipherBlocks[x].length);
			//send the block
			//check if final
			
			con.write(finalBlock);
		}
	}

}
