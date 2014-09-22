package net.wendal.iot.mqtt;

import net.wendal.iot.bean.IotUser;
import net.wendal.iot.service.IotService;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean(create = "init", depose = "close")
public class MqttService {
	
	private static final Log log = Logs.get();

	@Inject
	Dao dao;
	
	@Inject
	IotService iotService;
	
	@Inject("java:$config.get('mqtt.ip')")
	String mqttIp;
	
	@Inject("java:$config.get('mqtt.port')")
	int mqttPort;
	
	int qos = 2;
	String broker ;
	String clientId = "JavaSample" + System.currentTimeMillis();
	MemoryPersistence persistence = new MemoryPersistence();
	MqttClient sampleClient;

	public void init() throws Exception {
		broker = String.format("tcp://%s:%d", mqttIp, mqttPort);
		sampleClient = new MqttClient(broker, clientId, persistence);
	}
	
	public boolean _init() {
		if (sampleClient.isConnected())
			return true;
		IotUser root = iotService.rootUser();
		if (root == null)
			return false;
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName("root");
        connOpts.setPassword(root.getApikey().toCharArray());
        connOpts.setKeepAliveInterval(15);
        try {
			sampleClient.connect(connOpts);
			return true;
		} catch (Exception e) {
			log.info("mqtt connect fail", e);
			return false;
		}
	}
	
	public void publish(String topic, String msg) {
		if (!_init())
			return;
		log.debugf("mqtt topic=%s msg=%s", topic, msg);
		try {
			sampleClient.publish(topic, msg.getBytes(), 2, true);
		} catch (Exception e) {
			log.infof("publish mqtt msg fail topic=%s msg=%s", topic, msg, e);
		}
	}

	public void close() throws Exception {
		if (sampleClient != null)
			sampleClient.close();
	}
}
