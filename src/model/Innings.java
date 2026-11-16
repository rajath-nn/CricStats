package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.WicketInfo;

public class Innings {

  private Team battingTeam;
  private Team bowlingTeam;

  private ArrayList<Ball> deliveries;

  private Player striker;
  private Player nonStriker;
  private Player currentBowler;

  private ArrayList<Player> dismissedPlayers;
  private ArrayList<Player> battingOrder;
  private Player pendingDismissed;
  private Map<Player, BattingStats> battingStatistics;
  private Map<Player, BowlingStats> bowlingStatistics;
  private ArrayList<WicketInfo> fallOfWickets;

  private int totalRuns;
  private int totalWickets;
  private int legalBalls;
  private int maxOvers;

  public Innings(Team battingTeam, Team bowlingTeam, int maxOvers) {
    if (battingTeam == null || bowlingTeam == null) {
      throw new IllegalArgumentException("Teams cannot be null.");
    }

    if (battingTeam == bowlingTeam) {
      throw new IllegalArgumentException(
          "Batting and bowling teams must be different.");
    }

    if (maxOvers <= 0) {
      throw new IllegalArgumentException(
          "Maximum overs must be greater than zero.");
    }

    this.battingTeam = battingTeam;
    this.bowlingTeam = bowlingTeam;
    this.maxOvers = maxOvers;

    deliveries = new ArrayList<>();
    dismissedPlayers = new ArrayList<>();
    battingOrder = new ArrayList<>();
    fallOfWickets = new ArrayList<>();
    battingStatistics = new HashMap<>();

    for (Player player : battingTeam.getPlayers()) {
      battingStatistics.put(player, new BattingStats());
    }

    bowlingStatistics = new HashMap<>();

    for (Player player : bowlingTeam.getPlayers()) {
      bowlingStatistics.put(player, new BowlingStats());
    }

    totalRuns = 0;
    totalWickets = 0;
    legalBalls = 0;
  }

  public void setOpeningBatters(Player striker, Player nonStriker) {

    if (!deliveries.isEmpty()) {
      throw new IllegalStateException(
          "Opening batters cannot be changed after scoring starts.");
    }

    if (striker == null || nonStriker == null
        || striker.equals(nonStriker)) {
      throw new IllegalArgumentException(
          "Select two different opening batters.");
    }

    if (!battingTeam.containsPlayer(striker)
        || !battingTeam.containsPlayer(nonStriker)) {
      throw new IllegalArgumentException(
          "Both batters must belong to the batting team.");
    }

    this.striker = striker;
    this.nonStriker = nonStriker;

    battingOrder.add(striker);
    battingOrder.add(nonStriker);
  }

  public void setCurrentBowler(Player bowler) {

    if (bowler == null || !bowlingTeam.containsPlayer(bowler)) {
      throw new IllegalArgumentException(
          "Bowler must belong to the bowling team.");
    }

    currentBowler = bowler;
  }

  public Player getStriker() {
    return striker;
  }

  public Player getNonStriker() {
    return nonStriker;
  }

  public Player getCurrentBowler() {
    return currentBowler;
  }

  public boolean isDismissed(Player player) {
    return dismissedPlayers.contains(player);
  }

  public Player getPendingDismissedPlayer() {
    return pendingDismissed;
  }

  public boolean hasPendingReplacement() {
    return pendingDismissed != null;
  }

  // Record and process one delivery.
  public boolean recordDelivery(Ball ball) {

    if (ball == null) {
      throw new IllegalArgumentException(
          "Delivery cannot be null.");
    }

    if (isCompleted()) {
      System.out.println("Innings is already completed!");
      return false;
    }

    if (striker == null || nonStriker == null) {
      throw new IllegalStateException(
          "Select the opening batters before scoring.");
    }

    if (pendingDismissed != null) {
      throw new IllegalStateException(
          "Select a replacement batter before continuing.");
    }

    Player dismissed = null;

    // Validate the dismissed player before changing the score.
    if (ball.isWicket()) {

      dismissed = battingTeam.findPlayerByName(
          ball.getDismissedPlayer());

      if (dismissed == null
          || (!dismissed.equals(striker)
              && !dismissed.equals(nonStriker))) {

        throw new IllegalArgumentException(
            "The dismissed player must be one of the current batters.");
      }
    }

    // Remember who faced this delivery.
    Player batterFacing = striker;
    Player bowlerFacing = currentBowler;

    if (bowlerFacing == null) {
      throw new IllegalStateException(
          "Select a bowler before scoring.");
    }

    deliveries.add(ball);

    battingStatistics.get(batterFacing).recordDelivery(ball);

    bowlingStatistics.get(bowlerFacing).recordDelivery(ball);

    totalRuns += ball.getTotalRuns();

    if (ball.isLegalDelivery()) {
      legalBalls++;
    }

    if (ball.isWicket()) {
      totalWickets++;

      fallOfWickets.add(new WicketInfo(
          totalWickets,
          dismissed.getName(),
          totalRuns,
          legalBalls));
    }

    // Odd completed runs change the strike.
    if (ball.getCompletedRuns() % 2 != 0) {
      swapStrike();
    }

    // At the end of an over, the batters change ends.
    if (ball.isLegalDelivery() && legalBalls % 6 == 0) {
      swapStrike();
    }

    // Mark the dismissed batter and wait for a replacement.
    if (dismissed != null) {
      dismissedPlayers.add(dismissed);

      if (!isCompleted()) {
        pendingDismissed = dismissed;
      }
    }

    return true;
  }

