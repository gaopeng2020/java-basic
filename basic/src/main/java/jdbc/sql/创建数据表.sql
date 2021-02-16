/*
    创建数据库
    create database 数据库名;
*/
CREATE DATABASE mybase;
/*
  使用数据库
  use 数据库名
*/
USE mybase;

/*
      创建数据表
          create table 表名(
          列名1 数据类型 约束,
          列名2 数据类型 约束,
          列名3 数据类型 约束
      );
      将编号列,设置为主键约束,保证列的数据唯一性,非空性， 让主键列数据,实现自动增长
      primary key AUTO_INCREMENT
*/
 CREATE TABLE users (
     uid INT PRIMARY KEY AUTO_INCREMENT ,
     uname VARCHAR(20),
     uaddress VARCHAR(200)
 );
 
 
SHOW TABLES  -- 显示所有数据表
DESC  users    -- 查看表中结构
DROP TABLE users -- 删除数据表




