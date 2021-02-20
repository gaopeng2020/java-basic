# java
java projects

IDEA克隆后如果没有自动生成.iml文件，解决办法：打开terminal运行 mvn idea:module

Eclipse克隆后需要将项目转换为maven工程：选择根目录->右键选择Configure-> Convert to Maven Project

为了加快github的速度，在C:\Windows\System32\drivers\etc路径下，打开host文件，结尾添加两行：
151.101.185.194 global-ssl.fastly.Net
192.30.253.113 github.com
