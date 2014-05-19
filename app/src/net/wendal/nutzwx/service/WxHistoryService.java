package net.wendal.nutzwx.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.nutz.dao.Chain;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;

@IocBean(name="wxHistory", create="init", depose="depose")
public class WxHistoryService implements Runnable {
	
	private static final Log log = Logs.get();

	@Inject protected Dao dao;
	
	protected LinkedBlockingQueue<WxInMsg> queue;
	
	protected Thread thread;
	
	public void init() {
		queue = new LinkedBlockingQueue<>();
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void depose() {
		LinkedBlockingQueue<WxInMsg> tmp = queue;
		queue = null;
		if (tmp == null)
			return;
		try {
			tmp.put(new WxInMsg());
		} catch (InterruptedException e) {
			log.info("insert NULL item to close WxHistoryService.thread fail?", e);
		}
	}
	
	public void push(WxInMsg in) {
		if (queue == null) {
			log.info("WxHistoryService.queue is shutdown, ignore push");
			return;
		}
		queue.add(in);
	}
	
	public void run() {
		LinkedBlockingQueue<WxInMsg> tmp = null;
		WxInMsg msg = null;
		while (this.queue != null) {
			tmp = this.queue;
			if (tmp == null)
				break;
			try {
				msg = tmp.poll(5, TimeUnit.SECONDS);
				if (msg == null || msg.getFromUserName() == null)
					continue;
				dao.insert("wx_history_" + msg.getToUserName(), Chain.from(msg));
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				log.info("insert wx history fail >> " + Json.toJson(msg, JsonFormat.compact()), e);
			}
		}
		log.info("WxHistoryService.thread exit");
		queue = null; // 确保一下
	}
}
