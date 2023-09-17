package com.ipv.chat.listener;

import com.ipv.chat.udp.IMDatagramPacket;

/**
 *
 * @author Shahadat Hossain
 */
public interface PacketReceivedListener {

    public void onPacketReceived(IMDatagramPacket packet);
    
}
