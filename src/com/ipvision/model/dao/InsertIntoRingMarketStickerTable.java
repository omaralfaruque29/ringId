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
import com.ipvision.model.StickerImagesDTO;

/**
 *
 * @author Shahadat Hossain
 */
public class InsertIntoRingMarketStickerTable extends Thread {
    
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertIntoRingMarketStickerTable.class);
    private List<StickerImagesDTO> stickerImagesDTOs;

    public InsertIntoRingMarketStickerTable(List<StickerImagesDTO> stickerImagesDTOs) {
        this.stickerImagesDTOs = stickerImagesDTOs;
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
            
            log.warn("Stickers Total : " + stickerImagesDTOs.size());
            for (StickerImagesDTO entity : stickerImagesDTOs) {
                
                try {                       
                    String sql = "INSERT INTO " + DBConstants.TABLE_RING_MARKET_STICKER
                            + " ( " 
                            + DBConstants.STK_IMAGE_ID  + ", "
                            + DBConstants.LOGIN_USER_ID + ","
                            + DBConstants.STK_IMAGE_URL + ","
                            + DBConstants.STK_CATEGORY_ID
                            + " ) "
                            + " VALUES "
                            + " ( "
                            + entity.getImageId() + ","
                            + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                            + (entity.getImageUrl() != null ?  ("'" + entity.getImageUrl() + "',") : "'',") 
                            + entity.getStickerCategoryId()
                            + " )";
                    stmt.execute(sql);
                  //  log.warn("Saving : " + "CategoryId = " + entity.getStickerCategoryId() + ", ImageId = " + entity.getImageId());
                } catch (SQLException sqlExcept) {
                    if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                        String sql = "UPDATE " + DBConstants.TABLE_RING_MARKET_STICKER
                                + " SET "
                                + DBConstants.STK_IMAGE_URL + " = " + "'" + (entity.getImageUrl() != null ? entity.getImageUrl() : "") + "'"
                                + " WHERE " 
                                + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + DBConstants.STK_IMAGE_ID + " = " + entity.getImageId() + " AND "
                                + DBConstants.STK_CATEGORY_ID + " = " + entity.getStickerCategoryId();
                        stmt.execute(sql);
                      //  log.warn("Saving : " + "CategoryId = " + entity.getStickerCategoryId() + ", ImageId = " + entity.getImageId());
                    } else {
                        log.warn("Failed : " + "CategoryId = " + entity.getStickerCategoryId() + ", ImageId = " + entity.getImageId());
                        throw new Exception();
                    }
                }

            }
            
            conn.commit();
        } catch (Exception e) {
           try{
               if(conn!=null){
               conn.rollback();}
           }catch(Exception ex){
               
           }
        } finally {
            try {
                if(stmt!=null){
                stmt.close();}
                if(conn!=null){
                conn.close();}
            } catch (SQLException ex) {
            }
        }
    }
}
