package net.wendal.nutzwx.util;

import org.nutz.lang.Lang;


public class Toolkit {

	
	public static String captcha_attr = "nutz_captcha";

	public static boolean checkCaptcha(String expected, String actual) {
		if (expected == null || actual == null || actual.length() == 0 || actual.length() > 24)
			return false;
		return actual.equalsIgnoreCase(expected);
	}
	
	public static String passwordEncode(String password, String slat) {
		String str = slat + password + slat + password.substring(4);
		return Lang.digest("SHA-512", str);
	}
}
