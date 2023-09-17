/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.listeners;

/**
 *
 * @author Faiz Ahmed
 */
public interface OnReceiveVoiceInterface {
     public void onReceivedPacket(byte[] receivedBuffer, int length);
    
}
