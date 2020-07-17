package connection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import receiver.MainCode;

public class Connection {
	// The name of the file to open.
    String fileName = "temp.txt";
	
	public void Connection() {
		
	}
	
	public void write(byte[] message) {

	    try {
	        FileOutputStream os = new FileOutputStream(fileName);
	        //Writer fr = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);
	        //write the block in a new line
	        os.write(message);
	        
	        // Always close files.
	        os.close();
	    }
	    catch(IOException ex) {
	        System.out.println(
	            "Error writing to file '"
	            + fileName + "'");
	        // Or we could just do this:
	        // ex.printStackTrace();
	    }
	}
	
	public void read() {

        // This will reference one line at a time
        byte[] buffer = new byte[2000];

        try {
            FileInputStream is = new FileInputStream(fileName);

            int total = 0;
            int nRead = 0;
            while((nRead = is.read(buffer)) != -1) {
                // Convert to String so we can display it.
                // Of course you wouldn't want to do this with
                // a 'real' binary file.
            	byte[] message = new byte[272];
                is.read(message);
                MainCode.getMessage(message);
                total += nRead;
            }   


            // Always close files.
            is.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
}
