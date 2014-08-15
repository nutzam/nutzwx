package net.wendal.ito.bean.svalue;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("ito_onoff_history_${part}")
@TableIndexes({@Index(fields="sensorId", name="sensor_id", unique=false)})
public class ItoRawHistory {

	@Column("sid")
	private long sensorId;
	
	@Column("k")
	private String key;
	
	@Column("v")
	private String value;
	
	@Column("ct")
	private Date timestamp;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
