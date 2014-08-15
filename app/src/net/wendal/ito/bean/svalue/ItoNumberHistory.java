package net.wendal.ito.bean.svalue;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("ito_number_history_${part}")
@TableIndexes({ @Index(fields = "sensorId", name = "sensor_id", unique = false) })
public class ItoNumberHistory {

	@Column("sid")
	private long sensorId;

	@Column("val")
	private double value;

	@Column("ct")
	private Date timestamp;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
