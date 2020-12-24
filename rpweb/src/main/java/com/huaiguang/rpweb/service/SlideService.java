package com.huaiguang.rpweb.service;

import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.jdbc.SlideJDBC;
import com.huaiguang.rpweb.jdbc.UserJDBC;
import org.apache.ibatis.jdbc.SQL;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SlideService {

    public String JDBC_URL = "jdbc:mysql://huaiguangcsh.f3322.net:2006/rpweb?serverTimezone=GMT%2B8";
    public String JDBC_USER = "gxb";
    public String JDBC_PASSWORD = "1181895140";
    SlideJDBC slideJDBC = new SlideJDBC(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    public List<Slide> queryAll(String userid) throws SQLException {
        String sql = "SELECT * FROM slide WHERE userid='"+userid +"' order by create_time";
        List list = slideJDBC.query(sql);
        return list;
    }

    public void insertSlide(String userid, String slidename, String path, String flag, String score) throws SQLException {
        String sql = "INSERT INTO slide (id, userid, path, result ,flag, score) VALUES (?, ?, ?, ?, ?, ?)";
        Slide slide = new Slide();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        slide.setId(uuid);
        slide.setUserid(userid);
        slide.setPath(path+"\\"+slidename);
        slide.setResult(path+"\\"+slidename.substring(0, slidename.lastIndexOf('.'))+"\\");
        slide.setFlag(flag);
        slide.setScore(score);
        slideJDBC.insert(sql, slide);
    }

    public void updateSlide(String slideid, String flag, String score) throws SQLException {
        String sql = "UPDATE slide SET flag=?, score=? WHERE id=?";
        Slide slide = new Slide();

        slide.setId(slideid);
        slide.setFlag(flag);
        slide.setScore(score);

        slideJDBC.updateById(sql, slide);
    }

    public void deleteSlide(String slideid) throws SQLException {
        String sql = "DELETE FROM slide WHERE id=?";
        Slide slide = new Slide();
        slide.setId(slideid);
        slideJDBC.deleteById(sql, slide);
    }
}
