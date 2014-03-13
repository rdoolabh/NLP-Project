import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;
import Sentence.WordCombinationGenerator;
import Sentence.WordPair;
import Sentence.WordSimilarityEstimation;


public class SentenceSimilarity {
  private static final String USAGE = "Usage: Examples [properties file]";
  private static final Set<String> HELP_KEYS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
      "--help", "-help", "/help", "--?", "-?", "?", "/?")));

  private static Dictionary dictionary = null;
  private String sentence1;
  private String sentence2;

  public static void main(String[] args) throws JWNLException, CloneNotSupportedException, IOException {
    // Dictionary dictionary = null;
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

    SentenceSimilarity ss = new SentenceSimilarity();

    try {
      ss.readFileForInput("/input.txt");
    } catch (Exception e) {
      ss.promptForInput();
    }
    ss.printSentence();
    WordCombinationGenerator wc = new WordCombinationGenerator(dictionary, ss.sentence1, ss.sentence2);
    ArrayList<WordPair> wordPairs = wc.getSent2AndJointSetWordPairs(); 
    
    for (WordPair wp : wordPairs) {
//      System.out.println(wp.getWord1() + " & " + wp.getWord2());
      WordSimilarityEstimation wse = new WordSimilarityEstimation(wp);
      wse.computeSimilarity(dictionary, wp);

    }
    for (WordPair wp : wordPairs) {
    System.out.println(wp.getWord1() + " & " + wp.getWord2() + ": " + wp.getSemanticScore());
//    System.out.println(wse.getShortestPathLength() + " "+ wse.getCommonAncestorHeight());
  }}

  public void printSentence() {
    System.out.println(sentence1);
    System.out.println(sentence2);
  }

  public void readFileForInput(String filePath) throws IOException {
    // InputStream is = new FileInputStream(filePath);
    InputStream is = getClass().getClassLoader().getResourceAsStream("input.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    while (line != null) {
      if (line.startsWith("ENTER SENTENCE 1:")) {
        sentence1 = line.split("ENTER SENTENCE 1:")[1].trim();
      }
      if (line.startsWith("ENTER SENTENCE 2:")) {
        sentence2 = line.split("ENTER SENTENCE 2:")[1].trim();
      }
      line = reader.readLine();
    }
    reader.close();
    is.close();
  }

  public void promptForInput() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Sentence 1: ");
    sentence1 = scanner.nextLine();
    System.out.print("Enter Sentence 2: ");
    sentence2 = scanner.nextLine();
    scanner.close();
  }

  public List<String> convertSentenceToArray(String sentence) {
    return Arrays.asList(sentence.trim().split("[ ,.!]+"));
  }
}
