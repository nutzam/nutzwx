package org.nutz.weixin.at;

public class WxJsapiTicket {

    protected String ticket;

    protected int expires;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

}
