package com.huaiguang.rpweb.jdbc;

import com.huaiguang.rpweb.entity.Slide;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlideJDBC extends JDBCTemplate<Slide> {
    public SlideJDBC(String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD) {
        super(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public Slide queryTrans(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String userid = rs.getString(2);
        String path = rs.getString(3);
        String result = rs.getString(4);
        Slide slide = new Slide();
        slide.setId(id);
        slide.setUserid(userid);
        slide.setPath(path);
        slide.setResult(result);
        return slide;
    }

    @Override
    public void insertTrans(Slide slide, PreparedStatement ps) throws SQLException {
        ps.setObject(1, slide.getId());
        ps.setObject(2, slide.getUserid());
        ps.setObject(3, slide.getPath());
        ps.setObject(4, slide.getResult());
    }

    @Override
    public void updateTrans(Slide slide, PreparedStatement ps, int num) throws SQLException {

    }

    @Override
    public void deleteTrans(Slide slide, PreparedStatement ps) throws SQLException {
        ps.setObject(1, slide.getId());
    }
}
