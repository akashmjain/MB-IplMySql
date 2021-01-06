package com.akashmjain.ipl.utilities;

import javax.management.StandardEmitterMBean;
import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class PrepareData {

    private final static String matchFile = "./data/matches.csv";
    private final static String deliveryFile = "./data/deliveries.csv";
    private final static ArrayList<Match> matches = csvMatchTest();
    private final static ArrayList<Delivery> deliveries = csvDeliveryTest();

    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL      = "jdbc:mysql://localhost/ipl_db";
    public static final Connection connection = establishConnection();

    // private static boolean createDeliveryTable() {

    // }
    
    public static void main(String[] args) {
        if(createMatchesTable()) {
            populateMatchesTable();
        }
    }

    private static boolean createMatchesTable()  {
        final String query = "CREATE TABLE matches (\n" +
            "    id INT NOT NULL AUTO_INCREMENT,\n" +
            "    match_id varchar(256),\n" +
            "    season varchar(256),\n" +
            "    city varchar(256),\n" +
            "    match_date varchar(256),\n" +
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
            int result = executeUpdateQuery(query);
            tableCreationSuccess = true;
        }
        catch (SQLSyntaxErrorException e) {
            boolean tableAlreadyExist = e.getErrorCode() == 1050;// 1050 is error code for table already exist.
            if(tableAlreadyExist) {
                tableCreationSuccess = true;
            } else {
                e.printStackTrace();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return tableCreationSuccess;
    }
    private static String generateInsertQuery(Match match) {
        String query = "INSERT INTO `matches`" +
                "(" +
                "match_id, " +
                "season, " +
                "city, " +
                "match_date, " +
                "team1, " +
                "team2, " +
                "toss_winner, " +
                "toss_decision, " +
                "result, " +
                "dl_applied, " +
                "winner, " +
                "win_by_runs, " +
                "win_by_wickets, " +
                "player_of_match, " +
                "venue, " +
                "umpire1, " +
                "umpire2, " +
                "umpire3)" +
                "VALUE " +
                "(" +
                "'"+match.getId()+"'," +
                "'"+match.getSeason()+"'," +
                "'"+match.getCity()+"'," +
                "'"+match.getDate()+"'," +
                "'"+match.getTeam1()+"'," +
                "'"+match.getTeam2()+"'," +
                "'"+match.getToss_winner()+"'," +
                "'"+match.getToss_decision()+"'," +
                "'"+match.getResult()+"'," +
                "'"+match.getDl_applied()+"'," +
                "'"+match.getWinner()+"'," +
                "'"+match.getWin_by_runs()+"'," +
                "'"+match.getWin_by_wickets()+"'," +
                "'"+match.getPlayer_of_match()+"'," +
                "'"+match.getVenue()+"'," +
                "'"+match.getUmpire1()+"'," +
                "'"+match.getUmpire2()+"'," +
                "'"+match.getUmpire3()+"'" +
                ")";
        return query;
    }
    private static void executeInsertionForMatch(Match match) throws SQLException{
        String query = "INSERT INTO `matches`" +
                "(" +
                "match_id, " +
                "season, " +
                "city, " +
                "match_date, " +
                "team1, " +
                "team2, " +
                "toss_winner, " +
                "toss_decision, " +
                "result, " +
                "dl_applied, " +
                "winner, " +
                "win_by_runs, " +
                "win_by_wickets, " +
                "player_of_match, " +
                "venue, " +
                "umpire1, " +
                "umpire2, " +
                "umpire3)" +
                "VALUE " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstate = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstate.setString(1,  match.getId());
        pstate.setString(2,  match.getSeason());
        pstate.setString(3,  match.getCity());
        pstate.setString(4,  match.getDate());
        pstate.setString(5,  match.getTeam1());
        pstate.setString(6,  match.getTeam2());
        pstate.setString(7,  match.getToss_winner());
        pstate.setString(8,  match.getToss_decision());
        pstate.setString(9,  match.getResult());
        pstate.setString(10, match.getDl_applied());
        pstate.setString(11, match.getWinner());
        pstate.setString(12, match.getWin_by_runs());
        pstate.setString(13, match.getWin_by_wickets());
        pstate.setString(14, match.getPlayer_of_match());
        pstate.setString(15, match.getVenue());
        pstate.setString(16, match.getUmpire1());
        pstate.setString(17, match.getUmpire2());
        pstate.setString(18, match.getUmpire3());

        pstate.executeUpdate();


    }
    private static void populateMatchesTable() {
        int count = 0;
        try {
            for (Match match : matches) {
                executeInsertionForMatch(match);
                count++;
                float per = (((float)count) / matches.size()) * 100;
                System.out.format("%.2f \n", per) ;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Finished loading data");
    }


    private static void printData(ResultSet rows) throws SQLException{
        while(rows.next()) {
            System.out.println(rows.getString(1));
        }

    }

    /***
     * Execute Query Methods
     ***/
    private static int executeUpdateQuery(String query) throws SQLSyntaxErrorException, SQLException {
        Statement sqlState = connection.createStatement();
        int rows = sqlState.executeUpdate(query);
        return rows;
    }


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

    private static ArrayList<Match> csvMatchTest() {
        ArrayList<Match> matches = null;
        try {
            File matchData = new File(matchFile);
            CSVHelper csvHelper = new CSVHelper(matchData);
            matches = csvHelper.formatDataForMatches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matches;
    }

    private static ArrayList<Delivery> csvDeliveryTest() {
        ArrayList<Delivery> deliveries = null;
        try {
            File deliveryData = new File(deliveryFile);
            CSVHelper csvHelper = new CSVHelper(deliveryData);
            deliveries = csvHelper.formatDataForDelivery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries;
    }

}
