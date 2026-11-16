package model;

public class WicketInfo {

  private int wicketNumber;
  private String playerName;
  private int teamScore;
  private int legalBalls;

  public WicketInfo(
      int wicketNumber,
      String playerName,
      int teamScore,
      int legalBalls) {

    this.wicketNumber = wicketNumber;
    this.playerName = playerName;
    this.teamScore = teamScore;
    this.legalBalls = legalBalls;
  }

  public int getWicketNumber() {
    return wicketNumber;
  }

  public String getPlayerName() {
    return playerName;
  }

  public int getTeamScore() {
    return teamScore;
  }

  public int getLegalBalls() {
    return legalBalls;
  }

  public String getOvers() {
    return (legalBalls / 6) + "." + (legalBalls % 6);
  }

  public void display() {
    System.out.println(
        wicketNumber + "-" + teamScore
            + " (" + playerName + ", " + getOvers() + " overs)");
  }
}
