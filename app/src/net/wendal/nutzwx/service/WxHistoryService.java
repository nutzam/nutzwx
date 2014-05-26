package net.wendal.nutzwx.service;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.wendal.nutzwx.bean.WxMsgHistory;

import org.nutz.dao.Dao;
import org.nutz.dao.util.ExtDaos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;

@IocBean(name = "wxHistory", create = "init", depose = "depose")
public class WxHistoryService {

	private static final Log log = Logs.get();

	@Inject protected Dao dao;
	@Inject ResourceService resourceService;

	protected ExecutorService es;

	public void init() {
		es = Executors.newFixedThreadPool(16);
	}

	public void depose() {
		es.shutdown();
		try {
			es.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.info("WxHistoryService shutdown", e);
		}
		es = null;
	}

	public void push(final WxInMsg in) {
		ExecutorService es = this.es;
		if (es == null || es.isShutdown()) {
			log.info("WxHistoryService.queue is shutdown, ignore push");
			return;
		}
		es.submit(new Runnable() {
			public void run() {
				try {
					final WxMsgHistory history = new WxMsgHistory(in.getMsgId(),in.getFromUserName(), in.getMsgType(), 0, in.getCreateTime()*1000L);
					ExtDaos.ext(dao, in.getToUserName()).insert(history);
					resourceService.put(history.getMsgkey(), new ByteArrayInputStream(Json.toJson(in, JsonFormat.compact()).getBytes()));
				} catch (Throwable e) {
					log.info("record in msg fail", e);
				}
			}
		});
	}

	public void push(final WxOutMsg out) {
		ExecutorService es = this.es;
		if (es == null || es.isShutdown()) {
			log.info("WxHistoryService.queue is shutdown, ignore push");
			return;
		}
		es.submit(new Runnable() {
			public void run() {
				try {
					final WxMsgHistory history = new WxMsgHistory(0, out.getToUserName(), out.getMsgType(), 1, System.currentTimeMillis());
					ExtDaos.ext(dao, out.getFromUserName()).insert(history);
					resourceService.put(history.getMsgkey(), new ByteArrayInputStream(Json.toJson(out, JsonFormat.compact()).getBytes()));
				} catch (Throwable e) {
					log.info("record out msg fail", e);
				}
			}
		});
	}

}
