package net.wendal.iot.module;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wendal.iot.Iots;
import net.wendal.iot.bean.IotDevice;
import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.mvc.ApiKeyFilter;
import net.wendal.iot.service.IotSensorService;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

@IocBean
@Filters({@By(type=ApiKeyFilter.class, args="ioc:apiKeyFilter")})
public class IotExchangeModule {
	
//	private static final Log log = Logs.get();
	
	@Inject
	Dao dao;
	
	@Inject
	IotSensorService itoSensorService;

	@At({"/ito/device/?/sensor/?/datapoints", "/v1.1/device/?/sensor/?/datapoints"})
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	@Ok("void")
	@Fail("http:406")
	public void upload(String device_id, String sensor_id, InputStream in, @Attr("userId")long userId, HttpServletResponse resp) throws IOException {
		IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId).and("id", "=", sensor_id));
		if (sensor == null) {
			return;
		}
		if (sensor.getLastUpdateTime() != null && System.currentTimeMillis() - sensor.getLastUpdateTime().getTime() < Iots.Limit_Sensor_Update_Interval * 1000 ) {
			return ; // too fast
		}
		NutMap map = itoSensorService.upload(sensor, in);
		if (map == null || map.isEmpty()) {
			return;
		}
		resp.setStatus(map.getInt("status", 200));
		map.remove("status");
		Mvcs.write(resp, map, JsonFormat.compact());
	}
	
	@SuppressWarnings("unchecked")
	@At({"/ito/device/?/datapoints", "/v1.1/device/?/datapoints"})
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	@Ok("json:full")
	@Fail("http:406")
	public Object upload(String device_id, InputStream in, @Attr("userId")long userId, HttpServletResponse resp) throws IOException {
		IotDevice dev = dao.fetch(IotDevice.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId));
		if (dev == null) {
			return null;
		}
		Map<String, String> re = new LinkedHashMap<String, String>();
		List<Object> list = Json.fromJsonAsList(Object.class, new InputStreamReader(in));
		for (Object obj : list) {
			Map<String, Object> map = (Map<String, Object>)obj;
			long sensor_id = Long.parseLong(map.get("sensor_id").toString());
			IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId).and("id", "=", sensor_id));
			if (sensor == null) {
				re.put(""+sensor_id, "no such sensor");
				continue;
			}
			if (sensor.getLastUpdateTime() != null && System.currentTimeMillis() - sensor.getLastUpdateTime().getTime() < Iots.Limit_Sensor_Update_Interval * 1000 ) {
				re.put(""+sensor_id, "too fast");
				continue; // too fast
			}
			String tmp = itoSensorService.updateSensorValue(sensor, map);
			if (tmp == null)
				re.put(""+sensor_id, "");
			else
				re.put(""+sensor_id, tmp);
		}
		return re;
	}
	
	@At({"/ito/device/?/sensor/?/photos", "/v1.1/device/?/sensor/?/photos"})
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	@Ok("void")
	@Fail("http:406")
	public void uploadPhoto(String device_id, String sensor_id, @Attr("userId")long userId, 
			HttpServletResponse resp, HttpServletRequest req) throws IOException {
		if (req.getContentLength() > 1024*1024) {
			resp.setStatus(406);
			return;
		}
		IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and("userId", "=", userId).and("id", "=", sensor_id));
		if (sensor == null) {
			resp.setStatus(406);
			return;
		}
		if (sensor.getLastUpdateTime() != null && System.currentTimeMillis() - sensor.getLastUpdateTime().getTime() < Iots.Limit_Sensor_Update_Interval * 1000 ) {
			resp.setStatus(406);
			return ; // too fast
		}
		PushbackInputStream in = new PushbackInputStream(req.getInputStream(), 2048);
		in.mark(2048);
		byte[] buf = new byte[1];
		in.read(buf);
		String suffix = "jpg";
		if (buf[0] == 0x47) {
			suffix = "gif";
		} else if (buf[0] == 0x89) {
			suffix = "png";
		}
		in.reset();
		in.mark(2048);
		Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix(suffix);
		ImageReader r = it.next();
		
		int width;
		int height;
		try {
			r.setInput(in);
			width = r.getWidth(r.getMinIndex());
			height = r.getHeight(r.getMinIndex());
			r.dispose();
			in.reset();
		} catch (Exception e) {
			throw new RuntimeException("bad image", e);
		}
        
        itoSensorService.saveImage(sensor, in, width, height);
	}
}
