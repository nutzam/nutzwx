package net.wendal.nutzwx.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("wx_msg_history_${openid}")
@TableIndexes({@Index(fields="clientId", name="wx_msg_clientId", unique=false),
				@Index(fields="msgType", name="wx_msg_type", unique=false),
				@Index(fields="createTime", name="wx_msg_time", unique=false)})
public class WxMsgHistory {

	/**消息id*/
	private long msgId;
	/**客户id,在同一个公众号内,同一个微信用户id唯一*/
	private String clientId;
	/**消息类型*/
	private String msgType;
	/**消息的主要内容,当msgType=text时,为文本,其他类型时暂不确定*/
	@ColDefine(width=10240)
	private String msgContent;
	/**消息的方向 0 客户-->服务器, 1 服务器-->客户 */
	private int msgDirection;
	/**消息创建的数据, 毫秒算*/
	private long createTime;
	/**原始消息,json格式*/
	@ColDefine(width=10240)
	private String body;
	
	public WxMsgHistory() {
	}
	


	public WxMsgHistory(long msgId, String clientId, String msgType,
			String msgContent, int msgDirection, long createTime, String body) {
		super();
		this.msgId = msgId;
		this.clientId = clientId;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.msgDirection = msgDirection;
		this.createTime = createTime;
		this.body = body;
	}



	public long getMsgId() {
		return msgId;
	}
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public int getMsgDirection() {
		return msgDirection;
	}
	public void setMsgDirection(int msgDirection) {
		this.msgDirection = msgDirection;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}



	public long getCreateTime() {
		return createTime;
	}



	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	
	
}
