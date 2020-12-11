package com.huaiguang.rpweb.service;

import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.jdbc.SlideJDBC;
import com.huaiguang.rpweb.jdbc.UserJDBC;

import java.sql.SQLException;
import java.util.List;

public class SlideService {

    public String JDBC_URL = "jdbc:mysql://localhost:3306/rpweb?serverTimezone=GMT%2B8";
    public String JDBC_USER = "root";
    public String JDBC_PASSWORD = "lx123456";
    SlideJDBC slideJDBC = new SlideJDBC(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    public List<Slide> queryAll(String userid) throws SQLException {
        String sql = "SELECT * FROM slide WHERE userid='"+userid +"'";
        List list = slideJDBC.query(sql);
        return list;
    }

    public void queryByUserid(String userid) {

    }
}
