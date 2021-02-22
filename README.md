# java
java projects
IDE的字符集必须提前设置为UTF-8

IDEA克隆后如果没有自动生成.iml文件，解决办法：打开terminal运行 mvn idea:module

Eclipse克隆是选择General Project，克隆完成后从eclipse中将项目删除，但不要勾选从硬盘删除，然后选择导入->Existing Maven Projects即可。

为了加快github的速度，在C:\Windows\System32\drivers\etc路径下，打开host文件，结尾添加一下四行内容：

#可通过https://fastly.net.ipaddress.com/github.global.ssl.fastly.net 获取fastly.net的IP地址

199.232.69.194 github.global.ssl.fastly.net

#可通过https://github.com.ipaddress.com/#ipinfo 获取github.com的IP地址

140.82.112.3 github.com

