package net.wendal.iot.bean;

import java.util.Date;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 传感器的触发器
 *
 */
@Table("iot_sensor_trigger")
@TableIndexes(@Index(fields="sensorId", name="sensorId", unique=false))
public class IotSensorTrigger {
	/**
	 * 触发器id
	 */
	@Id
	private long id;
	
	/**
	 * 传感器id
	 */
	@Column("sid")
	private long sensorId;
	/**
	 * 传感器名称
	 */
	@Column
	private String title;
	/**
	 * 操作
	 */
	@Column
	private String op;
	/**
	 * 命令
	 */
	@Column
	private String cmd;
	/**
	 * 创建时间
	 */
	@Column("ct")
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	@Column("lt")
	private Date lastUpdateTime;
	
	/**
	 * 最后触发时间
	 */
	@Column("ltt")
	private Date lastTriggerTime;

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
