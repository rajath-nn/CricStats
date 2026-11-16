package engine;

import model.Ball;
import model.Innings;

public class ScoringEngine {

  // Process one delivery
  public boolean recordDelivery(Innings innings, Ball ball) {

    if (innings == null || ball == null) {
      throw new IllegalArgumentException(
          "Innings and delivery cannot be null!");
    }

    if (innings.isCompleted()) {
      System.out.println(
          "Cannot record delivery. Innings is completed!");
      return false;
    }

    // Validate dismissal rules before recording the delivery.
    validateDismissal(ball);

    boolean recorded = innings.recordDelivery(ball);

    if (!recorded) {
      return false;
    }

    System.out.println("\nDelivery recorded successfully!");

    displayDelivery(ball);
    innings.displayScore();

    if (innings.hasPendingReplacement()) {
      System.out.println(
          "\nA replacement batter must be selected before the next delivery.");
    }

    return true;
  }

  // Validate dismissal according to delivery type.
  private void validateDismissal(Ball ball) {

    if (!ball.isWicket()) {
      return;
    }

    String wicketType = ball.getWicketType();

    if (wicketType == null) {
      throw new IllegalArgumentException(
          "Wicket type cannot be empty!");
    }

    wicketType = wicketType.trim().toUpperCase();

    boolean validType = wicketType.equals("BOWLED")
        || wicketType.equals("CAUGHT")
        || wicketType.equals("LBW")
        || wicketType.equals("RUN_OUT")
        || wicketType.equals("STUMPED")
        || wicketType.equals("HIT_WICKET");

    if (!validType) {
      throw new IllegalArgumentException(
          "Invalid wicket type!");
    }

    String extraType = ball.getExtraType();

    // On a no-ball, only Run Out is allowed
    if (extraType.equals(Ball.NO_BALL)
        && !wicketType.equals("RUN_OUT")) {

      throw new IllegalArgumentException(
          "Invalid dismissal! Only Run Out is allowed on a no-ball.");
    }

    // On a wide, only Run Out and Stumped are allowed
    if (extraType.equals(Ball.WIDE)
        && !wicketType.equals("RUN_OUT")
        && !wicketType.equals("STUMPED")) {

      throw new IllegalArgumentException(
          "Invalid dismissal on a wide! Only Run Out or Stumped is allowed.");
    }
  }

  // Display delivery information
  public void displayDelivery(Ball ball) {

    System.out.println("\n========== DELIVERY DETAILS ==========");

    System.out.println("Batter Runs: " + ball.getBatterRuns());
    System.out.println("Extra Runs: " + ball.getExtraRuns());
    System.out.println("Extra Type: " + ball.getExtraType());
    System.out.println("Total Runs: " + ball.getTotalRuns());

    if (ball.isWicket()) {
      System.out.println("WICKET!");
      System.out.println(
          "Dismissed Player: " + ball.getDismissedPlayer());
      System.out.println(
          "Wicket Type: " + ball.getWicketType());
    } else {
      System.out.println("Wicket: No");
    }

    System.out.println(
        "Legal Delivery: " +
            (ball.isLegalDelivery() ? "Yes" : "No"));

    System.out.println("=======================================");
  }
}
