module basic { //在模块前使用open关键字可开放真个模块的反射权限
    requires java.base;
    requires java.desktop;
    requires static lombok;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires transitive com.fasterxml.jackson.databind; //传递依赖，如果模块x依赖当前模块，则模块x不需要再次导入该需求模块即可直接使用
    requires java.sql;
    requires org.dom4j;
    requires commons.dbcp;
    requires commons.dbutils;
    requires org.apache.commons.codec;
    requires org.apache.commons.io;
    requires eptutils;

    exports basic.collection to javafxdemos; //只把这个包暴露给javafxdemos模块
    exports basic.grammer;
    opens basic.grammer; //使用open关键字可开放这个包的反射权限
//    exports basic.oop.constructor;
//    exports basic.oop.grammer;
//    exports basic.reflect;
//    exports basic.string;
//    exports basic.thread;
//    exports crypto;
//    exports dbutils;
//    exports dom4j;
//    exports jdbc;
//    exports json;
//    exports log4j;
//    exports multhread;
//    exports proxySkeleton.server;
//    exports rpc.client;
//    exports rpc.server;
}