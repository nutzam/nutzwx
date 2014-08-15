package net.wendal.ito.module;

import java.util.List;

import net.wendal.ito.Itos;
import net.wendal.ito.bean.ItoDevice;
import net.wendal.ito.bean.ItoSensor;
import net.wendal.ito.mvc.ApiKeyFilter;
import net.wendal.ito.service.ItoSensorService;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;
import org.nutz.mvc.annotation.Param;

@Filters({@By(type=ApiKeyFilter.class, args="ioc:apiKeyFilter")})
@Fail("http:500")
@Ok("json")
@IocBean
@At("/v1.1")
public class ItoAdminModule {
	
	@Inject
	Dao dao;
	
	@Inject
	ItoSensorService itoSensorService;
	
	@At({"/ito/devices", "/v1.1/devices"})
	@GET
	public List<ItoDevice> listDev(@Attr("userId")long userId) {
		return dao.query(ItoDevice.class, Cnd.where("userId", "=", userId));
	}
	
	@At({"/ito/device/?sensors","/v1.1/device/?/sensors"})
	@GET
	@Filters()
	// TODO 区分private和public设备
	public List<ItoSensor> listSensors(long deviceId, @Attr("userId")long userId) throws IllegalAccessException {
		return dao.query(ItoSensor.class, Cnd.where("deviceId", "=", deviceId));
	}
	
	@At({"/ito/devices", "/v1.1/devices"})
	@POST
	@AdaptBy(type=JsonAdaptor.class)
	public ItoDevice createDev(@Param("..")ItoDevice dev, @Attr("userId")long userId) {
		if (dev == null) {
			return null;
		}
		int devCount = dao.count(ItoDevice.class, Cnd.where("userId", "=", userId));
		if (devCount > Itos.Limit_Dev_Per_User) { // TODO 可扩展,可配置
			return null;
		}
		dev.setId(userId);
		dev.setSensors(null);
		return dao.insert(dev);
	}
	
	@At({"/ito/device/?", "/v1.1/device/?"})
	@GET
	public ItoDevice getDev(long device_id, @Param("..")ItoDevice dev, @Attr("userId")long userId) {
		return dao.fetch(ItoDevice.class, Cnd.where("id", "=", device_id).and("userId", "=", userId));
	}
	
	@At({"/ito/device/?", "/v1.1/device/?"})
	@PUT
	@AdaptBy(type=JsonAdaptor.class)
	public ItoDevice updateDev(long device_id, @Param("..")ItoDevice dev, @Attr("userId")long userId) {
		if (dev == null || 0 == dao.count(ItoDevice.class, Cnd.where("id", "=", device_id).and("userId", "=", userId))) {
			return null;
		}
		dev.setId(device_id);
		dev.setUserId(userId);
		dao.update(dev);
		return dev;
	}
	
	@At({"/ito/device/?", "/v1.1/device/?"})
	@DELETE
	@Ok("void")
	public void deleteDev(long device_id, @Attr("userId")long userId) {
		dao.clear(ItoDevice.class, Cnd.where("id", "=", device_id).and("userId", "=", userId));
		dao.clear(ItoSensor.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId));
	}
	
	@At({"/ito/device/?/sensor/?", "/v1.1/device/?/sensor/?"})
	@GET
	@Filters()
	public ItoSensor getSensor(long device_id, long sensor_id) {
		return dao.fetch(ItoSensor.class, Cnd.where("deviceId", "=", device_id).and("id", "=", sensor_id));
	}
	
	@At({"/ito/device/?/sensors", "/v1.1/device/?/sensors"})
	@POST
	@AdaptBy(type=JsonAdaptor.class)
	public ItoSensor createSensor(long device_id, @Param("..")ItoSensor sensor, @Attr("userId")long userId) {
		if (sensor == null)
			return null;
		ItoDevice dev = dao.fetch(ItoDevice.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId));
		if (dev == null)
			return null;
		int sensorCount = dao.count(ItoSensor.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId));
		if (sensorCount > Itos.Limit_Sensor_Per_Dev) {
			return null;
		}
		sensor.setDeviceId(device_id);
		sensor.setUserId(userId);
		dao.insert(sensor);
		return sensor;
	}
	
	@At({"/ito/device/?/sensor/?", "/v1.1/device/?/sensor/?"})
	@PUT
	@AdaptBy(type=JsonAdaptor.class)
	public ItoSensor updateSensor(long device_id, @Param("..")ItoSensor sensor, @Attr("userId")long userId) {
		if (sensor == null)
			return null;
		ItoDevice dev = dao.fetch(ItoDevice.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId));
		if (dev == null)
			return null;
		sensor.setDeviceId(device_id);
		sensor.setUserId(userId);
		dao.update(sensor);
		return sensor;
	}
	
	@At({"/ito/device/?/sensor/?", "/v1.1/device/?/sensor/?"})
	@DELETE
	@Ok("http:200")
	public void deleteSensor(long device_id, @Param("sensor_id")long sensor_id, @Attr("userId")long userId) {
		dao.clear(ItoSensor.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId).and("id", "=", sensor_id));
	}
	
}
