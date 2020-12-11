package com.huaiguang.rpweb.jdbc;

import jdk.nashorn.internal.scripts.JD;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.stylesheets.LinkStyle;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public abstract class JDBCTemplate<T> {

    public static Connection conn;

    public Connection getConn() throws SQLException {
        if(conn!=null)
            return conn;
        else
        {
            conn = DriverManager.getConnection(this.JDBC_URL, this.JDBC_USER, this.JDBC_PASSWORD);
            return conn;
        }
    }

    private String JDBC_URL;
    private String JDBC_USER;
    private String JDBC_PASSWORD;
    public JDBCTemplate(String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD) {
        this.JDBC_URL = JDBC_URL;
        this.JDBC_USER = JDBC_USER;
        this.JDBC_PASSWORD = JDBC_PASSWORD;

    }

//    public T newT() {
//        T newT;
//        try{
//            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
//            Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[0];
//            newT = clazz.newInstance();
//        }catch(Exception e){
//            throw  new RuntimeException(e);
//        }
//        return newT;
//    }

    public abstract T queryTrans(ResultSet rs) throws SQLException;

    public List<T> query(String sql) throws SQLException {
        List list = new ArrayList();
        try(Connection conn = DriverManager.getConnection(this.JDBC_URL, this.JDBC_USER, this.JDBC_PASSWORD)){
            try(Statement stmt = conn.createStatement()){
                try(ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        list.add(queryTrans(rs));
                    }
                }
            }
        }
        return list;
    }

    public abstract void insertTrans(T t, PreparedStatement ps) throws SQLException;

    public int insert(String sql, T t) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    sql)) {
                insertTrans(t, ps);
                int n = ps.executeUpdate(); // 1
            }
        }
        return 0;
    }

    public abstract void updateTrans(T t, PreparedStatement ps, int num) throws SQLException;

    public int updateById(String sql, T t, int num) throws SQLException{
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    sql)) {
                updateTrans(t, ps, num);
                int n = ps.executeUpdate(); // 1
            }
        }
        return 0;
    }

    public abstract void deleteTrans(T t, PreparedStatement ps) throws SQLException;
    public int deleteById(String sql, T t) throws SQLException{
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    sql)) {
                deleteTrans(t, ps);
                int n = ps.executeUpdate(); // 1
            }
        }
        return 0;
    }


}
