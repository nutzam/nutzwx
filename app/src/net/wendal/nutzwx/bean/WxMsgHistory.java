package net.wendal.nutzwx.bean;

import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.lang.random.R;

/**
 * 微信消息列表. 注意,这个表相当于一个索引
 * @author Administrator
 *
 */
@Table("wx_msg_history_${openid}")
@TableIndexes({
		@Index(fields = "clientId", name = "wx_msg_clientId", unique = false),
		@Index(fields = "msgType", name = "wx_msg_type", unique = false),
		@Index(fields = "createTime", name = "wx_msg_time", unique = false) })
public class WxMsgHistory {

	@Name
	@Prev(els = { @EL(value = "$me._uuid()") })
	private String msgkey;

	/** 消息id */
	private long msgId;
	/** 客户id,在同一个公众号内,同一个微信用户id唯一 */
	private String clientId;
	/** 消息类型 */
	private String msgType;
	/** 消息的方向 0 客户-->服务器, 1 服务器-->客户 */
	private int msgDirection;
	/** 消息创建的数据, 毫秒算 */
	private long createTime;

	public WxMsgHistory() {
	}

	public String getMsgkey() {
		return msgkey;
	}
	
	public void setMsgkey(String msgkey) {
		this.msgkey = msgkey;
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

	public int getMsgDirection() {
		return msgDirection;
	}

	public void setMsgDirection(int msgDirection) {
		this.msgDirection = msgDirection;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public WxMsgHistory(long msgId, String clientId,
			String msgType, int msgDirection, long createTime) {
		super();
		this.msgId = msgId;
		this.clientId = clientId;
		this.msgType = msgType;
		this.msgDirection = msgDirection;
		this.createTime = createTime;
	}

	public String _uuid() {
		return R.UU16();
	}
}
