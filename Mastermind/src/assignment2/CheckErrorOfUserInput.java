//Hoang, Justin
//jah7399
//EE422C-Assignment 2

package assignment2;

import java.util.*;

public class CheckErrorOfUserInput {

	// List of valid color codes
	List<Character> gameColorList = new ArrayList<Character>();
	
	// Constructor
	public CheckErrorOfUserInput()
	{
		// List only needs created one time
		for (int i = 0; i < GameConfiguration.colors.length; i++)
		{
			gameColorList.add(GameConfiguration.colors[i].charAt(0));
		}		
	}
	
	// Method returns true if user guess is valid
	public boolean userInputValid(String userInput)
	{
		// convert array of valid color to list
		//List<String> gameColorList = Arrays.asList(GameConfiguration.colors);
		//List<Character> gameColorList = new ArrayList<Character>();
		
		//for (int i = 0; i < GameConfiguration.colors.length; i++)
		//{
		//	gameColorList.add(GameConfiguration.colors[i].charAt(0));
		//}
		
		// If length is not the same as predefined peg number
		if (userInput.length() != GameConfiguration.pegNumber)
		{
			return false;
		}
				
		for (int i = 0; i < GameConfiguration.pegNumber; i++) 
		{		
			
			// If list of valid colors doesn't contain user input
			if (!gameColorList.contains(userInput.charAt(i)))
			{
				return false;
			}
		}
		
		
		return true;
	}	
}	
