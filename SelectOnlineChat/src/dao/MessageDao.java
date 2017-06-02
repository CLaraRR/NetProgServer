package dao;

import java.sql.Date;
import java.util.List;

import entities.Message;

public class MessageDao extends BasicDao<Message> {
	public List<Message> getAll()
	{
		String sql="select *from message join user on message.userid=user.userid";
		return getForList(sql);
	}
	public void addMessage(String content,int userid,String time)
	{
		String sql="insert into message(userid,content,time) values(?,?,?)";
		update(sql, userid,content,time);
	}
}
