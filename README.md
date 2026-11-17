# CP-081 CricStats: Ball-by-Ball Cricket Scoring and Analytics Engine
## Team
Ningaraju (1AT23CS140), Rajath N N (1AT23CS165), Java track
## How to Run
Open the terminal in the project root directory and execute:

```
javac Main.java
java Main
```

## Features That Work

### Core Features

1. Create and manage two cricket teams with 11 players each.
2. Select opening batters and the bowler for each over.
3. Record ball-by-ball scoring.
4. Support normal deliveries, wides, no-balls, byes and leg-byes.
5. Record wickets with different dismissal types:
   - Bowled
   - Caught
   - LBW
   - Run Out
   - Stumped
   - Hit Wicket
6. Automatically maintain batting statistics:
   - Runs scored
   - Balls faced
   - Fours and sixes
   - Strike rate
7. Automatically maintain bowling statistics:
   - Overs bowled
   - Maidens
   - Runs conceded
   - Wickets
   - Economy rate
8. Calculate and display the current run rate.
9. Calculate runs needed and required run rate during the second innings.
10. Generate complete batting and bowling scorecards.
11. Maintain and display the fall-of-wickets list.
12. Automatically handle strike rotation and bowler changes.
13. Support replacement batters after dismissals.
14. End an innings after the maximum overs or 10 wickets.
15. Display match results, including successful chases, wins by runs, ties and incomplete matches.

### Additional Validated Functionality

- Validate player selections and team membership.
- Prevent invalid deliveries and dismissal combinations, such as LBW on a no-ball.
- Prevent scoring after an innings is completed.
- Support configurable maximum overs for testing and match simulation.
- Maintain actual batting order in the batting scorecard.

## Technologies Used

- **Programming Language:** Java
- **Concepts:** Object-Oriented Programming (OOP)
- **Collections:** ArrayList, HashMap
- **Packages:** model, engine
- **Development Environment:** Visual Studio Code

## Project Structure

```
CricStats/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Player.java
│   │   ├── Team.java
│   │   ├── Ball.java
│   │   ├── Innings.java
│   │   ├── BattingStats.java
│   │   ├── BowlingStats.java
│   │   └── WicketInfo.java
│   └── engine/
│       └── ScoringEngine.java
├── out/
└── README.md
```

## Sample Run

The application displays:

- Team and player setup
- Opening batter and bowler selection
- Delivery-by-delivery scoring details
- Current innings score and run rate
- Required run rate during the chase
- Batting and bowling scorecards
- Fall of wickets
- Final match result

### Sample Output

```
==================================
       CRICSTATS SYSTEM
       LIVE SCORING
==================================

Match: CSK vs RCB
First Innings: CSK batting

========== INNINGS SCORE ==========
Batting Team: CSK
Score: 12/0
Overs: 2.0
Run Rate: 6.00
===================================

========== MATCH RESULT ==========
MATCH TIED!
```

*Note: The sample output above illustrates the format. Actual scores and match results depend on user input.*

## Project Scope

This project focuses on automated cricket scoring and match statistics through a Java console application. It demonstrates object-oriented design, data management using Java collections, validation and real-time score calculations.
