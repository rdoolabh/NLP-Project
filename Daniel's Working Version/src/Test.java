//package net.sf.extjwnl.utilities;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;


public class Test {

    private static final String USAGE = "Usage: Examples [properties file]";
    private static final Set<String> HELP_KEYS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "--help", "-help", "/help", "--?", "-?", "?", "/?"
    )));

    public static void main(String[] args) throws FileNotFoundException, JWNLException, CloneNotSupportedException {
    	 //Dictionary dictionary = null;
        if (args.length != 1) {
            dictionary = Dictionary.getDefaultResourceInstance();
        } else {
            if (HELP_KEYS.contains(args[0])) {
                System.out.println(USAGE);
            } else {
                FileInputStream inputStream = new FileInputStream(args[0]);
                dictionary = Dictionary.getInstance(inputStream);
            }
        }

        if (null != dictionary) {
        	Test test = new Test();
        	test.promptForInput();
        	test.convertSentenceToArray();
        	test.createWordSets(dictionary);
        	test.computeSimilarityScore();
        	test.printWordSets();
            //new Test(dictionary).go();
        	//test.go();
        }
    }

    private IndexWord ACCOMPLISH;
    //private IndexWord WORD1;
    //private IndexWord WORD2;
    private IndexWord FUNNY;
    private IndexWord DROLL;
    private final static String MORPH_PHRASE = "running-away";
    private static Dictionary dictionary = null;
    
    private String sentence1;
    private String sentence2;
    private List<String> sent1List;
    private List<String> sent2List;
    private ArrayList<WordSet> wordSets = new ArrayList<WordSet>();
    private boolean DEBUG = true;
    
    
    //WordSet set1 = new WordSet("boy","girl");
    
    
    public void promptForInput()
    {
    	Scanner scanner = new Scanner( System.in );
    	System.out.print( "Enter Sentence 1: " );
    	sentence1 = scanner.nextLine();
    	System.out.print( "Enter Sentence 2: " );
    	sentence2 = scanner.nextLine();
    	
    	scanner.close();
    }
    
    public void convertSentenceToArray()
    {
    	sent1List = Arrays.asList(sentence1.trim().split("[ ,.!]+"));
    	sent2List = Arrays.asList(sentence2.trim().split("[ ,.!]+"));
    }

    public void createWordSets(Dictionary dictionary)
    {
    	IndexWord word1 = null;
    	IndexWord word2 = null;
    	//WordSet temp;
    	
    	// loop through sent1
    	for(String sent1String : sent1List)
    	{
    		for(String sent2String : sent2List)
    		{
    			try
    			{    
    				word1 = dictionary.getIndexWord(POS.NOUN, sent1String);
    				word2 = dictionary.lookupIndexWord(POS.NOUN, sent2String);
    			}
    			catch(Exception e)
    			{
    				System.out.println("ERROR SOMETHING WENT WRONG WITH WORD INITIALIZATION");
    			}
    	        
    			try
    			{
    				WordSet temp = new WordSet(sent1String, sent2String);
    				demonstrateAsymmetricRelationshipOperation(word1, word2, temp);
    				wordSets.add(temp);
    			}
    			catch(JWNLException e)
    			{
    				System.out.println("ERROR SOMETHING WENT WRONG! Throwing JWNL");
    			}
    			catch(CloneNotSupportedException e)
    			{
    				System.out.println("ERROR SOMETHING WENT WRONG! Throwing clone");
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    }
    
    public void printWordSets()
    {
    	// print out the results
    	for(WordSet _wordSet : wordSets)
    	{
    		System.out.println(_wordSet.toString());
    	}
    }
    
    public void computeSimilarityScore()
    {
    	for(WordSet _wordSet : wordSets)
    	{
    		// compute f(l)
    		double f_l = Math.pow(2.17, (-1)*0.2*_wordSet.Length);
    		
    		// compute f(h)
    		double heightTerm1 = Math.pow(2.71828, 0.45*_wordSet.Height);
    		double heightTerm2 = Math.pow(2.71828, ( (-1)*0.45*_wordSet.Height ) );
    		
    		//double answer = (Math.pow(2.71828, 0.45*_wordSet.Height) - Math.pow(2.71828, ( (-1)*0.45*_wordSet.Height ) ))/(Math.pow(2.71828, 0.45*_wordSet.Height) + Math.pow(2.71828, ( (-1)*0.45*_wordSet.Height ) ));
    	
    		/*
    		System.out.println("ANSWER: " +  answer);
    		System.out.println("Height: " + _wordSet.Height);
    		
    		*/
    		System.out.println("HeightTerm1----------------------------------> " + heightTerm1);
    		System.out.println("HeightTerm2----------------------------------> " + heightTerm2);
    		
    		double f_h = 0;
    		
    		if(heightTerm2 != 0)
    		{
    			f_h = (heightTerm1 - heightTerm2)/(heightTerm1 + heightTerm2);
    		
    			_wordSet.Score = f_l*f_h;
    		}
    		else
    		{
    			_wordSet.Score = 0;
    		}
    		
    		System.out.println("f(l)=" + f_l + " f(h)=" + f_h);
    		
    	}
    }
    
    /*
    public Test(Dictionary dictionary) throws JWNLException {
        this.dictionary = dictionary;
        ACCOMPLISH = dictionary.getIndexWord(POS.VERB, "accomplish");
        WORD1 = dictionary.getIndexWord(POS.NOUN, set1.Word1);
        WORD2 = dictionary.lookupIndexWord(POS.NOUN, set1.Word2);
        FUNNY = dictionary.lookupIndexWord(POS.ADJECTIVE, "funny");
        DROLL = dictionary.lookupIndexWord(POS.ADJECTIVE, "droll");
    }
    */
    
    
    /*
    public void go() throws JWNLException, CloneNotSupportedException {
    	demonstrateAsymmetricRelationshipOperation(WORD1, WORD2);
        //demonstrateMorphologicalAnalysis(MORPH_PHRASE);
        //demonstrateListOperation(ACCOMPLISH);
        //demonstrateTreeOperation(BOY);
        //demonstrateSymmetricRelationshipOperation(FUNNY, DROLL);
    }
    */

    private void demonstrateAsymmetricRelationshipOperation(IndexWord start, IndexWord end, WordSet obj) throws JWNLException, CloneNotSupportedException {
        // Try to find a relationship between the first sense of <var>start</var> and the first sense of <var>end</var>
        RelationshipList list = RelationshipFinder.findRelationships(start.getSenses().get(0), end.getSenses().get(0), PointerType.HYPERNYM);
        System.out.println("Hypernym relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\":");
        
        //Synset target = dictionary.getWordBySenseKey("entity%1:03:00::").getSynset();
        
        for (Relationship aList : list) {
        	System.out.print("Depth: " + aList.getDepth());
            ((Relationship) aList).getNodeList().print();
        }
        
        Relationship shollowestList = list.getShallowest();
        //shortestList.getNodeList().print();
        
        System.out.println("Here :" + shollowestList.getDepth());
        
        Iterator<PointerTargetNode> itr = shollowestList.getNodeList().iterator();
        long minIndex = Integer.MAX_VALUE;
        while(itr.hasNext()) {
        	String [] str = itr.next().toString().split("\\[");
        	int len = str[3].length();
        	
        	 
        	if(DEBUG) {System.out.println("here it is -------------->: " + str[3].substring(8,len-2));}
        	
        	if(Long.parseLong(str[3].substring(8,len-2)) < minIndex)
        	{
        		minIndex = Long.parseLong(str[3].substring(8,len-2));
        	}
        }
       
        if(DEBUG) {
        	System.out.println("-----------------------------------> MINIMUM: "+minIndex);
        }
        
        Synset commonAncestor = dictionary.getSynsetAt(POS.NOUN, minIndex);
        Synset target = dictionary.getWordBySenseKey("entity%1:03:00::").getSynset();
        RelationshipList rl = RelationshipFinder.findRelationships(commonAncestor, target, PointerType.HYPERNYM);
        int height = rl.getShallowest().getDepth();
        int length =shollowestList.getDepth();
        
        obj.Height = (double) height;
        obj.Length = (double) length;
        
        
        // set the common parent index THIS IS WRONG
        //obj.Height = ((AsymmetricRelationship) list.get(0)).getCommonParentIndex();
        /*
        obj.Height = ((AsymmetricRelationship) list.get(0)).getDepth();
        
        System.out.println("Here is the index: " + ((AsymmetricRelationship) list.get(0)).getCommonParentIndex());
        int ind = ((AsymmetricRelationship) list.get(0)).getCommonParentIndex();
        
        
        Iterator<PointerTargetNode> it = ptr.iterator();
        while(it.hasNext()) {
        	System.out.println(it.toString());
        }
        */
        //System.out.println("THE DEPTH IS: " + ((Relationship) aList).getDepth());
        
        //obj.Height= min*1.0;
        //obj.Length = list.get(0).getDepth()*1.0;
        
        //System.out.println("Height of Ancestor: " + obj.Height);
        //System.out.println("Length: " + obj.Length);
 
        /*
        for(Object aList : list){
        	System.out.println(aList.toString());
        }
        
        */
    }
    
    private Relationship findShortestList(RelationshipList list) {
    	Relationship shortest = null;
    	int minListLength = Integer.MAX_VALUE;
    	for (Relationship alist : list) {
    		int listLength = alist.getDepth();
    		if (listLength < minListLength) {
    			shortest = alist;
    			minListLength = listLength;
    		}	
    	}
    	
    	return shortest;
    }
    
    private void demonstrateMorphologicalAnalysis(String phrase) throws JWNLException {
        // "running-away" is kind of a hard case because it involves
        // two words that are joined by a hyphen, and one of the words
        // is not stemmed. So we have to both remove the hyphen and stem
        // "running" before we get to an entry that is in WordNet
        System.out.println("Base form for \"" + phrase + "\": " +
                dictionary.lookupIndexWord(POS.VERB, phrase));
    }

    private void demonstrateListOperation(IndexWord word) throws JWNLException {
        // Get all of the hypernyms (parents) of the first sense of <var>word</var>
        PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(word.getSenses().get(0));
        System.out.println("Direct hypernyms of \"" + word.getLemma() + "\":");
        hypernyms.print();
    }

    private void demonstrateTreeOperation(IndexWord word) throws JWNLException {
        // Get all the hyponyms (children) of the first sense of <var>word</var>
        PointerTargetTree hyponyms = PointerUtils.getHyponymTree(word.getSenses().get(0));
        System.out.println("Hyponyms of \"" + word.getLemma() + "\":");
        hyponyms.print();
    }

    private void demonstrateSymmetricRelationshipOperation(IndexWord start, IndexWord end) throws JWNLException, CloneNotSupportedException {
        // find all synonyms that <var>start</var> and <var>end</var> have in common
        RelationshipList list = RelationshipFinder.findRelationships(start.getSenses().get(0), end.getSenses().get(0), PointerType.SIMILAR_TO);
        System.out.println("Synonym relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\":");
        for (Object aList : list) {
            ((Relationship) aList).getNodeList().print();
        }
        System.out.println("Depth: " + list.get(0).getDepth());
    }
}

class WordSet{
	public String Word1;
	public String Word2;
	
	// the depth of the most recent common ancestor
	public double Height;

	// the length from Word1 to Word2
	public double Length;
	
	// the score computed from f(l)*f(h)
	public double Score;
	
	public WordSet(String w1, String w2)
	{
		Word1 = w1;
		Word2 = w2;
	}
	
	public String toString()
	{
		return "Word 1: " + Word1 + " Word 2: " + Word2 + " Height: " + Height + " Length: " + Length + " SCORE: " + Score;
	}
	
}