package org.nutz.weixin.bean;

import java.util.List;

public class WxOutMsg {
	private String fromUserName;
	private String ToUserName;
	private String msgType;
	private String content;
	private long createTime;
	private WxImage image;
	private WxVoice voice;
	private WxVideo video;
	private WxMusic music;
	private List<WxArticle> articles;
	
	public WxOutMsg() {
		createTime = System.currentTimeMillis();
	}
	
	public WxOutMsg(String msgType) {
		this();
		this.msgType = msgType;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public WxImage getImage() {
		return image;
	}

	public void setImage(WxImage image) {
		this.image = image;
	}

	public WxVoice getVoice() {
		return voice;
	}

	public void setVoice(WxVoice voice) {
		this.voice = voice;
	}

	public WxVideo getVideo() {
		return video;
	}

	public void setVideo(WxVideo video) {
		this.video = video;
	}

	public WxMusic getMusic() {
		return music;
	}

	public void setMusic(WxMusic music) {
		this.music = music;
	}

	public List<WxArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<WxArticle> articles) {
		this.articles = articles;
	}
	
}
