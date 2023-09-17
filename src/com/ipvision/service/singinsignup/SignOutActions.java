/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipv.chat.communicator.ChatCommunication;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import java.awt.GridBagLayout;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.loginsignup.LoginUI;
import static com.ipvision.view.loginsignup.LoginUI.MAIN_LOGIN_UI;
import com.ipvision.service.utility.call.TopLabelWrapper;
import com.ipvision.view.call.MainClass;
import com.ipvision.model.JsonFields;
import com.ipvision.service.KeepAlive;
import com.ipvision.service.auth.AuthenticationReceiverMain;
import com.ipvision.model.stores.StickerStorer;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.model.dao.DatabaseActivityDAO;
import com.ipvision.view.group.CreateGroupPanel;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.myprofile.Photos;
import com.ipvision.view.utility.BreakingPacketRepository;

/**
 *
 * @author Faiz
 */
public class SignOutActions extends JDialog {

    private boolean systemExit = false;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SignOutActions.class);

    public SignOutActions(boolean systemExit) {
        setModal(true);
        setSize(200, 100);
        setUndecorated(true);
        this.systemExit = systemExit;

        setLocationRelativeTo(null);

        // 
        JPanel content = (JPanel) getContentPane();
        content.setLayout(new GridBagLayout());
        content.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.APP_DEFAULT_THEME_COLOR));
        content.setOpaque(false);
        //  content.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);

        JLabel loader = DesignClasses.loadingLable(true);
        loader.setText("Please wait....");
        content.add(loader);

    }

    public void doClean() {
        new DoCleaning().start();
    }

    public static void send_logout_request() {
        try {
            if (MyFnFSettings.LOGIN_SESSIONID != null) {
                String pakId = MyFnFSettings.LOGIN_USER_ID + AppConstants.TYPE_SIGN_OUT + System.currentTimeMillis();
                JsonFields fld = new JsonFields();
                fld.setAction(AppConstants.TYPE_SIGN_OUT);
                fld.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                fld.setPacketId(pakId);

                SendToServer.sendPacketAsString(fld, ServerAndPortSettings.AUTHENTICATION_PORT);
                for (int i = 1; i <= 4; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(fld, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }
                BreakingPacketRepository.getInstance().removeAllBreakingPacketList();
            }
        } catch (Exception ex) {
        }
    }

    public void clear_all_hashmaps() {
        try {
            NewsFeedMaps.hash_maps = null;
            MyfnfHashMaps.getInstance().clearHashMaps();
            MyfnfHashMaps.hash_maps = null;
            FriendList.contactListObject = null;
        } catch (Exception e) {
        }
    }

    class DoCleaning extends Thread {

        public DoCleaning(){
          setName(this.getClass().getSimpleName());
        }
        @Override
        public void run() {
            try {

                send_logout_request();
                clear_all_hashmaps();
                KeepAlive.runningFlag = false;
                if (!systemExit) {
                    /*chat soscket*/
                    ChatCommunication.getInstance().stopService();
                    ChatHashMap.getInstance().clearAll();
                    /*voice socket*/
                    TopLabelWrapper.resetCallingWindows(false);
                    TopLabelWrapper.resetVoiceSocket();
                    MainClass.playAudioHelper = null;
//                    VoiceMainReceiver.getInstance().close_socket();
//                    VoiceMainReceiver.voiceMainReceiver = null;
                    /*voice socket end*/
                    /*mail start*/
                    StickerStorer.getInstance().clear();
                    /*mail end*/

                    /*auth message parser*/
                    AuthenticationReceiverMain.getInstance().close_socket();
                    AuthenticationReceiverMain.udp_transport = null;

                    /*authmessage parser*/
                    GuiRingID.setInstance(null);
                    Photos.setInstance(null);
//                    if (AlbumsHome.albumsHome != null) {
//                        AlbumsHome.albumsHome = null;
//                    }

                    /* if (AcceptedFriendList.instance != null) {
                     AcceptedFriendList.instance = null;
                     }*/
                    /*if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null) {
                     GuiRingID.getInstance().getMainLeftContainer().setFriendListContainer(null);
                     }*/
                    if (LoginUI.loginUI != null) {
                        LoginUI.loginUI = null;
                    }
                    CreateGroupPanel.setInstance(null);
                    MyFnFSettings.userProfile = null;
                    MyFnFSettings.LOGIN_SESSIONID = null;
                    MyFnFSettings.LOGIN_USER_ID = "";
                    MyFnFSettings.FRIEND_LIST_LOADED = false;
//                    MyFnFSettings.VALUE_LOGIN_USER_NAME = "";
//                    MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.RINGID_LOGIN;
//                    MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = "";
//                    MyFnFSettings.VALUE_LOGIN_AUTO_START = 0;
//                    MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 1;
//                    MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 0;
                    //   MyProfileEmailPanel.emailPanel = null;
                    Thread.sleep(500);
                    for (Window window : Window.getWindows()) {
                        if (window != null) {
                            window.dispose();
                            window = null;
                        }
                    }
                    ImageObjects.reset();
                    System.gc();
                    Runtime.getRuntime().gc();
                    DatabaseActivityDAO.getInstance().fetchRingSignInSettingsBeforeLogin(null);
                    LoginUI.getLoginUI().loadMainContent(MAIN_LOGIN_UI);
                    LoginUI.getLoginUI().setVisible(true);
                    HelperMethods.startAuthSockets();
                } else {
                    Thread.sleep(1000);
                    System.exit(0);
                }
            } catch (Exception e) {
                System.exit(0);
                // e.printStackTrace();
                log.error("failed signout actions" + e.getMessage());
            }
        }

    }

//    public static void main(String[] args) {
//        SignOutActions signOutActions = new SignOutActions();
//        signOutActions.doClean();
//        signOutActions.setVisible(true);
//    }
}
