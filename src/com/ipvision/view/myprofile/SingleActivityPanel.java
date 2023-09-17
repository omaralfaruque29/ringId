/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ActivityConstants;
import com.ipvision.view.utility.DesignClasses;
import static com.ipvision.view.utility.DesignClasses.getDefaultFont;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.WrapLayout;

/**
 *
 * @author Shahadat Hossain
 */
public class SingleActivityPanel extends JPanel {

    org.apache.log4j.Logger log = Logger.getLogger(SingleActivityPanel.class);
    public ActivityDTO activityDTO;
    private SingleActivityPanel instance;
    private Color activityMsgColor = RingColorCode.RING_CHAT_ACTIVITY;
    private JPanel container;

    public SingleActivityPanel(ActivityDTO activityDTO) {
        this.instance = this;
        this.activityDTO = activityDTO;

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel nPanel = new JPanel();
        nPanel.setOpaque(false);
        nPanel.setPreferredSize(new Dimension(565, 5));
        this.add(nPanel, BorderLayout.NORTH);

        JPanel sPanel = new JPanel();
        sPanel.setOpaque(false);
        sPanel.setPreferredSize(new Dimension(565, 5));
        this.add(sPanel, BorderLayout.SOUTH);

        this.initComponents();
    }

    private void initComponents() {
        try {
            JPanel mainPanel = new JPanel();
            mainPanel.setOpaque(false);
            mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            this.add(mainPanel, BorderLayout.CENTER);

            JPanel empty = new JPanel();
            empty.setOpaque(false);
            empty.setPreferredSize(new Dimension(50, 20));
            mainPanel.add(empty, BorderLayout.WEST);
            
            JPanel textPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(activityMsgColor);
                    int w = getWidth();
                    int h = getHeight();
                    g2d.fillRoundRect(0, 0, w, h, 40, 40);
                }
            };
            textPanel.setOpaque(false);
            mainPanel.add(textPanel);

            JLabel loadingLabel = DesignClasses.makeJLabelUnderFullName("");
            loadingLabel.setText(ChatHelpers.getDate(activityDTO.getUpdateTime(), ChatHelpers.CHAT_TIME_FORMAT));
            loadingLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
            loadingLabel.setPreferredSize(new Dimension(50, 20));
            mainPanel.add(loadingLabel, BorderLayout.EAST);

            JPanel textWrapper = new JPanel(new BorderLayout());
            textWrapper.setOpaque(false);
            textWrapper.setBorder(new EmptyBorder(7, 10, 7, 10));
            textPanel.add(textWrapper, BorderLayout.CENTER);

            container = new JPanel(new WrapLayout(WrapLayout.LEFT, 3, 0, 300));
            container.setBackground(activityMsgColor);
            textWrapper.add(container, BorderLayout.CENTER);

