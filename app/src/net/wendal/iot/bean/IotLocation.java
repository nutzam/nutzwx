package net.wendal.iot.bean;

import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.json.Json;
import org.nutz.json.JsonField;
import org.nutz.json.JsonFormat;

public class IotLocation {

	@Column("lng")
	@JsonField("lng")
	private Float longitude;
	@Column("lat")
	@JsonField("lat")
	private Float latitude;
	@Column("speed")
	@JsonField("speed")
	private Float speed;
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
}
