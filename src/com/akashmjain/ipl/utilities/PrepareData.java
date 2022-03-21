package com.akashmjain.ipl.utilities;

import javax.management.StandardEmitterMBean;
import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class PrepareData {

    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL      = "jdbc:mysql://localhost/ipl_db";

    public static final Connection connection = establishConnection();

    private static Connection establishConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, "ipl", "password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    private int executeUpdateQuery(String query) throws SQLSyntaxErrorException, SQLException {
        Statement sqlState = connection.createStatement();
        int rows = sqlState.executeUpdate(query);
        return rows;
    }
    public boolean createTable(String query)  {
        boolean tableCreationSuccess = false;
        try {
            int result = executeUpdateQuery(query);
            tableCreationSuccess = true;
        }
        catch (SQLSyntaxErrorException e) {
            boolean tableAlreadyExist = e.getErrorCode() == 1050;// 1050 is error code for table already exist.
            if(tableAlreadyExist) {
                tableCreationSuccess = true;
            } else {
                System.out.println(query);
                System.out.println(e.getErrorCode());
                e.printStackTrace();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return tableCreationSuccess;
    }


//    public static void main(String[] args) {
//        try {
//            System.out.println("GENERATING DELIVERIES DATA IN MYSQL");
//            PrepareDeliveryData.init();
//            System.out.println("DONE GENERATING DELIVERIES DATA IN MYSQL");
//            System.out.println("=======================================================================");
//            System.out.println("GENERATING MATCHES DATA IN MYSQL");
//            PrepareMatchData.init();
//            System.out.println("DONE GENERATING MATCHES DATA IN MYSQL");
//        } catch(Exception e ) {
//            e.printStackTrace();
//        }
//
//    }
}
