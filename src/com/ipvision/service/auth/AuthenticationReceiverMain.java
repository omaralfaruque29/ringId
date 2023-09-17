/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//  org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendToServer.class);
public class AuthenticationReceiverMain {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AuthenticationReceiverMain.class);
    AuthSendReceive send_receive;
    int port = 0;
    public static AuthenticationReceiverMain udp_transport;

    public static void setUdp_transport(AuthenticationReceiverMain udp_transport) {
        AuthenticationReceiverMain.udp_transport = udp_transport;
    }

    public static AuthenticationReceiverMain getInstance() {
        if (udp_transport == null) {
            udp_transport = new AuthenticationReceiverMain();
        }
        return udp_transport;
    }

    public void send(DatagramPacket packet) throws IOException {
        if (send_receive != null) {
            send_receive.send(packet);
        }
        log.debug("Sent packet to " + packet.getAddress() + ":" + packet.getPort() + "==>" + new String(packet.getData()).trim());
    }

    public void close_socket() {
        if (send_receive != null) {
            send_receive.halt();
        }
    }

    public AuthenticationReceiverMain() {
    }
    private boolean flag = true;

    public void initUdp() {
        DatagramSocket udp_socket = null;
        int udp_port = 6854;
        while (flag) {
            try {
                udp_socket = new DatagramSocket(udp_port++);
                flag = false;
                break;
            } catch (SocketException ex) {
            }
        }

        //  port = udp_socket.getLocalPort();
        if (udp_socket != null) {
            send_receive = new AuthSendReceive(udp_socket);
            send_receive.start();
        } else {
            log.error("No udp socket for authentication......");
        }
    }
    /*
     public void onReceivedPacket(UdpPacket packet) {
     try {
     byte received_bytes[] = packet.getData();

     int first_byte = (int) received_bytes[0];
     if (first_byte == 1) {
     AttributeValues attributes = parsePacket(received_bytes, 1);

     int total_packets = attributes.getTotalPacket();
     int key = attributes.getUniqueKey();
     int action = attributes.getAction();

     SendToServer.sendConfirmationWhenPacketIdFromServer(action + "," + attributes.getPacketID(), ServerAndPortSettings.CONFIRMATION_PORT);

     byte data_bytes[] = attributes.getData();
     HashMap<Integer, byte[]> packet_list = BreakingPacketRepository.getInstance().getBreakingPacketList(key);
     int packet_number = attributes.getPacketNumber();

     if (packet_list != null) {
     packet_list.put(packet_number, data_bytes);
     BreakingPacketRepository.getInstance().putBreakingPacketList(key, packet_list);
     } else {
     packet_list = new HashMap<Integer, byte[]>();
     packet_list.put(packet_number, data_bytes);
     BreakingPacketRepository.getInstance().putBreakingPacketList(key, packet_list);
     }

     if (total_packets == packet_list.size()) {
     int total_read = 0;
     for (byte[] bytes : packet_list.values()) {
     total_read += bytes.length;
     }

     byte[] all_data = new byte[total_read];
     total_read = 0;
     for (int i = 0; i < packet_list.size(); i++) {
     byte[] data = packet_list.get(i);
     System.arraycopy(data, 0, all_data, total_read, data.length);
     total_read += data.length;
     }
     AuthMsgParser auth = new AuthMsgParser(new String(all_data, "UTF-8").trim());
     auth.start();

     BreakingPacketRepository.getInstance().removeBreakingPacketList(key);
     }
     } else {
     String data = new String(packet.getData(), AppConstants.DATA_OFFSET, packet.getData().length - AppConstants.DATA_OFFSET, "UTF-8").trim();
     AuthMsgParser auth = new AuthMsgParser(data);
     auth.start();
     }
     } catch (UnsupportedEncodingException ex) {
     ex.printStackTrace();
     }
     }

     private AttributeValues parsePacket(byte[] bytes, int offset) {
     AttributeValues values = new AttributeValues();
     for (int index = offset; index < bytes.length; index++) {
     int attribute = (byte) bytes[index++];
     int length = 0;

     switch (attribute) {
     case AttributeCodes.ACTION: {
     length = getInt(bytes, index++, 1);
     values.setAction(getInt(bytes, index, length));
     break;
     }
     case AttributeCodes.PACKET_ID: {
     length = getInt(bytes, index++, 1);
     values.setPacketID(getInt(bytes, index, length));
     break;
     }
     case AttributeCodes.TOTAL_PACKET: {
     length = getInt(bytes, index++, 1);
     values.setTotalPacket(getInt(bytes, index, length));
     break;
     }
     case AttributeCodes.PACKET_NUMBER: {
     length = getInt(bytes, index++, 1);
     values.setPacketNumber(getInt(bytes, index, length));
     break;
     }
     case AttributeCodes.UNIQUE_KEY: {
     length = getInt(bytes, index++, 1);
     values.setUniqueKey(getInt(bytes, index, length));
     break;
     }
     case AttributeCodes.DATA: {
     length = getInt(bytes, index, 2);
     index += 2;
     values.setData(getBytes(bytes, index, length));
     break;
     }
     }
     index += length - 1;
     }

     return values;
     }

     private int getInt(byte[] data_bytes, int index, int length) {
     int result = 0;

     switch (length) {
     case 1: {
     result += (data_bytes[index++] & 0xFF);
     break;
     }
     case 2: {
     result += (data_bytes[index++] & 0xFF) << 8;
     result += (data_bytes[index++] & 0xFF);
     break;
     }
     case 4: {
     result += (data_bytes[index++] & 0xFF) << 24;
     result += (data_bytes[index++] & 0xFF) << 16;
     result += (data_bytes[index++] & 0xFF) << 8;
     result += (data_bytes[index++] & 0xFF);
     break;
     }
     }
     return result;
     }

     private byte[] getBytes(byte[] received_bytes, int index, int length) {
     byte[] data_bytes = new byte[length];
     System.arraycopy(received_bytes, index, data_bytes, 0, length);
     return data_bytes;
     }*/
}
