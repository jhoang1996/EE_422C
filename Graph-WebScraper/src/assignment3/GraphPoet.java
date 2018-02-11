//Justin Hoang
//jah7399
//11/3/2017
package assignment3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GraphPoet {

	ArrayList<String> words = new ArrayList<String>();
	ArrayList<String> inputWords = new ArrayList<String>();
	Graph graph = new Graph();
	
    /**
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
	
    public GraphPoet(File corpus) throws IOException {

        /* Read in the File and place into graph here */
    	
    	Scanner sc = null;
        try {
            sc = new Scanner(corpus);
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();  
    	}
    	 
    	while (sc.hasNextLine()) {
    	    Scanner s2 = new Scanner(sc.nextLine());
    	    while (s2.hasNext()) {
    	        String s = s2.next();
    	        if(s.endsWith("."))
    	            words.add(s.substring(0, s.length()-1));
    	        else
    	            words.add(s);
    	   }
    	}
    	 
    	for(int i=0;i<words.size();i++)
    	{
    	    if(i+1<words.size())
    		    graph.set(words.get(i), words.get(i+1));
    	}
    }

    /**
     * Generate a poem.
     *
     * @param input File from which to create the poem
     * @return poem (as described above)
     */
    public String poem(File input) throws IOException {

        /* Read in input and use graph to complete poem */
        String poem = "";
        Scanner sc = null;
	    try {
	    	sc = new Scanner(input);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
	    while (sc.hasNextLine()) {
	            Scanner s2 = new Scanner(sc.nextLine());
	        while (s2.hasNext()) {
	            String s = s2.next();
	            inputWords.add(s.replace(";", ""));
	        }
	    }

	    for(int i=0;i<inputWords.size();i++)
        {	
        	if((i+1)<inputWords.size())
        	{
        		if(graph.getEdgeLong(inputWords.get(i), inputWords.get(i+1))>2 || graph.getEdgeLong(inputWords.get(i), inputWords.get(i+1))<=1)
        		{
        			poem=poem+inputWords.get(i)+" ";
        		}
        		else
        		{
        			if(graph.containWord(inputWords.get(i)) && graph.containWord(inputWords.get(i+1)))
        			{     				
        				Edge edge=graph.getEdge(inputWords.get(i), inputWords.get(i+1));
        				if(edge!=null)
        				{
        					for(int c=graph.edges.indexOf(edge);c<graph.edges.size();c++)
        					{
        						if(c==graph.edges.indexOf(edge))
        						{
        							poem=poem+inputWords.get(i)+" ";
        						}
        						else
        						{
        							poem=poem+graph.edges.get(c).start+" ";
        						}
        						if(graph.edges.get(c).end.toLowerCase().equals(inputWords.get(i+1).toLowerCase()))
        						{
        							break;
        						}
        					}
        				}
        			}
        		}
        	}
        	else
        	{
        		poem=poem+inputWords.get(i)+" ";
        	}
        }               
        return poem;
    }

}

class Edge<T>{  
	String start;
	String end;
	int weight;
	
	public Edge(String s, String e, int w)
	{
		start=s;
		end=e;
		weight=w;
	}	 
}

class Graph {
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	public Comparator<Edge> EdgeComp = new Comparator<Edge>() {

		public int compare(Edge s1, Edge s2) {

		   int rollno1 = s1.weight;
		   int rollno2 = s2.weight;
		   return rollno2-rollno1;
	   }};
	   
	public boolean containEdge(String word1, String word2)
	{
		for (Edge edge : edges) {
			if((word1.toLowerCase().equals(edge.start.toLowerCase()) && word2.toLowerCase().equals(edge.end.toLowerCase())))
			{
				return true;
			}
		}
		return false;	
	} 
	
	public boolean containWord(String word1)
	{
		for (Edge edge : edges) {
			if(word1.toLowerCase().equals(edge.start.toLowerCase()) || word1.toLowerCase().equals(edge.end.toLowerCase()))
			{
				return true;
			}
		}
		return false;	
	}
	
	int getCountEdgeStartingWithWord(String word1)
	{
		int count=0;
		for (Edge edge : edges) {
			if(edge.start.equals(word1))
				count++;
		}
		return count;
	}
	
	int getEdgeLong(String word1,String word2)
	{
		int count=0;
		ArrayList<Integer> edgesMinPath=new ArrayList<Integer>();
		
		for (Edge edge : edges) {
			if(edge.start.toLowerCase().equals(word1.toLowerCase()))
			{
				for(int i=edges.indexOf(edge);i<edges.size();i++)
				{
					count++;
					if(edges.get(i).end.toLowerCase().equals(word2.toLowerCase()))
					{
						edgesMinPath.add(count);
						count=0;
						break;
					}
				}
			}
		}
		if(edgesMinPath.isEmpty())
			return 0;
		else
		{
			return Collections.min(edgesMinPath);
		}
			
	}
	
	Edge getEdge(String word1, String word2)
	{
		ArrayList<Edge> newedges=new ArrayList<Edge>();
		for (Edge edge : edges) {
			if(edge.start.toLowerCase().equals(word1.toLowerCase()))
			{
				newedges.add(edge);
			}
		}
		int count=0;
		int weight=0;
		ArrayList<Integer> edgesMinPathWeight=new ArrayList<Integer>();
		ArrayList<Integer> edgesMinPathCount=new ArrayList<Integer>();
		
		for (Edge edge : edges) {
			if(edge.start.toLowerCase().equals(word1.toLowerCase()))
			{
				for(int i=edges.indexOf(edge);i<edges.size();i++)
				{
					count++;
					weight+=edges.get(i).weight;
					if(edges.get(i).end.toLowerCase().equals(word2.toLowerCase()))
					{
						edgesMinPathWeight.add(weight);
						edgesMinPathCount.add(count);
						weight=0;
						count=0;
						break;
					}
				}
			}
		}
		
		if(newedges.isEmpty())
			return null;

		int edgeId=edgesMinPathWeight.indexOf(Collections.max(edgesMinPathWeight));
		while(edgesMinPathCount.get(edgeId)>2)
		{
			edgeId=edgesMinPathWeight.indexOf(edgesMinPathWeight.get(edgeId))+1;
		}
		return newedges.get(edgeId);
	}
	
	void set(String word1, String word2){
		if(containEdge(word1, word2))
		{
			for (Edge edge : edges) {
				if(word1.equals(edge.start) && word2.equals(edge.end))
				{
					edge.weight++;
				}
			}
		}
		else
		{
			edges.add(new Edge(word1,word2,1));	
		}		
	}	
}

