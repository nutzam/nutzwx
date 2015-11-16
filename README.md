nutzwx
======

微信(weixin) IN Nutz

没帐号测试个鸟?
---------------

* 测试帐号申请地址 http://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login
* 你的微信帐号必须绑定手机哦

没公网ip?80端口被封? 有ngrok
------------------------------

* https://nutz.cn/yvr/links/ngrok.html

异常java.security.InvalidKeyException:illegal Key Size的解决方案
-------------------------------------------------------------------

下载JCE无限制权限策略文件
	* 下载(jdk官网或本项目的jdk-patch目录下有)后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
	* 如果安装了JRE，将两个jar文件放到%JRE_HOME%/lib/security目录下覆盖原来的文件
	* 如果安装了JDK，将两个jar文件放到%JDK_HOME%/jre/lib/security目录下覆盖原来文件


LICENSE
=============================

Apache License
