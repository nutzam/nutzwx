package net.wendal.nutzwx.service;

import org.nutz.lang.util.Context;


public interface MailService {

	boolean send(String to, String subject, String tpl, Context ctx);
}
