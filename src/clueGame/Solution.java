package clueGame;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;

	public Solution(Card room, Card person, Card weapon) {
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}

	public String toString() {
		return person +" in the " + room + " with the " + weapon;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Solution solution = (Solution) obj;

		if (!room.equals(solution.room)) return false;
		if (!person.equals(solution.person)) return false;
		return weapon.equals(solution.weapon);
	}

	public Card getRoom() {
		return room;
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
	}

}
