package model;

public class BattingStats {

  private int runs;
  private int ballsFaced;
  private int fours;
  private int sixes;

  public BattingStats() {
    runs = 0;
    ballsFaced = 0;
    fours = 0;
    sixes = 0;
  }

  // Update batting figures after a delivery
  public void recordDelivery(Ball ball) {

    // Wide deliveries do not count as balls faced.
    if (!ball.getExtraType().equals(Ball.WIDE)) {
      ballsFaced++;
    }

    int batterRuns = ball.getBatterRuns();

    runs += batterRuns;

    if (batterRuns == 4) {
      fours++;
    }

    if (batterRuns == 6) {
      sixes++;
    }
  }

  public int getRuns() {
    return runs;
  }

  public int getBallsFaced() {
    return ballsFaced;
  }

  public int getFours() {
    return fours;
  }

  public int getSixes() {
    return sixes;
  }

  public double getStrikeRate() {

    if (ballsFaced == 0) {
      return 0.0;
    }

    return (double) runs * 100 / ballsFaced;
  }

  public void displayStats(String playerName) {

    System.out.printf(
        "%-15s %5d %5d %5d %5d %8.2f%n",
        playerName,
        runs,
        ballsFaced,
        fours,
        sixes,
        getStrikeRate());
  }
}
