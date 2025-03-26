## 0x01 简介

woodpecker-box是一款woodpecker插件, 用于集成一些个人平时遇到的常见漏洞以及漏洞复现过程中顺手编写的自动化利用方法, 方便攻防时提高效率

## 0x02 功能
### detector plugins
- [x] landray oa info detector: session泄露漏洞
- [x] spring actuator info detector: spring端点泄露

***
### exploit plugins
- [x] apache activemq openwire反序列化远程代码插件 
- [x] 帆软channel反序列化漏洞插件: 远程命令执行回显
- [x] geoserver远程代码执行插件: 内存马注入
- [x] jeecgboot jdbc远程代码执行插件
- [ ] 帆软V8上传插件漏洞插件
- [ ] 帆软V9任意文件写入漏洞插件
- [x] spring cloud远程代码执行漏洞(CVE-2022-22947)插件: 内存马注入
- [ ] xxl-job executor restful api未授权访问漏洞插件: 反弹shell
- [x] 通用hessian反序列化漏洞插件: 反弹shell
- [ ] jolokia logback rce
- [ ] jolokia create rce

![img.png](exploit.png)

***

### helper plugins
- [x] 大华ICCC数据库密码解密
- [x] 帆软报表数据库密码解密
- [x] 金蝶EAS数据库密码解密
- [x] spring bean xml生成: 反弹shell
- [x] behinder、suo5 jsp生成: bcel编码

![img_1.png](helper.png)
***

## 0x03 参考
* https://github.com/Rvn0xsy/PassDecode-jar/tree/main
* https://github.com/whwlsfb/cve-2022-22947-godzilla-memshell