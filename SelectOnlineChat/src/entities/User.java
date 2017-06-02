package entities;

import java.sql.Date;

/**
 * 
 * @author jy_meng
 *
 */
public class User {

	private int userid;
	private String userword;
	private String username;
	private int sex;
	private Date birth;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUserword() {
		return userword;
	}
	public void setUserword(String userword) {
		this.userword = userword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + ", userword=" + userword + ", username=" + username + "]";
	}
	public User(String userword, String username) {
		super();
		this.userword = userword;
		this.username = username;
	}
	public User() {
		super();
	}
	public User(int userid, String userword, String username) {
		super();
		this.userid = userid;
		this.userword = userword;
		this.username = username;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}
