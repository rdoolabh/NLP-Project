//package net.sf.extjwnl.utilities;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A class to demonstrate the functionality of the library.
 *
 * @author John Didion <jdidion@didion.net>
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public class Test {

    private static final String USAGE = "Usage: Examples [properties file]";
    private static final Set<String> HELP_KEYS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "--help", "-help", "/help", "--?", "-?", "?", "/?"
    )));

    public static void main(String[] args) throws FileNotFoundException, JWNLException, CloneNotSupportedException {
        Dictionary dictionary = null;
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
            new Test(dictionary).go();
        }
    }

    private IndexWord ACCOMPLISH;
    private IndexWord WORD1;
    private IndexWord WORD2;
    private IndexWord FUNNY;
    private IndexWord DROLL;
    private final static String MORPH_PHRASE = "running-away";
    private final Dictionary dictionary;
    
    private final String WORD1_param = "boy";
    private final String WORD2_param = "female";
    

    public Test(Dictionary dictionary) throws JWNLException {
        this.dictionary = dictionary;
        ACCOMPLISH = dictionary.getIndexWord(POS.VERB, "accomplish");
        WORD1 = dictionary.getIndexWord(POS.NOUN, WORD1_param);
        WORD2 = dictionary.lookupIndexWord(POS.NOUN, WORD2_param);
        FUNNY = dictionary.lookupIndexWord(POS.ADJECTIVE, "funny");
        DROLL = dictionary.lookupIndexWord(POS.ADJECTIVE, "droll");
    }

    public void go() throws JWNLException, CloneNotSupportedException {
    	demonstrateAsymmetricRelationshipOperation(WORD1, WORD2);
        //demonstrateMorphologicalAnalysis(MORPH_PHRASE);
        //demonstrateListOperation(ACCOMPLISH);
        //demonstrateTreeOperation(BOY);
        //demonstrateSymmetricRelationshipOperation(FUNNY, DROLL);
    }

    private void demonstrateAsymmetricRelationshipOperation(IndexWord start, IndexWord end) throws JWNLException, CloneNotSupportedException {
        // Try to find a relationship between the first sense of <var>start</var> and the first sense of <var>end</var>
        RelationshipList list = RelationshipFinder.findRelationships(start.getSenses().get(0), end.getSenses().get(0), PointerType.HYPERNYM);
        System.out.println("Hypernym relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\":");
        for (Object aList : list) {
            ((Relationship) aList).getNodeList().print();
        }
        System.out.println("Common Parent Index: " + ((AsymmetricRelationship) list.get(0)).getCommonParentIndex());
        System.out.println("Depth: " + list.get(0).getDepth());
        
        /*
        for(Object aList : list){
        	System.out.println(aList.toString());
        }
        
        System.out.println("The Depth of the Parent in relation to the source ");
        */
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
	
	// the depth of the word from
	public int Height;
	
}