/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.call;

import com.ipvision.view.utility.call.CallUIHelpers;
import com.desktopCall.dtos.CallSettingsDTo;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.net.CallStates;
import com.ipvision.audio.PlayAudioHelper;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.service.utility.call.CallDurationHelper2;
import com.ipvision.service.call.CallRegisterRequest;
import com.ipvision.service.call.NetworkStrength;
import com.ipvision.service.utility.call.TopLabelWrapper;
import com.ipvision.audio.Volume;
import com.ipvision.model.call.CallLogHelper;
import com.ipvision.view.utility.chat.ChatHelpers;

import com.ipvision.audio.AudioController;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ProfilePicBigPanel;

/**
 *
 * @author Faiz
 */
public class MainClass extends JFrame implements ActionListener {

    public JPanel main_contentPane;
    public Object actionSource;
    public Object focusedSource;
    /**/
    private JLabel friendFullNameLable;

    public int incommingCall;

    CallSettingsDTo friendProfileInfo;
    JLabel profilePictureLabel;// = new JLabel();
    /*buttons**/
    private JButton callAccept;
    public JButton callIgnore;
    private JButton muteButton;
    private JButton articulateButton;
    private JButton callEnd;
    private JButton callHold;
    private JButton callUnhold;
    private JButton volumeButton;
    private JButton videoButton;
    public Volume vol;

    private JPanel mainContents;
    public CallDurationHelper2 callDurationHelper;
    public static PlayAudioHelper playAudioHelper;
    int width = 310;
    int i = 1;
    private JButton messageButton;
    private JLabel label_with_animated_image;
    /*main 5 panels*/
    private JPanel mainNamePanel;
    private JPanel durationPanel;
    private JPanel profilePicPanel;
    private JPanel buttonsPanel;
    private JPanel netWorkStrengthPanel;
    // JPanel endButtonPanel;
    /**/
    public JLabel durationLabel;
    public JLabel networkStrengthLabel;
    private static MainClass mainClass;

    public static MainClass getMainClass() {
        return mainClass;
    }

    private void setMainClass() {
        mainClass = this;
    }

