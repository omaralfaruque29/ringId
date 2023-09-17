/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryService;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.ipv.chat.ChatConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.chat.ChatFileShareProcessor;
import com.ipvision.recorder.RecordingTimer;
import com.ipvision.recorder.TimerListener;
import com.ipvision.recorder.VideoRecorder;
import com.ipvision.view.utility.chat.ProgressRectangleUI;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.audio.AudioController;

/**
 *
 * @author Shahadat Hossain
 */
public class VideoRecorderWindow extends JWindow {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VideoRecorderWindow.class);
    DownLoaderHelps helps = new DownLoaderHelps();
    public static final int TYPE_FRIEND_VIDEO_RECORD = 1;
    public static final int TYPE_GROUP_VIDEO_RECORD = 2;
    private final String DEFAULT_COUNTER_TEXT = "00:00:00";
    public final int MAX_LENGTH = 10; //Seconds
    private int posX = 0;
    private int posY = 0;

    public static VideoRecorderWindow instance;
    private JPanel centerPanel;
    private JLabel friendFullNameLable;
    private JLabel lblDefaultWebCam = new JLabel(DesignClasses.return_image(GetImages.WEBCAM_LOGO));

    private JButton btnCloseWindow = DesignClasses.createImageButton(GetImages.NOTIFICATION_DELETE, GetImages.NOTIFICATION_DELETE_H, GetImages.NOTIFICATION_DELETE_H, "Cancel Recording");
    private JButton btnStartRecord = DesignClasses.createImageButton(GetImages.RECORD_START, GetImages.RECORD_START_H, "Start");
    private JButton btnPauseRecord = DesignClasses.createImageButton(GetImages.RECORD_PAUSE, GetImages.RECORD_PAUSE_H, "Pause");
    private JButton btnResumeRecord = DesignClasses.createImageButton(GetImages.RECORD_RESUME, GetImages.RECORD_RESUME_H, "Resume");
    private JButton btnStopRecord = DesignClasses.createImageButton(GetImages.RECORD_STOP, GetImages.RECORD_STOP_H, "Stop");
    private JButton btnSendRecord = DesignClasses.createImageButton(GetImages.RECORD_SEND, GetImages.RECORD_SEND_H, "Send");
    private JCheckBox chkIncludeVoice = new JCheckBox("Include Voice");
    private JLabel timerLabel;
    private JProgressBar timerProgressBar;

    private String userIdentity;
    private Long groupId;
    private VideoRecorder vedioRecorder;
    public RecordingTimer recordingTimer;

    public String fileName;
    public Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;
    public int camWidth = DefaultSettings.WEBCAM_CAPTURE_WIDTH;
    public int camHeight = DefaultSettings.WEBCAM_CAPTURE_HEIGHT;

    public VideoRecorderWindow(Long groupId) {
        instance = this;
        this.groupId = groupId;
        this.initContents();
    }

    public VideoRecorderWindow(String usreIdentity) {
        instance = this;
        this.userIdentity = usreIdentity;
        this.initContents();
    }

    private void initContents() {
        this.setSize(340, 329);
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
        content.setLayout(new BorderLayout(0, 7));
        content.addMouseListener(poxValueListener);
        content.addMouseMotionListener(frameDragListener);
        contentWrapperPanel.add(content, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 15));
        headerPanel.setOpaque(false);
        content.add(headerPanel, BorderLayout.NORTH);

        friendFullNameLable = DesignClasses.makeLableBold1("Shahadat Hossain", 15, Font.BOLD);
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
        headerPanel.add(friendFullNameLable, BorderLayout.WEST);
        headerPanel.add(btnCloseWindow, BorderLayout.EAST);

        centerPanel = new JPanel(new GridLayout());
        centerPanel.setBackground(new Color(0x333333));
        centerPanel.add(lblDefaultWebCam);
        content.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout(10, 0));
        footerPanel.setPreferredSize(new Dimension(0, 40));
        footerPanel.setOpaque(false);
        content.add(footerPanel, BorderLayout.SOUTH);

        JPanel timerProgressLabelPanel = new JPanel();
        timerProgressLabelPanel.setLayout(new BoxLayout(timerProgressLabelPanel, BoxLayout.Y_AXIS));
        timerProgressLabelPanel.setPreferredSize(new Dimension(100, 0));
        timerProgressLabelPanel.setOpaque(false);
        footerPanel.add(timerProgressLabelPanel, BorderLayout.WEST);

        timerProgressLabelPanel.add(Box.createVerticalStrut(4));
        timerProgressBar = new JProgressBar() {
            @Override
            public void updateUI() {
                super.updateUI();
                setUI(new ProgressRectangleUI());
                setBorder(null);
            }
        };
        timerProgressBar.setPreferredSize(new Dimension(105, 18));
        timerProgressBar.setForeground(RingColorCode.RING_THEME_COLOR);
        timerProgressBar.setOpaque(false);
        timerProgressLabelPanel.add(timerProgressBar);
        timerProgressLabelPanel.add(Box.createVerticalStrut(4));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timerPanel.setOpaque(false);
        timerLabel = new JLabel(DEFAULT_COUNTER_TEXT);
        timerLabel.setPreferredSize(new Dimension(120, 14));
        timerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        timerLabel.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        timerPanel.add(timerLabel);
        timerProgressLabelPanel.add(timerPanel);

        JPanel recordPanel = new JPanel(new BorderLayout(10, 0));
        recordPanel.setOpaque(false);
        footerPanel.add(recordPanel, BorderLayout.CENTER);

        JPanel chkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chkBoxPanel.setOpaque(false);
        chkBoxPanel.setPreferredSize(new Dimension(105, 0));
        recordPanel.add(chkBoxPanel, BorderLayout.WEST);

        chkIncludeVoice.setOpaque(false);
        chkIncludeVoice.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        chkIncludeVoice.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        chkIncludeVoice.setIcon(DesignClasses.return_image(GetImages.INCLUDE_VOICE));
        chkIncludeVoice.setRolloverIcon(DesignClasses.return_image(GetImages.INCLUDE_VOICE_H));
        chkIncludeVoice.setSelectedIcon(DesignClasses.return_image(GetImages.INCLUDE_VOICE_SELECTED));
        chkIncludeVoice.setSelected(true);
        chkBoxPanel.add(chkIncludeVoice);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        recordPanel.add(buttonPanel, BorderLayout.CENTER);

        buttonPanel.add(btnStartRecord);
        buttonPanel.add(btnPauseRecord);
        buttonPanel.add(btnResumeRecord);
        buttonPanel.add(btnStopRecord);
        buttonPanel.add(btnSendRecord);
        btnStartRecord.setVisible(false);
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
        chkIncludeVoice.addActionListener(actionListener);
    }

    public void initWebCam() {
        picker = new WebcamPicker();
        webcam = picker.getSelectedWebcam();
        if (webcam != null) {
            webcam.setViewSize(new Dimension(camWidth, camHeight));
            webcam.addWebcamListener(webcamListener);
            panel = new WebcamPanel(webcam, false);
            panel.setFPSDisplayed(false);

            Thread t = new Thread() {
                @Override
                public void run() {
                    recordingTimer = new RecordingTimer(timerLabel, timerProgressBar, timerListener);
                    recordingTimer.setLimit(MAX_LENGTH);
                    AudioController.setMasterOutputMute(!chkIncludeVoice.isSelected());
                    panel.start();
                    centerPanel.removeAll();
                    centerPanel.add(panel);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                }
            };
            t.setName("Open Webcam");
            t.setDaemon(true);
            t.start();
        } else {
            hideWindows();
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

    private WebcamListener webcamListener = new WebcamListener() {
        @Override
        public void webcamOpen(WebcamEvent we) {
            btnStartRecord.setVisible(true);
            btnCloseWindow.setEnabled(true);
        }

        @Override
        public void webcamClosed(WebcamEvent we) {
        }

        @Override
        public void webcamDisposed(WebcamEvent we) {
        }

        @Override
        public void webcamImageObtained(WebcamEvent we) {
        }
    };

    public void showRecodingVideoChat() {
        this.setAlwaysOnTop(true);
        this.toFront();
        this.setVisible(true);
        this.initWebCam();
    }

    public void sendChatVedioFile(File file) {
        if (userIdentity != null && userIdentity.trim().length() > 0) {
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);
            if (friendChatCallPanel != null) {
                new ChatFileShareProcessor(file, friendChatCallPanel, ChatConstants.TYPE_VIDEO_FILE, recordingTimer.getCount()).start();
            }
        } else if (groupId != null && groupId > 0) {
            GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
            if (groupPanel != null) {
                new ChatFileShareProcessor(file, groupPanel, ChatConstants.TYPE_VIDEO_FILE, recordingTimer.getCount()).start();
            }
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnStartRecord) {
                if (panel.isStarted() == false) {
                    panel.start();
                    while (panel.isStarted() == false) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                        }
                    }
                }
                fileName = MyFnFSettings.LOGIN_USER_ID + "_" + System.currentTimeMillis() + ".mp4";
                btnStartRecord.setVisible(false);
                btnSendRecord.setVisible(false);
                        
                vedioRecorder = new VideoRecorder(instance, new InitListener() {
                    @Override
                    public void onInit() {
                        btnStartRecord.setVisible(false);
                        btnPauseRecord.setVisible(true);
                        btnResumeRecord.setVisible(false);
                        btnStopRecord.setVisible(true);
                        btnSendRecord.setVisible(false);
                    }
                });
                vedioRecorder.start();

            } else if (e.getSource() == btnPauseRecord) {
                recordingTimer.pause();
                panel.pause();
                btnStartRecord.setVisible(false);
                btnPauseRecord.setVisible(false);
                btnResumeRecord.setVisible(true);
                btnStopRecord.setVisible(true);
                btnSendRecord.setVisible(false);
            } else if (e.getSource() == btnResumeRecord) {
                recordingTimer.resume();
                panel.resume();
                btnStartRecord.setVisible(false);
                btnPauseRecord.setVisible(true);
                btnResumeRecord.setVisible(false);
                btnStopRecord.setVisible(true);
                btnSendRecord.setVisible(false);
            } else if (e.getSource() == btnStopRecord) {
                vedioRecorder.stopRecording(false);
                btnStartRecord.setToolTipText("Restart");
                btnStartRecord.setVisible(true);
                btnPauseRecord.setVisible(false);
                btnResumeRecord.setVisible(false);
                btnStopRecord.setVisible(false);
                btnSendRecord.setVisible(true);
            } else if (e.getSource() == btnSendRecord) {
                File file = new File(helps.getChatFileDestinationFolder() + "/" + fileName);
                if (file.exists()) {
                    sendChatVedioFile(file);
                }
                hideWindows();
            } else if (e.getSource() == btnCloseWindow) {
                hideForceFully();
            } else if (e.getSource() == chkIncludeVoice) {
                AudioController.setMasterOutputMute(!chkIncludeVoice.isSelected());
            }
        }
    };

    public void hideForceFully() {
        try {
            if (vedioRecorder != null) {
                vedioRecorder.stopRecording(true);
            } else {
                if (webcam != null) {
                    WebcamDiscoveryService discovery = Webcam.getDiscoveryService();
                    discovery.stop();
                    webcam.close();
                }
            }

            while (vedioRecorder != null && vedioRecorder.THREAD_RUNNING) {
                System.err.println("WAITING FOR STOP VIDEO RECORDING");
                Thread.sleep(100);
            }
            hideWindows();
        } catch (Exception e) {
        }
    }

    public void hideWindows() {
        setAlwaysOnTop(false);
        toBack();
        setVisible(false);
        instance = null;
    }

    private TimerListener timerListener = new TimerListener() {
        @Override
        public void timerLimitExit() {
            btnStopRecord.doClick();
        }
    };

    public static void main(String[] args) {
        VideoRecorderWindow window = new VideoRecorderWindow("");
        window.showRecodingVideoChat();
    }

    public interface InitListener {
        public void onInit();
    }

}
