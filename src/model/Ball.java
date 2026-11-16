package model;

public class Ball {

  public static final String NONE = "NONE";
  public static final String WIDE = "WIDE";
  public static final String NO_BALL = "NO_BALL";
  public static final String BYE = "BYE";
  public static final String LEG_BYE = "LEG_BYE";

  private int batterRuns;
  private int extraRuns;
  private int completedRuns;
  private String extraType;

  private boolean wicket;
  private String wicketType;
  private String dismissedPlayer;

  public Ball(
      int batterRuns,
      int extraRuns,
      String extraType,
      boolean wicket,
      String wicketType,
      String dismissedPlayer) {

    if (batterRuns < 0 || extraRuns < 0) {
      throw new IllegalArgumentException("Runs cannot be negative!");
    }

    if (extraType == null) {
      throw new IllegalArgumentException("Extra type cannot be null!");
    }

    extraType = extraType.trim().toUpperCase();

    if (!extraType.equals(NONE)
        && !extraType.equals(WIDE)
        && !extraType.equals(NO_BALL)
        && !extraType.equals(BYE)
        && !extraType.equals(LEG_BYE)) {
      throw new IllegalArgumentException("Invalid extra type!");
    }

    if (extraType.equals(NONE) && extraRuns != 0) {
      throw new IllegalArgumentException(
          "Normal delivery cannot have extras!");
    }

    if ((extraType.equals(WIDE) || extraType.equals(NO_BALL))
        && extraRuns < 1) {
      throw new IllegalArgumentException(
          "Wide and no-ball must include at least one penalty run!");
    }

    if ((extraType.equals(BYE) || extraType.equals(LEG_BYE))
        && batterRuns != 0) {
      throw new IllegalArgumentException(
          "Bye and leg-bye runs cannot be credited to batter!");
    }

    if (extraType.equals(WIDE) && batterRuns != 0) {
      throw new IllegalArgumentException(
          "Wide runs cannot be credited to batter!");
    }

    if (wicket && (wicketType == null || wicketType.trim().isEmpty()
        || dismissedPlayer == null || dismissedPlayer.trim().isEmpty())) {
      throw new IllegalArgumentException(
          "Wicket type and dismissed player are required!");
    }

    this.batterRuns = batterRuns;
    this.extraRuns = extraRuns;
    this.extraType = extraType;
    this.wicket = wicket;
    this.wicketType = wicketType;
    this.dismissedPlayer = dismissedPlayer;

    // Estimate completed runs for basic strike rotation.
    if (extraType.equals(WIDE)) {
      this.completedRuns = Math.max(0, extraRuns - 1);
    } else if (extraType.equals(BYE) || extraType.equals(LEG_BYE)) {
      this.completedRuns = extraRuns;
    } else {
      this.completedRuns = batterRuns;
    }
  }

  public int getBatterRuns() {
    return batterRuns;
  }

  public int getExtraRuns() {
    return extraRuns;
  }

  public int getCompletedRuns() {
    return completedRuns;
  }

  public String getExtraType() {
    return extraType;
  }

  public boolean isWicket() {
    return wicket;
  }

  public String getWicketType() {
    return wicketType;
  }

  public String getDismissedPlayer() {
    return dismissedPlayer;
  }

  public int getTotalRuns() {
    return batterRuns + extraRuns;
  }

  public boolean isLegalDelivery() {
    return !extraType.equals(WIDE)
        && !extraType.equals(NO_BALL);
  }
}
