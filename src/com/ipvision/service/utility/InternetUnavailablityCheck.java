/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.AppConstants;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.model.constants.StatusConstants;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.singinsignup.SignInRequestBackground;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.service.auth.BreakingPacketData;
import com.ipvision.view.utility.BreakingPacketRepository;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class InternetUnavailablityCheck {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InternetUnavailablityCheck.class);
    private static Boolean prevStatus = null;
    public static boolean isInternetAvailable = true;
    private static ScheduledExecutorService exec;
    private static final int INIT_DELAY = 0;
    private static final int PERIOD = 30;

    public static boolean isInternetAvailable() {
        boolean var = false;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface interf = interfaces.nextElement();
                if (interf.isUp() && !interf.isLoopback()) {
                    List<InterfaceAddress> adrs = interf.getInterfaceAddresses();
                    for (Iterator<InterfaceAddress> iter = adrs.iterator(); iter.hasNext();) {
                        InterfaceAddress adr = iter.next();
                        InetAddress inadr = adr.getAddress();
                        if (inadr instanceof Inet4Address) {
                            var = true;
                        }
                    }
                }
            }

            if (var) {
                try {
                    URL url = new URL(ServerAndPortSettings.getPingURL());
                    URLConnection conn = url.openConnection();
                    conn.connect();
                } catch (MalformedURLException e) {
                    var = false;
                    return false;
                } catch (IOException e) {
                    var = false;
                    return false;
                }
            }
        } catch (SocketException ex) {
            var = false;
            return false;
        }
        return var;
    }

    public static void startInternetUnavailablityCheck() {
        log.debug("Start Internet Unavailablity Checker");
        prevStatus = null;
        isInternetAvailable = true;
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                isInternetAvailable = InternetUnavailablityCheck.isInternetAvailable();
                if (!isInternetAvailable) {
                    if (prevStatus != null && (prevStatus != isInternetAvailable)) {
                        log.debug("No Available Internet Connection!");
                        onInternetUnavailable();
                        repaintUI();
                    }
                } else {
                    boolean newConnection = false;
                    if (prevStatus != null && (prevStatus != isInternetAvailable)) {
                        log.debug("Internet Connection Available.");
                        newConnection = true;
                        onInternetAvailable();
                        repaintUI();
                    }
                    login(newConnection);
                }
                clearBrokenPacket();
                prevStatus = isInternetAvailable;
            }
        }, INIT_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    public static void onInternetAvailable() {
        MyFnFSettings.userProfile.setPresence(MyFnFSettings.USER_PREV_STATUS);
        GuiRingID.getInstance().getMyNamePanel().changeStatusIcon();
    }

    public static void onInternetUnavailable() {
        ChatService.unregisterAllChatConversation(ChatConstants.PRESENCE_OFFLINE);
        MyFnFSettings.USER_PREV_STATUS = MyFnFSettings.userProfile.getPresence();
        MyFnFSettings.userProfile.setPresence(StatusConstants.PRESENCE_OFFLINE);
        GuiRingID.getInstance().getMyNamePanel().changeStatusIcon();
    }

    public static void clearBrokenPacket() {
        try {
            Set<Entry<Integer, BreakingPacketData>> entrySet = BreakingPacketRepository.getInstance().getBreakingPackets().entrySet();
            for (Entry<Integer, BreakingPacketData> packet : entrySet) {
                if (packet.getValue().getTime() < System.currentTimeMillis() - AppConstants.FIVE_MINUTES) {
                    BreakingPacketRepository.getInstance().removeBreakingPacketList(packet.getKey());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void login(boolean newConnection) {
        if (((newConnection == true && MyFnFSettings.LOGIN_SESSIONID == null)
                || (MyFnFSettings.COMMUNICATION_SERVER_STATUS == false && MyFnFSettings.LOGIN_SESSIONID == null))
                && GuiRingID.getInstance() != null
                && MyFnFSettings.VALUE_LOGIN_USER_NAME != null
                && MyFnFSettings.VALUE_LOGIN_USER_NAME.length() > 0
                && MyFnFSettings.VALUE_LOGIN_USER_PASSWORD != null
                && MyFnFSettings.VALUE_LOGIN_USER_PASSWORD.length() > 0) {
            log.debug("Internet Connection Available. ==> Send SignInRequest in Background");
            SignInRequestBackground signIn = new SignInRequestBackground();
            signIn.startService();
        } else if (newConnection == true) {
            log.debug("Internet Connection Available. ==> Resend Pending Chat & Fetch Offline Chat");
            RecentChatCallActivityDAO.fetchAndResendPendingChat();
            ChatService.sendOfflineChatRequest();
        }
    }

    public static void repaintUI() {
        if (isFriendListExist()) {
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().repaint();
        }
        if (isCallHistoryExist()) {
            GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().repaint();
        }
        if (isChatHistoryExist()) {
            GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().repaint();
        }
    }

    public static void endInternetUnavailablityCheck() {
        try {
            exec.shutdown();
        } catch (Exception ex) {
        }
    }

    private static boolean isFriendListExist() {
        return GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null;
    }

    private static boolean isCallHistoryExist() {
        return GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer() != null;
    }

    private static boolean isChatHistoryExist() {
        return GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null;
    }

}
