package ui;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) {
		System.out.println("Enter name:");
		Scanner sc = new Scanner(System.in);
		String name = sc.next();
		System.out.println("hello, " + name);
	}
}
