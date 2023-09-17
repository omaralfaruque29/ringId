/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import static com.ipvision.view.utility.DesignClasses.return_image;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.friendprofile.MyFriendContactListPane;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class SendFriendRequest extends Thread {
//

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendFriendRequest.class);
    private boolean success = false;
    private String userId;
    private JLabel textLabel;
    private JButton button;
    private JButton button1;
    private String text;
    private Icon icon;
    private int contactType;
    private MyFriendContactListPane myFriendContactListPane;
    private boolean fromFriendList = false;

    public SendFriendRequest(String userid, JLabel textLabel, int contactType) {
        this.userId = userid;
        this.textLabel = textLabel;
        if (textLabel.getText() != null && textLabel.getText().length() > 0) {
            this.text = textLabel.getText();
        }
        if (textLabel.getIcon() != null) {
            this.icon = textLabel.getIcon();
        }
        this.contactType = contactType;
        setName(this.getClass().getSimpleName());
    }

    public SendFriendRequest(String userid, JButton button, int contactType) {
        this.userId = userid;
        this.button = button;
        this.myFriendContactListPane = null;
        this.contactType = contactType;
        setName(this.getClass().getSimpleName());
    }

    public SendFriendRequest(String userid, JButton button, JButton button1, int contactType) {
        this.userId = userid;
        this.button = button;
        this.button1 = button1;
        this.myFriendContactListPane = null;
        this.contactType = contactType;
        setName(this.getClass().getSimpleName());
    }

    public SendFriendRequest(String userid, JButton button, MyFriendContactListPane myFriendContactListPane, int contactType) {
        this.userId = userid;
        this.button = button;
        this.myFriendContactListPane = myFriendContactListPane;
        this.contactType = contactType;
        setName(this.getClass().getSimpleName());
    }

    public SendFriendRequest(String userid, JLabel textLabel, MyFriendContactListPane myFriendContactListPane, int contactType) {
        this.userId = userid;
        this.textLabel = textLabel;
        this.myFriendContactListPane = myFriendContactListPane;
        if (textLabel.getText() != null && textLabel.getText().length() > 0) {
            this.text = textLabel.getText();
        }
        this.contactType = contactType;
        this.fromFriendList = true;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {
                 "tm":1398067514945, // optional 
                 "actn":88,
                 "sId":"1395556590102nazmul3",
                 "pckId":"1398067514945"
                 }

                 */
                if (button != null) {
                    //button.setVisible(false);
                    button.setEnabled(false);
                }
                if (button1 != null) {
                    //button.setVisible(false);
                    button1.setEnabled(false);
                }
                if (text != null) {
                    textLabel.setText("");
                }
                if (icon != null && textLabel != null) {
                    textLabel.setIcon(return_image(GetImages.PLEASE_WAIT_MINI));
                    textLabel.revalidate();
                }

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_ADD_FRIEND);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setContactType(contactType);
                js_fields.setUserIdentity(userId);
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(2000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (js.getSuccess()) {
                            success = true;
                            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                            MyfnfHashMaps.getInstance().getSingleFriendPanel().remove(userId);

                            if (FriendList.getInstance().getPeopleYouMayKnow().getData(userId) != null) {
                                FriendList.getInstance().getPeopleYouMayKnow().removeData(userId);
                                if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                        && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel() != null) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel().remove_content(userId);
                                }
                                (new DefaultRequest(AppConstants.TYPE_YOU_MAY_KNOW_LIST)).start();
                            }
                            if (MyfnfHashMaps.getInstance().getInviteFriendsContainer() != null
                                    && !MyfnfHashMaps.getInstance().getInviteFriendsContainer().isEmpty()) {
                                for (int key : MyfnfHashMaps.getInstance().getInviteFriendsContainer().keySet()) {
                                    if (MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(key).containsKey(userId)) {
                                        MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(key).remove(userId);
                                        if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                                            if (GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel() != null) {
                                                GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().remove_content(userId);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            /* GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel().addSearchResultInContainer();
                             GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().inviteListWrapperPanel.remove(AllFriendList.selectedFriendPanel);
                             GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().inviteListWrapperPanel.repaint();*/

                            if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                                if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().remove_content(userId);
                                }
                            }
                            if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                    && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                                GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().remove_content(userId);
                            }

                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        MyfnfHashMaps.getInstance().getMyFriendProfiles().remove(userId);
                        MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().remove(userId);

                        if (GuiRingID.getInstance() != null
                                && GuiRingID.getInstance().getMainRight() != null
                                && GuiRingID.getInstance().getMainRight().getUnknownProfile() != null
                                && GuiRingID.getInstance().getMainRight().getUnknownProfile().isDisplayable()
                                && GuiRingID.getInstance().getMainRight().getUnknownProfile().friendProfileInfo.getUserIdentity().equals(userId)) {
                            GuiRingID.getInstance().showFriendProfile(userId);
                        }
                        break;
                    }
                }

                if (button != null) {
                    button.setEnabled(true);
                }
                if (button1 != null) {
                    button1.setEnabled(true);
                }
                if (text != null && textLabel != null) {
                    textLabel.setText(text);
                }
                if (icon != null && textLabel != null) {
                    textLabel.setIcon(null);
                    textLabel.revalidate();
                }
                if (myFriendContactListPane != null) {
                    if (success) {
                        //textLabel.setText("");
                        myFriendContactListPane.friendAdded(userId);
                    }
                }
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Error in SendFriendRequest class ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
