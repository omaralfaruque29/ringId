/*
 * Copyright (C) 2006 Luca Veltri - University of Parma - Italy
 * 
 * This source code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 * 
 */
package com.ipvision.main;

import com.ipvision.view.loginsignup.LoginUI;
import com.ipvision.audio.PlayAudioHelper;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.dao.DatabaseActivityDAO;
import com.ipvision.service.DeleteOldBookImages;
import com.ipvision.service.singinsignup.SignInRequestBackground;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.LoadUserBasicInformation;
import com.ipvision.view.SplashScreenBeforLogin;
import com.ipvision.service.utility.InternetUnavailablityCheck;

public class Main {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Main.class);
    private Class<?> classType = getClass();

    public static void init() {

        Statement stmt = null;
        //  try {
        if (DBConstants.conn != null) {
            try {
                stmt = DBConstants.conn.createStatement();
                DatabaseActivityDAO.getInstance().createRingLoginSettingTable(stmt);
                DatabaseActivityDAO.getInstance().createEmotionsTable(stmt);
                DatabaseActivityDAO.getInstance().fetchEmotions(stmt);
                DatabaseActivityDAO.getInstance().createInstantMessagesTable(stmt);
                //DatabaseActivityDAO.getInstance().fetchInstantMessages(stmt);
                DatabaseActivityDAO.getInstance().createRingMarketStickerCategoriesTable(stmt);
                DatabaseActivityDAO.getInstance().createRingMarketStickerTable(stmt);
                DatabaseActivityDAO.getInstance().createRingUserSettings(stmt);
                DatabaseActivityDAO.getInstance().createContractListTable(stmt);
                DatabaseActivityDAO.getInstance().createGroupListTable(stmt);
                DatabaseActivityDAO.getInstance().createGroupMemberListTable(stmt);
                DatabaseActivityDAO.getInstance().createCircleListTable(stmt);
                DatabaseActivityDAO.getInstance().createCircleMemberListTable(stmt);
                DatabaseActivityDAO.getInstance().createGroupChatTable(stmt);
                DatabaseActivityDAO.getInstance().createRingCallLogTable(stmt);
                DatabaseActivityDAO.getInstance().createSmsHistoryTable(stmt);
                DatabaseActivityDAO.getInstance().createNotificationHistoryTable(stmt);
                DatabaseActivityDAO.getInstance().createRingActivityTable(stmt);
                DatabaseActivityDAO.getInstance().createBrokenChatTable(stmt);
            } catch (SQLException ex) {
                HelperMethods.showPlaneDialogMessage("Problem while loading.");
                System.exit(0);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {

                }
            }
        }

    }

    public static void main(String[] args) {
        Main ua = new Main();
        for (String arg : args) {
            if (arg.equals("-startup")) {
                HelperMethods.defaultSettings(ua.classType);
                DatabaseActivityDAO.getInstance().createDatabaseConnection(false);
                DatabaseActivityDAO.getInstance().fetchRingSignInSettingsBeforeLogin(null);
                if (MyFnFSettings.VALUE_LOGIN_AUTO_START != 1) {
                    System.exit(0);
                }
            }
        }

        InternetUnavailablityCheck.startInternetUnavailablityCheck();
        HelperMethods.loadDefaultFont();
        HelperMethods.createProfileImageDirectory();
        HelperMethods.createCoverImageDirectory();
        HelperMethods.createBookImageDirectory();
        HelperMethods.createChatFilesDirectory();
        HelperMethods.createBrokenFilesDirectory();
        new DeleteOldBookImages().start();

        final SplashScreenBeforLogin sp = new SplashScreenBeforLogin();
        sp.setVisible(true);

        final PlayAudioHelper playAudioHelper = new PlayAudioHelper();
        try {
            UIManager.put("ToolTip.background", new ColorUIResource(244, 255, 255));
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            playAudioHelper.initLanding();
            if (playAudioHelper.landing_tune != null) {
                playAudioHelper.landing_tune.play();
            }
        } catch (Exception e) {
        }

        if (DBConstants.conn == null) {
            DatabaseActivityDAO.getInstance().createDatabaseConnection(true);
            DatabaseActivityDAO.getInstance().fetchRingSignInSettingsBeforeLogin(null);
        }

        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN == 1
                        && MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 1
                        && MyFnFSettings.VALUE_LOGIN_USER_INFO != null
                        && MyFnFSettings.VALUE_LOGIN_USER_NAME != null
                        && MyFnFSettings.VALUE_LOGIN_USER_NAME.length() > 0
                        && MyFnFSettings.VALUE_LOGIN_USER_PASSWORD != null
                        && MyFnFSettings.VALUE_LOGIN_USER_PASSWORD.length() > 0) {
                    openRingID(sp, playAudioHelper);
                } else {
                    openLoginUI(sp, playAudioHelper);
                }
                HelperMethods.startAuthSockets();
            }
        }).start();

    }

    private static void openRingID(SplashScreenBeforLogin sp, PlayAudioHelper playAudioHelper) {
        MyFnFSettings.userProfile = new UserBasicInfo();
        if (MyFnFSettings.VALUE_LOGIN_USER_INFO.getUserIdentity() != null) {
            MyFnFSettings.userProfile.setUserIdentity(MyFnFSettings.VALUE_LOGIN_USER_INFO.getUserIdentity());
            MyFnFSettings.LOGIN_USER_ID = MyFnFSettings.VALUE_LOGIN_USER_INFO.getUserIdentity();
        }
        if (MyFnFSettings.VALUE_LOGIN_USER_INFO.getUserTableId() != null) {
            MyFnFSettings.LOGIN_USER_TABLE_ID = MyFnFSettings.VALUE_LOGIN_USER_INFO.getUserTableId();
        }
        if (MyFnFSettings.VALUE_LOGIN_USER_INFO.getFullName() != null) {
            MyFnFSettings.userProfile.setFullName(MyFnFSettings.VALUE_LOGIN_USER_INFO.getFullName());
        }
        try {
            HelperMethods.bindMyProfileObject(MyFnFSettings.VALUE_LOGIN_USER_INFO, true);
            LoadUserBasicInformation loadUserBasicInformation = new LoadUserBasicInformation(MyFnFSettings.VALUE_LOGIN_USER_INFO);
            loadUserBasicInformation.loadInformation();
            if (playAudioHelper.landing_tune != null) {
                playAudioHelper.landing_tune.stop();
            }
            sp.dispose();
            System.gc();
            Runtime.getRuntime().gc();

            GuiRingID guiRingID = new GuiRingID();
            guiRingID.setVisible(true);

            SignInRequestBackground signIn = new SignInRequestBackground();
            signIn.startService();
        } catch (Exception e) {
            log.error("System error ==> " + e.getMessage());
            System.exit(0);
        }
    }

    private static void openLoginUI(SplashScreenBeforLogin sp, PlayAudioHelper playAudioHelper) {
        if (playAudioHelper.landing_tune != null) {
            playAudioHelper.landing_tune.stop();
        }

        sp.dispose();
        System.gc();
        Runtime.getRuntime().gc();

        LoginUI.getLoginUI().loadMainContent(LoginUI.MAIN_LOGIN_UI);
        LoginUI.getLoginUI().setVisible(true);
    }

}
