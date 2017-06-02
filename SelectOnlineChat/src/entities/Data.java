package entities;

import java.sql.Date;

public class Data {
	private int dataid;
	private String datacontent;
	private int userid;
	private Date time;
	public int getDataid() {
		return dataid;
	}
	public void setDataid(int dataid) {
		this.dataid = dataid;
	}
	public String getDatacontent() {
		return datacontent;
	}
	public void setDatacontent(String datacontent) {
		this.datacontent = datacontent;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
