nutzwx
======

微信(weixin) IN Nutz

[![Build Status](https://travis-ci.org/nutzam/nutzwx.png?branch=master)](https://travis-ci.org/nutzam/nutzwx)
[![codecov.io](http://codecov.io/github/nutzam/nutz/coverage.svg?branch=master)](http://codecov.io/github/nutzam/nutzwx?branch=master)
[![GitHub release](https://img.shields.io/github/release/nutzam/nutz.svg)](https://github.com/nutzam/nutzwx/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)



文档地址 http://nutzam.com/core/weixin/helloworld.html

下载JCE无限制权限策略文件

* 下载(jdk官网或本项目的jdk-patch目录下有)后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
* 如果安装了JRE，将两个jar文件放到%JRE_HOME%/lib/security目录下覆盖原来的文件
* 如果安装了JDK，将两个jar文件放到%JDK_HOME%/jre/lib/security目录下覆盖原来文件