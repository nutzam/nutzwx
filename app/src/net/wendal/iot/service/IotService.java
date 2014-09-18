package net.wendal.iot.service;

import java.util.Date;

import net.wendal.iot.bean.IotDevice;
import net.wendal.iot.bean.IotLocation;
import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.bean.IotSensorType;
import net.wendal.iot.bean.IotUser;
import net.wendal.iot.bean.IotUserLevel;
import net.wendal.iot.bean.IotVisible;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.random.R;

@IocBean
public class IotService {

	@Inject Dao dao;
	
	public IotUser addUser(long userId, IotUserLevel uv) {
		IotUser user = new IotUser();
		user.setApikey(R.UU64());
		user.setUserId(userId);
		user.setUserLevel(uv);
		switch (uv) {
		case VIP:
			user.setDeviceLimit(50);
			user.setSensorLimit(50);
			user.setTriggerLimit(5);
			break;
		case SVIP:
			user.setDeviceLimit(100);
			user.setSensorLimit(100);
			user.setTriggerLimit(5);
			break;
		case SSVIP:
			user.setDeviceLimit(1000);
			user.setSensorLimit(1000);
			user.setTriggerLimit(5);
			break;
		default:
			user.setDeviceLimit(20);
			user.setSensorLimit(20);
			user.setTriggerLimit(5);
			break;
		}
		user = dao.insert(user);
		
		IotDevice device = new IotDevice();
		device.setTitle("测试设备");
		device.setUserId(userId);
		IotLocation loc = new IotLocation();
		loc.setLongitude(113.0f);
		loc.setLatitude(23.0f);
		loc.setSpeed(0f);
		loc.setOffset(false);
		loc.setLoctionType("gps");
		device.setLoction(loc);
		device = dao.insert(device);
		
		IotSensor sensor = new IotSensor();
		sensor.setDeviceId(device.getId());
		sensor.setUserId(userId);
		sensor.setTitle("DS18B20温度传感器");
		sensor.setType(IotSensorType.number);
		sensor.setVisiable(IotVisible.PUBLIC);
		sensor.setCreateTime(new Date());
		
		dao.insert(sensor);
		
		sensor.setTitle("电源开关");
		sensor.setType(IotSensorType.onoff);
		sensor.setVisiable(IotVisible.PUBLIC);
		sensor.setCreateTime(new Date());
		
		dao.insert(sensor);
		
		return user;
	}
}
