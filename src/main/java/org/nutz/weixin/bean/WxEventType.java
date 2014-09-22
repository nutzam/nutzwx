package org.nutz.weixin.bean;

public enum WxEventType {
	subscribe,unsubscribe,SCAN,LOCATION,CLICK,VIEW,TEMPLATESENDJOBFINISH,
	/**5.4.1以上的微信*/
	scancode_push,
	scancode_waitmsg,
	pic_sysphoto,
	pic_photo_or_album,
	pic_weixin,
	location_select,

}
