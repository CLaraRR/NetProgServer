package dao;

import dao.BasicDao;
import entities.User;

/**
 * 数据库连接
 * @author jy_meng
 *
 */
public class UserDao extends BasicDao<User> {

	public int registered(String username,String userword)
	{
		String sql="insert into user (username,userword) values (?,?)";
		update(sql, username,userword);
		String sql2="select count(*) from user where username=? and userword=?";
		return Integer.valueOf(getForValue(sql2, username,userword).toString());
	}
	
	public int login(String username,String userword)
	{
		String sql="select count(*) from user where username=? and userword=?";
		return Integer.valueOf(getForValue(sql, username,userword).toString());
	}
}
