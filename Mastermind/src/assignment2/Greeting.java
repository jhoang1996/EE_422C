//Hoang, Justin
//jah7399
//EE422C-Assignment 2
package assignment2;

// Class to display greeting
public class Greeting {

	
	String greeting = 
		"Welcome to Mastermind.  Here are the rules.\n\n" +
		"This is a text version of the classic board game Mastermind.\n" +
		"The computer will think of a secret code. The code consists of 4 \n" + 
		"colored pegs. The pegs MUST be one of the six colors: blue, green, \n" +
		"orange, purple, red, or yellow. A color may appear more than once \n" +
		"in the code. You try to guess what colored pegs are in the code and \n" +
		"what order they are in. After you make a valid guess, the result \n" +
		"(feedback) will be displayed. \n\n" +
		"The result consists of a black peg for each peg you have guessed exactly \n" +
		"correct (color and position) in your guest. For each peg in the guess \n" +
		"that is the correct color, but is out of the position, you get a white \n" +
		"peg. For each peg, which is fully incorrect, you get no feedback. \n\n" +
		"Only the first letter of the color is displayed, B for blue, R for Red, \n" +
		"and so forth. When entering guesses you only need to enter the first \n" +
		"character of each color as a capital letter. \n\n" +
		"You have 12 guesses to figure out the secret code or you lose the game. \n";
	

	// Constructor
	public Greeting()
	{
	}
	
	// Display greeting
	public void DisplayGreeting()
	{
		System.out.println(greeting);
	}
	

}
