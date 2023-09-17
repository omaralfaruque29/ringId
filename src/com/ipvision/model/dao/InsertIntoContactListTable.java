/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.constants.StatusConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import com.ipvision.model.constants.DBConstants;

/**
 *
 * @author Faiz Ahmed
 */
public class InsertIntoContactListTable extends Thread {

    private List<UserBasicInfo> userBasicInfoList;

    public InsertIntoContactListTable(List<UserBasicInfo> userBasicInfoList) {
        this.userBasicInfoList = userBasicInfoList;
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
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            for (UserBasicInfo userBasicInfo : userBasicInfoList) {
                if (userBasicInfo.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_CONTACT_LIST
                            + " WHERE "
                            + DBConstants.USER_IDENTITY + " = '" + userBasicInfo.getUserIdentity() + "' AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    stmt.executeUpdate(query);
                } else {
                    String whatsInmind = userBasicInfo.getWhatisInYourMind() != null ? userBasicInfo.getWhatisInYourMind().replaceAll("'", "''") : "";
                    if (userBasicInfo.getPrivacy() != null && userBasicInfo.getPrivacy().length > 0) {
                        userBasicInfo.setEmailPrivacy(userBasicInfo.getPrivacy()[0]);
                        userBasicInfo.setMobilePrivacy(userBasicInfo.getPrivacy()[1]);
                        userBasicInfo.setProfileImagePrivacy(userBasicInfo.getPrivacy()[2]);
                        userBasicInfo.setBirthdayPrivacy(userBasicInfo.getPrivacy()[3]);
                        userBasicInfo.setCoverImagePrivacy(userBasicInfo.getPrivacy()[4]);
                    }

                    String fullName = (userBasicInfo.getFullName() != null ? userBasicInfo.getFullName().trim().replace("'", "''") : "");
                    //String lastName = (userBasicInfo.getLastName() != null ? userBasicInfo.getLastName().trim().replace("'", "''") : "");
                    String email = (userBasicInfo.getEmail() != null ? userBasicInfo.getEmail().trim() : "");
                    String gender = (userBasicInfo.getGender() != null ? userBasicInfo.getGender().trim() : "");
                    String birthDay = (userBasicInfo.getBirthday() != null ? userBasicInfo.getBirthday().trim() : "");
                    String profileImage = (userBasicInfo.getProfileImage() != null ? userBasicInfo.getProfileImage().trim() : "");
                    String coverImage = (userBasicInfo.getCoverImage() != null ? userBasicInfo.getCoverImage().trim() : "");
                    String deviceToken = (userBasicInfo.getDeviceToken() != null ? userBasicInfo.getDeviceToken().trim() : "");
                    long ut = userBasicInfo.getUt() == 0 ? userBasicInfo.getUt() : new Date().getTime();
                    long ct = userBasicInfo.getContactType() != null ? userBasicInfo.getContactType() : 0;
                    long nct = userBasicInfo.getNewContactType() != null ? userBasicInfo.getNewContactType() : 0;
                    boolean iscr = userBasicInfo.getIsChangeRequester() != null ? userBasicInfo.getIsChangeRequester() : false;
                    long bv = userBasicInfo.getBlocked() != null ? userBasicInfo.getBlocked() : 0;
//                    boolean isIncomingRequest = (userBasicInfo.getFriendShipStatus() != null && userBasicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING)
//                            || (userBasicInfo.getNewContactType() != null && userBasicInfo.getNewContactType() > 0 && !userBasicInfo.getIsChangeRequester());

                    try {
                        String sql = "INSERT INTO " + DBConstants.TABLE_RING_CONTACT_LIST
                                + "	("
                                + DBConstants.LOGIN_USER_ID + ","
                                + DBConstants.USER_IDENTITY + ","
                                + DBConstants.USER_TABLE_ID + ","
                                + DBConstants.FULL_NAME + ","
                                //+ DBConstants.LAST_NAME + ","
                                + DBConstants.EMAIL + ","
                                + DBConstants.GENDER + ","
                                + DBConstants.MOBILE_PHONE + ","
                                + DBConstants.COUNTRY + ","
                                + DBConstants.BIRTHDAY + ","
                                + DBConstants.WHAT_IS_IN_YOUR_MIND + ","
                                + DBConstants.FRIENDSHIP_STATUS + ","
                                + DBConstants.MOBILE_PHONE_DIALING_CODE + ","
                                + DBConstants.PROFILE_IMAGE + ","
                                + DBConstants.PROFILE_IMAGE_ID + ","
                                + DBConstants.COVER_IMAGE + ","
                                + DBConstants.COVER_IMAGE_ID + ","
                                + DBConstants.COVER_IMAGE_X + ","
                                + DBConstants.COVER_IMAGE_Y + ","
                                + DBConstants.RING_EMAIL + ","
                                + DBConstants.EMAIL_PRIVACY + ","
                                + DBConstants.MOBILE_PRIVACY + ","
                                + DBConstants.PROFILE_IMAGE_PRIVACY + ","
                                + DBConstants.BIRTHDAY_PRIVACY + ","
                                + DBConstants.COVER_IMAGE_PRIVACY + ","
                                + DBConstants.DEVICE_TOKEN + ","
                                + DBConstants.CONTACT_TYPE + ","
                                + DBConstants.NEW_CONTACT_TYPE + ","
                                + DBConstants.IS_CHANGE_REQUESTER + ","
                                + DBConstants.IS_BLOCKED + ","
                                + DBConstants.INCOMING_NOTIFICATION + ","
                                + DBConstants.UPDATE_TIME + ") "
                                + " VALUES "
                                + "	("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + "'" + userBasicInfo.getUserIdentity() + "',"
                                + "" + userBasicInfo.getUserTableId() + ","
                                + "'" + fullName + "',"
                                //+ "'" + lastName + "',"
                                + "'" + email + "',"
                                + "'" + gender + "',"
                                + " '" + userBasicInfo.getMobilePhone() + "', "
                                + " '" + userBasicInfo.getCountry() + "',"
                                + "'" + birthDay + "',"
                                + " '" + whatsInmind + "',"
                                + " " + userBasicInfo.getFriendShipStatus() + ", "
                                + " '" + userBasicInfo.getMobilePhoneDialingCode() + "', "
                                + "'" + profileImage + "',"
                                + " " + userBasicInfo.getProfileImageId() + ", "
                                + "'" + coverImage + "',"
                                + " " + userBasicInfo.getCoverImageId() + ", "
                                + " " + -userBasicInfo.getCoverImageX() + ", "
                                + " " + -userBasicInfo.getCoverImageY() + ", "
                                + " '" + userBasicInfo.getRingEmail() + "', "
                                + " " + userBasicInfo.getEmailPrivacy() + ", "
                                + " " + userBasicInfo.getMobilePrivacy() + ", "
                                + " " + userBasicInfo.getProfileImagePrivacy() + ", "
                                + " " + userBasicInfo.getBirthdayPrivacy() + ", "
                                + " " + userBasicInfo.getCoverImagePrivacy() + ", "
                                + "'" + deviceToken + "',"
                                + " " + ct + ","
                                + " " + nct + ","
                                + " " + iscr + ","
                                + " " + bv + ","
                                //+ " " + isIncomingRequest + ","
                                + " " + userBasicInfo.getIncomingNotification() + ","
                                + " " + ut + ""
                                + ")";
                        stmt.execute(sql);
                        //userBasicInfo.setIncomingNotification(isIncomingRequest);
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equalsIgnoreCase("23505")) {//duplicate
                            String sql = "UPDATE " + DBConstants.TABLE_RING_CONTACT_LIST
                                    + " SET "
                                    + DBConstants.USER_TABLE_ID + " =" + userBasicInfo.getUserTableId() + ","
                                    + DBConstants.FULL_NAME + " ='" + fullName + "',"
                                    //+ DBConstants.LAST_NAME + " ='" + lastName + "',"
                                    + DBConstants.EMAIL + " ='" + email + "',"
                                    + DBConstants.GENDER + " ='" + gender + "',"
                                    + DBConstants.MOBILE_PHONE + " = '" + userBasicInfo.getMobilePhone() + "',"
                                    + DBConstants.COUNTRY + " ='" + userBasicInfo.getCountry() + "',"
                                    + DBConstants.BIRTHDAY + " ='" + birthDay + "',"
                                    + DBConstants.WHAT_IS_IN_YOUR_MIND + " ='" + whatsInmind.trim() + "',"
                                    + DBConstants.FRIENDSHIP_STATUS + " =" + userBasicInfo.getFriendShipStatus() + ","
                                    + DBConstants.MOBILE_PHONE_DIALING_CODE + " ='" + userBasicInfo.getMobilePhoneDialingCode() + "',"
                                    + DBConstants.PROFILE_IMAGE + " ='" + profileImage + "',"
                                    + DBConstants.PROFILE_IMAGE_ID + " =" + userBasicInfo.getProfileImageId() + ","
                                    + DBConstants.COVER_IMAGE + " ='" + coverImage + "',"
                                    + DBConstants.COVER_IMAGE_ID + " =" + userBasicInfo.getCoverImageId() + ","
                                    + DBConstants.COVER_IMAGE_X + " =" + -userBasicInfo.getCoverImageX() + ","
                                    + DBConstants.COVER_IMAGE_Y + " =" + -userBasicInfo.getCoverImageY() + ","
                                    + DBConstants.RING_EMAIL + " ='" + userBasicInfo.getRingEmail() + "',"
                                    + DBConstants.EMAIL_PRIVACY + " =" + userBasicInfo.getEmailPrivacy() + ","
                                    + DBConstants.MOBILE_PRIVACY + " =" + userBasicInfo.getMobilePrivacy() + ","
                                    + DBConstants.PROFILE_IMAGE_PRIVACY + " =" + userBasicInfo.getProfileImagePrivacy() + ","
                                    + DBConstants.BIRTHDAY_PRIVACY + " =" + userBasicInfo.getBirthdayPrivacy() + ","
                                    + DBConstants.COVER_IMAGE_PRIVACY + " =" + userBasicInfo.getCoverImagePrivacy() + ","
                                    + DBConstants.DEVICE_TOKEN + " ='" + deviceToken + "',"
                                    + DBConstants.CONTACT_TYPE + " =" + ct + ","
                                    + DBConstants.NEW_CONTACT_TYPE + " =" + nct + ","
                                    + DBConstants.IS_CHANGE_REQUESTER + " =" + iscr + ","
                                    + DBConstants.IS_BLOCKED + " =" + bv + ","
                                    //+ (userBasicInfo.getIncomingNotification() != null ? (DBConstants.INCOMING_NOTIFICATION + " =" + userBasicInfo.getIncomingNotification() + ",") : "")
                                    + DBConstants.INCOMING_NOTIFICATION + " =" + userBasicInfo.getIncomingNotification() + ","
                                    + DBConstants.UPDATE_TIME + " =" + ut + ""
                                    + "WHERE "
                                    + DBConstants.USER_IDENTITY + " = '" + userBasicInfo.getUserIdentity() + "' AND "
                                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                            stmt.executeUpdate(sql);
                        } else {
                            sqlExcept.printStackTrace();
                            throw new Exception();
                        }
                    }
                }
            }

            for (UserBasicInfo entity : userBasicInfoList) {
                if (entity.getUt() > SettingsConstants.FNF_USERINFO_UT) {
                    SettingsConstants.FNF_USERINFO_UT = entity.getUt();
                }
                if (entity.getCut() != null && entity.getCut() > SettingsConstants.FNF_CONTACT_UT) {
                    SettingsConstants.FNF_CONTACT_UT = entity.getCut();
                }
            }
            conn.commit();
            (new InsertIntoRingUserSettings()).start();
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
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