            processActivity();
        } catch (Exception ex) {
        }
    }

    private void processActivity() {
        container.removeAll();
        if (activityDTO.getActivityType() == ActivityConstants.ACTIVITY_FRIEND_REQUEST) {
            if (activityDTO.getMessageType() == ActivityConstants.MSG_PENDING_FRIEND_REQUEST) {
                pendingFriendRequest();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_INCOMING_FRIEND_REQUEST) {
                incomingFriendRequest();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_ACCEPTED_FRIEND_REQUEST) {
                acceptedFriendRequest();
            }
        } else if (activityDTO.getActivityType() == ActivityConstants.ACTIVITY_ACCESS_REQUEST) {
            if (activityDTO.getMessageType() == ActivityConstants.MSG_PENDING_ACCESS_CHANGE_REQUEST) {
                pendingAccessRequest();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_INCOMING_ACCESS_CHANGE_REQUEST) {
                incomingAccessRequest();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_ACCEPTED_ACCESS_CHANGE_REQUEST) {
                acceptedAccessRequest();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_REJECTED_ACCESS_CHANGE_REQUEST) {
                rejectedAccessRequest();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_DOWNGRADE_ACCESS_CHANGE) {
                downgradeAccessChange();
            }
        } else if (activityDTO.getActivityType() == ActivityConstants.ACTIVITY_BLOCK_FRIEND) {
            if (activityDTO.getMessageType() == ActivityConstants.MSG_BLOCK_FRIEND) {
                blockFriend();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_UNBLOCK_FRIEND) {
                unblockFriend();
            }
        } else if (activityDTO.getActivityType() == ActivityConstants.ACTIVITY_GROUP_UPDATE) {
            if (activityDTO.getMessageType() == ActivityConstants.MSG_GROUP_CREATE) {
                groupCreate();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_GROUP_ADD_MEMBER) {
                groupAddMember();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_GROUP_REMOVE_MEMBER) {
                groupRemoveMember();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_GROUP_LEAVE) {
                groupLeave();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_GROUP_JOIN) {
                groupJoin();
            } else if (activityDTO.getMessageType() == ActivityConstants.MSG_GROUP_EDIT_NAME) {
                groupEditName();
            }
        }
        container.revalidate();
        container.setPreferredSize(container.getLayout().preferredLayoutSize(container));
    }

    private void pendingFriendRequest() {
        //Sent friend request to FRIEND
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String fullName = HelperMethods.getUserFullName(friendBasicInfo);

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("Sent"));
        textList.add(new LabelText("friend"));
        textList.add(new LabelText("request"));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(fullName, activityDTO.getFriendIdentity()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }

    }

    private void incomingFriendRequest() {
        //Received friend request from FRIEND
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String fullName = HelperMethods.getUserFullName(friendBasicInfo);

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("Received"));
        textList.add(new LabelText("friend"));
        textList.add(new LabelText("request"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(fullName, activityDTO.getFriendIdentity()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void acceptedFriendRequest() {
        //ME accepted FRIEND as friend
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String name1 = "You";
        String name2 = HelperMethods.getUserFullName(friendBasicInfo);
        String id1 = MyFnFSettings.LOGIN_USER_ID;
        String id2 = activityDTO.getFriendIdentity();

        if (!activityDTO.getActivityBy().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            name1 = name2;
            name2 = "you";
            id1 = id2;
            id2 = MyFnFSettings.LOGIN_USER_ID;
        }

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText(name1, id1));
        textList.add(new LabelText("accepted"));
        textList.add(new LabelText(name2, id2));
        textList.add(new LabelText("as"));
        textList.add(new LabelText("friend"));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void pendingAccessRequest() {
        //You requested for access change from CT1 to CT2
        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("You", MyFnFSettings.LOGIN_USER_ID));
        textList.add(new LabelText("requested"));
        textList.add(new LabelText("for"));
        textList.add(new LabelText("access"));
        textList.add(new LabelText("change"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(activityDTO.getFromContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(activityDTO.getToContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void incomingAccessRequest() {
        //FRIEND requested for access change from CT1 to CT2
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String name = HelperMethods.getUserFullName(friendBasicInfo);

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText(name, activityDTO.getFriendIdentity()));
        textList.add(new LabelText("requested"));
        textList.add(new LabelText("for"));
        textList.add(new LabelText("access"));
        textList.add(new LabelText("change"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(activityDTO.getFromContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(activityDTO.getToContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void acceptedAccessRequest() {
        //FRIEND acccepted your access change request from CT1 to CT2
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String name1 = HelperMethods.getUserFullName(friendBasicInfo);
        String name2 = "your";
        String id1 = activityDTO.getFriendIdentity();
        String id2 = MyFnFSettings.LOGIN_USER_ID;

        if (activityDTO.getActivityBy().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            name2 = name1 + "'s";
            name1 = "You";
            id2 = id1;
            id1 = MyFnFSettings.LOGIN_USER_ID;
        }

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText(name1, id1));
        textList.add(new LabelText("acccepted"));
        textList.add(new LabelText(name2, id2));
        textList.add(new LabelText("access"));
        textList.add(new LabelText("change"));
        textList.add(new LabelText("request"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(activityDTO.getFromContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(activityDTO.getToContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }

    }

    private void rejectedAccessRequest() {
        //FRIEND rejected your access change request from CT1 to CT2
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String name1 = HelperMethods.getUserFullName(friendBasicInfo);
        String name2 = "your";
        String id1 = activityDTO.getFriendIdentity();
        String id2 = MyFnFSettings.LOGIN_USER_ID;

        if (activityDTO.getActivityBy().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            name2 = name1 + "'s";
            name1 = "You";
            id2 = id1;
            id1 = MyFnFSettings.LOGIN_USER_ID;
        }

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText(name1, id1));
        textList.add(new LabelText("rejected"));
        textList.add(new LabelText(name2, id2));
        textList.add(new LabelText("access"));
        textList.add(new LabelText("change"));
        textList.add(new LabelText("request"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(activityDTO.getFromContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(activityDTO.getToContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void downgradeAccessChange() {
        //You changed access from CT1 to CT2
        String name1 = "You";
        String id1 = MyFnFSettings.LOGIN_USER_ID;

        if (!activityDTO.getActivityBy().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
            name1 = HelperMethods.getUserFullName(friendBasicInfo);
            id1 = activityDTO.getFriendIdentity();
        }

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText(name1, id1));
        textList.add(new LabelText("changed"));
        textList.add(new LabelText("access"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(activityDTO.getFromContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(activityDTO.getToContactType() == StatusConstants.ACCESS_FULL ? "'Full Profile'" : "'Chat & Call'"));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void blockFriend() {
        //You blocked FRIEND
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String name = HelperMethods.getUserFullName(friendBasicInfo);

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("You", MyFnFSettings.LOGIN_USER_ID));
        textList.add(new LabelText("blocked"));
        textList.add(new LabelText(name, activityDTO.getFriendIdentity()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void unblockFriend() {
        //You unblocked FRIEND
        UserBasicInfo friendBasicInfo = FriendList.getInstance().getFriend_hash_map().get(activityDTO.getFriendIdentity());
        String name = HelperMethods.getUserFullName(friendBasicInfo);

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("You", MyFnFSettings.LOGIN_USER_ID));
        textList.add(new LabelText("unblocked"));
        textList.add(new LabelText(name, activityDTO.getFriendIdentity()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void groupCreate() {
        //Created group GROUP_NAME
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(activityDTO.getGroupId());
        String name = groupInfo != null ? groupInfo.getGroupName() : "No Name";

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("Created"));
        textList.add(new LabelText("group"));
        textList.add(new LabelText(name, activityDTO.getGroupId()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void groupAddMember() {
        //member1, member2 and member3 added in group GROUP_NAME
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(activityDTO.getGroupId());
        String groupName = groupInfo != null ? groupInfo.getGroupName() : "No Name";

        if (activityDTO.getMembers().size() > 0) {
            List<LabelText> textList = new ArrayList<LabelText>();
            List<String> valueList = new ArrayList<String>(activityDTO.getMembers().values());
            List<String> keyList = new ArrayList<String>(activityDTO.getMembers().keySet());

            if (valueList.size() == 1) {
                textList.add(new LabelText(valueList.get(0), keyList.get(0)));
            } else if (valueList.size() == 2) {
                textList.add(new LabelText(valueList.get(0), keyList.get(0)));
                textList.add(new LabelText("and"));
                textList.add(new LabelText(valueList.get(1), keyList.get(1)));
            } else {
                for (int index = 0; index < valueList.size(); index++) {
                    String name = valueList.get(index);
                    String id = keyList.get(index);
                    if (index == valueList.size() - 2) {
                        textList.add(new LabelText(name, id));
                        textList.add(new LabelText("and"));
                    } else if (index == valueList.size() - 1) {
                        textList.add(new LabelText(name, id));
                    } else {
                        textList.add(new LabelText(name + ",", id));
                    }
                }
            }

            textList.add(new LabelText("added"));
            textList.add(new LabelText("in"));
            textList.add(new LabelText("group"));
            textList.add(new LabelText(groupName, activityDTO.getGroupId()));

            for (LabelText labelText : textList) {
                container.add(makeJLabel(labelText));
            }
        }

    }

    private void groupRemoveMember() {
        //member1, member2 and member3 removed from group GROUP_NAME
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(activityDTO.getGroupId());
        String groupName = groupInfo != null ? groupInfo.getGroupName() : "No Name";

        if (activityDTO.getMembers().size() > 0) {
            List<LabelText> textList = new ArrayList<LabelText>();
            List<String> valueList = new ArrayList<String>(activityDTO.getMembers().values());
            List<String> keyList = new ArrayList<String>(activityDTO.getMembers().keySet());

            if (valueList.size() == 1) {
                textList.add(new LabelText(valueList.get(0), keyList.get(0)));
            } else if (valueList.size() == 2) {
                textList.add(new LabelText(valueList.get(0), keyList.get(0)));
                textList.add(new LabelText("and"));
                textList.add(new LabelText(valueList.get(1), keyList.get(1)));
            } else {
                for (int index = 0; index < valueList.size(); index++) {
                    String name = valueList.get(index);
                    String id = keyList.get(index);
                    if (index == valueList.size() - 2) {
                        textList.add(new LabelText(name, id));
                        textList.add(new LabelText("and"));
                    } else if (index == valueList.size() - 1) {
                        textList.add(new LabelText(name, id));
                    } else {
                        textList.add(new LabelText(name + ",", id));
                    }
                }
            }

            textList.add(new LabelText("removed"));
            textList.add(new LabelText("from"));
            textList.add(new LabelText("group"));
            textList.add(new LabelText(groupName, activityDTO.getGroupId()));

            for (LabelText labelText : textList) {
                container.add(makeJLabel(labelText));
            }
        }
    }

    private void groupLeave() {
        //You left from group GROUP_NAME
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(activityDTO.getGroupId());
        String name = groupInfo != null ? groupInfo.getGroupName() : "No Name";

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("You", MyFnFSettings.LOGIN_USER_ID));
        textList.add(new LabelText("left"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText("group"));
        textList.add(new LabelText(name, activityDTO.getGroupId()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void groupJoin() {
        //You joined group GROUP_NAME
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(activityDTO.getGroupId());
        String name = groupInfo != null ? groupInfo.getGroupName() : "No Name";

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("You", MyFnFSettings.LOGIN_USER_ID));
        textList.add(new LabelText("joined"));
        textList.add(new LabelText("in"));
        textList.add(new LabelText("group"));
        textList.add(new LabelText(name, activityDTO.getGroupId()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    private void groupEditName() {
        //Changed group name from GROUP_NAME to NEW_GROUP_NAME
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(activityDTO.getGroupId());
        String name = groupInfo != null ? groupInfo.getGroupName() : "No Name";

        List<LabelText> textList = new ArrayList<LabelText>();
        textList.add(new LabelText("Changed"));
        textList.add(new LabelText("group"));
        textList.add(new LabelText("name"));
        textList.add(new LabelText("from"));
        textList.add(new LabelText(activityDTO.getPrevGroupName(), activityDTO.getGroupId()));
        textList.add(new LabelText("to"));
        textList.add(new LabelText(name, activityDTO.getGroupId()));

        for (LabelText labelText : textList) {
            container.add(makeJLabel(labelText));
        }
    }

    public JLabel makeJLabel(LabelText labelText) {
        JLabel label = new JLabel(labelText.getText());
        //label.setOpaque(false);
        label.setForeground(Color.WHITE);
        try {
            if (labelText.getType() == 2) {
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.setFont(getDefaultFont(Font.BOLD, 12));
                label.setBackground(Color.ORANGE);
                addMouseListener(label, labelText);
            } else if (labelText.getType() == 3) {
                label.setFont(getDefaultFont(Font.BOLD, 12));
                label.setBackground(Color.RED);
            } else {
                label.setFont(getDefaultFont(Font.PLAIN, 12));
                label.setBackground(Color.GREEN);
            }
        } catch (Exception e) {
        }
        return label;
    }

    public class LabelText {

        private String userIdentity;
        private Long groupId;
        private String text;
        private int type = 1;

        public LabelText(String text) {
            this.text = text;
            this.type = 1;
        }

        public LabelText(String text, String userIdentity) {
            this.text = text;
            this.userIdentity = userIdentity;
            this.type = 2;
        }

        public LabelText(String text, Long groupId) {
            this.text = text;
            this.groupId = groupId;
            this.type = 3;
        }

        public String getText() {
            return text;
        }

        public int getType() {
            return type;
        }

        public String getUserIdentity() {
            return userIdentity;
        }

        public Long getGroupId() {
            return groupId;
        }

    }

    public UserBasicInfo getUserInfo(LabelText labelText) {
        UserBasicInfo basicinfo = new UserBasicInfo();

        if (labelText.getUserIdentity() != null) {
            basicinfo.setUserIdentity(labelText.getUserIdentity());
            basicinfo.setFullName(labelText.getUserIdentity());
        }
        if (labelText.getText() != null) {
            basicinfo.setFullName(labelText.getText());
        } else {
            basicinfo.setFullName(labelText.getText());
        }

        return basicinfo;
    }

    private void addMouseListener(final JLabel label, final LabelText labelText) {
        label.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (labelText.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                    e.getComponent().setFont(original);
                    GuiRingID.getInstance().action_of_myProfile_button();
                } else if (FriendList.getInstance().getFriend_hash_map().get(labelText.getUserIdentity()) != null) {
                    e.getComponent().setFont(original);
                    GuiRingID.getInstance().showFriendProfile(labelText.getUserIdentity());
                } else if (labelText.getUserIdentity() != null && labelText.getUserIdentity().trim().length() > 0) {
                    e.getComponent().setFont(original);
                    GuiRingID.getInstance().showUnknownProfile(getUserInfo(labelText));
                } else {
                    e.getComponent().setFont(original);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
            }
        });
    }

}
