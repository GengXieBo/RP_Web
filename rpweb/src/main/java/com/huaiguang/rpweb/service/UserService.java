package com.huaiguang.rpweb.service;

import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.jdbc.UserJDBC;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserService {

    public String JDBC_URL = "jdbc:mysql://huaiguangcsh.f3322.net:2006/rpweb?serverTimezone=GMT%2B8";
    public String JDBC_USER = "gxb";
    public String JDBC_PASSWORD = "1181895140";
    UserJDBC userJDBC = new UserJDBC(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    public List<User> queryAll() throws SQLException {
        String sql = "SELECT * FROM user";
        List list = userJDBC.query(sql);
        return list;
    }

    public User queryByUsername(String username) throws SQLException{
        String sql = "SELECT * FROM user where username='"+username +"'";
        List list = userJDBC.query(sql);
        System.out.println(list);
        if(list.size()==0) return null;
        return (User) list.get(0);
    }

    public User queryById(String id) throws SQLException{
        String sql = "SELECT * FROM user where id='"+id +"'";
        List list = userJDBC.query(sql);

        return (User) list.get(0);

    }

    public void insertUser(User user) throws SQLException {

        String sql = "INSERT INTO user (id, username, password, company_unit, production_method, dyeing_method, scanning_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
        userJDBC.insert(sql, user);


    }
}
