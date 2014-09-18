package net.wendal.iot.bean;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 一个物联网设备
 *
 */
@Table("iot_device")
@TableIndexes({@Index(fields="userId", name="uid", unique=false),
				@Index(fields="title", name="device_title", unique=true)
})
public class IotDevice {

	/**
	 * 设备id
	 */
	@Id
	private long id;
	/**
	 * 归属的用户id
	 */
	@Column("uid")
	private long userId;
	/**
	 * 设备名
	 */
	@Column
	private String title;
	/**
	 * 备注
	 */
	@Column
	private String detail;
	/**
	 * 位置
	 */
	@Column("loc")
	@ColDefine(width=256)
	private IotLocation loction;
	/**
	 * 可见性
	 */
	@Column("vle")
	private IotVisible visible;
	
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
