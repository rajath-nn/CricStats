import java.util.ArrayList;
import java.util.Scanner;

import model.Player;
import model.Team;
import model.Ball;
import model.Innings;
import engine.ScoringEngine;

public class Main {

  static Scanner sc = new Scanner(System.in);

  public static void main(String[] args) {

    System.out.println("==================================");
    System.out.println("       CRICSTATS SYSTEM");
    System.out.println("       LIVE SCORING");
    System.out.println("==================================");

    Team csk = new Team("CSK");
    Team rcb = new Team("RCB");

    String[] cskNames = {
        "Dhoni", "Ruturaj", "Jadeja", "Dube", "Gaikwad",
        "Rahul", "Shivam", "Ashwin", "Deepak", "Pathirana", "Conway"
    };

    String[] rcbNames = {
        "Kohli", "Rajat", "Maxwell", "Siraj", "Dayal",
        "Karthik", "Lomror", "Faf", "Green", "Karn", "Topley"
    };

    for (int i = 0; i < cskNames.length; i++) {
      String role = (i == 0) ? "Wicketkeeper" : "Batsman";
      csk.addPlayer(new Player(cskNames[i], i + 1, role));
    }

    for (int i = 0; i < rcbNames.length; i++) {
      rcb.addPlayer(new Player(rcbNames[i], i + 1, "Player"));
    }

    ScoringEngine engine = new ScoringEngine();

    // FIRST INNINGS
    Innings firstInnings = new Innings(csk, rcb, 2);

    System.out.println("\nMatch: CSK vs RCB");
    System.out.println("First Innings: CSK batting");
    System.out.println("Maximum Overs: 20");

    startScoring(firstInnings, engine);

    System.out.println("\n========== FIRST INNINGS SUMMARY ==========");
    firstInnings.displayScore();
    firstInnings.displayBattingScorecard();
    firstInnings.displayBowlingScorecard();
    firstInnings.displayFallOfWickets();

    // Do not start the chase if the first innings was stopped manually.
    if (!firstInnings.isCompleted()) {
      System.out.println("\nFirst innings is incomplete.");
      System.out.println("Match cannot proceed to the second innings.");
      sc.close();
      return;
    }

    int target = firstInnings.getTotalRuns() + 1;

    System.out.println("\n========== SECOND INNINGS ==========");
    System.out.println("Target for RCB: " + target + " runs");

    // SECOND INNINGS
    Innings secondInnings = new Innings(rcb, csk, 2);

    startScoring(secondInnings, engine, target);

    System.out.println("\n========== SECOND INNINGS SUMMARY ==========");
    secondInnings.displayScore();
    secondInnings.displayBattingScorecard();
    secondInnings.displayBowlingScorecard();
    secondInnings.displayFallOfWickets();

    // MATCH RESULT
    System.out.println("\n========== MATCH RESULT ==========");

    if (secondInnings.getTotalRuns() >= target) {

      int wicketsRemaining = 10 - secondInnings.getTotalWickets();

      System.out.println("RCB WON BY " + wicketsRemaining + " WICKETS!");

    } else if (secondInnings.isCompleted()) {

      int runsDifference = target - 1 - secondInnings.getTotalRuns();

      if (runsDifference == 0) {
        System.out.println("MATCH TIED!");
      } else {
        System.out.println("CSK WON BY " + runsDifference + " RUNS!");
      }

    } else {

      System.out.println("Match incomplete. No winner declared.");

    }

    System.out.println("\nThank you for using CricStats!");

    sc.close();
  }

  // Interactive scoring system
  public static void startScoring(
      Innings innings,
      ScoringEngine engine,
      int target) {

    startScoring(innings, engine, target, true);
  }

  public static void startScoring(
      Innings innings,
      ScoringEngine engine) {

    startScoring(innings, engine, 0, false);
  }

