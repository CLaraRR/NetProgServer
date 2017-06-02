package dao;

import entities.Device;

public class DeviceDao extends BasicDao<Device>{
	public int getIdByDevice(int deviceId)
	{
		String sql="select userid from device where deviceid=?";
		return getForValue(sql, deviceId);
	}
	public int getDeviceById(int userid)
	{ 
		String sql="select deviceid from device where userid=?";
		if(getForValue(sql, userid)==null)
			return -1;
		else
			return getForValue(sql, userid);
	}
	public void add(int userid,int deviceid)
	{
		String sql="insert into device(deviceid,userid) values(?,?)";
		update(sql, deviceid,userid);
	}
}
