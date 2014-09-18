package net.wendal.iot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.wendal.iot.service.IotSensorService;
import net.wendal.iot.service.IotService;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(create = "init", depose="close")
public class IotNettyService {

	@Inject
	Dao dao;
	@Inject
	IotService iotService;
	@Inject
	IotSensorService iotSensorService;
	@Inject
	IotServerInitializer iotServerInitializer;
	
	public static final int PORT = 8999;
	
	ServerBootstrap b;
	
	EventLoopGroup bossGroup;
	EventLoopGroup workerGroup;
	
	ChannelFuture cf;

	public void init() throws InterruptedException {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(iotServerInitializer);

		cf = b.bind(PORT);
	}
	
	public void close() throws InterruptedException {
		if (cf != null) {
			try {
				cf.channel().close().sync();
			} finally {
				bossGroup.shutdownGracefully().await();
				workerGroup.shutdownGracefully().await();
			}
		}
	}
}
