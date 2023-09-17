/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import com.ipvision.model.AttributeValues;
import com.ipvision.model.constants.AttributeCodes;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import java.io.UnsupportedEncodingException;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.BreakingPacketRepository;
import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Faiz
 */
public class PacketProcessor extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PacketProcessor.class);
    private DatagramPacket received_packet;

    public PacketProcessor() {
        setName(this.getClass().getSimpleName());
    }

    public PacketProcessor(DatagramPacket p_packet) {
        setName(this.getClass().getSimpleName());
        this.received_packet = p_packet;
    }

//    public void process() {
//        try {
//            byte received_bytes[] = received_packet.getData();
//            int first_byte = (int) received_bytes[0];
//            log.debug("PacketProcessor ====> first_byte = " + first_byte);
//
//            if (first_byte == 1) {
//                AttributeValues attributes = parsePacket(received_bytes, 1);
//
//                int total_packets = attributes.getTotalPacket();
//                int key = attributes.getUniqueKey();
//                int action = attributes.getAction();
//
//                SendToServer.sendConfirmationWhenPacketIdFromServer(action + "," + attributes.getPacketID(), ServerAndPortSettings.CONFIRMATION_PORT);
//
//                byte data_bytes[] = attributes.getData();
//                BreakingPacketData breakingPacketData = BreakingPacketRepository.getInstance().getBreakingPacketList(key);
//                int packet_number = attributes.getPacketNumber();
//
//                if (breakingPacketData != null) {
//                    breakingPacketData.getValue().put(packet_number, data_bytes);
//                } else {
//                    ConcurrentHashMap<Integer, byte[]> map = new ConcurrentHashMap<Integer, byte[]>();
//                    map.put(packet_number, data_bytes);
//
//                    breakingPacketData = new BreakingPacketData();
//                    breakingPacketData.setValue(map);
//                    breakingPacketData.setTime(System.currentTimeMillis());
//
//                    BreakingPacketRepository.getInstance().putBreakingPacketList(key, breakingPacketData);
//                }
//
//                log.debug("PacketProcessor ====> dataSize = " + data_bytes.length + " || totalPacket = " + total_packets + " || storedPacket = " + breakingPacketData.getValue().size() + " || Action = " + action + " || Key = " + key + " || packetNo = " + packet_number);
//
//                if (total_packets == breakingPacketData.getValue().size()) {
//                    int total_read = 0;
//                    for (byte[] bytes : breakingPacketData.getValue().values()) {
//                        total_read += bytes.length;
//                    }
//
//                    byte[] all_data = new byte[total_read];
//                    total_read = 0;
//                    for (int i = 0; i < breakingPacketData.getValue().size(); i++) {
//                        byte[] data = breakingPacketData.getValue().get(i);
//                        System.arraycopy(data, 0, all_data, total_read, data.length);
//                        total_read += data.length;
//                    }
//                    AuthMsgParser auth = new AuthMsgParser(new String(all_data, "UTF-8").trim());
//                    auth.process();
//
//                    BreakingPacketRepository.getInstance().removeBreakingPacketList(key);
//                }
//            } else {
//                String data = new String(received_packet.getData(), AppConstants.DATA_OFFSET, received_packet.getData().length - AppConstants.DATA_OFFSET, "UTF-8").trim();
//                AuthMsgParser auth = new AuthMsgParser(data);
//                auth.process();
//            }
//        } catch (UnsupportedEncodingException ex) {
//            //  ex.printStackTrace();
//            log.error("Exception in PacketProcessor ==>" + ex.getMessage());
//        }
//    }
    @Override
    public void run() {
        try {
            byte received_bytes[] = received_packet.getData();
            int first_byte = (int) received_bytes[0];
            log.debug("PacketProcessor ====> first_byte = " + first_byte);

            if (first_byte == 1) {
                AttributeValues attributes = parsePacket(received_bytes, 1);

                int total_packets = attributes.getTotalPacket();
                int key = attributes.getUniqueKey();
                int action = attributes.getAction();

                SendToServer.sendConfirmationWhenPacketIdFromServer(action + "," + attributes.getPacketID(), ServerAndPortSettings.CONFIRMATION_PORT);

                byte data_bytes[] = attributes.getData();
                BreakingPacketData breakingPacketData = BreakingPacketRepository.getInstance().getBreakingPacketList(key);
                int packet_number = attributes.getPacketNumber();

                if (breakingPacketData != null) {
                    breakingPacketData.getValue().put(packet_number, data_bytes);
                } else {
                    ConcurrentHashMap<Integer, byte[]> map = new ConcurrentHashMap<Integer, byte[]>();
                    map.put(packet_number, data_bytes);

                    breakingPacketData = new BreakingPacketData();
                    breakingPacketData.setValue(map);
                    breakingPacketData.setTime(System.currentTimeMillis());

                    BreakingPacketRepository.getInstance().putBreakingPacketList(key, breakingPacketData);
                }

                log.debug("PacketProcessor ====> dataSize = " + data_bytes.length + " || totalPacket = " + total_packets + " || storedPacket = " + breakingPacketData.getValue().size() + " || Action = " + action + " || Key = " + key + " || packetNo = " + packet_number);

                if (total_packets == breakingPacketData.getValue().size()) {
                    int total_read = 0;
                    for (byte[] bytes : breakingPacketData.getValue().values()) {
                        total_read += bytes.length;
                    }

                    byte[] all_data = new byte[total_read];
                    total_read = 0;
                    for (int i = 0; i < breakingPacketData.getValue().size(); i++) {
                        byte[] data = breakingPacketData.getValue().get(i);
                        System.arraycopy(data, 0, all_data, total_read, data.length);
                        total_read += data.length;
                    }
                    AuthMsgParser auth = new AuthMsgParser(new String(all_data, "UTF-8").trim());
                    auth.process();

                    BreakingPacketRepository.getInstance().removeBreakingPacketList(key);
                }
            } else {
                String data = new String(received_packet.getData(), AppConstants.DATA_OFFSET, received_packet.getData().length - AppConstants.DATA_OFFSET, "UTF-8").trim();
                AuthMsgParser auth = new AuthMsgParser(data);
                auth.process();
            }
        } catch (UnsupportedEncodingException ex) {
            //  ex.printStackTrace();
            log.error("Exception in PacketProcessor ==>" + ex.getMessage());
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
    }

}
