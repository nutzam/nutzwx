package net.wendal.iot.service;

import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import net.wendal.base.service.UserService;
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
import org.nutz.repo.Base64;

@IocBean
public class IotService {

	@Inject
	Dao dao;
	
	@Inject UserService userService;
	
	public IotUser rootUser() {
		long rootUserId = userService.userId("root");
		if (rootUserId < 0) {
			return null;
		}
		IotUser admin = dao.fetch(IotUser.class, rootUserId);
		if (admin == null) {
			admin = addUser(rootUserId, IotUserLevel.SSVIP);
		}
		return admin;
	}
	
	public void makeApiKey(IotUser user) {
		user.setApikey(R.sg(16).next());
		user.setPbkdf2(pbkdf2(user.getApikey()));
	}

	public IotUser addUser(long userId, IotUserLevel uv) {
		IotUser user = new IotUser();
		makeApiKey(user);
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
	
	public static String pbkdf2(String passwd) {
		char[] password = passwd.toCharArray();
		byte[] salt = R.sg(12).next().getBytes();
		int iterations = 101;
		int bytes = 24;
		byte[] re = _pbkdf2(password, salt, iterations, bytes);
		return String.format("PBKDF2$sha256$101$%s$%s",Base64.encodeToString(salt, false), Base64.encodeToString(re, false));
	}

	public static byte[] _pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			return skf.generateSecret(spec).getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
