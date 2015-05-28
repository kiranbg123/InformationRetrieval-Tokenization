import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.stylesheets.DocumentStyle;


public class Information_Retrieval_Hw1 {
	
	//Global variables
	private static BufferedReader buffer;
	
	private static Hashtable<String, Integer> wordList = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> stemmedWordList = new Hashtable<String, Integer>();
	private static ArrayList<Hashtable <String,Integer>> fileMap = new ArrayList<Hashtable<String,Integer>>();
	private static ArrayList<Hashtable <String,Integer>> stemmedFileMap = new ArrayList<Hashtable<String,Integer>>();
	private static Set<String> tagNames = new HashSet<String>();
	//private static ArrayList<Map.Entry<String, Integer>> list;
	
	private static int documentsCount = 0;
	private static int totalTokens = 0;
	private static int uniqueWords = 0;
	private static int tagCount = 0;
	private static int singleOccureneWords = 0;
	private static int averageTokens;
	//private static int documentsCount = 0;
	private static int stemmedTotalTokens = 0;
	private static int stemmedUniqueWords = 0;
	private static int stemmedTagCount = 0;
	private static int stemmedSingleOccureneWords = 0;
	private static int stemmedAverageTokens;
	private static ArrayList<Map.Entry<String, Integer>> sortedList;
	
	//private static Stemmer myStemmer = new Stemmer();
	public Information_Retrieval_Hw1() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		
		if(args.length < 1)
		{
			System.out.println("You need to provide the path as first argument");
			System.exit(1);
		}
		String cranfield = args[0];
		System.out.println(cranfield);
		//Function to read Cranfield directory into string
		String cranfieldContent = new String();
		
		File cranfieldFiles = new File(cranfield);
		//Programm time starts
		long startTime = System.currentTimeMillis();
		ReadFile(cranfieldFiles);
		/*/
		Set<String> keys = wordList.keySet();
		 for(String key: keys){
	            System.out.println("Value of "+key+" is: "+wordList.get(key));
	        }*/
		System.out.println("Total number of documents: " + fileMap.size());
		 
		 //Calculate total number of tokens
		 //Calculat time to acquire the text characteristics
		 
		 totalTokens = CalculateNumberOfTokens(wordList);
		 
		 
		 System.out.println("Total number Of tokens = " + totalTokens);
		 //Calculate number of unique words
		 uniqueWords = CalculateUniqueWords(wordList);
		 System.out.println("Total number Of Unique words = " + uniqueWords);
		 
		//Calculate number of unique words
		 singleOccureneWords = CalculateSingleOccurenceWords(wordList);
		 System.out.println("Total number Of words that occur only once = " + singleOccureneWords);
		 
		 //Find the 30 most frequent words
		 FindThirtyMostFrequentWords(wordList);
		 
		 //Calculate average number of tokens
		 averageTokens = CalculateAverageTokens(fileMap, documentsCount);
		 System.out.println("Average number of word tokens per document is " + averageTokens);
		 
		 System.out.println("\nAfter Stemming\n");
		 //Stemming
	//	 Stemming(wordList);
		 //Printing the stemmed words
		 /*
		 Set<String> stemmedKeys = stemmedWordList.keySet();
		 for(String key: stemmedKeys){
	            System.out.println("Value of "+key+" is: "+stemmedWordList.get(key));
	        }*/
		 //System.out.println("Total number of documents: " + documentsCount);
		 //Calcualte everything for stemmed Words
		//Calculate total number of tokens
		 //The following is not required. Commenting out.
		 /*
		 stemmedTotalTokens = CalculateNumberOfTokens(stemmedWordList);
		 System.out.println("Total number Of Stemmed tokens = " + stemmedTotalTokens);
		 */
		 //Calculate number of unique words
		 stemmedUniqueWords = CalculateUniqueWords(stemmedWordList);
		 System.out.println("Total number Of distinct Stems = " + stemmedUniqueWords);
		 
