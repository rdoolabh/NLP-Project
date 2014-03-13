package Sentence;

import java.util.Iterator;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

public class WordSimilarityEstimation {
  private static Dictionary dictionary = null;
  private RelationshipList list = new RelationshipList();
  private Relationship shollowestList = null;
  private boolean DEBUG = true;

  public WordSimilarityEstimation(WordPair wp) throws CloneNotSupportedException, JWNLException {
    list =
        RelationshipFinder.findRelationships(wp.getIndexOfWord1().getSenses().get(0), wp.getIndexOfWord2()
            .getSenses().get(0), PointerType.HYPERNYM);
    shollowestList = list.getShallowest();
  }

  public void estimateHeight(WordPair wp) throws JWNLException, CloneNotSupportedException {
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

    Synset commonAncestor = dictionary.getSynsetAt(POS.NOUN, minIndex);
    Synset target = dictionary.getWordBySenseKey("entity%1:03:00::").getSynset();
    RelationshipList rl = RelationshipFinder.findRelationships(commonAncestor, target, PointerType.HYPERNYM);
    wp.setCommonAncestorHeight(rl.getShallowest().getDepth());
  }

  public void estimateLength(WordPair wp) {
    wp.setShortestPathLength(shollowestList.getDepth());
  }

  public void computeSimilarity(WordPair wp) {
    if (wp.getWord1() == null || wp.getWord1().length() == 0 || wp.getWord2() == null
        || wp.getWord2().length() == 0)
      wp.setWordSimilarity(0.0);
    // compute f(l)
    double f_l = Math.exp((-1) * 0.2 * wp.getShortestPathLength());

    // compute f(h)
    double heightTerm1 = Math.exp(0.45 * wp.getCommonAncestorHeight());
    double heightTerm2 = Math.exp((-1.0) * 0.45 * wp.getCommonAncestorHeight());
    
    double f_h = 0.0;
    if (heightTerm2 != 0) {
      f_h = (heightTerm1 - heightTerm2) / (heightTerm1 + heightTerm2);
      wp.setWordSimilarity(f_l * f_h);
    } else {
      wp.setWordSimilarity(0.0);
    }

  }
}
