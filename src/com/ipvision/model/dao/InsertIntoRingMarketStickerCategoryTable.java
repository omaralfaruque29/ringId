/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.StickerCategoryDTO;

/**
 *
 * @author Shahadat Hossain
 */
public class InsertIntoRingMarketStickerCategoryTable extends Thread {

    private List<StickerCategoryDTO> stickerCategoryDTOs;

    public InsertIntoRingMarketStickerCategoryTable(List<StickerCategoryDTO> stickerCategoryDTOs) {
        this.stickerCategoryDTOs = stickerCategoryDTOs;
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

            for (StickerCategoryDTO entity : stickerCategoryDTOs) {
                String categroyName = entity.getStickerCategoryName() != null ? entity.getStickerCategoryName().replaceAll("'", "''") : "";
                String description = entity.getDescription() != null ? entity.getDescription().replaceAll("'", "''") : "";

                try {
                    String sql = "INSERT INTO " + DBConstants.TABLE_RING_MARKET_STICKER_CATEGORY
                            + " ( "
                            + DBConstants.STK_CATEGORY_ID + ", "
                            + DBConstants.LOGIN_USER_ID + ","
                            + DBConstants.STK_CATEGORY_NAME + ","
                            + DBConstants.STK_COLLECTION_ID + ","
                            + DBConstants.STK_CATEGORY_BANNER_IMAGE + ","
                            + DBConstants.STK_CATEGORY_RANK + ","
                            + DBConstants.STK_CATEGORY_DETAIL_IMAGE + ","
                            + DBConstants.STK_CATEGORY_IS_NEW + ","
                            + DBConstants.STK_CATEGORY_ICON + ","
                            + DBConstants.STK_CATEGORY_PRIZE + ","
                            + DBConstants.STK_CATEGORY_IS_FREE + ","
                            + DBConstants.STK_CATEGORY_DESCRIPTION + ","
                            + DBConstants.STK_CATEGORY_IS_DOWNLOADED
                            + " ) "
                            + " VALUES "
                            + " ( "
                            + entity.getStickerCategoryId() + ","
                            + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                            + "'" + categroyName + "',"
                            + entity.getStickerCollectionId() + ","
                            + (entity.getCategoryBannerImage() != null ? ("'" + entity.getCategoryBannerImage() + "',") : "'',")
                            + entity.getRank() + ","
                            + (entity.getDetailImage() != null ? ("'" + entity.getDetailImage() + "',") : "'',")
                            + entity.getCategoryNew() + ","
                            + (entity.getIcon() != null ? ("'" + entity.getIcon() + "',") : "'',")
                            + entity.getPrize() + ","
                            + entity.getFree() + ","
                            + "'" + description + "',"
                            + (entity.getDownloaded() == null ? false : entity.getDownloaded())
                            + " )";
                    stmt.execute(sql);
                } catch (SQLException sqlExcept) {
                    if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                        String sql = "UPDATE " + DBConstants.TABLE_RING_MARKET_STICKER_CATEGORY
                                + " SET "
                                + DBConstants.STK_CATEGORY_NAME + " = " + "'" + categroyName + "',"
                                + DBConstants.STK_COLLECTION_ID + " = " + entity.getStickerCollectionId() + ","
                                + DBConstants.STK_CATEGORY_BANNER_IMAGE + " = " + "'" + (entity.getCategoryBannerImage() != null ? entity.getCategoryBannerImage() : "") + "',"
                                + DBConstants.STK_CATEGORY_RANK + " = " + entity.getRank() + ","
                                + DBConstants.STK_CATEGORY_DETAIL_IMAGE + " = " + "'" + (entity.getDetailImage() != null ? entity.getDetailImage() : "") + "',"
                                + DBConstants.STK_CATEGORY_IS_NEW + " = " + entity.getCategoryNew() + ","
                                + DBConstants.STK_CATEGORY_ICON + " = " + "'" + (entity.getIcon() != null ? entity.getIcon() : "") + "',"
                                + DBConstants.STK_CATEGORY_PRIZE + " = " + entity.getPrize() + ","
                                + DBConstants.STK_CATEGORY_IS_FREE + " = " + entity.getFree() + ","
                                + DBConstants.STK_CATEGORY_DESCRIPTION + " = " + "'" + description + "',"
                                + DBConstants.STK_CATEGORY_IS_DOWNLOADED + " = " + (entity.getDownloaded() == null ? false : entity.getDownloaded())
                                + " WHERE "
                                + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + DBConstants.STK_CATEGORY_ID + " = " + entity.getStickerCategoryId();
                        stmt.execute(sql);
                    } else {
                        sqlExcept.printStackTrace();
                        throw new Exception();
                    }
                }

            }

            conn.commit();
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {

            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }
    }
}
