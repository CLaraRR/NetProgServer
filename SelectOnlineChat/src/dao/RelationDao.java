package dao;

import java.util.List;

import entities.Relation;

public class RelationDao extends BasicDao<Relation>{
	public int add(int user1,int user2,int state)
	{
		String sql="insert into relation(user_id,friend,state) values(?,?,?)";
		update(sql, user1,user2,state);
		String sql2="select * from relation where user_id=? and friend=? and state=?";
		return Integer.valueOf(getForValue(sql2, user1,user2,state).toString());
	}
	public void agree(int user1,int user2)
	{
		String sql="update relation set state=1 where user_id=? and friend=?";
		update(sql, user1,user2);
	}
	public int delete(int user1,int user2)
	{
		String sql="delete from relation where user_id=? and friend=?";
		update(sql, user1,user2);
		String sql2="select * from relation where user_id=? and friend=?";
		return Integer.valueOf(getForValue(sql2, user1,user2).toString());
	}
	public List<Relation> getByuserId(int userid)//获得申请加客户为好友的列表
	{
		String sql="select * from relation where friend=?";
		return getForList(sql, userid);
	}
}
