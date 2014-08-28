package net.wendal.iot.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;

@Table("iot_sensor")
public class IotSensor {

	@Id
	private long id;
	
	@Column("did")
	@JsonField("device_id")
	private long deviceId;
	
	@Column("uid")
	private long userId;
	
	@Column
	private String title;
	
	@Column("vb")
	private IotVisible visiable;
	
	@Column("tp")
	private IotSensorType type;
	
	@Column("val")
	@ColDefine(width=1024)
	private String val;

	@Column("ct")
	private Date createTime;
	
	@Column("lt")
	private Date lastUpdateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public IotVisible getVisiable() {
		return visiable;
	}

	public void setVisiable(IotVisible visiable) {
		this.visiable = visiable;
	}

	public IotSensorType getType() {
		return type;
	}

	public void setType(IotSensorType type) {
		this.type = type;
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