  private static void startScoring(
      Innings innings,
      ScoringEngine engine,
      int target,
      boolean chasing) {

    // Select opening batters
    System.out.println("\n========== OPENING BATTERS ==========");

    Player striker = selectPlayer(
        innings.getBattingTeam(),
        "Select opening striker: ",
        innings, null, null, true);

    Player nonStriker = selectPlayer(
        innings.getBattingTeam(),
        "Select opening non-striker: ",
        innings, striker, null, true);

    innings.setOpeningBatters(striker, nonStriker);

    // Tracks whether a bowler has been selected for the current over.
    boolean bowlerSelectedForCurrentOver = false;

    while (!innings.isCompleted()) {

      // Select bowler only once at the beginning of each over
      if (!bowlerSelectedForCurrentOver) {

        System.out.println("\n========== BOWLER SELECTION ==========");

        Player previousBowler = innings.getCurrentBowler();

        Player bowler = selectPlayer(
            innings.getBowlingTeam(),
            "Select bowler for this over: ",
            null, previousBowler, null, false);

        innings.setCurrentBowler(bowler);

        bowlerSelectedForCurrentOver = true;
      }
      System.out.println("\n========== CURRENT MATCH ==========");
      System.out.println("Striker: " + innings.getStriker().getName());
      System.out.println("Non-Striker: " + innings.getNonStriker().getName());
      System.out.println("Bowler: " + innings.getCurrentBowler().getName());

      if (chasing) {
        System.out.println("Target: " + target);
        System.out.println("Runs Needed: " + innings.getRunsNeeded(target));
        System.out.printf("Required Run Rate: %.2f%n",
            innings.getRequiredRunRate(target));
      }

      System.out.println("\n========== NEW DELIVERY ==========");
      System.out.println("1. Normal delivery");
      System.out.println("2. Wide");
      System.out.println("3. No-ball");
      System.out.println("4. Bye");
      System.out.println("5. Leg-bye");

      int choice = readInt("Select delivery type: ", 1, 5);

      int batterRuns = 0;
      int extraRuns = 0;
      String extraType = Ball.NONE;

      switch (choice) {
        case 1:
          batterRuns = readInt("Enter batter runs (0-6): ", 0, 6);
          break;

        case 2:
          extraType = Ball.WIDE;
          extraRuns = readInt("Enter total wide extras (minimum 1): ", 1, 7);
          break;

        case 3:
          extraType = Ball.NO_BALL;
          extraRuns = 1;
          batterRuns = readInt("Enter batter runs (0-6): ", 0, 6);
          System.out.println("No-ball penalty of 1 run added.");
          break;

        case 4:
          extraType = Ball.BYE;
          extraRuns = readInt("Enter bye runs (1-6): ", 1, 6);
          break;

        case 5:
          extraType = Ball.LEG_BYE;
          extraRuns = readInt("Enter leg-bye runs (1-6): ", 1, 6);
          break;
      }

      // Wicket information
      boolean wicket = readYesNo("Did a wicket fall? (yes/no): ");

      String wicketType = null;
      String dismissedPlayer = null;

      if (wicket) {

        System.out.println("\nSelect dismissal type:");
        System.out.println("1. Bowled");
        System.out.println("2. Caught");
        System.out.println("3. LBW");
        System.out.println("4. Run Out");
        System.out.println("5. Stumped");
        System.out.println("6. Hit Wicket");

        int wicketChoice = readInt("Enter choice: ", 1, 6);

        switch (wicketChoice) {
          case 1:
            wicketType = "BOWLED";
            break;
          case 2:
            wicketType = "CAUGHT";
            break;
          case 3:
            wicketType = "LBW";
            break;
          case 4:
            wicketType = "RUN_OUT";
            break;
          case 5:
            wicketType = "STUMPED";
            break;
          case 6:
            wicketType = "HIT_WICKET";
            break;
        }

        if (wicketType.equals("RUN_OUT")) {

          System.out.println("\nWho was run out?");
          System.out.println("1. " + innings.getStriker().getName());
          System.out.println("2. " + innings.getNonStriker().getName());

          int dismissedChoice = readInt(
              "Select dismissed batter: ", 1, 2);

          dismissedPlayer = (dismissedChoice == 1)
              ? innings.getStriker().getName()
              : innings.getNonStriker().getName();

        } else {

          dismissedPlayer = innings.getStriker().getName();

          System.out.println(
              dismissedPlayer + " is dismissed (" + wicketType + ").");
        }
      }

      // Create delivery
      Ball ball = new Ball(
          batterRuns,
          extraRuns,
          extraType,
          wicket,
          wicketType,
          dismissedPlayer);

      // Record delivery
      try {
        boolean recorded = engine.recordDelivery(innings, ball);

        if (!recorded) {
          break;
        }

      } catch (IllegalArgumentException | IllegalStateException e) {
        System.out.println("\nError: " + e.getMessage());
        System.out.println("Delivery was not recorded.");
        continue;
      }

      // Allow a new bowler after six legal deliveries
      if (innings.getLegalBalls() > 0
          && innings.getLegalBalls() % 6 == 0) {

        bowlerSelectedForCurrentOver = false;
      }
      // Stop when target is achieved
      if (chasing && innings.getTotalRuns() >= target) {
        System.out.println("\nTARGET ACHIEVED!");
        break;
      }

      // Select replacement batter
      if (innings.hasPendingReplacement()) {

        System.out.println("\n========== NEW BATTER ==========");

        Player replacement = selectPlayer(
            innings.getBattingTeam(),
            "Select replacement batter: ",
            innings,
            innings.getStriker(),
            innings.getNonStriker(),
            true);

        if (replacement == null) {
          System.out.println("No replacement batter available.");
          break;
        }

        innings.replaceDismissedBatter(
            innings.getPendingDismissedPlayer().getName(),
            replacement);

        System.out.println(
            replacement.getName() + " comes to the crease.");
      }

      if (!innings.isCompleted()) {

        boolean continueScoring = readYesNo(
            "\nContinue scoring? (yes/no): ");

        if (!continueScoring) {
          break;
        }
      }
    }

    // Final innings message
    if (innings.isCompleted()) {
      System.out.println("\nINNINGS COMPLETED!");

    } else if (chasing && innings.getTotalRuns() >= target) {
      System.out.println("\nCHASE COMPLETED!");

    } else {
      System.out.println("\nScoring stopped by user.");
    }
  }

