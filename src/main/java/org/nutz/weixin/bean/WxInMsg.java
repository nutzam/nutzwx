package org.nutz.weixin.bean;

import org.nutz.json.JsonIgnore;

public class WxInMsg {

	protected String fromUserName;
	protected String toUserName;
	protected String event;
	protected String eventKey;
	protected String msgType;
	protected String content;
	protected long createTime;
	protected long msgID;
	protected String picUrl;
	protected String mediaId;
	protected String format;
	protected String thumbMediaId;
	/* 语音识别结果 需要开通语音识别 */
	protected String recognition;

	@JsonIgnore(null_double = 0)
	protected double location_X;
	@JsonIgnore(null_double = 0)
	protected double location_Y;
	@JsonIgnore(null_double = 0)
	protected double scale;
	protected String label;

	protected String title;
	protected String description;
	protected String url;

	protected String status;

	private WxScanCodeInfo scanCodeInfo;
	private WxSendLocationInfo sendLocationInfo;

	/** 从页面传来的key值 */
	private String extkey;

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public WxInMsg() {
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getMsgId() {
		return msgID;
	}

	public void setMsgId(long msgId) {
		this.msgID = msgId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public double getLocation_X() {
		return location_X;
	}

	public void setLocation_X(double location_X) {
		this.location_X = location_X;
	}

	public double getLocation_Y() {
		return location_Y;
	}

	public void setLocation_Y(double location_Y) {
		this.location_Y = location_Y;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExtkey() {
		return extkey;
	}

	public void setExtkey(String extkey) {
		this.extkey = extkey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WxScanCodeInfo getScanCodeInfo() {
		return scanCodeInfo;
	}

	public void setScanCodeInfo(WxScanCodeInfo scanCodeInfo) {
		this.scanCodeInfo = scanCodeInfo;
	}

	public WxSendLocationInfo getSendLocationInfo() {
		return sendLocationInfo;
	}

	public void setSendLocationInfo(WxSendLocationInfo sendLocationInfo) {
		this.sendLocationInfo = sendLocationInfo;
	}
}
