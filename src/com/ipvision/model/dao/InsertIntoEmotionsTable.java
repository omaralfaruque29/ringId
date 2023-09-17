/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.EmotionDtos;

/**
 *
 * @author Shahadat Hossain
 */
public class InsertIntoEmotionsTable extends Thread {

    private List<EmotionDtos> emotionDtosList;

    public InsertIntoEmotionsTable(List<EmotionDtos> emotionDtosList) {
        this.emotionDtosList = emotionDtosList;
    }

    @Override
    public void run() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:" + DBConstants.DB_NAME + ";create=true";

        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            conn.setAutoCommit(false);

            ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_EMOTICONS).toUpperCase(), null);
            if (!tablesw.isClosed() && tablesw.next()) {
                for (EmotionDtos emotionDtos : emotionDtosList) {
                    try {
                        stmt.execute("INSERT INTO " + DBConstants.TABLE_RING_EMOTICONS
                                + " (" + DBConstants.ID + "," + DBConstants.EMO_NAME + "," + DBConstants.EMO_SYMBOL + "," + DBConstants.EMO_URL + "," + DBConstants.EMO_TYPE + ")"
                                + " VALUES "
                                + " ("
                                + "" + emotionDtos.getId() + ","
                                + "'" + emotionDtos.getName() + "',"
                                + "'" + emotionDtos.getSymbol() + "',"
                                + "'" + emotionDtos.getUrl() + "',"
                                + "" + emotionDtos.getType() + ""
                                + ")");
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                            String sql = "UPDATE " + DBConstants.TABLE_RING_EMOTICONS
                                    + " SET "
                                    + DBConstants.ID + " =" + emotionDtos.getId() + ","
                                    + DBConstants.EMO_SYMBOL + " ='" + emotionDtos.getSymbol() + "',"
                                    + DBConstants.EMO_URL + " ='" + emotionDtos.getUrl() + "',"
                                    + DBConstants.EMO_TYPE + " =" + emotionDtos.getType() + ""
                                    + " WHERE "
                                    + DBConstants.EMO_NAME + " ='" + emotionDtos.getName() + "'";
                            stmt.executeUpdate(sql);
                        } else {
                            throw new Exception();
                        }
                    }
                }
                conn.commit();
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {

            }
        } finally {
            try {
                if (stmt != null && conn != null) {
                    stmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }

    }
}
