package net.wendal.iot.service;

import java.util.HashMap;
import java.util.Map;

import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.bean.IotUser;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.socket.SocketAction;
import org.nutz.lang.socket.SocketContext;
import org.nutz.lang.socket.Sockets;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean(create="init")
public class SocketUploadService implements Runnable, SocketAction {

	private static final Log log = Logs.get();
	
	@Inject Dao dao;
	
	@Inject IotSensorService iotSensorService;
	
	public void init() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		Map<String, SocketAction> actions = new HashMap<String, SocketAction>();
		// w,$apikey,$device_id,$sensor_id,{....}
		// r,$apikey,$device_id,$sensor_id
		actions.put("$:^(w|r),.+$", this);
		Sockets.localListenByLine(82, actions);
	}

	@Override
	public void run(SocketContext context) {
		String line = context.getCurrentLine();
		String[] tmp = line.split(",", 5);
		if (tmp.length < 4) {
			context.writeLine("err,miss args");
			return;
		}
		String apikey = tmp[1];
		IotUser user = dao.fetch(IotUser.class, apikey);
		if (user == null) {
			context.writeLine("err,bad apikey");
			return;
		}
		long deviceId = -1;
		try {
			deviceId = Long.parseLong(tmp[2]);
		} catch (NumberFormatException e) {
			context.writeLine("err,bad device_id");
			return;
		}
		long sensorId = -1;
		try {
			sensorId = Long.parseLong(tmp[3]);
		} catch (NumberFormatException e) {
			context.writeLine("err,bad sensor_id");
			return;
		}
		IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("id", "=", sensorId).and("deviceId", "=", deviceId).and("userId", "=", user.getUserId()));
		if (sensor == null) {
			context.writeLine("err,no such sensor");
			return;
		}
		if ("r".equals(tmp[0])) {
			context.writeLine("err,no support yet");
			return;
		}
		if (tmp.length != 5) {
			context.writeLine("err,miss value for write");
			return;
		}
		Map<String, Object> map;
		try{
			map = Json.fromJsonAsMap(Object.class, tmp[4]);
		} catch (Throwable e) {
			context.writeLine("err,bad json for write");
			return;
		}
		try {
			String re = iotSensorService.updateSensorValue(sensor, map);
			if (re == null) {
				context.writeLine("ok");
			} else {
				context.writeLine("err," + re);
			}
			return;
		} catch (Throwable e) {
			log.info("something fail", e);
			context.writeLine("err,some error");
			return;
		}
	}
	
	public static void main(String[] args) {
		new SocketUploadService().init();
		Lang.quiteSleep(100000);
	}
}
