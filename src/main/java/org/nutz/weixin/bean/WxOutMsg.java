package org.nutz.weixin.bean;

public class WxOutMsg {
	private String fromUserName;
	private String ToUserName;
	private String msgType;
	private String content;
	
	public WxOutMsg() {
	}
	
	public WxOutMsg(String fromUserName, String toUserName) {
		super();
		this.fromUserName = fromUserName;
		ToUserName = toUserName;
	}
	
	public static WxOutMsg init(WxInMsg in) {
		return new WxOutMsg(in.getToUserName(), in.getFromUserName());
	}
	
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
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
	
	
}
