package tournament;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BracketImpl_Martinez<P> extends BracketAbstract<P>
{
	public BracketImpl_Martinez(List<P> participantMatchups)
	{
		super(participantMatchups);
	}
	
	@Override
	public int getMaxLevel()
	{
		// lg(# of Participants)
		// used change of base formula
		int maxLevel = (int) (Math.log10((predictions.size()+ 1) / 2) / Math.log10(2));
		
		return maxLevel;
	}
	
	// make helper method for and clean up
	@Override
	public Set<Set<P>> getGroupings(int level)
	{
		assert level <= getMaxLevel() && level >= 0: "Level is out of Bounds";
		
		Set<Set<P>> groupings = new HashSet<Set<P>>();
		Set<P> groupingCandidate = new HashSet<P>();
		
		// Need start index in predictions list at beginning of zero level
		int currentZeroLevelIndex = (int) (Math.pow(2, getMaxLevel()) -1);
		
		int numOfParticipantsInGroup = (int) (Math.pow(2, level));
		// calculation for numOfGroups in level (# of participants / 2 * level)
		int pow2ToLevel = (int) Math.pow(2, level);
		int numOfGroupsInLevel = ((predictions.size() + 1) / 2) / pow2ToLevel;
		
		
		for (int i = 0; i < numOfGroupsInLevel; i++) {
			
			for (int j = currentZeroLevelIndex; j < predictions.size(); j++) {
				
				currentZeroLevelIndex = j;
				if (groupingCandidate.size() == numOfParticipantsInGroup) {
					break;
				}
				groupingCandidate.add(predictions.get(j));
			}
			
			// in case currentZeroIndex doesn't work
			// indexLeftOff = (numOfParticipantsInGroup * i) + (predictions.size()- 1) - 1;
			
			// add the group by creating a copy (pass by value)
			groupings.add(new HashSet<P>(groupingCandidate));
			
			// clear the group
			groupingCandidate.clear();
		}
		
		return groupings;
	}

	// how to find left and right children L: 2 * index +1 or R: 2 * i + 2
	@Override
	public Set<P> getViableParticipants(Set<P> grouping)
	{
		int levelCheck = 0;
		boolean isFound = false;
		for (int level = getMaxLevel(); level > 0; level--) {
			
			Set<Set<P>> currentGroupings = getGroupings(level);
			for (Set<P> group: currentGroupings) {
				if (group.equals(grouping)) {
					levelCheck = level;
					isFound = true;
					break;
				}
			}
		}
		assert isFound : "grouping is not in bracket";
		
		// takes in a grouping and determines who can be there (the winner at the circle)
		// gets the root index of the subtree to check if empty
		Set<P> viableParticipants = new HashSet<P>();
		
		// Base case:
		if (grouping.size() == 1) {
			return grouping;
		} else {
			
			List<P> participantsList = new ArrayList<P>(grouping);
			
			// loop level times to find index of subtree
			int indexOfRootSubtree = getParentIndex(getParticipantIndex(participantsList.get(0)));
			
			if (levelCheck != 1) {
				for (int i = 1; i < levelCheck; i++) {
					indexOfRootSubtree = getParentIndex(indexOfRootSubtree);
				}
			}
			
			if (predictions.get(indexOfRootSubtree) != null) {
				viableParticipants.add(predictions.get(indexOfRootSubtree));
				return viableParticipants;
				
			} else {
				// get grouping given level and member
				Set<P> leftChildGroup = getGrouping(predictions.get((indexOfRootSubtree * 2) + 1), levelCheck - 1);
				Set<P> rightChildGroup = getGrouping(predictions.get((indexOfRootSubtree * 2) + 2), levelCheck - 1);
				
				viableParticipants.addAll(getViableParticipants(leftChildGroup));
				viableParticipants.addAll(getViableParticipants(rightChildGroup));
				return viableParticipants;
				
			}
			
		}
		
	}
	
	
	@Override
	public void setWinCount(P participant, int winCount)
	{
		assert participant != null: "Participant is null";
		// iterator check
		Iterator<Set<P>> iterator = getGroupings(getMaxLevel()).iterator();
		
		Set<P> currentGroup = iterator.next();
		assert currentGroup.contains(participant): "Participant Not in Tournament! " + participant + "\n" + currentGroup;
		assert winCount >= 0 && winCount <= getMaxLevel(): "winCount is too high or too low";
		
		// sets the participants in the predictions list and makes changes to above groupings if necessary
		int participantIndex = getParticipantIndex(participant);
		int parentIndex = getParentIndex(participantIndex);
		
		for (int level = 1; level <= winCount; level++) {
			
			predictions.set(parentIndex, participant);
			if (parentIndex != 0 && level != winCount) {
				parentIndex = getParentIndex(parentIndex);
			}
			
		}
		
		// Check for changes
		if (parentIndex != 0) {
			int parentCheckIndex = parentIndex; //getParentIndex(parentIndex);
			Set<P> groupingCheck = getGrouping(predictions.get(parentIndex), winCount);
			
			
			while (parentCheckIndex != 0) {
				parentCheckIndex = getParentIndex(parentCheckIndex);
				
				if (groupingCheck.contains(predictions.get(parentCheckIndex))) {
					predictions.set(parentCheckIndex, null);
					
				}
			}
			
		}
		
	}
	
	//Find two groupings a and b at a lower level such that a U b = grouping with a INT b = empty
	private Set<Set<P>> getSubordinateGroupings(Set<P> grouping)
	{
		assert grouping.size() > 1 : "grouping.size() = " + grouping.size() + " <= 1!: grouping = " + grouping;
		
		Set<Set<P>> subordinateGroupings = new HashSet<Set<P>>();
		
		Set<P> leftGrouping = new HashSet<P>();
		Set<P> rightGrouping = new HashSet<P>();
		
		
		
		return subordinateGroupings;
	}
	
	private int getHighestParticipantIndex(P participant) {
		
		return predictions.indexOf(participant);
	}
	
	private int getParticipantIndex(P participant)
	{
		return predictions.lastIndexOf(participant);
	}
	
	// used in set win count
	private static int getParentIndex(int childIndex)
	{
		return (childIndex - 1) / 2;
	}
	
	// used in set win count
	private Set<P> getGrouping(P member, int level)
	{
		Set<P> grouping = new HashSet<P>();
		Set<Set<P>> groupings = getGroupings(level);
		
		for (Set<P> group: groupings) {
			if (group.contains(member)) {
				grouping = group;
			}
		}
		
		return grouping;
	}
	
	
	public void printBracket() {
		// Goal is to print out the current bracket structure using the predictions list
		int nodeCountLevel = 1;
		int currentIndex = 0;
		
		for (int level = getMaxLevel(); level >= 0; level--) {
			
			for (int j = 0; j < nodeCountLevel; j++) {
				// print out the participants in the groupings on that level
				
				if (predictions.get(currentIndex) == null) {
					
					System.out.print("    "); // instead of *
					System.out.print("    ");
				} else {
					
					System.out.print(predictions.get(currentIndex));
					System.out.print("    ");
				}
				
				currentIndex++;
				
			}
			
			System.out.println();
			nodeCountLevel = nodeCountLevel * 2;
		}
		
	}
	
	@Override
	public String toString() {
		// Goal is to print out the current bracket structure using the predictions list
		// modify print Bracket into string
//		int nodeCountLevel = 1;
//		
//		for (int level = getMaxLevel(); level >= 0; level--) {
//			System.out.println(predictions.get(nodeCountLevel));
//			System.out.println();
//			
//			nodeCountLevel = nodeCountLevel * 2;
//		}
		printBracket();
		
		return "";
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean areSameType = (obj != null && Bracket.class.isAssignableFrom(obj.getClass()));
		
		boolean foundDisagreement = !areSameType;
		
		if (areSameType) {
			// Implement equals comparison look at permutation comparison
			
			Bracket<P> other = (Bracket<P>)obj;
			if (other.getMaxLevel() == this.getMaxLevel()) {
				
				for (int level = getMaxLevel(); level > 0; level--) {
					Set<Set<P>> groupings = getGroupings(level);
					Set<Set<P>> groupingsOther = other.getGroupings(level);
					
					if (groupings.equals(groupingsOther)) {
						continue;
						
					} else {
						foundDisagreement = true;
						break;
					}
				}
				
				if (!foundDisagreement) {
					// check viable participants
					for (int level = getMaxLevel(); level > 0; level--) {
						Set<Set<P>> groupings = getGroupings(level);
						Set<Set<P>> groupingsOther = other.getGroupings(level);
						
						for (Set<P> group: groupings) {
							if (getViableParticipants(group).equals(other.getViableParticipants(group))) {
								continue;
								
							} else {
								foundDisagreement = true;
								break;
							}
							
						}
						
					}
					
				}
				
			} else {
				foundDisagreement = true;
				
			}
		}
		boolean areEqual = (!foundDisagreement);
		
		return areEqual;
	}
}
