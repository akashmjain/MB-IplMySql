package com.akashmjain.ipl.utilities;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;

public class PrepareMatchData extends PrepareData {
    private final static String matchFile = "./data/matches.csv";
    private ArrayList<Match> matches = csvMatchTest();

    public PrepareMatchData(){}

    private boolean CreateMatchTable() {
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
        return createTable(query);
    }


    private void executeInsertionForMatch(Match match) throws SQLException{
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
    private void populateMatchesTable() {
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

    private ArrayList<Match> csvMatchTest() {
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


    public static void init() {
        PrepareMatchData prepareMatchData = new PrepareMatchData();
        if (prepareMatchData.CreateMatchTable()) prepareMatchData.populateMatchesTable();
    }
}
