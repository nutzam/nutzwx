package net.wendal.iot.bean;

import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.json.Json;
import org.nutz.json.JsonField;
import org.nutz.json.JsonFormat;

/**
 * 描述一个位置
 *
 */
public class IotLocation {

	/**数据类型, 如gps,北斗,伽利略等等*/
	@Column("tp")
	private String loctionType;
	/**
	 * 经度
	 */
	@Column("lng")
	@JsonField("lng")
	private Float longitude;
	/**
	 * 纬度
	 */
	@Column("lat")
	@JsonField("lat")
	private Float latitude;
	/**
	 * 海拔
	 */
	@Column()
	@JsonField("atd")
	private Float altitude;
	/**
	 * 速度
	 */
	@Column("speed")
	@JsonField("speed")
	private Float speed;
	/**
	 * 是否进行便宜
	 */
	@Column("offset")
	@JsonField("offset")
	private Boolean offset;
	
	public IotLocation() {
	}
	
	public IotLocation(String str) {
		Map<String, Object> map = Json.fromJsonAsMap(Object.class, str);
		longitude = ((Number)map.get("longitude")).floatValue();
		latitude = ((Number)map.get("latitude")).floatValue();
		speed = ((Number)map.get("speed")).floatValue();
		offset = (Boolean)map.get("offset");
	}
	
	public String toString() {
		return Json.toJson(this, JsonFormat.compact());
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Boolean getOffset() {
		return offset;
	}

	public void setOffset(Boolean offset) {
		this.offset = offset;
	}

	public String getLoctionType() {
		return loctionType;
	}

	public void setLoctionType(String loctionType) {
		this.loctionType = loctionType;
	}

	public Float getAltitude() {
		return altitude;
	}

	public void setAltitude(Float altitude) {
		this.altitude = altitude;
	}
}
