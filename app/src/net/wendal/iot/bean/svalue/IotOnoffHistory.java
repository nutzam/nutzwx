package net.wendal.iot.bean.svalue;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("iot_onoff_history_${part}")
@TableIndexes({@Index(fields="sensorId", name="sensor_id", unique=false)})
public class IotOnoffHistory {
	@Id
	private long id;

	@Column("sid")
	private long sensorId;
	
	@Column("val")
	private int value;
	
	@Column("ct")
	private Date timestamp;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
