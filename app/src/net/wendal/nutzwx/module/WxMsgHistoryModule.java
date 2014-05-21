package net.wendal.nutzwx.module;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.wendal.nutzwx.bean.WxMpInfo;
import net.wendal.nutzwx.bean.WxMsgHistory;
import net.wendal.nutzwx.service.NutDaoWxContext;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.ExtDaos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Scope;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.view.HttpStatusView;

@At("/msg/history")
@IocBean
@Ok("json")
@Filters(@By(type=CheckSession.class, args={"usr", "/"}))
public class WxMsgHistoryModule {
	
//	private static final Log log = Logs.get();
	
	@Inject protected Dao dao;
	
	@Inject protected NutDaoWxContext wxctx;

	@At({"/?", "/?/?"})
	public Object query(String openid, String clientId, 
			Map<String, Object> map, @Param("..")Pager pager,
			@Attr(value="usr", scope=Scope.SESSION)String usr) {
		WxMpInfo master = (WxMpInfo) wxctx.get(openid);
		if (master == null || usr.equals(master.getOwner()))
			return new HttpStatusView(403);
		Cnd cnd = Cnd.NEW();
		if (clientId != null)
			cnd.and("client", "=", clientId);
		if (map != null) {
			for (Entry<String, Object> en : map.entrySet()) {
				cnd.and(en.getKey(), "=", en.getValue());
			}
		}
		int count = ExtDaos.ext(dao, openid).count(WxMsgHistory.class, cnd);
		cnd.desc("createTime"); // count之后再加orderBy嘛
		List<WxMsgHistory> list = ExtDaos.ext(dao, FieldFilter.locked(WxMsgHistory.class, "$(body)$"), openid).query(WxMsgHistory.class, cnd, pager);
		pager.setRecordCount(count);
		return new QueryResult(list, pager);
	}
	
	@At({"/?/?"})
	public Object lastInMsgTime(String openid, String clientId,
			@Attr(value="usr", scope=Scope.SESSION)String usr) {
		WxMpInfo master = (WxMpInfo) wxctx.get(openid);
		if (master == null || usr.equals(master.getOwner()))
			return new HttpStatusView(403);
		WxMsgHistory msg = ExtDaos.ext(dao, FieldFilter.create(WxMsgHistory.class, "$(createTime)$"), openid).fetch(WxMsgHistory.class, Cnd.where("clientId", "=", clientId).desc("createTime"));
		if (msg == null)
			return 0;
		return msg.getCreateTime();
	}
}
