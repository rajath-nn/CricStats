package model;

import java.util.ArrayList;

public class Team {

  private String teamName;
  private ArrayList<Player> players;

  private Player captain;
  private Player wicketkeeper;

  public Team(String teamName) {
    this.teamName = teamName;
    this.players = new ArrayList<>();
  }

  public String getTeamName() {
    return teamName;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public Player getCaptain() {
    return captain;
  }

  public Player getWicketkeeper() {
    return wicketkeeper;
  }

  public void setCaptain(Player captain) {
    this.captain = captain;
  }

  public void setWicketkeeper(Player wicketkeeper) {
    this.wicketkeeper = wicketkeeper;
  }

  public void addPlayer(Player player) {

    if (players.size() >= 11) {
      System.out.println("Team already has 11 players!");
      return;
    }

    players.add(player);

    System.out.println(
        player.getName() + " added successfully!");
  }

  public void displayTeam() {

    System.out.println("\n========== " + teamName + " ==========");

    for (int i = 0; i < players.size(); i++) {

      System.out.print((i + 1) + ". ");
      players.get(i).displayPlayer();
    }

    System.out.println("Captain: " +
        (captain != null ? captain.getName() : "Not selected"));

    System.out.println("Wicketkeeper: " +
        (wicketkeeper != null ? wicketkeeper.getName() : "Not selected"));

    System.out.println("Total Players: " + players.size());
  }

  // Find a player using their name
  public Player findPlayerByName(String name) {

    if (name == null || name.trim().isEmpty()) {
      return null;
    }

    for (Player player : players) {

      if (player.getName().equalsIgnoreCase(name.trim())) {
        return player;
      }
    }

    return null;
  }

  // Check whether a player belongs to this team
  public boolean containsPlayer(Player player) {

    return player != null && players.contains(player);
  }

}
