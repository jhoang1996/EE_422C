//Justin Hoang
//jah7399
//11/3/2017
package assignment3;

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Main_Bonus {
	private static URL URLObj;
    private static URLConnection connect;
	
    public static void main(String[] args) throws IOException {
			  			  
		      try {
		            URLObj = new URL("http://www.ece.utexas.edu/people/faculty/edison-thomaz");
		            connect = URLObj.openConnection(); 
		            connect.setDoOutput(true);	
		      }
		      catch (MalformedURLException ex) {
		            System.out.println("Cannot open the specified web page.");
		            System.exit(1); 
		      }
		      catch (Exception ex) {
		            System.out.println("An exception happened. " + ex.getMessage());
		            System.exit(1);
		      }
				
		      try {
		           		            
		            // Create a buffered reader to read the web site input stream.
		            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
					
		            BufferedWriter writer2 = new BufferedWriter( new FileWriter("src/assignment3/corpus_Prof_Thomaz.txt"));
		            String lineRead = "";
		            writer2.write(lineRead.replace("<p>", "").replace("</p>", ""));
					// Web scrape the UT professor's web page.
		            while ((lineRead = reader.readLine()) != null) {
		            	if(lineRead.contains("<p>")){
		            		writer2.write(lineRead.replace("<p>", "").replace("</p>", ""));
		            		break;
		            	}
		            }
					
		            reader.close();
		            writer2.close();			     		            
		      }
		      catch (Exception ex) {
		            System.out.println("An error reading or writing to the web siteL: " + ex.getMessage());
		      }
			  			  			  
			  final GraphPoet nimoy = new GraphPoet(new File("src/assignment3/corpus_Prof_Thomaz.txt"));
	          System.out.println(nimoy.poem(new File("src/assignment3/input_Prof_Thomaz.txt")));			 	            
     }
}
