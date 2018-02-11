//Hoang, Justin
//jah7399
//EE422C-Assignment 2
package assignment2;

import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
							
		boolean testing;
		if (args[0].charAt(0) == '1') {
			testing = true;
		} else {
			testing = false;
		} 
		
		// Display greeting
		Greeting greeting = new Greeting();
		greeting.DisplayGreeting();
					
        // Declare a Scanner object and initialize with
        // predefined standard input object
        Scanner console = new Scanner(System.in);
        
		// ask player want to play or not?
		System.out.print("Are you ready to play? (Y/N): ");
		String input = console.nextLine();     	
		String answer = input.trim();
		
		if (answer.equals("Y") || answer.equals("y"))
		{        
			Game masterMind = new Game(console, testing);
			masterMind.runGame();
		}

		System.out.println("\nGoodbye...");		
		System.exit(0);	
	}	
}
