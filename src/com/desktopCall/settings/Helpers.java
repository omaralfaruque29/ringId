/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.settings;

import java.security.SecureRandom;

/**
 *
 * @author Faiz
 */
public class Helpers {

    public static void systemPrint(String msg) {
        if (ConfigFile.systemPrint) {
            System.out.println(msg);
        }
    }

    public static String getPack(String pakid, int type) {
        return pakid + "_" + type;
    }

    public static String getRandromPacketId() {
        SecureRandom random = new SecureRandom();
        char[] chars = new char[8];
        for (int i = 0; i < chars.length; i++) {
            int v = random.nextInt(10 + 26 + 26);
            char c;
            if (v < 10) {
                c = (char) ('0' + v);
            } else if (v < 36) {
                c = (char) ('a' - 10 + v);
            } else {
                c = (char) ('A' - 36 + v);
            }
            chars[i] = c;
        }

        String key = new String(chars);
        return key + "" + System.currentTimeMillis();
    }
}
