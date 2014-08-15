package net.wendal.ito.bean;

import java.util.Map;

import org.nutz.dao.entity.annotation.Table;

@Table("ito_sensor_trigger_{part}")
public class ItoSensorTrigger {
	
	private long sensorId;
	private String title;
	private String op;
	private String cmd;

	public void trigger(ItoSensor sensor, Map<String, Object> all, Object v) {
	
	}

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}
