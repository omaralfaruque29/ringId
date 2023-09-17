/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.net;

/**
 *
 * @author Faiz
 */
public class CallStates {

    // ************************* UA internal state *************************
    /**
     * UA_IDLE=0
     */
    public static final int UA_IDLE = 0;
    /**
     * UA_INCOMING_CALL=1
     */
    public static final int UA_INCOMING_CALL = 1;
    /**
     * UA_OUTGOING_CALL=2
     */
    public static final int UA_OUTGOING_CALL = 2;
    /**
     * UA_ONCALL=3
     */
    public static final int UA_ONCALL = 3;
    /**
     * Call state:
     * <P>
     * UA_IDLE=0, <BR>UA_INCOMING_CALL=1, <BR>UA_OUTGOING_CALL=2,
     * <BR>UA_ONCALL=3
     */

    private static int call_state = UA_IDLE;
    public static final int HOLD_CALL = 4;

    /**
     * Changes the call state
     *
     * @param state
     */
    public static void changeStatus(int state) {
        call_state = state;
    }

    /**
     * Checks the call state
     *
     * @param state
     * @return
     */
//    public static boolean statusIs(int state) {
//        return call_state;
//    }
    /**
     * Gets the call state
     *
     * @return
     */
    public static int getStatus() {
        return call_state;
    }
}
