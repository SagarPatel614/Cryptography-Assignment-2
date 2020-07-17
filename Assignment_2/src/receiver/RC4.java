package receiver;

public class RC4 {
	
	//private variables
	private final byte[] S = new byte[256];
    private final byte[] T = new byte[256];
    private final int keyLength;
    private int l = 0, m = 0;

    //constructor (KSA) for the class object
    public RC4(final byte[] key) {
    	
    	//check if the key matches the requirements
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
        	
        	//start initialization of S array
            keyLength = key.length;
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                T[i] = key[i % keyLength];
            }
            int j = 0;
            byte tmp;
            
            //Generate loop for S array
            for (int i = 0; i < 256; i++) {
            	//obtain value of j 
                j = (j + S[i] + T[i]) & 0xFF;		//performing Bitwise AND to calculate (modulo 256)
                //swapping S[i] and S[j]
                tmp = S[j];
                S[j] = S[i];
                S[i] = tmp;
            }        	
        }
    }
 
    //PRGA logic for specific number of rounds (n)
    public void prga(int n) {
    	
    	//local variable with respect to current (S,i,j) state
    	int i = l, j = m;
    	int loopLimit = n * 268;
    	byte tmp;
    	
    	//the PRGA algorithm
    	for(int counter = 0; counter < loopLimit; counter++) {
    		i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
    	}
    	
    	//variables to pass value of required (S,i,j) state 
    	l = i;
    	m = j;
    }
    
    //IPRGA logic for specific number of rounds (n)
    public void iprga(int n) {
    	
    	//local variables
    	byte tmp;
    	int loopLimit = n * 268;
    	//take value of current (S,i,j) state
    	int i = l;
    	int j = m;
    	
    	//IPRGA algorithm
    	for(int counter = 0; counter < loopLimit; counter++) {
    		tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            j = (j - S[i]) & 0xFF;
            i = (i - 1) & 0xFF;
        }
    	
    	//variables to pass value of required (S,i,j) state 
    	l = i;
    	m = j;
    	
    } 
    
    public byte[] decrypt(final byte[] ciphertext) {
        final byte[] plaintext = new byte[ciphertext.length];
        int i = l, j = m, k, t;
        byte tmp;
        for (int counter = 0; counter < ciphertext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            plaintext[counter] = (byte) (ciphertext[counter] ^ k);
        }
        return plaintext;
    }
    
    public byte[] calculateState(final byte[] senderSC, final byte[] privateSC) {
    	int n = 0;
    	
    	n = senderSC[3] - privateSC[3];
    	if(n == 0) {
    		return privateSC;
    	} else if (n < 0) {
    		iprga(Math.abs(n));
    		return incrementCounter(senderSC);
    	} else if (n > 0) {
    		prga(n);
    		return incrementCounter(senderSC);
    	}
		return null;
    }
    
  //method to increment SC by 1
  	public byte[] incrementCounter(byte[] tempSC) {
  		byte[] increment = {0,0,0,1};
  		for(int i = 0; i < 4; i++) {
  			tempSC[i] = (byte) (tempSC[i] + increment[i]);
  		}
  		return tempSC;
  	}

}