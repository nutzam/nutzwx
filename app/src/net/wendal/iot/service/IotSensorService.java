package net.wendal.iot.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wendal.iot.Iots;
import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.bean.IotSensorTrigger;
import net.wendal.iot.bean.SensorUploadResult;
import net.wendal.iot.bean.history.IotImageHistory;
import net.wendal.iot.bean.history.IotKvHistory;
import net.wendal.iot.bean.history.IotLocationHistory;
import net.wendal.iot.bean.history.IotNumberHistory;
import net.wendal.iot.bean.history.IotOnoffHistory;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

@IocBean(create="init")
public class IotSensorService {
	
	@Inject Dao dao;

	@SuppressWarnings("unchecked")
	public SensorUploadResult upload(IotSensor sensor, InputStream in) throws IOException {
		SensorUploadResult re = new SensorUploadResult();
		Object data;
		try {
			data = Json.fromJson(new InputStreamReader(in));
		} catch (Throwable e) {
			re.err = "Bad json";
			return re;
		}
		if (data == null) {
			re.err = "NULL json";
			return re;
		}
		switch (sensor.getType()) {
		case number:
		case location:
		case kv:
		case onoff:
			if (data instanceof List) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) data;
				for (Map<String, Object> map : list) {
					updateSensorValue(sensor, map);
				}
				return null;
			} else if (data instanceof Map) {
				String msg = updateSensorValue(sensor, (Map<String, Object>)data);
				if (msg != null) {
					return re;
				}
			} else {
				re.err = "bad data type";
				return re;
			}
			break;
		default:
			re.err = "not updateable";
			return re;
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String updateSensorValue(IotSensor sensor, Map<String, Object> map) {
		Object v = map.get("value");
		Object t = map.get("timestamp");
		Date time = null;
		if (t == null) {
			time = new Date();
		} else {
			try {
				time = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss").parse(String.valueOf(t));
			} catch (ParseException e) {
				return "bad timestamp";
			}
		}
		switch (sensor.getType()) {
		case number:
			double value = Double.NaN;
			try {
				value = ((Number)v).doubleValue();
			} catch (Throwable e) {
				return "bad value";
			}
			IotNumberHistory h = new IotNumberHistory();
			h.setSensorId(sensor.getId());
			h.setValue(value);
			h.setTimestamp(time);
			partDao(sensor).insert(h);
			break;
		case location:
			Map<String, Object> tmp = (Map)v;
			if (!tmp.containsKey("lan") || !tmp.containsKey("lat") || !tmp.containsKey("speed")) {
				return "miss some gps key";
			}
			IotLocationHistory gps = null;
			try {
				gps = Lang.map2Object(tmp, IotLocationHistory.class);
			} catch (Throwable e) {
				return "bad gps data";
			}
			gps.setSensorId(sensor.getId());
			gps.setTimestamp(time);
			partDao(sensor).insert(gps);
			break;
		case kv:
			Map<String, Object> tmp2 = (Map)v;
			String key = (String)tmp2.get("key");
			if (Strings.isBlank(key)) {
				return "key is blank or miss";
			}
			IotKvHistory raw = new IotKvHistory();
			raw.setSensorId(sensor.getId());
			raw.setTimestamp(time);
			raw.setKey(key);
			raw.setValue(Json.toJson(v, JsonFormat.full().setIndent(0)));
			partDao(sensor).insert(raw);
			break;
		case onoff:
			IotOnoffHistory onoff = new IotOnoffHistory();
			onoff.setTimestamp(time);
			if ("1".equals(String.valueOf(v))) {
				onoff.setValue(1);
				v = 1;
			} else {
				onoff.setValue(0);
				v = 0;
			}
			partDao(sensor).insert(onoff);
			break;
		default:
			break;
		}
		sensor.setLastUpdateTime(new Date());
		partDao(sensor).update(sensor, "^(lastUpdateTime)$");
		List<IotSensorTrigger> tirggers = partDao(sensor).query(IotSensorTrigger.class, Cnd.where("sensorId", "=", sensor.getId()));
		for (IotSensorTrigger trigger : tirggers) {
			trigger.trigger(sensor, map, v);
		}
		return null;
	}
	
	public Dao partDao(IotSensor sensor) {
		long part = sensor.getId() / Iots.PART;
		return Daos.ext(dao, "" + part);
	}
	
	public static String imagePath = "/data/ito";
	
	public String uploadPhoto(IotSensor sensor, InputStream in) {
		return null;
	}
	
	public void saveImage(IotSensor sensor, InputStream in, int w, int h) throws IOException {
		IotImageHistory img = new IotImageHistory();
		img.setSensorId(sensor.getId());
		img.setWidth(w);
		img.setHeight(h);
		img.setTimestamp(new Date());
		partDao(sensor).insert(img);
		Files.write(String.format("%s/%s/%s", imagePath, sensor.getId(), img.getId()), in);
		sensor.setLastUpdateTime(new Date());
		partDao(sensor).update(sensor, "^(lastUpdateTime)$");
	}
	
	public void init() {
		Files.makeDir(new File(imagePath));
	}
}
