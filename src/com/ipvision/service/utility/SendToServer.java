/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.ipvision.service.auth.AuthenticationReceiverMain;
import com.google.gson.Gson;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.auth.SendDevidedPacket;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.FeedUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author user
 */
public class SendToServer {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendToServer.class);

    public static void sendConfirmationPak(String packetIDfromServer, int port) {
        JsonFields fld = new JsonFields();
        fld.setAction(AppConstants.TYPE_CONFIRMATION);
        fld.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
        fld.setPacketIdFromServer(packetIDfromServer);
        sendPacketAsString(fld, port);

    }

    public static void sendConfirmationWhenPacketIdFromServer(String packetIDfromServer, int port) {
        if (MyFnFSettings.LOGIN_SESSIONID != null) {
            String confirmation = packetIDfromServer + "," + MyFnFSettings.LOGIN_SESSIONID;
            sendDatagramPacket(confirmation.getBytes(), port);
//            try {
////                UdpPacket pkd_to_server = new UdpPacket(confirmation.getBytes());
////                pkd_to_server.setIpAddress(new IpAddress(IpAddress.getByName(ServerAndPortSettings.AUTH_SERVER_IP)));
////                pkd_to_server.setPort(port);
////                if (AuthenticationReceiverMain.getInstance() != null) {
////                    AuthenticationReceiverMain.getInstance().send(pkd_to_server);
////                }
//                log.debug("sending packet " + ServerAndPortSettings.AUTH_SERVER_IP + ":" + port + "==>" + confirmation);
//            } catch (Exception ex) {
//                log.error("Confirmation error " + ServerAndPortSettings.AUTH_SERVER_IP + ":" + port + "==>" + confirmation);
//            }
        } else {
            log.debug("sendConfirmationWhenPacketIdFromServer==>null session");
        }
    }

    public static void sendConfirmationWhenPacketIdFromServer(int action, String packetIDfromServer, int port) {
        if (MyFnFSettings.LOGIN_SESSIONID != null) {
            String confirmation = action + "," + packetIDfromServer + "," + MyFnFSettings.LOGIN_SESSIONID;
            sendDatagramPacket(confirmation.getBytes(), port);
//            try {
////                UdpPacket pkd_to_server = new UdpPacket(confirmation.getBytes());
////                pkd_to_server.setIpAddress(new IpAddress(IpAddress.getByName(ServerAndPortSettings.AUTH_SERVER_IP)));
////                pkd_to_server.setPort(port);
////                if (AuthenticationReceiverMain.getInstance() != null) {
////                    AuthenticationReceiverMain.getInstance().send(pkd_to_server);
////                }
//
//            } catch (Exception ex) {
//                log.error("Confirmation error " + ServerAndPortSettings.AUTH_SERVER_IP + ":" + port + "==>" + confirmation);
//            }
        } else {
            log.debug("sendConfirmationWhenPacketIdFromServer==>null session");
        }
    }

    public static void sendPacketAsString(Object ob, int port) {

        try {
            String packet = new Gson().toJson(ob);
            sendDatagramPacket(packet.getBytes("UTF-8"), port);
            //    System.out.println("packet==>"+packet);
//            UdpPacket pkd_to_server = new UdpPacket(packet.getBytes("UTF-8"));
//            pkd_to_server.setIpAddress(new IpAddress(IpAddress.getByName(ServerAndPortSettings.AUTH_SERVER_IP)));
//            pkd_to_server.setPort(port);
//            if (AuthenticationReceiverMain.getInstance() != null) {
//                AuthenticationReceiverMain.getInstance().send(pkd_to_server);
//            }
        } catch (Exception e) {
            log.debug("Problem in sending to auth sendPacketAsString");
        }
    }

    public static void sendAllBrokenPacket(Map<String, byte[]> packets, int port) {
        for (byte[] data : packets.values()) {
            sendDatagramPacket(data, port);
            try {
                Thread.sleep(25);
            } catch (Exception e) {
            }
//                UdpPacket pkd_to_server = new UdpPacket(data);
//                pkd_to_server.setIpAddress(new IpAddress(IpAddress.getByName(ServerAndPortSettings.AUTH_SERVER_IP)));
//                pkd_to_server.setPort(port);
//                if (AuthenticationReceiverMain.getInstance() != null) {
//                    AuthenticationReceiverMain.getInstance().send(pkd_to_server);
//                }
        }
    }

    public static DatagramPacket makeDatagramPacket(byte[] data, int port) {
        DatagramPacket dataGram = new DatagramPacket(data, data.length);
        try {
            dataGram.setAddress(InetAddress.getByName(ServerAndPortSettings.AUTH_SERVER_IP));
        } catch (UnknownHostException ex) {
        }
        dataGram.setPort(port);
        return dataGram;
    }

