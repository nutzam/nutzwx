package net.wendal.iot.bean;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("iot_device")
@TableIndexes(@Index(fields="userId", name="uid", unique=false))
public class IotDevice {

	@Id
	private long id;
	@Column("uid")
	private long userId;
	@Column
	private String title;
	@Column
	private String detail;
	@Column("loc")
	private IotLocation loction;
	
	@Many(target=IotSensor.class, field = "deviceId")
	private List<IotSensor> sensors;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public IotLocation getLoction() {
		return loction;
	}

	public void setLoction(IotLocation loction) {
		this.loction = loction;
	}

	public List<IotSensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<IotSensor> sensors) {
		this.sensors = sensors;
	}
}
