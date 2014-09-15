package ui;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		//Greet the user:
		printStars(20);
		sayHello();
		printStars(20);
	}
	
	private static void sayHello()	{

		System.out.println("Hello!\n");
		// User enter his name
		System.out.print("Enter name: ");
		Scanner sc = new Scanner(System.in);
		// gets the line entered by the user
		String name = sc.next();
		// output welcome message
		System.out.println("Hallo, " + name + "!");
		// close the input stream
		sc.close();
	
	}

	/**
	 * Prints a line of stars.
	 * @param stars Nr. of stars to print 
	 */
	private static void printStars(int stars){
		System.out.println();
		for (int i = 0; i < stars ; i++) {
			System.out.print("*");
		}
		System.out.println();
	}
	
}
