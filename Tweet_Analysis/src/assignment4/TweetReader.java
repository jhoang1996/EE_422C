//Justin Hoang
//jah7399
//EE422C assignment4
package assignment4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * TweetReader contains method used to return tweets from method
 * Do not change the method header
 */
public class TweetReader {

    /**
     * Find tweets written by a particular user.
     *
     * @param url
     *            url used to query a GET Request from the server
     * @return return list of tweets from the server
     *
     */
    public static List<Tweets> readTweetsFromWeb(String url) throws Exception
    {
    	
    	final String USER_AGENT = "Mozilla/5.0";   
    	
        List<Tweets> tweetList = new ArrayList<>();
        
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();      
		
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);		
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();		
		

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		

		//print result
		System.out.println("");
		System.out.println("------ List of tweets:");
		System.out.println(response.toString());	
		
		try 
		{
		
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
						
			// Convert JSON string to Object
			Tweets[] tweets = mapper.readValue(response.toString(), Tweets[].class);	
			
			// convert array to list
			tweetList = Arrays.asList(tweets);		
		} 
		catch (JsonGenerationException e) 
		{
			System.out.println("JsonGenerationException");
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			System.out.println("JsonMappingException");
			e.printStackTrace();
		} 
		catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}		

        return tweetList;
    }
}