  // Select an eligible player from a team
  public static Player selectPlayer(
      Team team,
      String message,
      Innings innings,
      Player exclude1,
      Player exclude2,
      boolean excludeDismissed) {

    ArrayList<Player> eligiblePlayers = new ArrayList<>();

    for (Player player : team.getPlayers()) {

      if (player.equals(exclude1) || player.equals(exclude2)) {
        continue;
      }

      if (innings != null && excludeDismissed
          && innings.isDismissed(player)) {
        continue;
      }

      eligiblePlayers.add(player);
    }

    if (eligiblePlayers.isEmpty()) {
      return null;
    }

    System.out.println("\nAvailable players:");

    for (int i = 0; i < eligiblePlayers.size(); i++) {
      System.out.println(
          (i + 1) + ". " + eligiblePlayers.get(i).getName());
    }

    int selection = readInt(
        message, 1, eligiblePlayers.size());

    return eligiblePlayers.get(selection - 1);
  }

  // Read an integer within a range
  public static int readInt(String message, int min, int max) {

    while (true) {

      System.out.print(message);
      String input = sc.nextLine().trim();

      try {
        int value = Integer.parseInt(input);

        if (value >= min && value <= max) {
          return value;
        }

        System.out.println(
            "Enter a number between " + min + " and " + max + ".");

      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number!");
      }
    }
  }

  // Read yes or no
  public static boolean readYesNo(String message) {

    while (true) {

      System.out.print(message);
      String input = sc.nextLine().trim();

      if (input.equalsIgnoreCase("yes")
          || input.equalsIgnoreCase("y")) {
        return true;
      }

      if (input.equalsIgnoreCase("no")
          || input.equalsIgnoreCase("n")) {
        return false;
      }

      System.out.println("Please enter yes or no!");
    }
  }
}
