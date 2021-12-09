package com.xgf.designpattern.structure.bridge;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2021-12-09 21:06
 * @description 桥接模式测试
 *  常用的JDBC和DriverManager使用桥接模式实现
 *  JDBC在连接数据库时，在各个数据库之间进行切换而不需要修改代码，因为JDBC提供了统一的接口，每个数据库都提供了各自的实现，通过数据库驱动的程序来桥接即可
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class BridgeTest {

    @Resource(name = "mySqlDriver")
    private MySqlDriver mySqlDriver;

    @Resource(name = "oracleDriver")
    private OracleDriver oracleDriver;

    @Resource(name = "sqlServerDriver")
    private SqlServerDriver sqlServerDriver;

    @Test
    public void test(){
        MyDriverBridge myDriverBridge = new MyDriverBridge();

        String sql = "select * from tableName order by createdTime desc";

        myDriverBridge.setDriver(mySqlDriver);
        myDriverBridge.execute(sql);

        // 切换 sqlServer 驱动
        myDriverBridge.setDriver(sqlServerDriver);
        myDriverBridge.execute(sql);

        // 切换 oracle 驱动
        myDriverBridge.setDriver(oracleDriver);
        myDriverBridge.execute(sql);

    }

}
