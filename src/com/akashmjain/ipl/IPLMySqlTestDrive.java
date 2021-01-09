package com.akashmjain.ipl;

import java.sql.*;

public class IPLMySqlTestDrive {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/ipl_db";
    private static final Connection connection = establishConnection();
    private static final String MATCHES_TABLE = "matches";
    private static final String DELIVERY_TABLE = "deliveries";

    public static void main(String[] args) throws SQLException {
        findNumberOfMatchesWonPerTeamsOverAllYears();
        findNumberOfMatchesPlayedPerYearForAllYears();
        findYearWiseExtraRunConcededPerTeam("2016");
        findYearWiseTopEconomicalBowlers("2015", 5);
        findTopMostCatchesInHistoryPlayers(5);
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

    private static void findNumberOfMatchesWonPerTeamsOverAllYears() {
        String query = "SELECT COUNT(id),winner FROM "+MATCHES_TABLE+" GROUP BY winner;";
        try {
            Statement statement = connection.createStatement();
            ResultSet rows = statement.executeQuery(query);
            while(rows.next()) {
                String noOfMatches = rows.getString(1);
                String team   = rows.getString(2).equals("") ? "NO_WINNER" : rows.getString(2);

                System.out.println(team+ " : "+noOfMatches);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // second problem
    private static void findNumberOfMatchesPlayedPerYearForAllYears() {
        String query = "SELECT COUNT(id),season FROM "+MATCHES_TABLE+" GROUP BY season;\n";
        try {
            Statement statement = connection.createStatement();
            ResultSet rows = statement.executeQuery(query);
            while(rows.next()) {
                String noOfMatches = rows.getString(1);
                String year = rows.getString(2);
                System.out.println(year+ " : "+noOfMatches);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void findYearWiseExtraRunConcededPerTeam(String year) {
        String query = "select batting_team,sum(extra_runs) " +
                "from deliveries where match_id in (select id from matches where season=2016) " +
                "group by batting_team;";
        try {
            Statement statement = connection.createStatement();
            ResultSet rows = statement.executeQuery(query);
            while(rows.next()) {
                String battingTeam = rows.getString(1);
                String runConceded = rows.getString(2);
                System.out.println(battingTeam + " : " + runConceded);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void findYearWiseTopEconomicalBowlers(String year, int top) {
        String query = "SELECT cnt_table.bowler, runs_table.total_runs/(cnt_table.ball_count DIV 6) as eco_rate from\n" +
                "\t(select bowler, count(bowler) as ball_count from \n" +
                "\t\t(select d.* from deliveries d, matches m where d.match_id=m.id and m.season=2015) as del group by bowler) as cnt_table,\n" +
                "\t(select bowler, sum(total_runs) as total_runs from \n" +
                "\t\t(select d.* from deliveries d, matches m where d.match_id=m.id and m.season=2015) as del group by bowler) as runs_table \n" +
                "where cnt_table.bowler = runs_table.bowler order by eco_rate;\n";
        try {
            Statement statement = connection.createStatement();
            ResultSet rows = statement.executeQuery(query);
            while(rows.next() && top > 0) {
                String bowler = rows.getString(1);
                String economyRate = rows.getString(2);
                System.out.println(bowler + " : " + economyRate);
                top--;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void findTopMostCatchesInHistoryPlayers(int top) {
        String query = "SELECT fielder, COUNT(dismissal_kind) AS catch_count FROM deliveries WHERE dismissal_kind='caught' GROUP BY fielder ORDER BY catch_count DESC LIMIT "+top+";";
        try {
            Statement statement = connection.createStatement();
            ResultSet rows = statement.executeQuery(query);
            while(rows.next()) {
                String bowler = rows.getString(1);
                String economyRate = rows.getString(2);
                System.out.println(bowler + " : " + economyRate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
