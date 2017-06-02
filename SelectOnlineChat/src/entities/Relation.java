package entities;

public class Relation {
	private int user_id;
	private int friend;
	private int state;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getFriend() {
		return friend;
	}
	public void setFriend(int friend) {
		this.friend = friend;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
