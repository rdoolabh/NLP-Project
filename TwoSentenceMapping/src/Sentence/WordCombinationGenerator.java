package Sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
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

  public POS parseWordPOS(String str) {
    if (str.equals("NOUN") || str.equals("N") || str.equals("n")) {
      return POS.NOUN;
    } else if (str.equals("VERB") || str.equals("V") || str.equals("v")) {
      return POS.VERB;
    } else if (str.equals("JJ") || str.equals("ADJ") || str.equals("adj")) {
      return POS.ADJECTIVE;
    } else {
      return POS.ADVERB;
    }
  }

  public WordCombinationGenerator(Dictionary dictionary, String sentence1, String sentence2)
      throws JWNLException {
    sent1WordList.addAll(Arrays.asList(sentence1.trim().split("[ ,.!?]+")));
    sent2WordList.addAll(Arrays.asList(sentence2.trim().split("[ ,.!?]+")));
    createJointWordSet(sent1WordList, sent2WordList);
    // System.out.println(this.jointWordsList);
    generateCombination(dictionary);
  }

  private void generateCombination(Dictionary dictionary) throws JWNLException {
    for (int i = 0; i < jointWordsList.size(); ++i) {
      String jointWord = jointWordsList.get(i).split("_")[0].toLowerCase();
      POS jointWordPOS = parseWordPOS(jointWordsList.get(i).split("_")[1]);

      for (int m = 0; m < sent1WordList.size(); ++m) {
        String sentWord = sent1WordList.get(m).split("_")[0].toLowerCase();
        POS sentWordPOS = parseWordPOS(sent1WordList.get(m).split("_")[1]);
        WordPair wp = new WordPair(dictionary, jointWord, jointWordPOS, sentWord, sentWordPOS);
        this.sent1AndJointSetWordPairs.add(wp);
      }
      for (int n = 0; n < sent2WordList.size(); ++n) {
        String sentWord = sent2WordList.get(n).split("_")[0].toLowerCase();
        POS sentWordPOS = parseWordPOS(sent2WordList.get(n).split("_")[1]);
        WordPair wp = new WordPair(dictionary, jointWord, jointWordPOS, sentWord, sentWordPOS);
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

//  public String getSent(String sentence) {
//    StringBuffer res = new StringBuffer();
//    String[] words = sentence.split(",");
//    for (String word : words) {
//      res.append(word.split("_")[0]+" ");
//    }
//    return res.toString();
//  }
  
  public String getSent1() {
    StringBuffer res = new StringBuffer();
    for (String word : sent1WordList) {
      res.append(word.split("_")[0]+" ");
    }
    return res.toString();
  }
  public String getSent2() {
    StringBuffer res = new StringBuffer();
    for (String word : sent2WordList) {
      res.append(word.split("_")[0]+" ");
    }
    return res.toString();
  }
}
