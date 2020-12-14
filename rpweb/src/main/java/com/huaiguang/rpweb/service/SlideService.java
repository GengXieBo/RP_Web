package com.huaiguang.rpweb.service;

import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.jdbc.SlideJDBC;
import com.huaiguang.rpweb.jdbc.UserJDBC;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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

    public void insertSlide(String userid, String slidename, String path, String flag) throws SQLException {
        String sql = "INSERT INTO slide (id, userid, path, result ,flag) VALUES (?, ?, ?, ?, ?)";
        Slide slide = new Slide();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        slide.setId(uuid);
        slide.setUserid(userid);
        slide.setPath(path+"\\"+slidename);
        slide.setResult(path+"\\"+slidename.substring(slidename.lastIndexOf('.')+1)+"\\");
        slide.setFlag(flag);
        slideJDBC.insert(sql, slide);
    }

    public void updateSlide(String slideid, String flag) throws SQLException {
        String sql = "UPDATE slide SET flag=? WHERE id=?";
        Slide slide = new Slide();

        slide.setId(slideid);
        slide.setFlag(flag);

        slideJDBC.updateById(sql, slide);
    }
}
