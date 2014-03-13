package Sentence;

import java.util.Iterator;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;
import SentenceTool.Stemmer;

public class WordSimilarityEstimation {
  private boolean DEBUG = false;
 
  private int height;
  private int length;
  private double simScore;
  private RelationshipList list = new RelationshipList();
  private Relationship shollowestList = null;
 
  private void estimateHeight(Dictionary dictionary, WordPair wp) throws JWNLException, CloneNotSupportedException {
    Iterator<PointerTargetNode> itr = shollowestList.getNodeList().iterator();
    long minIndex = Integer.MAX_VALUE;
    while (itr.hasNext()) {
      String[] str = itr.next().toString().split("\\[");
      int len = str[3].length();


      if (DEBUG) {
        System.out.println("here it is -------------->: " + str[3].substring(8, len - 2));
      }

      if (Long.parseLong(str[3].substring(8, len - 2)) < minIndex) {
        minIndex = Long.parseLong(str[3].substring(8, len - 2));
      }
    }

    if (DEBUG) {
      System.out.println("-----------------------------------> MINIMUM: " + minIndex);
    }
    POS synsetPOS = wp.getWord1POS();
    Synset commonAncestor = dictionary.getSynsetAt(synsetPOS, minIndex);
    Synset target = dictionary.getWordBySenseKey("entity%1:03:00::").getSynset();
    RelationshipList rl = RelationshipFinder.findRelationships(commonAncestor, target, PointerType.HYPERNYM);
    height = rl.getShallowest().getDepth();
  }

  private void estimateLength(WordPair wp) {
    length = shollowestList.getDepth();
  }

  
  private String stdTrans(String str) {
    Stemmer s = new Stemmer();
    if (str.length() == 0)
      return "";
    for (int i = 0; i < str.length(); ++i) {
      s.add(Character.toLowerCase(str.charAt(i)));
    }
    s.stem();
    return s.toString();
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

  public void printAllList() {
    for (Relationship aList : list) {
      System.out.print("Depth: " + aList.getDepth());
      aList.getNodeList().print();
    }
  }

  public void printShollowestList() {
    shollowestList.getNodeList().print();
  }

  public WordSimilarityEstimation(WordPair wp) throws CloneNotSupportedException, JWNLException {
    try{
      list = RelationshipFinder.findRelationships(wp.getIndexOfWord1().getSenses().get(0), wp.getIndexOfWord2()
            .getSenses().get(0), PointerType.HYPERNYM);
    shollowestList = list.getShallowest();
    } catch (Exception e){
      System.out.println("ERROR:" + wp.getWord1() + " "+ wp.getWord2() + " ");
    }
  }

  
  public void computeSimilarity(Dictionary dictionary, WordPair wp) {
    if (wp.getWord1() == null || wp.getWord1().length() == 0 || wp.getWord2() == null
        || wp.getWord2().length() == 0) {
      simScore = 0.0;
      return;
    }
    
    if (wp.getWord1().equals(wp.getWord2()) || stdTrans(wp.getWord1()).equals(stdTrans(wp.getWord2()))) {
      wp.setScore(1.0);
      return;
    }
    
    try {
      estimateHeight(dictionary, wp);
      estimateLength(wp);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // compute f(l)
    double f_l = Math.exp((-1) * 0.2 * ((double) length));

    // compute f(h)
    double heightTerm1 = Math.exp(0.45 * ((double) height));
    double heightTerm2 = Math.exp((-1.0) * 0.45 * ((double) height));

    double f_h = 0.0;
    if (heightTerm2 != 0) {
      f_h = (heightTerm1 - heightTerm2) / (heightTerm1 + heightTerm2);
      wp.setScore(f_l * f_h);
    } else {
      wp.setScore(0.0);
    }

  }
}
