package net.wendal.iot.netty;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.util.Map;

import net.wendal.Zs;
import net.wendal.iot.bean.IotSensor;
import net.wendal.iot.bean.IotUser;
import net.wendal.iot.service.IotSensorService;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;

/**
 * Handles a server-side channel.
 */
@Sharable
@IocBean
public class IotServerHandler extends SimpleChannelInboundHandler<String> {
	
	public static final String version = "1.0";
	
	@Inject Dao dao;
	
	@Inject IotSensorService iotSensorService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        resp(ctx, "ok", "ver,"+version);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String req) throws Exception {
        // Generate and write a response.
        if (req.isEmpty()) {
            resp(ctx, "ok", "^-^");
            return;
        } 
        if ("bye".equals(req.toLowerCase())) {
        	resp(ctx, "ok", "byte").addListener(ChannelFutureListener.CLOSE);
            return;
        }
        String[] tmp = req.split(",", 2);
        if (tmp.length != 2) {
            	resp(ctx,"err","bad cmd");
            	return;
        }
        if ("auth".equals(tmp[0])) {
        	IotUser usr = dao.fetch(IotUser.class, tmp[1]);
        	if (usr == null) {
            	resp(ctx, "err","bad api key");
            	return;
        	}
            ctx.attr(AttributeKey.valueOf(Zs.UID)).set(usr.getUserId());;
            resp(ctx,"ok","auth ok");
            return;
        }    
        if ("w".equals(tmp[0]) || "r".equals(tmp[0])) {
            Long userId = (Long) ctx.attr(AttributeKey.valueOf(Zs.UID)).get();
            if (userId == null) {
            	resp(ctx, "err", "not auth yet");
            	return;
            }
            String[] tmp2 = tmp[1].split(",", 2);
            long sensorId = -1;
            try {
				sensorId = Long.parseLong(tmp2[0]);
			} catch (Exception e) {
			}
            if (sensorId < 0) {
            	resp(ctx, "err", "bad sensor id = " + tmp2[0]);
            	return;
            }
            IotSensor sensor = dao.fetch(IotSensor.class, sensorId);
            if (sensor == null) {
            	resp(ctx, "err" ,"no such sensor");
            	return;
            }
            if (sensor.getUserId() != userId) {
            	resp(ctx, "err", "not your sensor");
            	return;
            }
            if ("r".equals(tmp[0])) {
            	resp(ctx, "ok", sensor.getValue() == null ? "{}" : sensor.getValue());
            	return;
            }
            if (tmp2.length != 2) {
            	resp(ctx, "err", "need values");
            	return;
            }
            if (Strings.isBlank(tmp[1])) {
            	resp(ctx, "err", "bad value");
            	return;
            }
            Map<String, Object> map = null;
            try {
				map = Json.fromJsonAsMap(Object.class, tmp[1]);
			} catch (Exception e) {
				resp(ctx, "err", "bad json");
				return;
			}
            if (map == null) {
            	resp(ctx, "err", "null json value");
				return;
            }
            try {
            	String re = iotSensorService.updateSensorValue(sensor, map);
            	if (re == null) {
            		resp(ctx, "ok", "done");
            	} else {
            		resp(ctx, "err", re);
            	}
            	return;
			} catch (Exception e) {
				resp(ctx, "err", "udpate fail");
				return;
			}
        }
        resp(ctx, "err", "unknow cmd");
    }
    
    public ChannelFuture resp(ChannelHandlerContext ctx, String stat, String msg) {
    	return ctx.write(stat + "," + msg + "\r\n");
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}