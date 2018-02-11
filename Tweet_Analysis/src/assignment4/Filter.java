//Justin Hoang
//jah7399
//EE422C assignment4
package assignment4;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 *
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     *
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweets> writtenBy(List<Tweets> tweets, String username) {
    	
        List<Tweets> filteredList = new ArrayList<Tweets>();
        
        for (int i = 0; i < tweets.size(); i++) {
        	    	
        	String tweeter = tweets.get(i).getName();
        
        	if ((tweeter != null) && tweeter.equals(username)){
        		
        		filteredList.add(tweets.get(i));
        	}       	
        	
        }
          
        return filteredList;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     *
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     * @throws InvalidTimespanException 
     */
    public static List<Tweets> inTimespan(List<Tweets> tweets, Timespan timespan) {
    	
        List<Tweets> filteredList = new ArrayList<Tweets>();
        
        // Get start and end points of timespan
        Instant start = timespan.getStart();
        Instant end = timespan.getEnd();
        
        // check the validity of time span
        if (start.isAfter(end))
        {        	
        	//throw new InvalidTimespanException("End Date before Start Date");        	
        	try {
				throw new InvalidTimespanException("End Date before Start Date");
			} catch (InvalidTimespanException e) {
				System.out.println(e.getMessage());
			}
        }  
        
        // loop through tweet list
        for (int i = 0; i < tweets.size(); i++) {
        	       	
        	// Get date of this tweet
        	String tweetDate = tweets.get(i).getDate();
        	
        	// Check for null date first
        	if (tweetDate != null)
        	{ 		      	       	
        		try
        		{
        			// Convert date to number of seconds passed since January 1, 1970
        			Instant numSeconds = Instant.parse(tweetDate);
        			
        			// If tweet date is equal of after start 
            		if (numSeconds.equals(start) || numSeconds.isAfter(start))
            		{
            			// If tweet date is before or equal to end 
            			if (numSeconds.isBefore(end) || numSeconds.equals(end))
    					{
            				filteredList.add(tweets.get(i));
            			}
            		}
        		}
        		catch (Exception e)
        		{
        			continue;        	          
        		}   		
        	}        	
        }
    
        return filteredList;
    }

    /**
     * Find tweets that contain certain words.
     *
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets.
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when
     *         represented as a sequence of nonempty words bounded by space characters
     *         and the ends of the string) includes *at least one* of the words
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweets> containing(List<Tweets> tweets, List<String> words) {
    	
        List<Tweets> filteredList = new ArrayList<Tweets>();
        
        // Loop through tweet list
        for (int i = 0; i < tweets.size(); i++) 
        {    
    		String text = tweets.get(i).getText();
    		
    		// make sure there is something to test first
    		if (text != null)
    		{ 		
    			boolean wordFound = false;
    			
    			// Loop through word list
    			for (int y = 0; y < words.size(); y ++)
    			{
    				// Get word and convert to lower case
    				String word = words.get(y).toLowerCase();
    				
    				if (text.toLowerCase().contains(word))
    				{
    					wordFound = true;   					
    				}    			
        		}
    			
    			// If tweet contains at least one of the words, add it to list
    			if (wordFound)
    			{
    				filteredList.add(tweets.get(i));
    			}
        	}       	
        }
      
        return filteredList;
    }
    
}