Cryptography Assignment #2

Documentation
1.	Introduction to code
The program works for two user interfaces: sender and receiver. 
a.	Sender
The sender is prompted to enter the secret key for the RC4 algorithm being implemented. After that the plaintext message is accepted from the user and is passed on for processing. Here, we have three-byte blocks: SC (4 byte), Data (252 byte), hash (16 byte). SC is the sequence counter for the data block, hash is the hash value of the SC and data block combined. The plaintext block is first divided into consecutive blocks each of the length 252 bytes. And each of them is stored in a 2D byte array, where SC points to the “row” and the data block respective to that SC is stored in that “row”. SC is incremented with each new block. For the hashing method we make use of the MD5 hashing algorithm which returns a 16 bytes hash from the combination of SC and its data block. The encryption method takes in all the three byte blocks and performs encryption only of the combination of data block and hash value, then stored them according to their SC value in a new 2D byte array (cipheredBlocks). At the end, the cipheredBlocks are one by one sent to the receiver. (in our case, they are stored in a temporary text file in a particular sequence which is selected by the user.

b.	Receiver
The receiver first enters the same key as given by the sender. Then it starts reading the message blocks from the temporary file, note: he does not know the particular order of the message in which they where stored. SO now, he extracts the SC and encrypted block from the file, then based on the SC value obtains the correct RC4 state (with use of PRGA and IPRGA), after obtaining the correct state he starts decrypting the encrypted block. After decryption he checks the hash value of the received block with the one he decrypted. If they match he sends them to storage. The storage function basically stored the data block in a 2D byte array on the position based on the SC obtained from the file. He updates his SC according to the one he obtained from the file and again starts reading the remaining portion of the file. After he has read the whole file, he then just prints out the 2D byte array storing the decrypted data after converting into string.

2.	External Comments
We have a single project file here, but it contains 4 packages:
a.	Main
It contains the main UI for the code which is run every time.
b.	Sender
It contains the sender side code for the application: RC4, MainCode, SenderInterface.
c.	Receiver
It contains the receiver side interfaces and code: RC4, MainCode, ReceiverInterface.
d.	Connection
It contains the file handling operations which in this application act as the connection between the sender and receiver.

3.	How to run the Code
Using Eclipse
You should have an eclipse IDE with java development plugins installed on your PC.
	Open the Eclipse IDE and navigate to workspace
	Select File -> Import
	Expand General, select Existing projects into Workspace and then Next
	Click the Select archive file radio button and browse for the zip file containing the project.
	Click Finish and the project should appear in your workspace. Note: Eclipse will not import a project if you already have one with the same name. 
	Now, from the Package Explorer window, navigate to Project Name -> src -> main then, open MainUI.java.
	Then you can simply select Run -> Run as Java Project.
	The result will be shown in the Console window.
	First select the sender option, then re-run the MainUI.java file and select receiver option.

