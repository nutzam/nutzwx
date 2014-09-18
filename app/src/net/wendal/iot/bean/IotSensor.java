package net.wendal.iot.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.json.JsonField;

/**
 * 传感器
 *
 */
@Table("iot_sensor")
@TableIndexes({@Index(fields="userId", name="uid", unique=false),
	@Index(fields={"deviceId", "title"}, name="sensor_title", unique=true)
})
public class IotSensor {

	/**
	 * 传感器id
	 */
	@Id
	private long id;
	
	/**
	 * 设备id
	 */
	@Column("did")
	@JsonField("device_id")
	private long deviceId;
	
	/**
	 * 用户id
	 */
	@Column("uid")
	private long userId;
	
	/**
	 * 传感器名称
	 */
	@Column
	private String title;
	
	/**
	 * 传感器的可见性(备用属性)
	 */
	@Column("vb")
	private IotVisible visiable;
	
	/**
	 * 传感器类型
	 */
	@Column("tp")
	private IotSensorType type;

	/**
	 * 创建时间
	 */
	@Column("ct")
	private Date createTime;
	
//----------------------------------------
	/**
	 * 最后更新时间
	 */
	@Column("lt")
	private Date lastUpdateTime;
	
	/**
	 * 最新的值(考虑中)
	 */
	@Column("val")
	@ColDefine(width=1024)
	private String value;
	
//----------------------------------------

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
