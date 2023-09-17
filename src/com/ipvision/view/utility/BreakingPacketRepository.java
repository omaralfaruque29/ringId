/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.service.auth.BreakingPacketData;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author User
 */
public class BreakingPacketRepository {

    private final ConcurrentHashMap<Integer, BreakingPacketData> breaking_packets;
    private static BreakingPacketRepository instance;

    public static BreakingPacketRepository getInstance() {
        if (instance == null) {
            instance = new BreakingPacketRepository();
        }
        return instance;
    }

    public BreakingPacketRepository() {
        breaking_packets = new ConcurrentHashMap<Integer, BreakingPacketData>();
    }

    /* BreakingPacketList functions*/
    public void putBreakingPacketList(Integer key, BreakingPacketData list) {
        breaking_packets.put(key, list);
    }

    public boolean containsBreakingPacketList(Integer key) {
        return breaking_packets.containsKey(key);
    }

    public BreakingPacketData getBreakingPacketList(Integer key) {
        return breaking_packets.get(key);
    }

    public void removeBreakingPacketList(Integer key) {
        breaking_packets.remove(key);
    }

    public void removeAllBreakingPacketList() {
        breaking_packets.clear();
    }

    public ConcurrentHashMap<Integer, BreakingPacketData> getBreakingPackets() {
        return breaking_packets;
    }

}
