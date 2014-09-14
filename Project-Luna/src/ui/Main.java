package ui;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		
		// User enter his name
		System.out.print("Enter name:");
		Scanner sc = new Scanner(System.in);
		// gets the line entered by the user
		String name = sc.next();
		// output welcome message
		System.out.println("Hallo, " + name + "!");
		// close the input stream
		sc.close();
	
		//AND WE ARE NOT DONE
		//PUSH!
	}
	
}
