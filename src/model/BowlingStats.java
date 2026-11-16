package model;

public class BowlingStats {

  private int legalBalls;
  private int runsConceded;
  private int wickets;
  private int maidens;

  private int currentOverRuns;
  private int currentOverBalls;

  public BowlingStats() {
    legalBalls = 0;
    runsConceded = 0;
    wickets = 0;
    maidens = 0;
    currentOverRuns = 0;
    currentOverBalls = 0;
  }

  public void recordDelivery(Ball ball) {

    int chargedRuns = ball.getBatterRuns();

    if (ball.getExtraType().equals(Ball.WIDE)
        || ball.getExtraType().equals(Ball.NO_BALL)) {
      chargedRuns += ball.getExtraRuns();
    }

    runsConceded += chargedRuns;
    currentOverRuns += chargedRuns;

    if (ball.isWicket()
        && !ball.getWicketType().equalsIgnoreCase("RUN_OUT")) {
      wickets++;
    }

    if (ball.isLegalDelivery()) {
      legalBalls++;
      currentOverBalls++;

      if (currentOverBalls == 6) {

        if (currentOverRuns == 0) {
          maidens++;
        }

        currentOverBalls = 0;
        currentOverRuns = 0;
      }
    }
  }

  public int getLegalBalls() {
    return legalBalls;
  }

  public int getRunsConceded() {
    return runsConceded;
  }

  public int getWickets() {
    return wickets;
  }

  public int getMaidens() {
    return maidens;
  }

  public String getOvers() {
    return (legalBalls / 6) + "." + (legalBalls % 6);
  }

  public double getEconomyRate() {

    if (legalBalls == 0) {
      return 0.0;
    }

    return (double) runsConceded * 6 / legalBalls;
  }

  public void displayStats(String bowlerName) {

    System.out.printf(
        "%-15s %5s %5d %5d %5d %8.2f%n",
        bowlerName,
        getOvers(),
        maidens,
        runsConceded,
        wickets,
        getEconomyRate());
  }
}
