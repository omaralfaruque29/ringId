/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.utils;

import com.ipv.chat.ChatSettings;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatUtility {

    private static int PACKET_INCREMENT_VALUE = 0;

    public static String getRandomPacketID() {
        if (PACKET_INCREMENT_VALUE > 9998) {
            PACKET_INCREMENT_VALUE = 0;
        } else {
            PACKET_INCREMENT_VALUE++;
        }
        return ChatSettings.USER_IDENTITY + PACKET_INCREMENT_VALUE + System.currentTimeMillis();

//        SecureRandom random = new SecureRandom();
//        char[] chars = new char[8];
//        for (int i = 0; i < chars.length; i++) {
//            int v = random.nextInt(10 + 26 + 26);
//            char c;
//            if (v < 10) {
//                c = (char) ('0' + v);
//            } else if (v < 36) {
//                c = (char) ('a' - 10 + v);
//            } else {
//                c = (char) ('A' - 36 + v);
//            }
//            chars[i] = c;
//        }
//
//        String key = new String(chars);
//        return key + "" + System.currentTimeMillis();
    }
    
    public static byte[] readBytes(File file) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            for (int read; (read = in.read()) != -1;) {
                baos.write(read);
            }

            return baos.toByteArray();
        } catch (Exception ex) {
        }
        return null;
    }

    public static int getChunkNumber(String fileName) {
        int index = 0;
        try {
            index = Integer.parseInt(fileName.substring(fileName.lastIndexOf('.') + 1));
        } catch (Exception e) {
        }
        return index;
    }
}
