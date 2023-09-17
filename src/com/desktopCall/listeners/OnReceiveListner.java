/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.listeners;

/**
 *
 * @author Faiz
 */
public interface OnReceiveListner {

    public void onReceivedMessage(byte[] receivedByte, int length);
}