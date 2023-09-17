/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import java.net.DatagramPacket;
import org.apache.log4j.Logger;

/**
 *
 * @author FaizAhmed
 */
public class KeepAlive extends Thread {

    static org.apache.log4j.Logger log = Logger.getLogger(KeepAlive.class);
    public static boolean runningFlag = true;
    private DatagramPacket keepalivePacket;

    public KeepAlive() {
        runningFlag = true;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        while (runningFlag) {
            try {
                send_keepAlive();
                Thread.sleep(MyFnFSettings.KEEP_ALIVE_TIME);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void send_keepAlive() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0) {
            if (keepalivePacket == null) {

                keepalivePacket = SendToServer.makeDatagramPacket(MyFnFSettings.LOGIN_SESSIONID.getBytes(), ServerAndPortSettings.KEEP_ALIVE_PORT);
            }
            SendToServer.sendDatagramPacket(keepalivePacket);
        } else {
            keepalivePacket = null;
        }
    }
}
