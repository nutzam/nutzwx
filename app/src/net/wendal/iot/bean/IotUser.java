package net.wendal.iot.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 这是一个Iot用户对象,存储特定用户的IOT信息. 普通信息,如最后登录日期,登录信息, 由主User表提供.
 * @author Administrator
 *
 */
@Table("iot_user")
public class IotUser {

	/**主用户id,关联主User表*/
	@Id(auto=false)
	@Column("uid")
	private long userId;
	
	/**这是API接口的鉴权KEY*/
	@Name
	private String apikey;
	
	@Column("uv")
	private IotUserLevel userLevel = IotUserLevel.FREE;
	
	/**允许存在的最大设备数*/
	@Column("dlt")
	private int deviceLimit;
	
	/**每个设备拥有的传感器数量*/
	@Column("slt")
	private int sensorLimit;

	/**每个传感器允许拥有的触发器数量*/
	@Column("tlt")
	private int triggerLimit;
	
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}

	public String getApikey() {
		return apikey;
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public int getDeviceLimit() {
		return deviceLimit;
	}

	public void setDeviceLimit(int deviceLimit) {
		this.deviceLimit = deviceLimit;
	}

	public int getSensorLimit() {
		return sensorLimit;
	}

	public void setSensorLimit(int sensorLimit) {
		this.sensorLimit = sensorLimit;
	}

	public int getTriggerLimit() {
		return triggerLimit;
	}

	public void setTriggerLimit(int triggerLimit) {
		this.triggerLimit = triggerLimit;
	}

	public IotUserLevel getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(IotUserLevel userLevel) {
		this.userLevel = userLevel;
	}
	
	
}
