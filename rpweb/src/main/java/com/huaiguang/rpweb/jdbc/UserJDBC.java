package com.huaiguang.rpweb.jdbc;

import com.huaiguang.rpweb.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJDBC extends JDBCTemplate<User> {


    public UserJDBC(String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD) {
        super(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public User queryTrans(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String username = rs.getString(2);
        String password = rs.getString(3);
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    @Override
    public void insertTrans(User user, PreparedStatement ps) throws SQLException {
        ps.setObject(1, user.getId());
        ps.setObject(2, user.getUsername());
        ps.setObject(3, user.getPassword());
        ps.setObject(4, user.getCompany_unit());
        ps.setObject(5, user.getProduction_method());
        ps.setObject(6, user.getDyeing_method());
        ps.setObject(7, user.getScanning_method());
    }

    @Override
    public void updateTrans(User user, PreparedStatement ps) throws SQLException {

    }

    @Override
    public void deleteTrans(User user, PreparedStatement ps) throws SQLException {
        ps.setObject(1, user.getId());
    }

}
