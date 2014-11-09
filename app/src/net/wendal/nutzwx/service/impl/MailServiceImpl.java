package net.wendal.nutzwx.service.impl;

import net.wendal.nutzwx.service.MailService;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.nutz.dao.Dao;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.segment.Segments;
import org.nutz.lang.util.Context;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean(name="mailService")
public class MailServiceImpl implements MailService {
	
	private static final Log log = Logs.get();

	@Inject protected Dao dao;

	@Inject PropertiesProxy config;
	
	public boolean send(String to, String subject, String tpl, Context ctx) {
		Email email = new SimpleEmail();
		email.setHostName(config.get("mail.host"));
		// email.setAuthenticator(new DefaultAuthenticator(config.get("mail.user"), config.get("mail.passwd")));
		if (config.getInt("mail.ssl", 0) == 1) {
			email.setSSLOnConnect(true);
			email.setSmtpPort(config.getInt("mail.port", 465));
		} else {
			email.setSmtpPort(config.getInt("mail.port", 25));
		}
		try {
			email.setFrom(config.get("mail.from"));
			email.setSubject("["+config.get("mail.suject.prefix", "Test") + "] " + subject);
			email.setMsg(Segments.create(tpl).render(ctx).toString());
			email.addTo(to);
			email.send();
			return true;
		} catch (EmailException e) {
			log.info("Send email fail", e);
			return false;
		}
	}
	
}
