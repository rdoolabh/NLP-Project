import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


public class SentenceSimilarity {
  private static final String USAGE = "Usage: Examples [properties file]";
  private static final Set<String> HELP_KEYS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
      "--help", "-help", "/help", "--?", "-?", "?", "/?")));
  
  private static Dictionary dictionary = null;
  private String sentence1;
  private String sentence2;
  private List<String> sent1List;
  private List<String> sent2List;

  public static void main(String[] args) throws FileNotFoundException, JWNLException,
      CloneNotSupportedException {
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

    if (null != dictionary) {
      Test test = new Test();
      test.promptForInput();
      test.convertSentenceToArray();
      test.createWordSets(dictionary);
      test.computeSimilarityScore();
      test.printWordSets();
      // new Test(dictionary).go();
      // test.go();
    }
  }
  
  public void readFileForInput(String filePath) throws IOException {
    InputStream is = new FileInputStream(filePath);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    while (line != null) {
      if (line.startsWith("ENTER SENTENCE 1:")) {
        sentence1 = line.split("ENTER SENTENCE 1:")[1].trim();
      }
      if (line.startsWith("ENTER SENTENCE 2:")) {
        sentence1 = line.split("ENTER SENTENCE 2:")[1].trim();
      }
      line = reader.readLine();
    }
    reader.close();
    is.close();
  }
  
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
}
