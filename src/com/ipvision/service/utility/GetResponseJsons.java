/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.ipvision.constants.AppConstants;
import com.ipvision.constants.MyFnFSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ipvision.constants.ServerAndPortSettings;

import com.ipvision.model.JsonFields;
//import myfnfui.packetsInfo.PackageActions;
import com.ipvision.model.CommunicationPortMapping;
//import utils.IntegerConstants;

/**
 *
 * @author FaizAhmed
 */
public class GetResponseJsons extends HttpRequest {

    private static Gson jsonLib;

    public GetResponseJsons() {
        jsonLib = new GsonBuilder().serializeNulls().create();
    }

    public static int getComPort(String jsSting) {
        jsonLib = new GsonBuilder().serializeNulls().create();
        CommunicationPortMapping cm = jsonLib.fromJson(jsSting, CommunicationPortMapping.class);
        return cm.getCommunicationPort();
    }

    public static void inviteUserByCategory(String searchString, int searchCategory) {
        String search_pak = MyFnFSettings.LOGIN_USER_ID + AppConstants.TYPE_CONTACT_SEARCH + System.currentTimeMillis();
        JsonFields fld = new JsonFields();
        fld.setAction(AppConstants.TYPE_CONTACT_SEARCH);
        fld.setPacketId(search_pak);
        fld.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
        fld.setSearchParam(searchString);
        fld.setSearchCategory(searchCategory);
        SendToServer.sendPacketAsString(fld, ServerAndPortSettings.REQUEST_PORT);
    }

    public static void check_user_avaiability(String user_id) {

        String search_pak = SendToServer.getRanDomPacketID();// ChatCommunicationMethods.getRandrompckId() + AppConstants.TYPE_USER_ID_AVAILABLE + System.currentTimeMillis();
        JsonFields fld = new JsonFields();
        fld.setAction(AppConstants.TYPE_USER_ID_AVAILABLE);
        fld.setPacketId(search_pak);
        fld.setUserIdentity(user_id);

        SendToServer.sendPacketAsString(fld, ServerAndPortSettings.AUTHENTICATION_PORT);

    }
}
