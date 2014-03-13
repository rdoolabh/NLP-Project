package Sentence;

import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

public class WordPair {
  private static Dictionary dictionary = null;
  private String word1;
  private String word2;
  private IndexWord indexW1 = null;
  private IndexWord indexW2 = null;
  private int height;
  private int length;
  private double simScore;

  public WordPair(String w1, POS pos1, String w2, POS pos2) {
    word1 = w1;
    word2 = w2;
    try {
      indexW1 = dictionary.getIndexWord(pos1, w1);
      indexW2 = dictionary.getIndexWord(pos2, w2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getWord1() {
    return word1;
  }

  public String getWord2() {
    return word2;
  }
  public IndexWord getIndexOfWord1() {
    return indexW1;
  }
  public IndexWord getIndexOfWord2() {
    return indexW2;
  }
  public String[] getWordPair() {
    String[] wordpair = null;
    wordpair[0] = word1;
    wordpair[1] = word2;
    return wordpair;
  }

  public int getCommonAncestorHeight() {
    return height;
  }

  public int getShortestPathLength() {
    return length;
  }

  public double getWordSimScore() {
    return simScore;
  }

  public void setWordSimilarity(double ws) {
    this.simScore = ws;
  }

  public void setCommonAncestorHeight(int h) {
    this.height = h;
  }

  public void setShortestPathLength(int l) {
    this.length = l;
  }

  public boolean equalTo(WordPair wp) {
    return ((this.word1.equals(wp.getWord1()) && this.word2.equals(wp.word2)) || (this.word1.equals(wp
        .getWord2()) && this.word2.equals(wp.word1)));
  }
  
  public void print() {
    System.out.println("Word 1: " + word1 + " Word 2: " + word2 + " Height: " + height + " Length: " + length + " SCORE: " + simScore);
  }
}
