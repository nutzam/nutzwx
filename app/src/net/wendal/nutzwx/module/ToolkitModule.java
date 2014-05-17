package net.wendal.nutzwx.module;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpSession;

import net.wendal.nutzwx.util.Toolkit;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;

import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

@At("/toolkit")
public class ToolkitModule {

	@At("/captcha/update")
	@Ok("raw:image/png")
	public BufferedImage updateCaptcha(HttpSession session, @Param("w") int w, @Param("h") int h) {
		if (w * h == 0) { //长或宽为0?重置为默认长宽.
			w = 200;
			h = 60;
		}
		Captcha captcha = new Captcha.Builder(w, h)
								.addText().addBackground(new GradiatedBackgroundProducer())
//								.addNoise(new StraightLineNoiseProducer()).addBorder()
								.gimp(new FishEyeGimpyRenderer())
								.build();
		String text = captcha.getAnswer();
		session.setAttribute(Toolkit.captcha_attr, text);
		return captcha.getImage();
	}
	
	@At("/captcha/check")
	public boolean checkCaptcha(@Param("answer")String answer) {
		HttpSession session = Mvcs.getHttpSession(false);
		if (session != null) {
			Object text = session.getAttribute(answer);
			if (text != null) {
				if (Toolkit.checkCaptcha(text.toString(), answer))
					return true;
			}
			// 当输入出错,强制要求重新生成新的验证码
			//session.removeAttribute(captcha_attr);
		}
		return false;
	}
}
