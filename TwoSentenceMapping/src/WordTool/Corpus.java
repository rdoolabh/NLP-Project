package WordTool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Corpus {
  private String text = null;
  private HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();
  private int wordNum = 0;

  private void readCorpusText() throws IOException {
    InputStream is = new FileInputStream("corpus.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    StringBuffer sb = new StringBuffer();
    sb.append(line);
    while (line != null) {
      sb.append(line);
      line = reader.readLine();
    }
    text = sb.toString();
    reader.close();
    is.close();
  }
  
  // contructor of nltk.corpus.brown corpus  
  public Corpus() throws IOException {
    readCorpusText();
    String[] words = text.toString().split(",");
    for (String word : words) {
      String key = word.toLowerCase();
      if (key.length() == 0 && !key.matches("^[a-zA-Z].*"))
        continue;
      ++wordNum;
      if (wordFreq.containsKey(key)) {
        int i = wordFreq.get(key);
        wordFreq.put(key, ++i);
      } else {
        wordFreq.put(key, 1);
      }
    }
  }

  private int getWordNum(String word) {
    if (!wordFreq.containsKey(word.toLowerCase())) return 0;
    return wordFreq.get(word.toLowerCase());
  }

  // get the word weight of information contribution 
  public double getWordWeight(String word) {
    return 1.0 - (Math.log(getWordNum(word) + 1) / Math.log(wordNum + 1));
  }
}
