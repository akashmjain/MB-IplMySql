package com.akashmjain.ipl.utilities;

import javax.management.StandardEmitterMBean;
import java.sql.*;

public class PrepareData {
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL      = "jdbc:mysql://localhost/ipl_db";

    private static boolean createMatchesTable()  {
        final String query = "CREATE TABLE matches (\n" +
            "    id INT NOT NULL AUTO_INCREMENT,\n" +
            "    match_id varchar(256),\n" +
            "    season varchar(256),\n" +
            "    city varchar(256),\n" +
            "    match_date date,\n" +
            "    team1 varchar(256),\n" +
            "    team2 varchar(256),\n" +
            "    toss_winner varchar(256),\n" +
            "    toss_decision varchar(256),\n" +
            "    result varchar(256),\n" +
            "    dl_applied varchar(256),\n" +
            "    winner varchar(256),\n" +
            "    win_by_runs varchar(256),\n" +
            "    win_by_wickets varchar(256),\n" +
            "    player_of_match varchar(256),\n" +
            "    venue varchar(256),\n" +
            "    umpire1 varchar(256),\n" +
            "    umpire2 varchar(256),\n" +
            "    umpire3 varchar(256),\n" +
            "    PRIMARY KEY(id)\n" +
            ");";
        boolean tableCreationSuccess = false;

        try {
            Connection connection = establishConnection();
            int result = executeUpdateQuery(query, connection);
            tableCreationSuccess = true;
        }
        catch (SQLSyntaxErrorException e) {
            boolean tableAlreadyExist = e.getErrorCode() == 1050;
            if(tableAlreadyExist) {
                tableCreationSuccess = true;
            } else {
                e.printStackTrace();
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return tableCreationSuccess;
    }

    private static void populateMatchesTable() {

    }

    private static int executeUpdateQuery(String query, Connection connection) throws SQLException, SQLSyntaxErrorException{
        Statement sqlState = connection.createStatement();
        int rows = sqlState.executeUpdate(query);
        return rows;
    }


    private static void printData(ResultSet rows) throws SQLException{
        while(rows.next()) {
            System.out.println(rows.getString(1));
        }

    }
    private static ResultSet executeQuery(String sql, Connection connection) throws SQLException{
        Statement sqlState = connection.createStatement();
        ResultSet rows = sqlState.executeQuery(sql);
        return rows;
    }
    private static Connection establishConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, "ipl", "password");
        return conn;
    }

    public static void main(String[] args) {
        
    }
}
