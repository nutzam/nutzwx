package net.wendal.iot.bean;

import java.util.Date;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("ito_sensor_trigger_{part}")
public class IotSensorTrigger {
	@Id
	private long id;
	
	@Column("sid")
	private long sensorId;
	@Column
	private String title;
	@Column
	private String op;
	@Column
	private String cmd;
	@Column("ct")
	private Date createTime;
	@Column("lt")
	private Date lastUpdateTime;

	public void trigger(IotSensor sensor, Map<String, Object> all, Object v) {
	
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