  // Keep the existing method name for compatibility.
  public void addDelivery(Ball ball) {
    recordDelivery(ball);
  }

  private void swapStrike() {
    Player temp = striker;
    striker = nonStriker;
    nonStriker = temp;
  }

  public void replaceDismissedBatter(
      String dismissedName,
      Player replacement) {

    if (pendingDismissed == null) {
      throw new IllegalStateException(
          "There is no dismissed batter waiting for replacement.");
    }

    if (dismissedName == null || replacement == null) {
      throw new IllegalArgumentException(
          "Player details cannot be empty.");
    }

    if (!pendingDismissed.getName().equalsIgnoreCase(
        dismissedName.trim())) {
      throw new IllegalArgumentException(
          "Incorrect dismissed player.");
    }

    if (!battingTeam.containsPlayer(replacement)
        || isDismissed(replacement)
        || replacement.equals(striker)
        || replacement.equals(nonStriker)) {

      throw new IllegalArgumentException(
          "Invalid replacement batter.");
    }

    if (pendingDismissed.equals(striker)) {
      striker = replacement;
    } else if (pendingDismissed.equals(nonStriker)) {
      nonStriker = replacement;
    } else {
      throw new IllegalStateException(
          "Dismissed batter is not in the current batting pair.");
    }
    battingOrder.add(replacement);
    pendingDismissed = null;
  }

  public Team getBattingTeam() {
    return battingTeam;
  }

  public Team getBowlingTeam() {
    return bowlingTeam;
  }

  public List<Ball> getDeliveries() {
    return Collections.unmodifiableList(deliveries);
  }

  public int getTotalRuns() {
    return totalRuns;
  }

  public int getTotalWickets() {
    return totalWickets;
  }

  public int getLegalBalls() {
    return legalBalls;
  }

  public int getMaxOvers() {
    return maxOvers;
  }

  public String getOvers() {
    return (legalBalls / 6) + "." + (legalBalls % 6);
  }

  public double getRunRate() {

    if (legalBalls == 0) {
      return 0.0;
    }

    return (double) totalRuns * 6 / legalBalls;
  }

  public boolean isCompleted() {
    return totalWickets >= 10
        || legalBalls >= maxOvers * 6;
  }

  public void displayScore() {

    System.out.println("\n========== INNINGS SCORE ==========");

    System.out.println(
        "Batting Team: " + battingTeam.getTeamName());

    System.out.println(
        "Score: " + totalRuns + "/" + totalWickets);

    System.out.println("Overs: " + getOvers());

    System.out.printf(
        "Run Rate: %.2f%n", getRunRate());

    System.out.println(
        "Striker: " +
            (striker != null ? striker.getName() : "Not selected"));

    System.out.println(
        "Non-Striker: " +
            (nonStriker != null ? nonStriker.getName() : "Not selected"));

    System.out.println(
        "Bowler: " +
            (currentBowler != null ? currentBowler.getName() : "Not selected"));

    System.out.println("===================================");
  }

  // Get statistics of a particular batter.
  public BattingStats getBattingStats(Player player) {
    return battingStatistics.get(player);
  }

  // Display batting scorecard.
  public void displayBattingScorecard() {

    System.out.println("\n============== BATTING SCORECARD ==============");

    System.out.printf(
        "%-15s %5s %5s %5s %5s %8s%n",
        "Batter", "Runs", "Balls", "4s", "6s", "SR");

    // Display players in their actual batting order.
    for (Player player : battingOrder) {

      BattingStats stats = battingStatistics.get(player);

      if (stats != null) {
        stats.displayStats(player.getName());
      }
    }

    // Display players who have not yet come to bat.
    for (Player player : battingTeam.getPlayers()) {

      if (!battingOrder.contains(player)) {

        BattingStats stats = battingStatistics.get(player);

        if (stats != null) {
          stats.displayStats(player.getName());
        }
      }
    }

    System.out.println("================================================");
  }

  public BowlingStats getBowlingStats(Player player) {
    return bowlingStatistics.get(player);
  }

  public void displayBowlingScorecard() {

    System.out.println("\n============== BOWLING SCORECARD ==============");

    System.out.printf(
        "%-15s %5s %5s %5s %5s %8s%n",
        "Bowler", "Overs", "M", "Runs", "Wkts", "Economy");

    for (Player player : bowlingTeam.getPlayers()) {

      BowlingStats stats = bowlingStatistics.get(player);

      if (stats != null) {
        stats.displayStats(player.getName());
      }
    }

    System.out.println("================================================");
  }

  public void displayFallOfWickets() {
    System.out.println("\n========== FALL OF WICKETS ==========");

    if (fallOfWickets.isEmpty()) {
      System.out.println("No wickets lost.");
    } else {
      for (WicketInfo info : fallOfWickets) {
        info.display();
      }
    }

    System.out.println("=====================================");
  }

  public int getRunsNeeded(int target) {
    return Math.max(0, target - totalRuns);
  }

  public double getRequiredRunRate(int target) {

    int runsNeeded = getRunsNeeded(target);
    int ballsRemaining = (maxOvers * 6) - legalBalls;

    if (runsNeeded == 0 || ballsRemaining <= 0) {
      return 0.0;
    }

    return (double) runsNeeded * 6 / ballsRemaining;
  }
}
