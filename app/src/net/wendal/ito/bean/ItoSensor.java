package net.wendal.ito.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;

@Table("ito_sensor")
public class ItoSensor {

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
	private ItoVisible visiable;
	
	@Column("tp")
	private ItoSensorType type;

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

	public ItoVisible getVisiable() {
		return visiable;
	}

	public void setVisiable(ItoVisible visiable) {
		this.visiable = visiable;
	}

	public ItoSensorType getType() {
		return type;
	}

	public void setType(ItoSensorType type) {
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
