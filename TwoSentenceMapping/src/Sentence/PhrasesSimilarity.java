package Sentence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import WordTool.Corpus;

public class PhrasesSimilarity {
  // compute two phrase similarity
  public double computeSimilarity(String phrase1, String phrase2, HashMap<String, Double> wordSim,
      Corpus wordCorpus) throws IOException {

    ArrayList<String> words1 = new ArrayList<String>();
    ArrayList<String> words2 = new ArrayList<String>();
    words1.addAll(Arrays.asList(phrase1.split(" ")));
    words2.addAll(Arrays.asList(phrase2.split(" ")));

    // use hash map to get the joint set of words
    ArrayList<String> jointWords = new ArrayList<String>();
    HashMap<String, Boolean> hash = new HashMap<String, Boolean>();
    for (int i = 0; i < words1.size(); ++i) {
      String key = words1.get(i).toLowerCase();
      if (!hash.containsKey(key)) {
        hash.put(key, true);
        jointWords.add(key);
      }
    }
    for (int j = 0; j < words2.size(); ++j) {
      String key = words2.get(j).toLowerCase();
      if (!hash.containsKey(key)) {
        hash.put(key, true);
        jointWords.add(key);
      }
    }

    // compute the matrix of phrase 1 and phrase 2
    ArrayList<ArrayList<Double>> vector1 = new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> vector2 = new ArrayList<ArrayList<Double>>();
    for (int j = 0; j < jointWords.size(); ++j) {
      ArrayList<Double> element = new ArrayList<Double>();
      for (int n1 = 0; n1 < words1.size(); ++n1) {
        String key1 = jointWords.get(j) + "_" + words1.get(n1);
        String key2 = words1.get(n1) + "_" + jointWords.get(j);
        double simScore = 0.0;
        if (wordSim.containsKey(key1) && wordSim.containsKey(key2)) {
          double jointWordWeight = wordCorpus.getWordWeight(jointWords.get(j));
          double sentWordWeight = wordCorpus.getWordWeight(words1.get(n1));
          simScore = wordSim.get(key1) * jointWordWeight * sentWordWeight;
        }
        element.add(simScore);
      }
      vector1.add(element);
    }
    for (int j = 0; j < jointWords.size(); ++j) {
      ArrayList<Double> element = new ArrayList<Double>();
      for (int n2 = 0; n2 < words2.size(); ++n2) {
        String key1 = jointWords.get(j) + "_" + words2.get(n2);
        String key2 = words2.get(n2) + "_" + jointWords.get(j);
        double simScore = 0.0;
        if (wordSim.containsKey(key1) && wordSim.containsKey(key2)) {
          double jointWordWeight = wordCorpus.getWordWeight(jointWords.get(j));
          double sentWordWeight = wordCorpus.getWordWeight(words2.get(n2));
          simScore = wordSim.get(key1) * jointWordWeight * sentWordWeight;
        }
        element.add(simScore);
      }
      vector2.add(element);
    }

    // elements in final semantic vector 1 and vector 2 are decided by the largest value in the
    // column
    ArrayList<Double> vector1res = new ArrayList<Double>();
    ArrayList<Double> vector2res = new ArrayList<Double>();
    for (ArrayList<Double> doubleArr : vector1) {
      vector1res.add(getMaxElement(doubleArr));
    }
    for (ArrayList<Double> doubleArr : vector2) {
      vector2res.add(getMaxElement(doubleArr));
    }
    
//    // print the matrix of phrase 1 
//    DecimalFormat three = new DecimalFormat("0.000");
//    System.out.println(jointWords);
//    System.out.println(words1);
//    System.out.print("[");
//    for (int i = 0; i < vector1.get(0).size(); ++i) {
//      for (int j = 0; j < vector1.size(); ++j)
//        System.out.print(three.format(vector1.get(j).get(i)) + ", ");
//      System.out.println();
//    }
//    System.out.println("]");

    return getCosineSimilarity(vector1res, vector2res);
  }
  
  // find the max element in a array
  private double getMaxElement(ArrayList<Double> arr) {
    double max = Double.MIN_VALUE;
    for (Double d : arr) {
      if (d > max)
        max = d;
    }
    return max;
  }

  // compute cosine similarity of two vectors
  private double getCosineSimilarity(ArrayList<Double> arr1, ArrayList<Double> arr2) {
    double squareSum1 = 0.0, squareSum2 = 0.0;

    int n = arr1.size();
    double sum = 0.0;
    for (int i = 0; i < n; i++) {
      sum += arr1.get(i) * arr2.get(i);
      squareSum1 += arr1.get(i) * arr1.get(i);
      squareSum2 += arr2.get(i) * arr2.get(i);
    }
    if (squareSum1 == 0 || squareSum2 == 0)
      return 0.0;
    return sum / (Math.sqrt(squareSum1) * Math.sqrt(squareSum2));
  }


}
