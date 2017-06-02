package dao;

import java.sql.Date;
import java.util.List;

import dao.BasicDao;
import entities.User;

/**
 * 数据库连接
 * @author jy_meng
 *
 */
public class UserDao extends BasicDao<User> {

	public int registered(String username,String userword,int sex,String birth)
	{
		String sql="insert into user (username,userword,sex,birth) values (?,?,?,?)";
		update(sql, username,userword,sex,birth);
		String sql2="select count(*) from user where username=? and userword=?";
		return Integer.valueOf(getForValue(sql2, username,userword).toString());
	}
	
	public int login(String username,String userword)
	{
		String sql="select count(*) from user where username=? and userword=?";
		return Integer.valueOf(getForValue(sql, username,userword).toString());
	}
	public int getIdByName(String username)
	{
		String sql="select userid from user where username=?";
		if(getForValue(sql, username)==null)
			return-1;
		else
		return getForValue(sql, username);
	}
	public String getNameById(int id)
	{
		String sql="select username from user where userid=?";
		return getForValue(sql, id);
	}
	public List<User> getFriendById(int userId)
	{
		String sql="select * from user join relation on user.userid=relation.friend where relation.user_id=?";
		return getForList(sql,userId);
	}
	public int set(int userid,String username,String userword,int sex,String birth)
	{
		if(getIdByName(username)!=-1)
			return -1;//用户名存在
		String sql="update user set username=?,userword=?,sex=?,birth=? where userid=? ";
		update(sql, username,userword,sex,birth,userid);
		return 1;
	}
	public User getById(int userid)
	{
		String sql="select * from user where userid=?";
		return get(sql, userid);
	}
	public List<User> getall()
	{
		String sql="select * from user";
		return getForList(sql);
	}
}
