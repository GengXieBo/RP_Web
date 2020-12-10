package com.huaiguang.rpweb.service;


import com.huaiguang.rpweb.jdbc.UserJDBC;

import java.sql.SQLException;

public class testUser {

    public static void main(String[] args) throws SQLException {

        String JDBC_URL = "jdbc:mysql://localhost:3306/rpweb?serverTimezone=GMT%2B8";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "lx123456";


        UserJDBC userJDBC = new UserJDBC(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

        //查询
        //String sql = "SELECT * FROM user where username='lixu'";
//        String sql = "SELECT * FROM user";
//        List list = userJDBC.query(sql);
//        for(int i=0; i<list.size(); i++){
//            User user =  (User)list.get(i);
//            System.out.print(user.getId()+" ");
//            System.out.print(user.getUsername()+" ");
//            System.out.println(user.getPassword());
//        }

        //插入
//        String sql = "INSERT INTO user (id, username, password) VALUES (?, ?, ?)";
//        User user = new User();
//        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
//        user.setId(uuid);
//        user.setUsername("vvv");
//        user.setPassword("123456");
//        userJDBC.updateById(sql, user);


        //删除
//        String sql = "DELETE FROM user WHERE id=?";
//        User user = new User();
//        String id = "d65556563a8811eb9b0600155d78a40d";
//        user.setId(id);
//        //user.setUsername("lixulixu");
//        userJDBC.deleteById(sql, user);

        //修改
//        String sql = "UPDATE user SET username=? WHERE id=?";
//        User user = new User();
//        String id = "d65556563a8811eb9b0600155d78a40d";
//        user.setId(id);
//        user.setUsername("lixulixu");
//        userJDBC.updateById(sql, user, 1);
    }
}
