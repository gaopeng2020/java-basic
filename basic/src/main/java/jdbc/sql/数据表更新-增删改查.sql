CREATE TABLE product(
   -- 主键列,自动增长
   id INT PRIMARY KEY AUTO_INCREMENT,
   -- 商品名字,可变字符,非空
   pname VARCHAR(100) NOT NULL,
   -- 商品的价格,double
   price DOUBLE
);

-- 数据的增加

/*
   向数据表中添加数据 insert
   格式:
     insert into 表名(列名1,列名2,列名3) values (值1,值2,值3)
*/

INSERT INTO product (id,pname,price) VALUES (1,'笔记本',5555.99);
INSERT INTO product (id,pname,price) VALUES (2,'智能手机',9999);

/*
   添加数据格式,可以不用不考虑主键
      insert into 表名 (列名) values (值)
*/
INSERT INTO product (pname,price) VALUES('洗衣机',800);

/*
   添加数据格式,批量写入
     insert into 表名 (列名1,列名2,列名3) values (值1,值2,值3),(值1,值2,值3)
*/
INSERT INTO product (pname,price) VALUES 
('智能机器人',25999.22),
('彩色电视',1250.36),
('沙发',5899.02)


/*
   添加数据格式,所有值全给出
     insert into 表名 values (全列值)
*/
INSERT INTO product VALUES (4,'微波炉',300.25);




-- 数据的修改


-- 修改智能手机,价格上调到15999
UPDATE product SET price = 15999 WHERE id=2

-- 修改彩色电视,名字改为黑白电视机,价格,100
UPDATE product SET pname='黑白电视机', price=100 WHERE id = 6

/*
    修改条件的写法
    id=6
    id<>6
    id<=6  
    与或非  && || !
    && and
    || or 
    ! not
    
    id in (1,3,4,5,6) 包含
*/
-- 将笔记本的价格,和空调的价格,全部修改为2000
UPDATE product SET price = 2000 WHERE id = 1 OR id = 7;



-- 数据表的操作

/*
  删除表中的数据
  格式:
    delete from 表名 where 条件，删除行
    
    drop table 表名 删除整个数据表
*/
DELETE FROM product WHERE id=8;

  /*
    删除列
    alter table 表名 drop 列名  
  */
  ALTER TABLE users DROP newtel;

/*
    添加列,添加字段
    alter table 表名 add 列名 数据类型 约束
 */
  ALTER TABLE users ADD tel INT ;
  
  /*
    更新列的约束条件
    alter table 表名 modify 列名 数据类型 约束
  */
  ALTER TABLE users MODIFY tel VARCHAR(50);
  
  /*
     修改列名
     alter table 表名 change 旧列名 新列名 数据类型 约束
  */
  ALTER TABLE users CHANGE tel newtel DOUBLE;
  
  
  /*
     修改表名
     rename table 表名 to 新名 
  */
  RENAME TABLE users TO newusers
