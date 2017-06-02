package dao;

import java.util.List;

import entities.Data;

public class DataDao extends BasicDao<Data>{
	public int addData(String content,int userid,String time)
	{ 
		String sql="insert into data(datacontent,userid,time) values(?,?,?)";
		update(sql, content,userid,time);
		String sql2="select * from data where datacontent=? and userid=?";
		return Integer.valueOf(getForValue(sql2, content,userid).toString());
	}
	public List<Data> GetData(String username)
	{
		String sql="select * from data join user on user.userid=data.userid where username=?";
		return getForList(sql, username);
	}
}