    public MainClass(CallSettingsDTo callSettingsDTo) {
        ConfigFile.systemPrint = true;
        ConfigFile.noUI = false;
        this.friendProfileInfo = callSettingsDTo;
        this.incommingCall = callSettingsDTo.getIncomming();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(350, 480);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setMainClass();
        addWindowListener(
                new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        HelperMethods.showConfirmationDialogMessage(NotificationMessages.CALL_TERMINATE);
                        if (JOptionPanelBasics.YES_NO) {
                            TopLabelWrapper.cancelButtonAction(incommingCall > 0);
                        }
                    }
                });
        try {
            BufferedImage bi = DesignClasses.return_buffer_image(GetImages.APP_LOGO);
            Image img3 = new ImageIcon(bi).getImage();
            Image img1 = new ImageIcon(DesignClasses.return_buffer_image(GetImages.APP_LOGO_16)).getImage();
            Image img2 = new ImageIcon(DesignClasses.return_buffer_image(GetImages.APP_LOGO_32)).getImage();
            List<Image> icons = new ArrayList<>();
            icons.add(img3);
            icons.add(img1);
            icons.add(img2);
            setIconImages(icons);
        } catch (Exception e) {
        }
        main_contentPane = (JPanel) getContentPane();
        main_contentPane.setBackground(Color.WHITE);
        main_contentPane.setLayout(new BorderLayout());

        makeStructure();
        initGlobalComponents();
        initAudioPlayer();
        addDefaultComponents();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                addProfileImageAndName();
                callDurationHelper = new CallDurationHelper2();
                if (incommingCall > 0) {
                    setTitle("Incomming...");
                    addButtonsWhileIncomming();
                    executor = Executors.newSingleThreadScheduledExecutor();
                    executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);
                    TopLabelWrapper.sendReg();
                } else {
                    setTitle("Calling...");
                    addButtonsWhileAcceptedOrOutGoing();
                    durationLabel.setIcon(DesignClasses.return_image(CallUIHelpers.OUT_GOING_ANIMATIONL));
                    startRegisterRequest();
                }
            }
        });
    }

    private void initAudioPlayer() {
        if (playAudioHelper == null) {
            playAudioHelper = new PlayAudioHelper();
            playAudioHelper.init();
            playAudioHelper.initHoldSound();
        }
    }

    private void makeStructure() {

        /*UI Panel */
        mainContents = new JPanel();
        mainContents.setBackground(RingColorCode.CALL_WINDOW_GB);
        mainContents.setLayout(new GridBagLayout());
        setBackground(Color.yellow);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.insets = new Insets(3, 0, 0, 0);
        mainNamePanel = new JPanel(new GridBagLayout());
        mainNamePanel.setPreferredSize(new Dimension(width, 30));
        mainContents.add(mainNamePanel, cons);
        mainNamePanel.setOpaque(false);
        cons.gridy++;
        durationPanel = new JPanel(new GridBagLayout());
        durationPanel.setPreferredSize(new Dimension(width, 31));
        durationPanel.setOpaque(false);
        mainContents.add(durationPanel, cons);
        cons.gridy++;

        netWorkStrengthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        netWorkStrengthPanel.setOpaque(false);
        netWorkStrengthPanel.setVisible(false);
        mainContents.add(netWorkStrengthPanel, cons);
        cons.gridy++;
        profilePicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        profilePicPanel.setOpaque(false);
        mainContents.add(profilePicPanel, cons);
        cons.gridy++;
        buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        mainContents.add(buttonsPanel, cons);

        main_contentPane.add(mainContents, BorderLayout.CENTER);
        JPanel bottomPanel2 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(DefaultSettings.ORANGE_BACKGROUND_COLOR);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                QuadCurve2D q = new QuadCurve2D.Double();
                q.setCurve(
                        -10, 10,
                        150, -10,
                        310, 32
                );
                g2d.fill(q);

                Polygon poly = new Polygon(
                        new int[]{0, (int) 310, 0},
                        new int[]{10, 30, 30},
                        3);
                g2d.fill(poly);

            }
        };
        bottomPanel2.setPreferredSize(new Dimension(0, 30));
        bottomPanel2.setBackground(RingColorCode.CALL_WINDOW_GB);
        main_contentPane.add(bottomPanel2, BorderLayout.SOUTH);
    }

    private void initGlobalComponents() {
        friendFullNameLable = DesignClasses.makeLableBold1("Unknown friend", 16, Font.BOLD);
        durationLabel = DesignClasses.makeAncorLabelSystemFontDefaultColor("", Font.BOLD, 18);
        durationLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        //networkStrengthLabel = DesignClasses.makeLableBold1("Checking..", 11, Font.BOLD);
        networkStrengthLabel = new JLabel("Good");
        profilePictureLabel = new JLabel();
        profilePictureLabel.setIcon(DesignClasses.return_image(GetImages.UNKNOW_IMAGE));

        durationLabel.setBackground(new Color(0, 0, 0, 0));
        initButtons();
    }

    private void initButtons() {
        try {
            messageButton = DesignClasses.createImageButton(CallUIHelpers.Message_Button, CallUIHelpers.Message_Button_H, "Call Ignore with Message");
            messageButton.addActionListener(this);
            callIgnore = DesignClasses.createImageButton(CallUIHelpers.CALL_IGNORE, CallUIHelpers.CALL_IGNORE_H, "Call Ignore");
            callIgnore.addActionListener(this);
            label_with_animated_image = DesignClasses.create_image_label_with_preferredSize(108, 108, CallUIHelpers.Animated_Label_Image);
            callAccept = DesignClasses.createImageButton(CallUIHelpers.BUTTON_CALL_RECEIVE, CallUIHelpers.BUTTON_CALL_RECEIVE_H, "Accept call");
            callAccept.addActionListener(this);
            callHold = DesignClasses.createImageButton(CallUIHelpers.BUTTON_CALL_HOLD, CallUIHelpers.BUTTON_CALL_HOLD_H, "Hold");
            callHold.addActionListener(this);
            callUnhold = DesignClasses.createImageButton(CallUIHelpers.BUTTON_CALL_UNHOLD, CallUIHelpers.BUTTON_CALL_UNHOLD_H, "Continue call");
            callUnhold.addActionListener(this);

            //    label_Out_Animated = DesignClasses.create_image_label_with_preferredSize(150, 35, CallUIHelpers.Outgoing_Animated_Call);
            muteButton = DesignClasses.createImageButton(CallUIHelpers.Mute_Button, CallUIHelpers.Mute_Button_H, "");
            muteButton.addActionListener(this);
            articulateButton = DesignClasses.createImageButton(CallUIHelpers.BUTTON_UNMUTE_MINI, CallUIHelpers.BUTTON_UNMUTE_MINI_H, "Articulate");
            articulateButton.addActionListener(this);
            volumeButton = DesignClasses.createImageButton(CallUIHelpers.Volume_Button, CallUIHelpers.Volume_Button_H, "");
            volumeButton.addActionListener(this);

            videoButton = DesignClasses.createImageButton(CallUIHelpers.Button_Video, CallUIHelpers.Button_Video_H, "");
            videoButton.addActionListener(this);
            callEnd = DesignClasses.createImageButton(CallUIHelpers.Call_End_Button, CallUIHelpers.Call_End_Button_H, "Call end");
            callEnd.addActionListener(this);
            AudioController.setDefaultVolume();
        } catch (Exception e) {
        }

    }
    private JPanel muteArticulateWrapper;
    private JPanel holdUnholdWrapper;

    private JPanel muteWrapper;
    private JPanel articulateWrapper;
    private JPanel holdWrapper;
    private JPanel unholdWrapper;
    private JPanel volumeButtonWrapper;
    private JPanel videoButtonWrapper;

    private JLabel createButtonText(String text) {
        return DesignClasses.makeJLabelButtonText(text);
    }

    private JPanel createMiddleButtonPanel() {
        JPanel midButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));//.removeAll();
        midButtons.setPreferredSize(new Dimension(width, 60));
        midButtons.setOpaque(false);
        JLabel holdLabel = createButtonText("Hold");
        JLabel unholdLabel = createButtonText("Unhold");
        JLabel muteLabel = createButtonText("Mute");
        JLabel unmuteLabel = createButtonText("Unmute");
        JLabel volumeLabel = createButtonText("Volume");
        JLabel videoLabel = createButtonText("Video");
        holdUnholdWrapper = new JPanel(new BorderLayout());
        holdUnholdWrapper.setOpaque(false);
        holdUnholdWrapper.setPreferredSize(new Dimension(50, 55));
        midButtons.add(holdUnholdWrapper);
        holdWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        holdWrapper.setOpaque(false);
        holdWrapper.add(callHold);
        holdWrapper.add(holdLabel);
        holdUnholdWrapper.add(holdWrapper);
        holdUnholdWrapper.setVisible(false);

        unholdWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        unholdWrapper.setOpaque(false);
        unholdWrapper.add(callUnhold);
        unholdWrapper.add(unholdLabel);

        muteArticulateWrapper = new JPanel(new BorderLayout());
        muteArticulateWrapper.setOpaque(false);
        muteArticulateWrapper.setPreferredSize(new Dimension(50, 55));

        muteWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        muteWrapper.setOpaque(false);
        muteWrapper.add(muteButton);
        muteWrapper.add(muteLabel);

        articulateWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        articulateWrapper.setOpaque(false);
        articulateWrapper.add(articulateButton);
        articulateWrapper.add(unmuteLabel);

        if (AudioController.getMasterOutputMute() != null) {
            if (AudioController.getMasterOutputMute()) {
                muteArticulateWrapper.add(articulateWrapper);
            } else {
                muteArticulateWrapper.add(muteWrapper);
            }
        }
        midButtons.add(muteArticulateWrapper);

        volumeButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        volumeButtonWrapper.setOpaque(false);
        volumeButtonWrapper.setPreferredSize(new Dimension(50, 55));
        volumeButtonWrapper.add(volumeButton);
        volumeButtonWrapper.add(volumeLabel);
        midButtons.add(volumeButtonWrapper);

        videoButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        videoButtonWrapper.setOpaque(false);
        videoButtonWrapper.setPreferredSize(new Dimension(50, 55));
        videoButtonWrapper.add(videoButton);
        videoButtonWrapper.add(videoLabel);
        midButtons.add(videoButtonWrapper);
        return midButtons;
    }

    private void addButtonsWhileAcceptedOrOutGoing() {
        buttonsPanel.removeAll();
        buttonsPanel.setPreferredSize(new Dimension(width, 120));
        JPanel tempPanel1 = new JPanel(new GridBagLayout());
        tempPanel1.setOpaque(false);
        JPanel temppJPanel2 = new JPanel(new GridBagLayout());
        temppJPanel2.setOpaque(false);
        GridBagConstraints consCancell = new GridBagConstraints();
        consCancell.insets = new Insets(10, 0, 0, 0);
        temppJPanel2.add(callEnd, consCancell);

        GridBagConstraints consoutBottm = new GridBagConstraints();
        consoutBottm.gridx = 0;
        consoutBottm.gridy = 0;
        tempPanel1.add(createMiddleButtonPanel(), consoutBottm);
        consoutBottm.gridy++;
        tempPanel1.add(temppJPanel2, consoutBottm);
        buttonsPanel.add(tempPanel1);
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void addButtonsWhileIncomming() {
        buttonsPanel.removeAll();
        buttonsPanel.setPreferredSize(new Dimension(width, 170));
        JPanel tempPanelIncoming1 = new JPanel(new GridBagLayout());
        tempPanelIncoming1.setOpaque(false);
        JPanel incomingMessagePanel = new JPanel(new GridBagLayout());
        incomingMessagePanel.setOpaque(false);
        incomingMessagePanel.add(messageButton);

        JPanel incomingBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        incomingBottomPanel.setOpaque(false);
        incomingBottomPanel.add(callIgnore);
        incomingBottomPanel.add(label_with_animated_image);
        incomingBottomPanel.add(callAccept);
        GridBagConstraints incCons = new GridBagConstraints();
        incCons.gridx = 0;
        incCons.gridy = 0;
        tempPanelIncoming1.add(incomingMessagePanel, incCons);
        incCons.gridy++;
        tempPanelIncoming1.add(incomingBottomPanel, incCons);
        buttonsPanel.add(tempPanelIncoming1);
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void addDefaultComponents() {
        mainNamePanel.add(friendFullNameLable);
        durationPanel.add(durationLabel);
        JLabel networkText = DesignClasses.makeLableBold1("Network : ", 11, Font.BOLD);
        netWorkStrengthPanel.add(networkText);
        netWorkStrengthPanel.add(networkStrengthLabel);
        ProfilePicBigPanel profilePicBigPanel = new ProfilePicBigPanel(profilePictureLabel, true);
        profilePicPanel.add(profilePicBigPanel);
        profilePicPanel.revalidate();
    }

    private void addProfileImageAndName() {
        if (this.friendProfileInfo != null) {
            friendFullNameLable.setText(this.friendProfileInfo.getFn());
            if (friendProfileInfo.getPrIm() != null && friendProfileInfo.getPrIm().trim().length() > 4) {
                ImageHelpers.addProfileImageThumb(profilePictureLabel, this.friendProfileInfo.getPrIm(), DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, true);
            }
        }
    }

    public void showNotification() {
        this.setAlwaysOnTop(false);
        this.toFront();
        this.setVisible(true);
    }

    private void startRegisterRequest() {
        if (friendProfileInfo != null) {
            new CallRegisterRequest(friendProfileInfo.getFndId(), durationLabel).start();
        }
    }

    public void hideAndDeleteObject() {
        if (executor != null) {
            executor.shutdown();
        }
        this.dispose();
        mainClass = null;
    }

    public void showWindow() {
        this.setVisible(true);
        this.setAlwaysOnTop(false);
        this.toFront();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == muteButton) {
            muteArticulateWrapper.removeAll();
            muteArticulateWrapper.add(articulateWrapper);
            muteArticulateWrapper.revalidate();
            muteArticulateWrapper.repaint();
            AudioController.setMasterOutputMute(true);
        } else if (e.getSource() == articulateButton) {
            muteArticulateWrapper.removeAll();
            muteArticulateWrapper.add(muteWrapper);
            muteArticulateWrapper.revalidate();
            muteArticulateWrapper.repaint();
            AudioController.setMasterOutputMute(false);
        } else if (e.getSource() == callEnd) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    TopLabelWrapper.cancelButtonAction(incommingCall > 0);
                    playClipOffSound();
                }
            });
        } else if (e.getSource() == volumeButton) {
            if (vol != null) {
                vol.setVisible(false);
                vol = null;
            } else {
                vol = new Volume(volumeButton);
                vol.setVisible(true);
            }
        } else if (e.getSource() == callIgnore) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    playClipOffSound();
                    TopLabelWrapper.cancelButtonAction(incommingCall > 0);
                }
            });

        } else if (e.getSource() == callAccept) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    callAccept.setEnabled(false);
                    addButtonsWhileAcceptedOrOutGoing();
                    try {
                        if (executor != null) {
                            executor.shutdown();
                        }
                        TopLabelWrapper.answerButtonAction();
                        stopRinging();
                    } catch (Exception e3) {
                    }
                }
            });
        } else if (e.getSource() == callHold) {
            if (CallStates.getStatus() != CallStates.HOLD_CALL) {
                CallStates.changeStatus(CallStates.HOLD_CALL);
                holdUnholdWrapper.removeAll();
                holdUnholdWrapper.add(unholdWrapper);
                holdUnholdWrapper.revalidate();
                holdUnholdWrapper.repaint();
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        startHoldtune();
                        TopLabelWrapper.holdButtonAction();
                    }
                });
            }
        } else if (e.getSource() == callUnhold) {
            if (CallStates.getStatus() == CallStates.HOLD_CALL) {
                CallStates.changeStatus(CallStates.UA_ONCALL);
                holdUnholdWrapper.removeAll();
                holdUnholdWrapper.add(holdWrapper);
                holdUnholdWrapper.revalidate();
                holdUnholdWrapper.repaint();
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopHoldTune();
                        TopLabelWrapper.unHoldButtonAction();
                    }
                });

            }
        } else if (e.getSource() == messageButton) {
            ChatHelpers.startFriendChat(friendProfileInfo.getFndId());
            InstantMessagesJDialog instantMessagesJDialog = new InstantMessagesJDialog(friendProfileInfo.getFndId());
            instantMessagesJDialog.setVisible(messageButton);
        }
    }

    public void showHoldUnholdPanel(boolean show) {
        if (AudioController.getMasterOutputMute() != null) {
            holdUnholdWrapper.setVisible(show);
        }
    }

    public void showAcceptButton(boolean show) {
        callAccept.setVisible(show);
    }

    public void callConnected(boolean outgoing) {

        durationLabel.setIcon(null);
        durationLabel.setText("00:00:00");
        CallStates.changeStatus(CallStates.UA_ONCALL);
        callDurationHelper.setStopDurationCounting(false);
        callDurationHelper.reset();
        callDurationHelper.callDurationChecker();
        netWorkStrengthPanel.setVisible(true);
        (new NetworkStrength(networkStrengthLabel)).start();
        TopLabelWrapper.callConnectedAction();
        try {
            CallLogHelper.callStartTimeToCallLog(ConfigFile.CALL_ID, ConfigFile.FRIEND_ID, outgoing);
        } catch (Exception e) {
        }

    }
    /*duration label change*/
    /*incoming call*/
    public ScheduledExecutorService executor;// = Executors.newSingleThreadScheduledExecutor();
    public Runnable periodicTask = new Runnable() {
        private int counterVariable = 1;

        @Override
        public void run() {
            if (MainClass.getMainClass() == null) {
                executor.shutdown();
                counterVariable = 1;
            }
            if (CallStates.getStatus() != CallStates.UA_INCOMING_CALL) {
                try {
                    executor.shutdown();
                    counterVariable = 1;
                } catch (Exception e) {
                }
            }
            if (counterVariable > 30) {
                executor.shutdown();
                TopLabelWrapper.noResponse(incommingCall > 0);
                stopRinging();
            }
            counterVariable++;
        }
    };

    public void stopRinging() {
        if (playAudioHelper != null && playAudioHelper.clip_ring != null) {
            playAudioHelper.clip_ring.stop();
        }
        stopPrgress();
    }

    public void stopPrgress() {
        if (playAudioHelper != null && playAudioHelper.clip_progress != null) {
            playAudioHelper.clip_progress.stop();
        }
    }

    public void startHoldtune() {
        if (playAudioHelper != null && playAudioHelper.hold_tune != null) {
            playAudioHelper.hold_tune.play();
        }
    }

    public void stopHoldTune() {
        if (playAudioHelper != null && playAudioHelper.hold_tune != null) {
            playAudioHelper.hold_tune.stop();
        }
    }

    public void playClipOffSound() {
        if (playAudioHelper != null && playAudioHelper.clip_off != null) {
            playAudioHelper.clip_off.play();
        }
    }
//

    public static void initCallServices(CallSettingsDTo callSettingsDTo) {
        MainClass mainClass2 = new MainClass(callSettingsDTo);
        mainClass2.showWindow();
    }

//    public static void main(String[] args) {
//        String separator = "__~@~__";
//        //    String argmnts = "callid" + separator + "userid" + separator + "friendid" + separator + "friendFullname" + separator + "ip" + separator + "port" + separator+ "incomming(1)/outgoing(0)" + separator + "imageUrl";
//        //3111__b__a__Faiz ahmed__38.127.68.57__1250__1__https://mojosimon.files.wordpress.com/2011/12/large-company.jpg?w=600
//        CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
//        callSettingsDTo.setDvc(1);
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//        }
//
//        try {
//
//            callSettingsDTo.setCallID("565658");
//            callSettingsDTo.setUserid("a");
//            callSettingsDTo.setFndId("b");
//            callSettingsDTo.setFn("ffffffffffffffffff");
//            // callSettingsDTo.setSwIp();
//            callSettingsDTo.setSwPr(1250);
//            callSettingsDTo.setIncomming(0);
//
//            initCallServices(callSettingsDTo);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Exception Invalid arguments");
//            System.exit(0);
//        }
//
//    }
}
