/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipv.chat.ChatConstants;
import com.ipvision.constants.AppConstants;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.recorder.AudioRecorder;
import com.ipvision.service.chat.ChatFileShareProcessor;
import com.ipvision.recorder.RecordingTimer;
import com.ipvision.recorder.TimerListener;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.audio.AudioController;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.chat.ProgressRectangleUI;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Shahadat Hossain
 */
public class AudioRecorderWindow extends JWindow {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AudioRecorderWindow.class);
    public DownLoaderHelps helps = new DownLoaderHelps();
    public static final int TYPE_FRIEND_VOICE_RECORD = 1;
    public static final int TYPE_GROUP_VOICE_RECORD = 2;
    public final int MAX_LENGTH = 10; //Seconds
    private final String DEFAULT_COUNTER_TEXT = "00:00:00";
    private int posX = 0;
    private int posY = 0;

    public static AudioRecorderWindow instance;
    private JLabel friendFullNameLable;
    private AudioRecorder audioRecorder;

    private JButton btnCloseWindow = DesignClasses.createImageButton(GetImages.NOTIFICATION_DELETE, GetImages.NOTIFICATION_DELETE_H, GetImages.NOTIFICATION_DELETE_H, "Cancel Recording");
    private JButton btnStartRecord = DesignClasses.createImageButton(GetImages.RECORD_START, GetImages.RECORD_START_H, "Start");
    private JButton btnPauseRecord = DesignClasses.createImageButton(GetImages.RECORD_PAUSE, GetImages.RECORD_PAUSE_H, "Pause");
    private JButton btnResumeRecord = DesignClasses.createImageButton(GetImages.RECORD_RESUME, GetImages.RECORD_RESUME_H, "Resume");
    private JButton btnStopRecord = DesignClasses.createImageButton(GetImages.RECORD_STOP, GetImages.RECORD_STOP_H, "Stop");
    private JButton btnSendRecord = DesignClasses.createImageButton(GetImages.RECORD_SEND, GetImages.RECORD_SEND_H, "Send");
    //DesignClasses.createImageButton(GetImages.STOP_RECORDING, GetImages.STOP_RECORDING_H, "Send Audio Record");
    private JLabel profilePictureLabel;

    private JLabel timerLabel;
    private JProgressBar timerProgressBar;
    public RecordingTimer recordingTimer;

    private String userIdentity;
    private Long groupId;
    public String fileName;

    //private int type;
    public AudioRecorderWindow(Long groupId) {
        instance = this;
        this.groupId = groupId;
        this.initContents();
    }

    public AudioRecorderWindow(String userIdentity) {
        instance = this;
        this.userIdentity = userIdentity;
        this.initContents();
    }

    private void initContents() {
        this.setSize(380, 148);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setAlwaysOnTop(true);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(scrSize.width - (getWidth() + 30), scrSize.height - (getHeight() + 60));

        JPanel contentWrapperPanel = new JPanel(new BorderLayout());
        contentWrapperPanel.setOpaque(false);
        contentWrapperPanel.setBorder(new MatteBorder(3, 3, 3, 3, Color.LIGHT_GRAY));
        this.setContentPane(contentWrapperPanel);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(7, 7, 7, 7));
        content.setLayout(new BorderLayout());
        content.addMouseListener(poxValueListener);
        content.addMouseMotionListener(frameDragListener);
        contentWrapperPanel.add(content, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        content.add(headerPanel, BorderLayout.NORTH);
        headerPanel.add(btnCloseWindow, BorderLayout.EAST);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 7));
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(0, 15, 0, 15));
        content.add(centerWrapper, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        centerPanel.setOpaque(false);
        centerWrapper.add(centerPanel, BorderLayout.CENTER);

        JPanel profileImagePanel = new JPanel(new GridBagLayout());
        profileImagePanel.setOpaque(false);
        profilePictureLabel = new JLabel();
        profileImagePanel.add(profilePictureLabel);
        centerPanel.add(profileImagePanel, BorderLayout.WEST);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 7));
        namePanel.setOpaque(false);
        friendFullNameLable = DesignClasses.makeLableBold1("", 15, Font.BOLD);
        if (userIdentity != null && userIdentity.trim().length() > 0) {
            String fullName = HelperMethods.getUserFullName(FriendList.getInstance().getFriend_hash_map().get(this.userIdentity));
            friendFullNameLable.setText(fullName);
            friendFullNameLable.setToolTipText(fullName);
        } else if (groupId != null && groupId > 0) {
            GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(this.groupId);
            String groupName = "No Name";
            if (groupInfo != null) {
                groupName = groupInfo.getGroupName();
            }
            friendFullNameLable.setText(groupName);
            friendFullNameLable.setToolTipText(groupName);
        }
        friendFullNameLable.setPreferredSize(new Dimension(220, 20));
        namePanel.add(friendFullNameLable);
        centerPanel.add(namePanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setPreferredSize(new Dimension(0, 72));
        footerPanel.setOpaque(false);
        centerWrapper.add(footerPanel, BorderLayout.SOUTH);

        JPanel timerProgressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        timerProgressPanel.setOpaque(false);
        footerPanel.add(timerProgressPanel, BorderLayout.WEST);

        timerProgressBar = new JProgressBar() {
            @Override
            public void updateUI() {
                super.updateUI();
                setUI(new ProgressRectangleUI());
                setBorder(null);
            }
        };
        timerProgressBar.setPreferredSize(new Dimension(200, 20));
        timerProgressBar.setForeground(RingColorCode.RING_THEME_COLOR);
        timerProgressBar.setOpaque(false);
        timerProgressPanel.add(timerProgressBar);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrapper.setOpaque(false);
        footerPanel.add(buttonWrapper, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0xe4e0dd));
                Area border = new Area(new RoundRectangle2D.Double(0, 0, 90, 45, 45, 45));
                g2d.fill(border);

                g2d.setColor(new Color(0xfffaf6));
                Area shape = new Area(new RoundRectangle2D.Double(1, 1, 88, 43, 43, 43));
                g2d.fill(shape);
            }
        };
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(90, 45));
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        buttonPanel.setOpaque(false);
        buttonWrapper.add(buttonPanel, BorderLayout.CENTER);

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timerPanel.setOpaque(false);
        timerLabel = new JLabel(DEFAULT_COUNTER_TEXT);
        timerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        timerLabel.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        timerPanel.add(timerLabel);
        footerPanel.add(timerPanel, BorderLayout.SOUTH);

        buttonPanel.add(btnStartRecord);
        buttonPanel.add(btnPauseRecord);
        buttonPanel.add(btnResumeRecord);
        buttonPanel.add(btnStopRecord);
        buttonPanel.add(btnSendRecord);
        btnStartRecord.setVisible(true);
        btnPauseRecord.setVisible(false);
        btnResumeRecord.setVisible(false);
        btnStopRecord.setVisible(false);
        btnSendRecord.setVisible(false);

        btnCloseWindow.addActionListener(actionListener);
        btnStartRecord.addActionListener(actionListener);
        btnPauseRecord.addActionListener(actionListener);
        btnResumeRecord.addActionListener(actionListener);
        btnStopRecord.addActionListener(actionListener);
        btnSendRecord.addActionListener(actionListener);

        buildProfileImage();
    }


    private void buildProfileImage() {
        try {
            if (userIdentity != null && userIdentity.trim().length() > 0) {
                UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
                short[] friendPrivacy = new short[5];
                if (friendProfileInfo != null && friendProfileInfo.getPrivacy() != null) {
                    friendPrivacy = friendProfileInfo.getPrivacy();
                }
                short profileImagePrivacy = friendPrivacy[2];

                if (friendProfileInfo != null
                        && friendProfileInfo.getProfileImage() != null
                        && friendProfileInfo.getProfileImage().trim().length() > 0
                        && profileImagePrivacy > 0
                        && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND)) {
                    ImageHelpers.addProfileImageThumb(profilePictureLabel, friendProfileInfo.getProfileImage());
                } else {
                    BufferedImage img = ImageHelpers.getUnknownImage(true);
                    profilePictureLabel.setIcon(new ImageIcon(img));
                }
                profilePictureLabel.revalidate();
                profilePictureLabel.repaint();
            } else if (groupId != null && groupId > 0) {
                DesignClasses.setGroupProfileImage(profilePictureLabel, groupId);
                /*BufferedImage img = DesignClasses.scaleToRoundedImage(35, 35, ImageIO.read(new Object() {
                 }.getClass().getClassLoader().getResource(GetImages.GROUP_LARGE_IMAGE)), 35);
                 profilePictureLabel.setIcon(new ImageIcon(img));
                 profilePictureLabel.revalidate();
                 profilePictureLabel.repaint();*/
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in buildProfileImage ==>" + e.getMessage());
        }
    }

    MouseAdapter poxValueListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            posX = e.getX();
            posY = e.getY();
        }
    };

    MouseMotionAdapter frameDragListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent evt) {
            setLocation(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY);
        }
    };

    public void showRecodingVoiceChat() {
        this.setAlwaysOnTop(true);
        this.toFront();
        this.setVisible(true);
        this.recordingTimer = new RecordingTimer(timerLabel, timerProgressBar, timerListener);
        this.recordingTimer.setLimit(MAX_LENGTH);
        AudioController.setMasterOutputMute(false);
    }

    public void sendChatAudioFile(File file) {
        if (userIdentity != null && userIdentity.trim().length() > 0) {
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);
            if (friendChatCallPanel != null) {
                new ChatFileShareProcessor(file, friendChatCallPanel, ChatConstants.TYPE_AUDIO_FILE, recordingTimer.getCount()).start();
            }
        } else if (groupId != null && groupId > 0) {
            GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
            if (groupPanel != null) {
                new ChatFileShareProcessor(file, groupPanel, ChatConstants.TYPE_AUDIO_FILE, recordingTimer.getCount()).start();
            }
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnStartRecord) {
                fileName = MyFnFSettings.LOGIN_USER_ID + "_" + System.currentTimeMillis() + "." + "mp3";
                audioRecorder = new AudioRecorder(instance);
                audioRecorder.start();
                btnStartRecord.setVisible(false);
                btnPauseRecord.setVisible(true);
                btnResumeRecord.setVisible(false);
                btnStopRecord.setVisible(true);
                btnSendRecord.setVisible(false);
            } else if (e.getSource() == btnPauseRecord) {
                recordingTimer.pause();
                btnStartRecord.setVisible(false);
                btnPauseRecord.setVisible(false);
                btnResumeRecord.setVisible(true);
                btnStopRecord.setVisible(true);
                btnSendRecord.setVisible(false);
            } else if (e.getSource() == btnResumeRecord) {
                recordingTimer.resume();
                btnStartRecord.setVisible(false);
                btnPauseRecord.setVisible(true);
                btnResumeRecord.setVisible(false);
                btnStopRecord.setVisible(true);
                btnSendRecord.setVisible(false);
            } else if (e.getSource() == btnStopRecord) {
                audioRecorder.stopRecording(false);
                btnStartRecord.setToolTipText("Restart");
                btnStartRecord.setVisible(true);
                btnPauseRecord.setVisible(false);
                btnResumeRecord.setVisible(false);
                btnStopRecord.setVisible(false);
                btnSendRecord.setVisible(true);
            } else if (e.getSource() == btnSendRecord) {
                File file = new File(helps.getChatFileDestinationFolder() + "/" + fileName);
                if (file.exists()) {
                    sendChatAudioFile(file);
                }
                hideWindows();
            } else if (e.getSource() == btnCloseWindow) {
                hideForceFully();
            }
        }
    };

    public void hideForceFully() {
        try {
            if (audioRecorder != null) {
                audioRecorder.stopRecording(true);
            }

            while (audioRecorder != null && audioRecorder.THREAD_RUNNING) {
                System.err.println("WAITING FOR STOP AUDIO RECORDING");
                Thread.sleep(100);
            }
            hideWindows();
        } catch (Exception e) {
        }
    }

    public void hideWindows() {
        this.setAlwaysOnTop(false);
        this.toBack();
        this.setVisible(false);
        instance = null;
    }

    private TimerListener timerListener = new TimerListener() {
        @Override
        public void timerLimitExit() {
            btnStopRecord.doClick();
        }
    };

    public static void main(String[] args) {
        AudioRecorderWindow window = new AudioRecorderWindow("AAA");
        window.showRecodingVoiceChat();
    }

}
