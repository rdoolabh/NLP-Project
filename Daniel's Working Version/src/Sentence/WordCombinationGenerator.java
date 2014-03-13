package Sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;

public class WordCombinationGenerator {
  private ArrayList<String> sent1WordList = new ArrayList<String>();
  private ArrayList<String> sent2WordList = new ArrayList<String>();
  private ArrayList<String> jointWordsList = new ArrayList<String>();
  private ArrayList<WordPair> sent1AndJointSetWordPairs = new ArrayList<WordPair>();
  private ArrayList<WordPair> sent2AndJointSetWordPairs = new ArrayList<WordPair>();
  
  private void createJointWordSet(ArrayList<String> arr1, ArrayList<String> arr2) {
    
    HashMap<String, Boolean> hash = new HashMap<String, Boolean>();
    for (int i = 0; i < arr1.size(); ++i) {
      if (hash.containsKey(arr1.get(i)))
        continue;
      hash.put(arr1.get(i), true);
    }
    for (int j = 0; j < arr2.size(); ++j) {
      if (hash.containsKey(arr2.get(j)))
        continue;
      hash.put(arr2.get(j), true);
    }

    for (String key : hash.keySet()) {
      jointWordsList.add(key);
    }
    
  }

  public WordCombinationGenerator(Dictionary dictionary, String sentence1, String sentence2) throws JWNLException {
    sent1WordList.addAll(Arrays.asList(sentence1.trim().split("[ ,.!]+")));
    sent2WordList.addAll(Arrays.asList(sentence2.trim().split("[ ,.!]+")));
    createJointWordSet(sent1WordList, sent2WordList);
//    System.out.println(this.jointWordsList);
    generateCombination(dictionary);
  }

  private void generateCombination(Dictionary dictionary) throws JWNLException {
    for (int i = 0; i < jointWordsList.size(); ++i) {
      for (int m = 0; m < sent1WordList.size(); ++m) {
        WordPair wp = new WordPair(dictionary, jointWordsList.get(i), sent1WordList.get(m));
        this.sent1AndJointSetWordPairs.add(wp);
      }
      for (int n = 0; n < sent2WordList.size(); ++n) {
        WordPair wp = new WordPair(dictionary, jointWordsList.get(i), sent2WordList.get(n));
        this.sent2AndJointSetWordPairs.add(wp);
      }  
    }
    
  }
  
  public ArrayList<WordPair> getSent1AndJointSetWordPairs() {
    return this.sent1AndJointSetWordPairs;
  }

  public ArrayList<WordPair> getSent2AndJointSetWordPairs() {
    return this.sent2AndJointSetWordPairs;
  }
}
