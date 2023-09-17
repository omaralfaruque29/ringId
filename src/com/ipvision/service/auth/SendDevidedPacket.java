/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import com.ipvision.model.constants.AttributeCodes;
import com.ipvision.constants.AppConstants;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Ashraful
 */
public class SendDevidedPacket {

    public static Map<String, byte[]> buildDevidedPackets(int action, byte[] all_bytes, int length, int port) {
        Map<String, byte[]> tempMap = new ConcurrentHashMap<String, byte[]>();
        int total_packets = length / AppConstants.CLIENT_DATA_SIZE;
        if (length - total_packets * AppConstants.CLIENT_DATA_SIZE > 0) {
            total_packets++;
        }

        int current_packet = 0;
        int read_bytes = 0;
        String uniqueKey = SendToServer.getRanDomPacketID(); //"123" + sixDigit;  
        try {
            while (read_bytes < length) {
                int diff = length - read_bytes;
                int data_size = AppConstants.CLIENT_DATA_SIZE;
                if (diff < AppConstants.CLIENT_DATA_SIZE) {
                    data_size = diff;
                }

                int header_length = 128;
                byte header_bytes[] = new byte[header_length];

                header_bytes[0] = (byte) 1;
                int index = addInteger(AttributeCodes.ACTION, action, 2, header_bytes, 1);

                String packet_id = SendToServer.getRanDomPacketID(); //"123" + sixDigit;
                index = addString(AttributeCodes.PACKET_ID, packet_id, header_bytes, index);

                if (total_packets < 128) {
                    index = addInteger(AttributeCodes.TOTAL_PACKET, total_packets, 1, header_bytes, index);
                    index = addInteger(AttributeCodes.PACKET_NUMBER, current_packet, 1, header_bytes, index);
                } else {
                    index = addInteger(AttributeCodes.TOTAL_PACKET, total_packets, 2, header_bytes, index);
                    index = addInteger(AttributeCodes.PACKET_NUMBER, current_packet, 2, header_bytes, index);
                }
                
                index = addString(AttributeCodes.UNIQUE_KEY, uniqueKey, header_bytes, index);

                byte send_bytes[] = new byte[index + data_size + 3];
                System.arraycopy(header_bytes, 0, send_bytes, 0, index);
                addBytes(AttributeCodes.DATA, all_bytes, read_bytes, send_bytes, index, data_size);

                current_packet++;
                read_bytes += data_size;
                tempMap.put(packet_id, send_bytes);
            }
        } catch (Exception ioEx) {
            //FnFLogger.getInstance().error("Exception in buildDevidedPackets->", ioEx);
        }

        return tempMap;
    }

    private static int addInteger(int attribute, int value, int length, byte[] send_bytes, int index) {
        send_bytes[index++] = (byte) attribute;
        send_bytes[index++] = (byte) length;

        switch (length) {
            case 1: {
                send_bytes[index++] = (byte) (value);
                break;
            }
            case 2: {
                send_bytes[index++] = (byte) (value >> 8);
                send_bytes[index++] = (byte) (value);
                break;
            }
            case 4: {
                send_bytes[index++] = (byte) (value >> 24);
                send_bytes[index++] = (byte) (value >> 16);
                send_bytes[index++] = (byte) (value >> 8);
                send_bytes[index++] = (byte) (value);
                break;
            }
        }

        return index;
    }

    private static int addString(int attribute, String value, byte[] send_bytes, int index) {
        send_bytes[index++] = (byte) attribute;

        byte[] id_bytes = value.getBytes();
        int length = id_bytes.length;

        send_bytes[index++] = (byte) length;
        System.arraycopy(id_bytes, 0, send_bytes, index, length);
        index += length;

        return index;
    }

    private static int addBytes(int attribute, byte[] data_bytes, int read_bytes, byte[] send_bytes, int index, int length) {
        send_bytes[index++] = (byte) attribute;

        send_bytes[index++] = (byte) (length >> 8);
        send_bytes[index++] = (byte) (length);

        System.arraycopy(data_bytes, read_bytes, send_bytes, index, length);
        index += length;
        return index;
    }
}
