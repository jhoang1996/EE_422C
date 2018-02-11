//Hoang, Justin
//jah7399
//EE422C-Assignment 2
package assignment2;

import java.util.*;

public class Game {
		
	// Local variables
	Scanner console;
	boolean testMode;
	CheckErrorOfUserInput validate = new CheckErrorOfUserInput();
	
	// Constructor
	Game(Scanner sc, boolean testing){
		
		// Save values passed in to local variables
		console = sc;
		testMode = testing;
	}
		
	// Begin running Mastermind game
	public void runGame()
	{	
		String secret_code ="";     
		
		// Get number of guesses allowed
		int numGuesses = GameConfiguration.guessNumber;
					
		boolean newGame = true;
		boolean play = true;
		int blackPegs = 0;
		int whitePegs = 0;
		String feedback = " ";
		
		// History list for guesses and feedbacks		
		List<String> history = new ArrayList<String>();		
		List<Integer> blackPegHistory = new ArrayList<Integer>();
		List<Integer> whitePegHistory = new ArrayList<Integer>();
		
		while (play)
		{			
			// generate secret code
			if (newGame)
			{
				System.out.println("\nGenerating secret code ...");
				secret_code = SecretCodeGenerator.getInstance().getNewSecretCode();	

				// if running in test mode
				if (testMode)
				{
					System.out.println("--> TEST MODE:  Secret code = " + secret_code);
				}				
			}
							
			String user_input = "";		
			boolean readyToCheck = false;
			
			// Loop until we have a valid guess input
			while(!readyToCheck)
			{
				System.out.println("");		
				System.out.println("You have " + numGuesses + " guesses left");
				System.out.println("What is your next guess?");
				System.out.println("Type in the characters for your guess and press enter.");	
				System.out.print("Enter guess: ");
				
				user_input = console.nextLine();					
				
				// If user entered history
				if (user_input.equals("HISTORY"))
				{
					displayHistory(history, blackPegHistory, whitePegHistory);
				}
				else 
				{
					// If user input is invalid
					if (!validate.userInputValid(user_input))
					{		
						System.out.println();
						System.out.println(user_input + " -> INVALID INPUT");
					
						// Clear old input before read new one
						user_input = "";
					}
					else
					{
						readyToCheck = true;
					}
				}
			}
	
			// used to store guess input
			char[] workingUserGuess = new char[GameConfiguration.pegNumber];		
					
			// Store user guess in working arrays
			for (int i = 0; i < GameConfiguration.pegNumber; i++)
			{
				workingUserGuess[i] = user_input.charAt(i);
			}	
				
			// Used to store secret code
			char[] workingSecretCode = new char[GameConfiguration.pegNumber];
			
			// Make a working array of secret codes to check this guess	
			for (int i = 0; i < GameConfiguration.pegNumber; i++)
			{
				workingSecretCode[i] = secret_code.charAt(i);
			}	
			
			// Reset the peg counts
			blackPegs = 0;
			whitePegs = 0;
			
			// Check for black pegs first
			for (int i = 0; i < GameConfiguration.pegNumber; i++)
			{
				// A match found
				if (workingUserGuess[i] == workingSecretCode[i])
				{
					// Scratch out so they won't be checked again
					workingSecretCode[i] = '-';
					workingUserGuess[i] = '-';
					
					blackPegs++;
				}						
			}			
			
			// Check for white pegs now
			for (int i = 0; i < GameConfiguration.pegNumber; i++)
			{
				if (workingUserGuess[i] == '-')
				{
					// skip scratched out guess
					continue;
				}
				
				for (int j = 0; j < GameConfiguration.pegNumber; j++)
				{
					if (workingUserGuess[i] == workingSecretCode[j])
					{
						workingSecretCode[j] = '-';
						workingUserGuess[i] = '-';						
						whitePegs++;
							
						// done once found a match
						break;
					}
				}
			}
			
			// Generate feedback
			if (blackPegs == 4)
			{
				feedback = user_input + " -> Result: " + blackPegs + " black pegs" + " - You win !!";
			}
			else
			{				
				if (blackPegs == 0 && whitePegs == 0)
				{
					feedback = user_input + " -> Result: No pegs";
				}
				else if (blackPegs == 1)
				{
					feedback = user_input + " -> Result: " + blackPegs + " black peg.  ";
					
					if (whitePegs == 1)
					{
						feedback = feedback + whitePegs + " white peg.";
					}
					else if (whitePegs > 1)
					{
						feedback = feedback + whitePegs + " white pegs.";
					}					
				}
				else if (blackPegs > 1)
				{
					feedback = user_input + " -> Result: " + blackPegs + " black pegs.  ";
					
					if (whitePegs == 1)
					{
						feedback = feedback + whitePegs + " white peg.";
					}
					else if (whitePegs > 1)
					{
						feedback = feedback + whitePegs + " white pegs.";
					}			
				}
				else if (blackPegs == 0)
				{		
					if (whitePegs == 1)
					{
						feedback = user_input + " -> Result: " + whitePegs + " white peg.  ";
					}
					else if (whitePegs > 1)
					{
						feedback = user_input + " -> Result: " + whitePegs + " white pegs.  ";
					}	
				}		
			}	
			
			history.add(user_input);
			blackPegHistory.add(blackPegs);
			whitePegHistory.add(whitePegs);
			
			// Display feedback for current guess
			System.out.println();				
			System.out.print(feedback);
			System.out.println();				
			
			// Deduct guess count
			--numGuesses;
			
			// Evaluate standing			
			if (blackPegs == 4)
			{
				newGame = true;
			}
			else if (numGuesses == 0)
			{
				System.out.println();
				System.out.println("Sorry, you are out of guesses. You lose, boo-hoo.");
				System.out.println("Secret code -> " + secret_code);
				
				// Start new game
				newGame = true;
			}
			else
			{
				newGame = false;
			}
			
			
			// Start new game
			if (newGame)
			{		
				// New game.  Reset everything
				numGuesses = GameConfiguration.guessNumber;
				blackPegs = 0;
				whitePegs = 0;
				history.clear();
				blackPegHistory.clear();
				whitePegHistory.clear();
				
				// ask player to play some more
				System.out.print("\nAre you ready for another game (Y/N): ");
				String input = console.nextLine();			
				char answer = input.charAt(0);
				
				play = false;
				if (answer == 'Y' || answer == 'y')
				{
					play = true;
				}
			}
					
		}
	}
	
		
	// Method to display guess history
	private void displayHistory(List<String> history, List<Integer> blackPegHistory, 
			List<Integer> whitePegHistory)
	{
		System.out.println();
		
		// Loop through history list
		for (int i = 0; i < history.size(); i++ )
		{
			System.out.println(history.get(i) + "\t\t" + 
				blackPegHistory.get(i) + "B_" + whitePegHistory.get(i) + "W");
		}
	}	
	
}
