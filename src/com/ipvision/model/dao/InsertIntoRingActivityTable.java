/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Shahadat Hossain
 */
public class InsertIntoRingActivityTable extends Thread {

    private List<ActivityDTO> activityDTOs;

    public InsertIntoRingActivityTable(List<ActivityDTO> activityDTOs) {
        this.activityDTOs = activityDTOs;
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

            for (ActivityDTO entity : activityDTOs) {
                String activityBy = (entity.getActivityBy() != null ? entity.getActivityBy().trim().replace("'", "''") : "");
                String friendIdentity = (entity.getFriendIdentity() != null ? entity.getFriendIdentity().trim().replace("'", "''") : "");
                String prevGroupName = (entity.getPrevGroupName() != null ? entity.getPrevGroupName().trim().replace("'", "''") : "");
                String members = entity.getMembers() != null && entity.getMembers().size() > 0 ? HelperMethods.makeActivityGroupMembersMap(entity.getMembers()) : "";
                entity.setUpdateTime(System.currentTimeMillis());

                try {
                    String sql = "INSERT INTO " + DBConstants.TABLE_RING_ACTIVITY
                            + "	( "
                            + DBConstants.LOGIN_USER_ID + ","
                            + DBConstants.FRIEND_IDENTITY + ","
                            + DBConstants.GROUP_ID + ","
                            + DBConstants.ACTIVITY_TYPE + ","
                            + DBConstants.MESSAGE_TYPE + ","
                            + DBConstants.ACTIVITY_BY + ","
                            + DBConstants.MEMBER_LIST + ","
                            + DBConstants.FROM_CONTACT_TYPE + ","
                            + DBConstants.TO_CONTACT_TYPE + ","
                            + DBConstants.PREVIOUS_GROUP_NAME + ","
                            + DBConstants.UPDATE_TIME + ","
                            + DBConstants.PACKET_ID + ","
                            + DBConstants.IS_PROCESSED
                            + " ) "
                            + " VALUES "
                            + "	( "
                            + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                            + "'" + friendIdentity + "',"
                            + "" + entity.getGroupId() + ","
                            + " " + entity.getActivityType() + ", "
                            + " " + entity.getMessageType() + ", "
                            + "'" + activityBy + "',"
                            + "'" + members + "',"
                            + " " + entity.getFromContactType() + ", "
                            + " " + entity.getToContactType() + ", "
                            + "'" + prevGroupName + "',"
                            + " " + entity.getUpdateTime() + ", "
                            + "'" + entity.getPacketID() + "',"
                            + " " + entity.getIsProcessed()
                            + " )";
                    stmt.execute(sql);
                } catch (SQLException sqlExcept) {
                    if (sqlExcept.getSQLState().equalsIgnoreCase("23505")) {//duplicate
                        String sql = "UPDATE " + DBConstants.TABLE_RING_ACTIVITY
                                + " SET "
                                + DBConstants.IS_PROCESSED + " =" + entity.getIsProcessed()
                                + " WHERE "
                                + DBConstants.PACKET_ID + " = '" + entity.getPacketID() + "' AND "
                                + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                        stmt.executeUpdate(sql);
                    } else {
                        sqlExcept.printStackTrace();
                        throw new Exception();
                    }
                }
            }

            conn.commit();
        } catch (Exception e) {
            try {
                if(conn!=null){
                conn.rollback();}
            } catch (Exception ex) {
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

        for (ActivityDTO entity : activityDTOs) {
            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(entity.getUpdateTime());
            recentDTO.setActivityDTO(entity);

            MyFriendChatCallPanel friendChatCallPanel = null;
            GroupPanel groupPanel = null;

            if (entity.getGroupId() != null && entity.getGroupId() > 0) {
                recentDTO.setContactId(entity.getGroupId() + "");
                recentDTO.setContactType(RecentContactDAO.TYPE_GROUP);
                recentDTO.setType(RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY);
                groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(entity.getGroupId());
            } else {
                recentDTO.setContactId(entity.getFriendIdentity());
                recentDTO.setContactType(RecentContactDAO.TYPE_FRIEND);
                recentDTO.setType(RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY);
                friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(entity.getFriendIdentity());
            }

            if (entity.getGroupId() != null && groupPanel != null && groupPanel.isDisplayable()) {
                groupPanel.addSinglePanelInChatContainerPanel(entity);
            } else if (entity.getFriendIdentity() != null && friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible()) {
                friendChatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(entity);
            }
        }

    }
}
