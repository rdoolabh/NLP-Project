import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;
import Sentence.PhrasePair;
import Sentence.PhrasesSimilarity;
import Sentence.WordCombinationGenerator;
import Sentence.WordPair;
import Sentence.WordSimilarityEstimation;
import StanfordParser.SentencePhrases;
import WordTool.Corpus;
import WordTool.FileWriter;


public class SentenceSimilarity {
  private static final String USAGE = "Usage: Examples [properties file]";
  private static final Set<String> HELP_KEYS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
      "--help", "-help", "/help", "--?", "-?", "?", "/?")));

  private static Dictionary dictionary = null;
  private String sentence1;
  private String sentence2;

  public static void main(String[] args) throws JWNLException, CloneNotSupportedException, IOException {
    // create a dictionary
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
    // read 2 sentences from file or from console
    try {
      ss.readFileForInput("/input.txt");
    } catch (Exception e) {
      e.printStackTrace();
      ss.promptForInput();
    }
    ss.printSentence();

    // get all word pairs in two sentences to joint sentence
    WordCombinationGenerator wc = new WordCombinationGenerator(dictionary, ss.sentence1, ss.sentence2);
    ArrayList<WordPair> wordPairs = new ArrayList<WordPair>();
    wordPairs.addAll(wc.getSent1AndJointSetWordPairs()); // lower case words
    wordPairs.addAll(wc.getSent2AndJointSetWordPairs()); // lower case words

    // compute all word pairs semantic similarity
    HashMap<String, Double> wordSimHash = new HashMap<String, Double>();
    for (WordPair wp : wordPairs) {
      String key1 = wp.getWord1() + "_" + wp.getWord2();
      String key2 = wp.getWord2() + "_" + wp.getWord1();
      if (wordSimHash.containsKey(key1) && wordSimHash.containsKey(key2))
        continue;
      WordSimilarityEstimation wse = new WordSimilarityEstimation(wp);
      wse.computeSimilarity(dictionary, wp);
      wordSimHash.put(key1, wp.getSemanticScore());
      wordSimHash.put(key2, wp.getSemanticScore());
    }

    // get all phrase pairs
    SentencePhrases sp = new SentencePhrases();
    ArrayList<String> sent1phrases = new ArrayList<String>();
    ArrayList<String> sent2phrases = new ArrayList<String>();
    sent1phrases = sp.getPhrasesList(wc.getSent1()); // wc.getSent1() is sentence 1 without [ .,!?],
                                                     // words might be upper or lower case w/o POS
    sent2phrases = sp.getPhrasesList(wc.getSent2()); // wc.getSent2() is sentence 2 without [ .,!?],
                                                     // words might be upper or lower case w/o POS

    // compute all phrase pair similarity
    PhrasesSimilarity ps = new PhrasesSimilarity();
    ArrayList<PhrasePair> allPhrasePairs = new ArrayList<PhrasePair>();
    Corpus wordCorpus = new Corpus(); // brown corpus object
    HashMap<String, PhrasePair> phraseHashInSent1 = new HashMap<String, PhrasePair>();
    HashMap<String, PhrasePair> phraseHashInSent2 = new HashMap<String, PhrasePair>();
    double thres = 0.7; // threshold for showing the phrases mapping
    for (String sent1phrase : sent1phrases) {
      for (String sent2phrase : sent2phrases) {

        PhrasePair ppAll = new PhrasePair(sent1phrase, sent2phrase);
        // compute phrase similarity and set to the phrase pair object
        ppAll.setScore(ps.computeSimilarity(sent1phrase, sent2phrase, wordSimHash, wordCorpus));
        allPhrasePairs.add(ppAll);
        
        // save to the hash if the similarity is higher than the threshold
        if (ppAll.getScore() < thres)
          continue;
        if (!phraseHashInSent1.containsKey(sent1phrase)) {
          PhrasePair ppHigh = new PhrasePair(sent1phrase, wc.getSent2());
          ppHigh.setScore(ps.computeSimilarity(sent1phrase, wc.getSent2(), wordSimHash, wordCorpus));
          phraseHashInSent1.put(sent1phrase, ppHigh);
        }

        if (!phraseHashInSent2.containsKey(sent2phrase)) {
          PhrasePair ppHigh = new PhrasePair(sent2phrase, wc.getSent1());
          ppHigh.setScore(ps.computeSimilarity(sent2phrase, wc.getSent1(), wordSimHash, wordCorpus));
          phraseHashInSent2.put(sent2phrase, ppHigh);
        }

      }
    }
    
    // rank by the phrase similarity
    try {
      allPhrasePairs = rankPhrasePairBySim(allPhrasePairs);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // write to a output text file
    DecimalFormat three = new DecimalFormat("0.000");
    FileWriter fw = new FileWriter();
    StringBuffer res = new StringBuffer();
    PhrasePair sentPP = allPhrasePairs.get(0);
    double sentenceSim = sentPP.getScore();
    for (PhrasePair pp : allPhrasePairs) {
      if (pp.getScore() > thres) {
        PhrasePair ph1sent2 = phraseHashInSent1.get(pp.getPhrase1());
        PhrasePair ph2sent1 = phraseHashInSent2.get(pp.getPhrase2());
        res.append(" ,Phrase Similarity,Sentence Similarity,Score");
        res.append('\n');
        res.append("Phrase v.s. Sentence Similarity," + ph1sent2.getPhrase1() + "," + ph1sent2.getPhrase2()
            + "," + three.format(ph1sent2.getScore()) + ",");
        res.append('\n');
        res.append("Phrase v.s. Sentence Similarity," + ph2sent1.getPhrase1() + "," + ph2sent1.getPhrase2()
            + "," + three.format(ph2sent1.getScore()) + ",");
        res.append('\n');
        res.append("Score," + three.format(pp.getScore()) + "," + three.format(sentenceSim) + ",");
        res.append('\n');
        res.append('\n');
      }
    }
    fw.writeFile("H", res);


  }

  public void printSentence() {
    System.out.println(sentence1);
    System.out.println(sentence2);
  }

  public void readFileForInput(String filePath) throws IOException {
    // InputStream is = new FileInputStream(filePath);
    String caseMaker = "H";
    InputStream is = getClass().getClassLoader().getResourceAsStream("input.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    while (line != null) {
      if (line.startsWith(caseMaker + "1")) {
        sentence1 = line.split(caseMaker + "1:")[1].trim();
      }
      if (line.startsWith(caseMaker + "2")) {
        sentence2 = line.split(caseMaker + "2:")[1].trim();
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

  public static ArrayList<PhrasePair> rankPhrasePairBySim(ArrayList<PhrasePair> allPhrasePairs) {
    Collections.sort(allPhrasePairs, new Comparator<PhrasePair>() {
      public int compare(PhrasePair pp1, PhrasePair pp2) {
        double p1 = pp1.getScore();
        double p2 = pp2.getScore();
        if (p1 > 1.0)
          p1 = 1.0;
        if (p1 < 0.0)
          p1 = 0.0;
        if (p2 > 1.0)
          p2 = 1.0;
        if (p2 < 0.0)
          p2 = 0.0;

        if (p2 > p1)
          return 1;
        else
          return -1;
      }
    });

    return allPhrasePairs;
  }
}
