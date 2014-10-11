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

import net.wendal.Zs;
import net.wendal.iot.Iots;
import net.wendal.iot.bean.IotDevice;
import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.bean.IotSensorUpdateRule;
import net.wendal.iot.bean.SensorUploadResult;
import net.wendal.iot.mvc.ApiKeyFilter;
import net.wendal.iot.service.IotSensorService;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

@IocBean
@Filters({@By(type=ApiKeyFilter.class, args="ioc:apiKeyFilter")})
@Fail("http:500")
public class IotExchangeModule {
	
	private static final Log log = Logs.get();
	
	@Inject
	Dao dao;
	
	@Inject
	IotSensorService iotSensorService;
	
	@At({"/iot/device/?/sensor/?/datapoints", "/v1.1/device/?/sensor/?/datapoints"})
	@GET
	@Ok("void")
	public void getLastData(String device_id, String sensor_id, @Attr(Zs.UID)long userId, HttpServletResponse resp) throws IOException {
		IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and(Zs.UID, "=", userId).and("id", "=", sensor_id));
		if (sensor == null) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.NOTFOUND);
			return;
		}
		if (sensor.getValue() == null)
			return;
		resp.getWriter().write(sensor.getValue());
	}

	@At({"/iot/device/?/sensor/?/datapoints", "/v1.1/device/?/sensor/?/datapoints"})
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	@Ok("void")
	public void upload(String device_id, String sensor_id, InputStream in, @Attr(Zs.UID)long userId, HttpServletResponse resp) throws IOException {
		IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and(Zs.UID, "=", userId).and("id", "=", sensor_id));
		if (sensor == null) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.NOTFOUND);
			return;
		}
		if (sensor.getUpdateRule() != IotSensorUpdateRule.onlyvalue && sensor.getLastUpdateTime() != null && System.currentTimeMillis() - sensor.getLastUpdateTime().getTime() < Iots.Limit_Sensor_Update_Interval * 1000 ) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.TOOFAST);
			return ; // too fast
		}
		SensorUploadResult re = iotSensorService.upload(sensor, in);
		if (re.err == null)
			return;
		resp.setStatus(406);
		Mvcs.write(resp, re, JsonFormat.compact());
	}
	
	@SuppressWarnings("unchecked")
	@At({"/iot/device/?/datapoints", "/v1.1/device/?/datapoints"})
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	@Ok("json:full")
	public Object upload(String device_id, InputStream in, @Attr(Zs.UID)long userId, HttpServletResponse resp) throws IOException {
		IotDevice dev = dao.fetch(IotDevice.class, Cnd.where("deviceId", "=", device_id).and(Zs.UID, "=", userId));
		if (dev == null) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.NOTFOUND);
			return null;
		}
		Map<String, String> re = new LinkedHashMap<String, String>();
		List<Object> list = Json.fromJsonAsList(Object.class, new InputStreamReader(in));
		if (list == null || list.isEmpty()) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.NULLJSON);
		}
		for (Object obj : list) {
			Map<String, Object> map = (Map<String, Object>)obj;
			long sensor_id = Long.parseLong(map.get("sensor_id").toString());
			IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and(Zs.UID, "=", userId).and("id", "=", sensor_id));
			if (sensor == null) {
				re.put(""+sensor_id, "no such sensor");
				continue;
			}
			if (sensor.getLastUpdateTime() != null && System.currentTimeMillis() - sensor.getLastUpdateTime().getTime() < Iots.Limit_Sensor_Update_Interval * 1000 ) {
				re.put(""+sensor_id, "too fast");
				continue; // too fast
			}
			String err = iotSensorService.updateSensorValue(sensor, map);
			if (err == null)
				re.put(""+sensor_id, "");
			else
				re.put(""+sensor_id, err);
		}
		return re;
	}
	
	@At({"/iot/device/?/sensor/?/photos", "/v1.1/device/?/sensor/?/photos"})
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	@Ok("void")
	public void uploadPhoto(String device_id, String sensor_id, @Attr(Zs.UID)long userId, 
			HttpServletResponse resp, HttpServletRequest req) throws IOException {
		if (req.getContentLength() > 1024*1024) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.TOOBIG);
			return;
		}
		IotSensor sensor = dao.fetch(IotSensor.class, Cnd.where("deviceId", "=", device_id).and(Zs.UID, "=", userId).and("id", "=", sensor_id));
		if (sensor == null) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.NOTFOUND);
			return;
		}
		if (sensor.getLastUpdateTime() != null && System.currentTimeMillis() - sensor.getLastUpdateTime().getTime() < Iots.Limit_Sensor_Update_Interval * 1000 ) {
			resp.setStatus(406);
			resp.getWriter().write(Iots.TOOFAST);
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
			log.debug("bad image", e);
			resp.setStatus(406);
			resp.getWriter().write(Iots.BADIMAGE);
			return;
		}
        
		iotSensorService.saveImage(sensor, in, width, height);
	}
	
}
