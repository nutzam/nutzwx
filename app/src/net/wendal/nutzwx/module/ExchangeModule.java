package net.wendal.nutzwx.module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.wendal.nutzwx.service.MediaService;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.weixin.bean.WxMaster;
import org.nutz.weixin.bean.WxMedia;
import org.nutz.weixin.spi.WxAPI;
import org.nutz.weixin.util.WxContext;

@At("/ex")
@IocBean
public class ExchangeModule {

	@Inject WxContext wxctx;
	
	@Inject MediaService mediaService;
	
	@GET
	@At("/media/?/?")
	@Ok("raw")
	@Fail("http:404")
	public InputStream readMedia(String openid, String mediaId, @Attr("usr")String usr, HttpServletResponse resp) throws IOException {
		WxMaster master = wxctx.get(openid);
		if (master == null || !master.getOpenid().equals(usr)) {
			throw new IllegalArgumentException("not allow " + openid + "," + mediaId);
		}
		WxMedia media = mediaService.get(openid, mediaId);
		if (media == null) {
			throw new IllegalArgumentException("not found " + openid + "," + mediaId);
		}
		if (Strings.isBlank(media.getContentType())) {
			resp.setContentType(media.getContentType());
		}
		return media.getStream();
	}
	
	@AdaptBy(type=UploadAdaptor.class)
	@POST
	@At("/media/?/?")
	@Ok("json")
	public Object upload(String openid, String type, @Attr("usr")String usr, @Param("filedata")TempFile tmpf) {
		WxMaster master = wxctx.get(openid);
		if (master == null || !master.getOpenid().equals(usr)) {
			throw new IllegalArgumentException("not allow " + openid);
		}
		WxAPI api = wxctx.getAPI(openid);
		String mediaId = api.mediaUpload(type, tmpf.getFile());
		File target = new File(mediaService.mediaPath(openid, mediaId));
		Files.makeDir(target.getParentFile());
		Files.copy(tmpf.getFile(), target);
		return new NutMap().setv("media_id", mediaId);
	}
	
	// TODO 因为上传的媒体文件有效期仅3天,需要支持重新上传的功能.
}
