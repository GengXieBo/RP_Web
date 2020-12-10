package com.huaiguang.rpweb;

import com.huaiguang.rpweb.entity.JDBCConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RpwebApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource


    @Test
    public void testbind(){
        JDBCConfig config = new JDBCConfig();
        System.out.println(config.getJDBC_URL());
    }

}
