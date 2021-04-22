# java
java projects
IDE的字符集必须提前设置为UTF-8

IDEA克隆后如果没有自动生成.iml文件，解决办法：打开terminal运行 mvn idea:module

Eclipse克隆是选择General Project，克隆完成后从eclipse中将项目删除，但不要勾选从硬盘删除，然后选择导入->Existing Maven Projects即可。

Git global setup
git config --global user.name "GaoPeng"
git config --global user.email "gaopeng@e-panet.cn"

Create a new repository
git clone git@localhost:gaopeng/test.git
cd test
touch README.md
git add README.md
git commit -m "add README"
git push -u origin main

Push an existing folder
cd existing_folder
git init
git remote add origin git@localhost:gaopeng/test.git
git add .
git commit -m "Initial commit"
git push -u origin main

Push an existing Git repository
cd existing_repo
git remote rename origin old-origin
git remote add origin git@localhost:gaopeng/test.git
git push -u origin --all
git push -u origin --tags