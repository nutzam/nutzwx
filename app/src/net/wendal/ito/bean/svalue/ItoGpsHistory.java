package net.wendal.ito.bean.svalue;

import java.util.Date;

import net.wendal.ito.bean.ItoLocation;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("ito_gps_history_${part}")
@TableIndexes({@Index(fields="sensorId", name="sensor_id", unique=false)})
public class ItoGpsHistory extends ItoLocation {

	@Column("sid")
	private long sensorId;
	
	@Column("ct")
	private Date timestamp;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
}
