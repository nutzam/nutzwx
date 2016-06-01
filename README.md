nutzwx
======

微信(weixin) IN Nutz

[![Build Status](https://travis-ci.org/nutzam/nutzwx.png?branch=master)](https://travis-ci.org/nutzam/nutzwx)
[![Circle CI](https://circleci.com/gh/nutzam/nutzwx/tree/master.svg?style=svg)](https://circleci.com/gh/nutzam/nutzwx/tree/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.nutz/nutzwx/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.nutz/nutzwx/)
[![codecov.io](http://codecov.io/github/nutzam/nutz/coverage.svg?branch=master)](http://codecov.io/github/nutzam/nutzwx?branch=master)
[![GitHub release](https://img.shields.io/github/release/nutzam/nutz.svg)](https://github.com/nutzam/nutzwx/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)



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
	
Maven配置
=============================

快照版本在每次提交后会自动deploy到sonatype快照库,享受各种bug fix和新功能

```xml
	<repositories>
		<repository>
			<id>ossrh</id>
			<url>https://oss.sonatype.org/content/repositories/snapshots</url>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>org.nutz</groupId>
			<artifactId>nutzwx</artifactId>
			<version>1.r.57-SNAPSHOT</version>
		</dependency>
		<!-- 其他依赖 -->
	</dependencies>
```

也可以将repositories配置放入$HOME/.m2/settings.xml中

或者直接去[快照库下载](https://oss.sonatype.org/content/repositories/snapshots/org/nutz/nutz/1.r.57-SNAPSHOT/)



LICENSE
=============================

Apache License
