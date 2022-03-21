package com.akashmjain.ipl;

import java.sql.*;

public class IPLPostgreSQLTestDrive {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ipl_db";
    private static final String DB_USER_NAME = "postgres";
    private static final String DB_USER_PASSWORD = "password";
    private static final Connection connection = establishConnection();
    private static Statement statement = null;

    public static void main(String[] args) throws SQLException {
        statement = connection.createStatement();
        findNumberOfMatchesWonPerTeamsOverAllYears();
        findNumberOfMatchesPlayedPerYearForAllYears();
        findYearWiseExtraRunConcededPerTeam("2016");
        findYearWiseTopEconomicalBowlers("2015", 2);
        findTopMostCatchesInHistoryPlayers(2);
    }

    private static Connection establishConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_USER_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return conn;
    }

    private static void findNumberOfMatchesWonPerTeamsOverAllYears() {
        String query = "SELECT winner, COUNT(id) FROM matches GROUP BY winner;";
        try {
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

    private static void findNumberOfMatchesPlayedPerYearForAllYears() {
        String query = "SELECT COUNT(id),season FROM matches GROUP BY season;";
        try {
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
        String query = "select batting_team,sum(extra_runs) from deliveries where match_id in (select id from matches where season=2016) group by batting_team;";
        try {
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
        String query = "SELECT cnt_table.bowler, runs_table.total_runs::decimal/(cnt_table.ball_count::decimal/6) as eco_rate from " +
                "(select bowler, count(bowler) as ball_count from (select d.* from deliveries d, matches m where d.match_id=m.id and m.season="+year+") as del group by bowler) as cnt_table," +
                "(select bowler, sum(total_runs) as total_runs from (select d.* from deliveries d, matches m where d.match_id=m.id and m.season="+year+") as del group by bowler) as runs_table " +
                "where cnt_table.bowler = runs_table.bowler order by eco_rate limit "+top+";";
        try {
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

    private static void findTopMostCatchesInHistoryPlayers(int top) {
        String query = "SELECT fielder, COUNT(dismissal_kind) AS catch_count FROM deliveries WHERE dismissal_kind='caught' GROUP BY fielder ORDER BY catch_count DESC LIMIT "+top+";";
        try {
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
