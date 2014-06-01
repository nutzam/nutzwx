package net.wendal.nutzwx.module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.wendal.nutzwx.service.MediaService;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
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
	
	@AdaptBy(type=UploadAdaptor.class, args={"~/tmp", "8192", "utf-8", "200000", "1048576"})
	@POST
	@At("/media/?/?")
	@Ok("json")
	public Object upload(String openid, String type, @Attr("usr")String usr, @Param("filedata")File tmpf) {
		String contentType = null;
		if (tmpf == null) {
			throw new IllegalArgumentException("file is null");
		}
		long fz = tmpf.length();
		if ("image".equals(type)) { //TODO 基于文件内容进行判断,而非用户传入的值
			if (fz > 256*1024)
				throw new IllegalArgumentException("file too large");
			contentType = "image/jpeg";
		} else if ("voice".equals(type)) {
			if (fz > 256*1024)
				throw new IllegalArgumentException("file too large");
			contentType = "audio/amr";
		} else if ("video".equals(type)) {
			if (fz > 1024*1024)
				throw new IllegalArgumentException("file too large");
			contentType = "video/mp4";
		} else if ("thumb".equals(type)) {
			if (fz > 64*1024)
				throw new IllegalArgumentException("file too large");
			contentType = "image/jpeg";
		} else {
			throw new IllegalArgumentException("not allow " + openid + ", " + type);
		}
		WxMaster master = wxctx.get(openid);
		if (master == null || !master.getOpenid().equals(usr)) {
			throw new IllegalArgumentException("not allow " + openid);
		}
		WxAPI api = wxctx.getAPI(openid);
		String mediaId = api.mediaUpload(type, tmpf);
		mediaService.save(openid, new WxMedia(openid, fz, contentType), tmpf);
		tmpf.delete();
		return new NutMap().setv("media_id", mediaId);
	}
	
	// TODO 因为上传的媒体文件有效期仅3天,需要支持重新上传的功能.
}
