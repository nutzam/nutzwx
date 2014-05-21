package net.wendal.nutzwx.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.wendal.nutzwx.bean.WxMsgHistory;

import org.nutz.dao.Dao;
import org.nutz.dao.TableName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMsgType;
import org.nutz.weixin.bean.WxOutMsg;

@IocBean(name = "wxHistory", create = "init", depose = "depose")
public class WxHistoryService {

	private static final Log log = Logs.get();

	@Inject
	protected Dao dao;

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
				final WxMsgHistory msgHistory = new WxMsgHistory(in.getMsgId(),
						in.getFromUserName(), in.getMsgType(), null, 0, in.getCreateTime()*1000L, Json
								.toJson(in, JsonFormat.compact()));
				switch (WxMsgType.valueOf(in.getMsgType())) {
				case text:
					msgHistory.setMsgContent(in.getContent());
					break;
				case image:
					msgHistory.setMsgContent(in.getMediaId());
					break;
				case voice:
					msgHistory.setMsgContent(in.getMediaId());
					break;
				case video:
					msgHistory.setMsgContent(in.getMediaId());
					break;
				case location:
					msgHistory.setMsgContent(in.getLocation_X() + "x"
							+ in.getLocation_Y());
					break;
				case link:
					msgHistory.setMsgContent(in.getUrl());
					break;
				case event:
					msgHistory.setMsgContent(in.getEventKey());
					break;
				default:
					break;
				}
				TableName.run(in.getToUserName(), new Runnable() {
					public void run() {
						dao.fastInsert(msgHistory);
					}
				});
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
				final WxMsgHistory msgHistory = new WxMsgHistory(0, out
						.getToUserName(), out.getMsgType(), null, 1, System.currentTimeMillis(), Json
						.toJson(out, JsonFormat.compact()));
				switch (WxMsgType.valueOf(out.getMsgType())) {
				case text:
					msgHistory.setMsgContent(out.getContent());
					break;
				default:
					break;
				}
				TableName.run(out.getFromUserName(), new Runnable() {
					public void run() {
						dao.fastInsert(msgHistory);
					}
				});
			}
		});
	}

}
