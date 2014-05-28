package net.wendal.nutzwx;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * 与普通的MainModule无任何差异, nutzwx就希望能以普通模块的方式嵌入到你的应用中
 */
@Modules(scanPackage=true)
@IocBy(type=ComboIocProvider.class, args={"*js", "ioc/", "*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "net.wendal.nutzwx"})
public class MainModule {
}
