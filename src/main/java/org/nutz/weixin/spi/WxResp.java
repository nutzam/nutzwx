package org.nutz.weixin.spi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.WxException;
import org.nutz.weixin.bean.WxGroup;
import org.nutz.weixin.bean.WxUser;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WxResp extends NutMap {

    private static final long serialVersionUID = -1;

    public boolean ok() {
        return errcode() == 0;
    }
    
    public void check() {
        if (!ok())
            throw new WxException("errcode=" + errcode() + ", " + this);
    }
    
    public int errcode() {
        return getInt("errcode", 0);
    }
    
    public String errmsg() {
        return getString("errmsg");
    }
    
    public String msg_id() {
        return getString("msg_id");
    }
    
    public String msg_status() {
        return getString("msg_status");
    }
    
    public String type() {
        return getString("type");
    }
    
    public int created_at() {
        return getInt("created_at", 0);
    }
    
    public Date created_at_date() {
        return new Date(created_at());
    }
    
    //--------------------------------------
    // 各种帮助方法
    
    public WxGroup group() {
        Map map = getAs("group", Map.class);
        if (map == null)
            return null;
        return Lang.map2Object(map, WxGroup.class);
    }
    
    public List<WxGroup> groups() {
        List<Map<String, Object>> list = getAs("groups", List.class);
        List<WxGroup> groups = new ArrayList<WxGroup>();
        for (Map<String, Object> e : list) {
            groups.add(Lang.map2Object(e, WxGroup.class));
        }
        return groups;
    }
    
    public WxUser user() {
        return Lang.map2Object(this, WxUser.class);
    }
}
