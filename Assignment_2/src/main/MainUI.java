package main;

import java.util.Scanner;

import receiver.ReceiverInterface;
import sender.SenderInterface;

public class MainUI {

	public static void main(String[] args) {
		System.out.println("Which interface do you want? Sender (S)/ Receiver(R)");
		
		System.out.println("Enter choice (S/R): ");
		Scanner sc = new Scanner(System.in);
		String choice = sc.nextLine();
		
		switch(choice){
		case "S":
			try {
				SenderInterface.main(args);
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			break;
			
		case "R":
			try {
				ReceiverInterface.main(args);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			break;
			
		default:
		}

	}

}
