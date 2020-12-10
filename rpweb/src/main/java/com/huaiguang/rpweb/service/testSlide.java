package com.huaiguang.rpweb.service;

import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.jdbc.SlideJDBC;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class testSlide {
    public static void main(String[] args) throws SQLException {
        String JDBC_URL = "jdbc:mysql://localhost:3306/rpweb?serverTimezone=GMT%2B8";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "lx123456";
        SlideJDBC slideJDBC = new SlideJDBC(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

        //插入
//        String sql = "INSERT INTO slide (id, userid, path, result) VALUES (?, ?, ?, ?)";
//        Slide slide = new Slide();
//        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
//        slide.setId(uuid);
//        slide.setUserid("477c8b24e4f649ab954eaa70a43b8ccd");
//        slide.setPath("e:/test/fdsf/f55555555555555555");
//        //slide.setResult("e:/dsasd/sds/sad55555555555555555555555555");
//        slideJDBC.insert(sql, slide);

        //查询
        //String sql = "SELECT * FROM user where username='lixu'";
//        String sql = "SELECT * FROM slide";
//        List list = slideJDBC.query(sql);
//        for(int i=0; i<list.size(); i++){
//            Slide user =  (Slide)list.get(i);
//            System.out.print(user.getId()+" ");
//            System.out.print(user.getUserid()+" ");
//            System.out.print(user.getPath()+" ");
//            System.out.println(user.getResult());
//        }

        //删除
        String sql = "DELETE FROM slide WHERE id=?";
        Slide user = new Slide();
        String id = "eaef0034b1a8452089fd5318da0e362f";
        user.setId(id);
        //user.setUsername("lixulixu");
        slideJDBC.deleteById(sql, user);

    }
}
