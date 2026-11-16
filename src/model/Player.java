package model;

public class Player {

  private String name;
  private int jerseyNumber;
  private String role;

  public Player(String name, int jerseyNumber, String role) {
    this.name = name;
    this.jerseyNumber = jerseyNumber;
    this.role = role;
  }

  public String getName() {
    return name;
  }

  public int getJerseyNumber() {
    return jerseyNumber;
  }

  public String getRole() {
    return role;
  }

  public void displayPlayer() {
    System.out.println(
        jerseyNumber + " | " + name + " | " + role);
  }
}
