package com.huaiguang.rpweb.service;

import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.jdbc.UserJDBC;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    public String JDBC_URL = "jdbc:mysql://localhost:3306/rpweb?serverTimezone=GMT%2B8";
    public String JDBC_USER = "root";
    public String JDBC_PASSWORD = "lx123456";
    UserJDBC userJDBC = new UserJDBC(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    public List<User> queryAll() throws SQLException {
        String sql = "SELECT * FROM user";
        List list = userJDBC.query(sql);
        return list;
    }

    public User queryById(String id) throws SQLException{
        String sql = "SELECT * FROM user where id='"+id +"'";
        List list = userJDBC.query(sql);
//        for(int i=0; i<list.size(); i++){
//            User user =  (User)list.get(i);
//            System.out.print(user.getId()+" ");
//            System.out.print(user.getUsername()+" ");
//            System.out.println(user.getPassword());
//        }
        return (User) list.get(0);

    }
}
