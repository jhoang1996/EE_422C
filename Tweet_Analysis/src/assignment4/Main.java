//Justin Hoang
//jah7399
//EE422C assignment4
package assignment4;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    final static String URLEndpoint = "http://kevinstwitterclient2.azurewebsites.net/api/products";

    /**
     * We will not use your Main class to test your code
     * Feel free to modify this as much as you want
     * Here is an example of how you might test the code from main
     * for Problem 1 and Problem 2
     */
    public static void main(String[] args) throws Exception {

        // Problem 1: Returning Tweets from Server
        TweetReader reader = new TweetReader();
        List<Tweets> tweetsList = reader.readTweetsFromWeb(URLEndpoint);
//      System.out.println(tweetsList);

        // Problem 2:
        // Filter Tweets by Username
        Filter filter = new Filter();
        List<Tweets> filteredUser = filter.writtenBy(tweetsList,"kevinyee");
        System.out.println("");
        System.out.println("------ Filter Tweets by Username:");
        System.out.println("");        
        System.out.println(filteredUser);

        // Filter by Timespan
        Instant testStart = Instant.parse("2017-11-11T00:00:00Z");
        Instant testEnd = Instant.parse("2017-11-12T12:00:00Z");        
        Timespan timespan = new Timespan(testStart,testEnd);
        List<Tweets> filteredTimeSpan = filter.inTimespan(tweetsList,timespan);
        System.out.println("");
        System.out.println("------ Filter Tweets by timespan:");
        System.out.println("");
        System.out.println(filteredTimeSpan);

        //Filter by words containinng
        List<Tweets> filteredWords = filter.containing(tweetsList,Arrays.asList("good","luck"));
        System.out.println("");
        System.out.println("------ Filter Tweets by words containing:"); 
        System.out.println("");
        System.out.println(filteredWords);
        
        // Problem 3
        List<Set<String>> cliques = SocialNetwork.findCliques(tweetsList);
        System.out.println("");
        System.out.println("------ List of all cliques:");
        System.out.println("");
		for (int i = 0; i < cliques.size(); i++)
		{
			System.out.print(">>>>>>>>>> clique : " );			
			Set<String> setNames = cliques.get(i);			
			for (String name: setNames){				
				System.out.print(name + "  ");
			}
			System.out.println();  
		}
        
        int K = 5;
		List<String> mostFollowerList = SocialNetwork.findKMostFollower(tweetsList, K);  // K can be any integer
		System.out.println("");
		System.out.println("------ Tweeters with most followers:");
		System.out.println("");
        for (int i = 0; i < K; i++){
        	String name = mostFollowerList.get(i);
    		System.out.println(name );
        }		        
    }
}
