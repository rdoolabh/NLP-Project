package Sentence;

public class PhrasePair {
  private String phrase1;
  private String phrase2;
  private double score;
  
  public void setScore(double s) {
    this.score = s;
  }
  
  public String getPhrase1() {
    return this.phrase1;
  }
  
  public String getPhrase2() {
    return this.phrase2;
  }
  
  public double getScore() {
    return this.score;
  }

  public PhrasePair() {}

  public PhrasePair(String ph1, String ph2) {
    this.phrase1 = ph1;
    this.phrase2 = ph2;
  }

}