		//Calculate number of unique words
		 stemmedSingleOccureneWords = CalculateSingleOccurenceWords(stemmedWordList);
		 System.out.println("Total number Of Stemmed words that occur only once = " + stemmedSingleOccureneWords);
		 
		 //Find the 30 most frequent words
		 FindThirtyMostFrequentWords(stemmedWordList);
		 
		 stemmedAverageTokens = CalculateAverageTokens(stemmedFileMap, documentsCount);
		 System.out.println("Average number of stemed tokens per document is " + averageTokens);
		 long endTime   = System.currentTimeMillis();
		 long totalTime = endTime - startTime;
		 System.out.println("total time :" + totalTime +"ms");
	}
	
	public static void ReadFile(File cranfieldFiles) throws IOException{
		//String cranfieldContent = new String();
		
		for (File file: cranfieldFiles.listFiles())
		{
			
			//read files recursively if path contains folder
			if(file.isDirectory())
			{
				ReadFile(file);
			}
			
			else
			{
				documentsCount++;
				try
				{
				buffer = new BufferedReader(new FileReader(file));
				}
				catch (FileNotFoundException e)
				{
					System.out.println("File not Found");
					
				}
				//find the tags and their count
				tagCount = tagCount + TagHandler(file, tagNames);
				//find words in the cranfield
				TokenHandler(file, tagNames);
				
			}
		}
		
		
	}
	
	public static int TagHandler(File file, Set<String> tagNames) throws IOException
	{
		String line;
		int tag_count = 0;
		

		buffer = new BufferedReader(new FileReader(file));
		while((line = buffer.readLine()) != null)
        {
			/*
			 * If the line contains a '<', it is considered a tag and tag_count is incremented.
			 */
        	if(line.contains("<"))
        	{
        		tag_count++;
        		
        		String b = line.replaceAll("[<*>/]", "");
        		tagNames.add(b);
        	}
        	
        }
        tag_count/=2; //Since each tag represent the beginning and the end, we divide it by two to get the actual count.
        return tag_count;
	}
	
	public static void TokenHandler(File file, Set<String> tagNames) throws IOException
	{
		String line;
		String words[];
		
		buffer = new BufferedReader(new FileReader(file));
		Hashtable<String, Integer> tempMap = new Hashtable<String, Integer>();
		Hashtable<String, Integer> stemmedTempMap = new Hashtable<String, Integer>();
        
        while((line = buffer.readLine()) != null)
        {
        	String s1 = line.replaceAll("[^a-zA-Z.]+"," "); //Replace everything that is not an alphabet with a blank space.
            String s2 = s1.replaceAll("[.]", "");//Replace words with . (eg U.S) as 1 word
        	words = s2.split(" ");
            
            for(String word : words)
            {
            	//Handle the tags properly
            	if(!tagNames.contains(word) && !word.equals(""))
            	{
            		word = word.toLowerCase(); // Converts all words to lower case.
            		
            		//add word if it isn't added already
            		if(!wordList.containsKey(word))
            		{                             
            			//first occurance of this word
            			wordList.put(word, 1); 
            			
            			//Following is to compute the unique words in each document
            			if(!tempMap.containsKey(word))
            			{
            				tempMap.put(word,1);
            				
            			}
            			else
            			{
            				tempMap.put(word, tempMap.get(word)+ 1);
            				
            			}
            		}
            		else
            		{
            			//Increament the count of that word
            			wordList.put(word, wordList.get(word) + 1);
            			if(!tempMap.containsKey(word))
            			{
            				tempMap.put(word,1);
            				
            			}
            			else
            			{
            				tempMap.put(word, tempMap.get(word)+ 1);
            			}
            		}
            		
            	//Now stem the word and put it in stemmedWordList
            		String stemmedWord = null;
            		String temp = word;
      			  Stemmer myStemmer = new Stemmer();
      			  //add the word to the stemmer
      			  myStemmer.add(temp.toCharArray(), temp.length());
      			  myStemmer.stem();
      			  stemmedWord = myStemmer.toString();
      			  //Add it to the stemmedword list
      			  if(!stemmedWordList.containsKey(stemmedWord))
            		{                             
            			//first occurance of this word
            			stemmedWordList.put(stemmedWord, 1); 
            			if(!stemmedTempMap.containsKey(word))
            			{
            				stemmedTempMap.put(word,1);
            				
            			}
            			else
            			{
            				stemmedTempMap.put(word, stemmedTempMap.get(word)+ 1);
            				
            			}
            		}
            		else
            		{
            			//Increament the count of that word
            			stemmedWordList.put(stemmedWord, stemmedWordList.get(stemmedWord) + 1);
            			if(!stemmedTempMap.containsKey(word))
            			{
            				stemmedTempMap.put(word,1);
            				
            			}
            			else
            			{
            				stemmedTempMap.put(word, stemmedTempMap.get(word)+ 1);
            				
            			}
            		}
            	}
            }
        }
        
        //Add count to file map and stemmed file map respectively after reading every file
        fileMap.add(tempMap);
        stemmedFileMap.add(stemmedTempMap);
	}
	
	//Function to find the total number of tokens in the cranfield database
	
	public static int CalculateNumberOfTokens(Hashtable<String, Integer> myWordList)
	{
		int noOfTokens = 0;
		
		for (Integer value: myWordList.values())
		{
			noOfTokens = noOfTokens + value;
		}
		return noOfTokens;
	}
	
	public static int CalculateUniqueWords(Hashtable<String, Integer> myWordList)
	{
		
		return myWordList.size();
	}
	
	public static int CalculateSingleOccurenceWords(Hashtable<String, Integer> myWordList)
	{
		int count = 0;
		
		for (Integer value: myWordList.values())
		{
			if(value == 1)
			{
				count++;
			}
		}
		return count;
	}
	
	
	//Sorting the hashTable
	//reference:http://stackoverflow.com/questions/5176771/sort-hashtable-by-values
	public static ArrayList<Map.Entry<String, Integer>> SortHashTable(Hashtable<String, Integer> myWordList)
	{
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(myWordList.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	            return o2.getValue().compareTo(o1.getValue());
	        }});
		return list;
	}

	public static void FindThirtyMostFrequentWords(Hashtable<String, Integer> myWordList)
	{
		//Sort the hashtable based on value
		
		sortedList = SortHashTable(myWordList);
		System.out.println("The 30 most frequent words are: ");
		for(int i=0;i<30;i++)
		{
			System.out.println("\t" + (i+1) + "." + " " + sortedList.get(i));
		}
		
	}
	
	public static int CalculateAverageTokens(ArrayList<Hashtable<String, Integer>> fileList, int noOfDocuments)
	{
//		return (int) (totalNoOfTokens/noOfDocuments);
		int averageTokens = 0;
		int count = 0;
		for (int i =0;i<fileList.size(); i++)
		{
			count += fileList.get(i).size();
		}
		
		averageTokens = count/noOfDocuments;
		return averageTokens;
	}
	
	/*
	public static void Stemming(Hashtable<String, Integer> myWordList)
	{
		for ( String word: myWordList.keySet())
		{	
			  String stemmedWord = new String();
			  Stemmer myStemmer = new Stemmer();
			  //add the word to the stemmer
			  myStemmer.add(word.toCharArray(), word.length());
			  myStemmer.stem();
			  stemmedWord = myStemmer.toString();
			  //Add it to the stemmedword list
			  if(!stemmedWordList.containsKey(stemmedWord))
      		{                             
      			//first occurance of this word
      			stemmedWordList.put(stemmedWord, 1); 
      		}
      		else
      		{
      			//Increment the count of that word
      			stemmedWordList.put(word, stemmedWordList.get(stemmedWord) + 1);
      		}
		}
	}
*/
}
