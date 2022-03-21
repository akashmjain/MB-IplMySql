package com.akashmjain.ipl.utilities;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PrepareDeliveryData extends PrepareData {
    private final static String deliveryFile = "./data/deliveries.csv";
    private final static ArrayList<Delivery> deliveries = csvDeliveryTest();

    String query = "INSERT INTO `deliveries`" +
            "(" +
            "`match_id`, " +
            "`inning`, " +
            "`batting_team`, " +
            "`bowling_team`, " +
            "`over`, " +
            "`ball`, " +
            "`batsman`, " +
            "`non_striker`, " +
            "`bowler`, " +
            "`is_super_over`, " +
            "`wide_runs`, " +
            "`bye_runs`, " +
            "`legbye_runs`, " +
            "`noball_runs`, " +
            "`penalty_runs`, " +
            "`batsman_runs`, " +
            "`extra_runs`, " +
            "`total_runs`, " +
            "`player_dismissed`, " +
            "`dismissal_kind`, " +
            "`fielder`)" +
            "VALUE " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    PreparedStatement pstate = null;

    public boolean createDeliveryTable() {
        final String query = "CREATE TABLE deliveries (\n" +
                "    `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `match_id` varchar(256),\n" +
                "    `inning` varchar(256),\n" +
                "    `batting_team` varchar(256),\n" +
                "    `bowling_team` varchar(256),\n" +
                "    `over` varchar(256),\n" +
                "    `ball` varchar(256),\n" +
                "    `batsman` varchar(256),\n" +
                "    `non_striker` varchar(256),\n" +
                "    `bowler` varchar(256),\n" +
                "    `is_super_over` varchar(256),\n" +
                "    `wide_runs` varchar(256),\n" +
                "    `bye_runs` varchar(256),\n" +
                "    `legbye_runs` varchar(256),\n" +
                "    `noball_runs` varchar(256),\n" +
                "    `penalty_runs` varchar(256),\n" +
                "    `batsman_runs` varchar(256),\n" +
                "    `extra_runs` varchar(256),\n" +
                "    `total_runs` varchar(256),\n" +
                "    `player_dismissed` varchar(256),\n" +
                "    `dismissal_kind` varchar(256),\n" +
                "    `fielder` varchar(256),\n" +
                "    PRIMARY KEY(id)\n" +
                ");";
        return createTable(query);
    }


    private void executeInsertionForMatch(Delivery delivery) throws SQLException {

        pstate.setString(1,  delivery.getMatch_id());
        pstate.setString(2,  delivery.getInning());
        pstate.setString(3,  delivery.getBatting_team());
        pstate.setString(4,  delivery.getBowling_team());
        pstate.setString(5,  delivery.getOver());
        pstate.setString(6,  delivery.getBall());
        pstate.setString(7,  delivery.getBatsman());
        pstate.setString(8,  delivery.getNon_striker());
        pstate.setString(9,  delivery.getBowler());
        pstate.setString(10, delivery.getIs_super_over());
        pstate.setString(11, delivery.getWide_runs());
        pstate.setString(12, delivery.getBye_runs());
        pstate.setString(13, delivery.getLegbye_runs());
        pstate.setString(14, delivery.getNoball_runs());
        pstate.setString(15, delivery.getPenalty_runs());
        pstate.setString(16, delivery.getBatsman_runs());
        pstate.setString(17, delivery.getExtra_runs());
        pstate.setString(18, delivery.getTotal_runs());
        pstate.setString(19, delivery.getPlayer_dismissed());
        pstate.setString(20, delivery.getDismissal_kind());
        pstate.setString(21, delivery.getFielder());

        pstate.executeUpdate();
    }
    private void populateDeliveryTable() {
        try {
            for(int i = 0; i < deliveries.size(); i++) {
                executeInsertionForMatch(deliveries.get(i));
                float per = (((float)i) / deliveries.size()) * 100;
                System.out.print("       \r"+String.format("%.02f", per)) ;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Finished loading data");
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


    public static void init() throws SQLException {
        PrepareDeliveryData prepareDeliveryData = new PrepareDeliveryData();
        prepareDeliveryData.pstate = connection.prepareStatement(prepareDeliveryData.query, Statement.RETURN_GENERATED_KEYS);
        if(prepareDeliveryData.createDeliveryTable()) prepareDeliveryData.populateDeliveryTable();
    }
}