//    private static InetAddress getInetAddress(String ipAddress) {
//       
//    }
    public static void sendDatagramPacket(byte[] data, int port) {
        if (AuthenticationReceiverMain.getInstance() != null) {
            try {
                DatagramPacket dataGram = makeDatagramPacket(data, port);
                AuthenticationReceiverMain.getInstance().send(dataGram);
            } catch (Exception ex) {
                log.error("error while sending data to auth==>" + ex.getMessage());
            }
        }
    }
//

    public static void sendDatagramPacket(DatagramPacket pkd_to_server) {
        if (AuthenticationReceiverMain.getInstance() != null) {
            try {
                AuthenticationReceiverMain.getInstance().send(pkd_to_server);
            } catch (IOException ex) {
                log.error("error while sending data to auth==>" + ex.getMessage());
            }
        }
    }

    public static boolean checkAllBrokenPacketConfirmation(Map<String, byte[]> packets) {
        Set<String> packetIds = packets.keySet();
        for (String packetId : packetIds) {
            if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(packetId) != null) {
                packets.remove(packetId);
            }
        }
        return packets.size() > 0;
    }

    public static void removeAllBrokenPacketConfirmation(List<String> packetIds) {
        for (String packetId : packetIds) {
            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(packetId);
        }
        System.out.println("MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().size==>" + MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().size());
    }

    public static Map<String, byte[]> buildBrokenPacket(Object ob, int action, String packetId, int port) {
        Map<String, byte[]> packets = new ConcurrentHashMap<>();
        try {
            String packet = new Gson().toJson(ob);
            byte[] data = packet.getBytes("UTF-8");
            if (data.length < AppConstants.CLIENT_DATA_SIZE) {
                packets.put(packetId, data);
            } else {
                packets = SendDevidedPacket.buildDevidedPackets(action, data, data.length, port);
            }
        } catch (Exception e) {
           // e.printStackTrace();
            log.debug("Problem in sending to auth sendBreakingPacketAsString");
        }
        return packets;
    }
    public static int PACKET_INCREMENT_VALUE = 0;

    public static String getRanDomPacketID() {
        String key;
        if (PACKET_INCREMENT_VALUE > 9998) {
            PACKET_INCREMENT_VALUE = 0;
        } else {
            PACKET_INCREMENT_VALUE++;
        }
        key = MyFnFSettings.LOGIN_USER_ID + PACKET_INCREMENT_VALUE + System.currentTimeMillis();

//        if (MyFnFSettings.userProfile != null) {
//            key = MyFnFSettings.userProfile.getMobilePhoneDialingCode()
//                    + MyFnFSettings.userProfile.getMobilePhone();
//
//            if (key.charAt(0) == '+') {
//                key = key.substring(1);
//            }
//            if (PACKET_INCREMENT_VALUE > 9998) {
//                PACKET_INCREMENT_VALUE = 0;
//            } else {
//                PACKET_INCREMENT_VALUE++;
//            }
//
//            key = key + PACKET_INCREMENT_VALUE;
//            long keyValue = 0;
//            try {
//                keyValue = Long.parseLong(key);
//                key = Long.toHexString(keyValue);
//            } catch (NumberFormatException e) {
//            }
//        } else {
//            SecureRandom random = new SecureRandom();
//            char[] chars = new char[8];
//            for (int i = 0; i < chars.length; i++) {
//                int v = random.nextInt(10 + 26 + 26);
//                char c;
//                if (v < 10) {
//                    c = (char) ('0' + v);
//                } else if (v < 36) {
//                    c = (char) ('a' - 10 + v);
//                } else {
//                    c = (char) ('A' - 36 + v);
//                }
//                chars[i] = c;
//            }
//            key = new String(chars);
//        }
        return key;
    }
}
