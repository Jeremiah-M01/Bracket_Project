# Bracket

### Description:
 In my Algorithms and Data Structures class we developed our internal representation of what a bracket was and what we wanted it to do like for example when a team won a game they would advance to the next position in their group and how the bracket was changed depended on the participants. We developed the bracket only given a list of participants which was our overarching data structure and the list was two times the participants minus one because there would be an overall champion. The easiest way to view the list was in a perfect binary tree with all the participants at the bottom which was how most of my methods I implemented revolved around. The main implementation for the project is in the BracketImpl_Martinez file. The project is an introduction into how most other bracket structures are created for sports like NCAA basketball and so on.

### Visualization:
The participants list is viewed as a perfect binary tree the length depending on how many participants there are. Each node is a grouping which contains the winner or if it is blank all the participants that can get to that position.

<img width="300" height="200" alt="image" src="https://github.com/user-attachments/assets/d66e0446-b033-47d6-a95c-bcf440747b82" />

### Example Implementation:

```
List<FIFASoccerTeam> worldCup2014KnockoutRoundMatchups = Arrays.asList(BRAZIL, CHILE, COLOMBIA, URUGUAY, FRANCE, NIGERIA, GERMANY, ALGERIA, NETHERLANDS, MEXICO, COSTA_RICA, GREECE, ARGENTINA, SWITZERLAND, BELGIUM, USA);
		
worldCup2014KnockoutResults = new BracketImpl_Martinez<FIFASoccerTeam>(worldCup2014KnockoutRoundMatchups);
worldCup2014KnockoutResults.setWinCount(GERMANY, 4);
worldCup2014KnockoutResults.setWinCount(NETHERLANDS, 2);
worldCup2014KnockoutResults.setWinCount(BRAZIL, 3);
worldCup2014KnockoutResults.setWinCount(ARGENTINA, 1);
worldCup2014KnockoutResults.setWinCount(COLOMBIA, 2);
worldCup2014KnockoutResults.setWinCount(FRANCE, 1);
worldCup2014KnockoutResults.setWinCount(COSTA_RICA, 1);
```
Here the FIFASoccerTeam is an enumerated class that contains the teams listed which is used to pass into the participants list. The next following lines are using setWinCount to change the state of the bracket. Here GERMANY will be the champion. For more examples in depth check out the test folder.


Credits: Instructor - Dr. Kart, Image - GeeksForGeeks
