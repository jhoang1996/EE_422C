//Justin Hoang
//jah7399
//EE422C assignment4
package assignment4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Social Network consists of methods that filter users matching a
 * condition.
 *
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {
	
	// Valid characters for tweeter names
	final static String validTweeterNamePattern = "^[a-zA-Z0-9_]+$";
	
	// Map of tweeters and the list of names they mentioned in their texts
	static Map<String, Set<String>> tweetersMap = new HashMap<String, Set<String>>();
			
	// List of pairs of friend (i.e. edges)
    static List<Set<String>> friendPairsList = new ArrayList<Set<String>>();    
     
    // List contains sets of mutual friends
    static List<Set<String>> mutualFriendsList = new ArrayList<Set<String>>();  
    
    // Set contains all valid tweeters
    static Set<String> allTweeters = new HashSet<String>();
    

    /**
     * Get K most followed Users.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @param k
     *            integer of most popular followers to return
     * @return the set of usernames who are most mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getName()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like ethomaz@utexas.edu does NOT
     *         contain a mention of the username.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static List<String> findKMostFollower(List<Tweets> tweets, int k) 
    {
        List<String> mostFollowers = new ArrayList<>();
        
        collectTweeters(tweets);
        
        // Map contains tweeters and number times they are mentioned
        Map<String, Integer> tweeterAndNumOfFollowersMap = new HashMap<String, Integer>();   
        
        for (String currentTweeter : tweetersMap.keySet()) 
        {
			// Get list of tweeter mentions
			Set<String> tweeterMentionsList = tweetersMap.get(currentTweeter);   
						
			for (String mentionedTweeter : tweeterMentionsList) 
			{
				// If map already contains mentioned tweeter
				if (tweeterAndNumOfFollowersMap.containsKey(mentionedTweeter))
				{
					// Get current number of times mentioned
					int count = tweeterAndNumOfFollowersMap.get(mentionedTweeter);					
					++count;
					
					// Update the counts of time tweeter is mentioned
					tweeterAndNumOfFollowersMap.put(mentionedTweeter, count);
            	}
            	else
            	{                 				
            		// First time mention, add to map
            		tweeterAndNumOfFollowersMap.put(mentionedTweeter, 1);		
            	}				
			}    	
        }
        
        
        LinkedList<String> sortedList = new LinkedList<String>(); 
        
    	// Go through each tweeter
        for (String tweeter : tweeterAndNumOfFollowersMap.keySet()) 
        {       	
        	// Number of times tweeter was mentioned
        	int numFollowers = tweeterAndNumOfFollowersMap.get(tweeter);
        	
        	if (sortedList.isEmpty())
        	{
        		sortedList.add(tweeter);
        	}
        	else
        	{ 
        		boolean inserted = false;
        		
        		// loop thru sorted list
        		for(int i=0; i < sortedList.size(); i++)
        		{
        			// Get name from sorted list
        			String name = sortedList.get(i);
        			
        			// Get tweeter's num of followers
                	int count = tweeterAndNumOfFollowersMap.get(name);   
                	
                	// If new tweeter has more followers
                	if (numFollowers >= count)
                	{
                		// Place new tweeter in front of current one
                		sortedList.add(i, tweeter);
                		inserted= true;
                		break;
                	}                		
        		}  
        		
        		if (!inserted){
        			sortedList.addLast(tweeter);
        		}
        	}
        }
        
    
        // return the top k tweeters with highest num followers
        for (int i = 0; i < k; i++){
        	mostFollowers.add(sortedList.get(i));
        }
        
        return mostFollowers;
    }

    /**
     * Find all cliques in the social network.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     *
     * @return list of set of all cliques in the graph
     */
    public static List<Set<String>> findCliques(List<Tweets> tweets) {
    	
        List<Set<String>> result = new ArrayList<Set<String>>();
        
        collectTweeters(tweets);
                               
        // All tweeter mentions have been collected, determine tweeters' actual friends      
        findTweetersFriends();
        
        // Find mutual friends of tweeters
        result = findTweetersMutualFriends();
                
        return result;
    }
    
    /**
     * This method creates a collection of tweeters and their tweeter mentions
     * from the input tweets list
     * @param tweets
     */
    private static void collectTweeters(List<Tweets> tweets) 
    {

    	tweetersMap.clear();
    	
        // loop through tweet list to collect tweeters and the names they mentioned
        for (int i = 0; i < tweets.size(); i++) 
        {
        	String tweeter = tweets.get(i).getName();
        	
        	if (!isTweeterNameValid(tweeter))
        	{
        		continue;
        	}
	
    		tweeter = tweeter.toLowerCase();
    		
			allTweeters.add(tweeter);  
   		
        	String text = tweets.get(i).getText();
        	
        	// if text is null or contains no "@"
        	if (text == null || !text.contains("@"))
        	{
        		// skip to next tweet
        		continue;
        	}
        			    			
	    	// Split text words by space
    		String[] words = text.split(" "); 
	                
    		for (String word : words) 
    		{	                		                	
    			// if word has "@"
    			if (word.contains("@"))
    			{                        
    				String tweeterMention = getTweeterName(word);
    					
    				// if word is valid tweeter name
    				if (tweeterMention != null)
    				{    												    						
    					Set<String> tweeterMentionsSet;

    					// If map already contains tweeter
    					if (tweetersMap.containsKey(tweeter))
    					{
    						// Get set of names mentioned by this tweeter
    						tweeterMentionsSet = tweetersMap.get(tweeter);
                    	}
                    	else
                    	{                 				
                    		tweeterMentionsSet = new HashSet<String>();
                    	}
    						
						tweeterMentionsSet.add(tweeterMention.toLowerCase());
						
						// Update tweeter mentions list of tweeter
						tweetersMap.put(tweeter, tweeterMentionsSet);
    				}
    			} 	                
    		}
        }    	
    	
    }
    
    /**
     * This method determines if tweeter mention is a valid 
     * tweeter name.  If it is a valid name then return the name
     */
    private static String getTweeterName(String tweeterMention) 
    {				
		if (tweeterMention.charAt(0) == '@')
		{
			tweeterMention = tweeterMention.replace("@", "");
			
			// If 1st char is @ then just check the rest of name
			if (isTweeterNameValid (tweeterMention))
			{
				return tweeterMention;
			}
		}
		else
		{
			// get location of @ in word
			int index = tweeterMention.indexOf("@");
			
			// Get characters in front of @
			String precedingChars = tweeterMention.substring(0, index);
			
			// if preceding characters match valid tweeter characters
			if (precedingChars.matches(validTweeterNamePattern))
			{		
				// then tweeter mention is not valid name
				return null;
			}
			else
			{
				// Get the rest of string after @
				String name = tweeterMention.substring(index+1);
				
				if (isTweeterNameValid (name))
				{
					return name;
				}			
			}		
		}
		
		// tweeter mention is not a valid tweeter name
		return null;
    }
        
    /**
     * This method checks if the input tweeter name is valid
     */
    private static boolean isTweeterNameValid (String tweeterName)
    {   			
    	if (tweeterName == null || !tweeterName.matches(validTweeterNamePattern))
    	{
    		return false;
    	} 	
    	
    	return true;
    }
        
    /**
     *  This method filters list of tweeter mentions for actual friends.
     *  Two tweeters are friend when they both mention each other
     */
    private static void findTweetersFriends()
    {  
    	// Go through each tweeter
        for (String currentTweeter : tweetersMap.keySet()) 
        {
			// Get list of tweeter mentions
			Set<String> tweeterMentionsList = tweetersMap.get(currentTweeter);   
			
			Set<String> actualFriends = new HashSet<String>(); 
						
			for (String mentionedTweeter : tweeterMentionsList) 
			{
		         
				// Get the mentioned tweeter his own tweeter mentions list
				Set<String> names = tweetersMap.get(mentionedTweeter); 			         
		         
				// If the mentioned tweeter also mentioned the current tweeter
				if (names != null && names.contains(currentTweeter)) 
				{	        	 
					// the tweeter and the mentioned tweeter are friend
					actualFriends.add(mentionedTweeter);
		        	 
					// Create a set containing these two friends
					Set<String> friendPair = new HashSet<>(Arrays.asList(currentTweeter, mentionedTweeter));
					
					if (!friendPairsList.contains(friendPair))
					{
						// Add to list of pairs
						friendPairsList.add(friendPair);
					}
		         }       
			}	
			
       	 	// update with new list of actual friends
			tweetersMap.put(currentTweeter, actualFriends);       
        }    	   	
    }  
    
    /**
     * This method creates a list of mutual friends among the tweeters
     * @return 
     */
    private static List<Set<String>> findTweetersMutualFriends()
    {       
        // loop through each tweeter
        for (String currentTweeter : tweetersMap.keySet()) 
        {	
        	// Get current tweeter's friends
        	Set<String> tweeterFriends = tweetersMap.get(currentTweeter); 	  
	         
        	// loop through each friend
        	for (String myFriend : tweeterFriends) 
        	{   
        		Set<String> mutualFriends = new HashSet<String>();     
        		
        		// Save all tweeter's friends to new set
        		mutualFriends.addAll(tweeterFriends);
        		
        		// Now get friends of the friend
        		Set<String> friendsOfFriend = tweetersMap.get(myFriend); 	 	
        		
        		// Keep only names that appear in both list. 
        		mutualFriends.retainAll(friendsOfFriend);
        		   		
        		List<String> nameList = new ArrayList<String>(mutualFriends);
        		
        		Set<String> notFriend = new HashSet<String>();   
        		
        		// Mutual friends are friends of both currentTweeter and myFriend.
        		// But still need to find out if they are friends to each other    		
        		for (int i = 0; i < nameList.size()-1; i++) 
        		{
        			for (int y = 0; y < nameList.size(); y++)
        			{     				
        				if (i == y) 
        				{
        					// Skip when doing same name
        					continue;
        				}
        				
        				// Create a set with these two names
        				Set<String> friendPair = 
        						new HashSet<>(Arrays.asList(nameList.get(i), nameList.get(y)));
        			
        				// If the pair doesn't appear in the FriendParisList
        				if (!friendPairsList.contains(friendPair))
        				{
        					notFriend.add(nameList.get(i));
        					notFriend.add(nameList.get(y));
        				
        					mutualFriends.remove(nameList.get(i));
        					mutualFriends.remove(nameList.get(y));      				
        				}
        			}
        		}
        		
        		// Add current tweeter and the friend to the list.
        		mutualFriends.add(currentTweeter);
        		mutualFriends.add(myFriend);
        	
        		// If notFriend has names then these names are mutually exclusive.
        		// Create new groups of friends for each of the name
        		if (!notFriend.isEmpty())
        		{
        			for (String name : notFriend) 
        			{
        				// Create a new group and copy current mutual friends into it
        				Set<String> newFriendsGroup = new HashSet<String>();   
        				newFriendsGroup.addAll(mutualFriends);
    				
        				newFriendsGroup.add(name);
    				
            			if (!mutualFriendsList.contains(newFriendsGroup))
            			{
            				mutualFriendsList.add(newFriendsGroup);
            			}
        			}
        		}
        		else
        		{
        			if (!mutualFriendsList.contains(mutualFriends))
        			{
        				mutualFriendsList.add(mutualFriends);
        			}
        		}	
        	}
        }    
        
        // Remove overlapped lists
        List<Set<String>> subsetList = new ArrayList<Set<String>>();          
        
        for (int i=0; i < mutualFriendsList.size(); i++)
        {
        	Set<String> set1 = mutualFriendsList.get(i);
        	
            for (int y=0; y < mutualFriendsList.size(); y++)
            {    	
            	if (y != i)
            	{
                	Set<String> set2 = mutualFriendsList.get(y);
                	
    	        	if (set1.containsAll(set2) && !(set1.equals(set2))) 
    	        	{ 
    	        		// Set 2 is subset of set1, add it to list
    	        		if (!subsetList.contains(set2)){
    	        			subsetList.add(set2);  	        			
    	        		}
    	        	}
            	}
            }
        }   
                
        // remove subsets from cliques list
        mutualFriendsList.removeAll(subsetList); 
           
        return mutualFriendsList;
    }
}

