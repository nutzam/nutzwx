package net.wendal.ito.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wendal.ito.bean.ItoSensor;
import net.wendal.ito.bean.ItoSensorTrigger;
import net.wendal.ito.bean.svalue.ItoGpsHistory;
import net.wendal.ito.bean.svalue.ItoImageHistory;
import net.wendal.ito.bean.svalue.ItoNumberHistory;
import net.wendal.ito.bean.svalue.ItoOnoffHistory;
import net.wendal.ito.bean.svalue.ItoRawHistory;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.resource.Scans;

@IocBean(create="init")
public class ItoSensorService {
	
	public static int PART = 20;
	
	@Inject Dao dao;

	@SuppressWarnings("unchecked")
	public NutMap upload(ItoSensor sensor, InputStream in) throws IOException {
		NutMap re = new NutMap();
		re.put("status", 406);
		switch (sensor.getType()) {
		case number:
		case gps:
		case raw:
		case onoff:
			Object data = null;
			try {
				data = Json.fromJson(new InputStreamReader(in));
			} catch (Throwable e) {
				re.put("error", "Bad json");
				return re;
			}
			if (data == null) {
				re.put("error", "null data");
				return re;
			}
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
				re.put("error", "bad data type");
				return re;
			}
			break;
		default:
			re.put("error", "not updateable");
			return re;
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String updateSensorValue(ItoSensor sensor, Map<String, Object> map) {
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
			ItoNumberHistory h = new ItoNumberHistory();
			h.setSensorId(sensor.getId());
			h.setValue(value);
			h.setTimestamp(time);
			partDao(sensor).insert(h);
			break;
		case gps:
			Map<String, Object> tmp = (Map)v;
			if (!tmp.containsKey("lan") || !tmp.containsKey("lat") || !tmp.containsKey("speed")) {
				return "miss some gps key";
			}
			ItoGpsHistory gps = null;
			try {
				gps = Lang.map2Object(tmp, ItoGpsHistory.class);
			} catch (Throwable e) {
				return "bad gps data";
			}
			gps.setSensorId(sensor.getId());
			gps.setTimestamp(time);
			partDao(sensor).insert(gps);
			break;
		case raw:
			Map<String, Object> tmp2 = (Map)v;
			String key = (String)tmp2.get("key");
			if (Strings.isBlank(key)) {
				return "key is blank or miss";
			}
			ItoRawHistory raw = new ItoRawHistory();
			raw.setSensorId(sensor.getId());
			raw.setTimestamp(time);
			raw.setKey(key);
			raw.setValue(Json.toJson(v, JsonFormat.full().setIndent(0)));
			partDao(sensor).insert(raw);
			break;
		case onoff:
			ItoOnoffHistory onoff = new ItoOnoffHistory();
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
		List<ItoSensorTrigger> tirggers = partDao(sensor).query(ItoSensorTrigger.class, Cnd.where("sensorId", "=", sensor.getId()));
		for (ItoSensorTrigger trigger : tirggers) {
			trigger.trigger(sensor, map, v);
		}
		return null;
	}
	
	public Dao partDao(ItoSensor sensor) {
		long part = sensor.getId() / PART;
		return Daos.ext(dao, "" + part);
	}
	
	public static String imagePath = "/data/ito";
	
	public String uploadPhoto(ItoSensor sensor, InputStream in) {
		return null;
	}
	
	public void saveImage(ItoSensor sensor, InputStream in, int w, int h) throws IOException {
		ItoImageHistory img = new ItoImageHistory();
		img.setSensorId(sensor.getId());
		img.setWidth(w);
		img.setHeight(h);
		img.setTimestamp(new Date());
		partDao(sensor).insert(img);
		Files.write(String.format("%s/%s/%s", imagePath, sensor.getId(), img.getId()), in);
	}
	
	public void init() {
		List<Class<?>> ks = Scans.me().scanPackage(ItoNumberHistory.class);
		for (int i = 0; i < PART; i++) {
			Dao dao = Daos.ext(this.dao, ""+i);
			for (Class<?> klass : ks) {
				if (klass.getAnnotation(Table.class) != null)
					dao.create(klass, false);
			}
		}
		Files.makeDir(new File(imagePath));
	}
}
