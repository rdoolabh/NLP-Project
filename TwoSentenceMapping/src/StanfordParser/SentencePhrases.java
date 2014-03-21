package StanfordParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class SentencePhrases {
  // use stanford parser API to get all phrases 
  public ArrayList<String> getPhrasesList(String sentence) {
    String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
    String[] options = {"-maxLength", "80", "-retainTmpSubcategories"};
    LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
    List<Tree> trs = lp.parse(sentence).subTreeList();
    ArrayList<String> res = new ArrayList<String>();
    HashMap<String, Boolean> hash = new HashMap<String, Boolean>();
    for (Tree tr : trs) {
      String s = tr.toString();
      String ss = s.replace("(", "").replace(")", "").replace(".", "").replace("!", "");
      String[] resStr = ss.split(" ");
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < resStr.length; ++i) {
        if (resStr[i].equals("S"))
          continue;
        if (resStr[i].length() > 1 && Character.isUpperCase(resStr[i].charAt(1)))
          continue;
        sb.append(resStr[i] + " ");
      }
      String key = sb.toString().trim();
      if (hash.containsKey(key))
        continue;
      if (key.length() > 0) {
        hash.put(key, true);
        res.add(key);
      }

    }

    return res;

  }
}
