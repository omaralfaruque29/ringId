/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.model.SearchItem;
import com.ipvision.service.utility.GetResponseJsons;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import static com.ipvision.service.SearchThread.start;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import utils.SearchStringQueue;

/**
 *
 * @author Ashraful
 */
public class SearchThread extends Thread {

    //------------------------------------------- added_by_wasif_08062013 -----------------------------------------
    public static volatile boolean start = false;
    private static int searchCategory = 0;

    public SearchThread(int category) {
        searchCategory = category;
        setName(this.getClass().getSimpleName());
    }

    public static void setSearchCategory(int category) {
        searchCategory = category;
    }

    public static void stopSearchThread() {
        start = false;
    }

    public static void startSearchThread() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            start = true;
        }
    }

    public static boolean getStatus() {
        return start;
    }

    @Override
    public void run() {
        while (start) {
//            if (GuiRingID.guiRingID != null && GuiRingID.getInstance().addContactPanel != null) {
//                if (!GuiRingID.getInstance().addContactPanel.friendList.isDisplayable()) {
//                    stopSearchThread();
//                }
//            }
//            
            //  System.out.println("SearchThread");
            if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
                // System.out.println("search thread");
                //------------------------------------------- added_by_wasif_08062013 -----------------------------------------
                try {
                    if (!SearchStringQueue.getInstance().isEmpty()) {
                        while (SearchStringQueue.getInstance().size() != 1) {
                            SearchStringQueue.getInstance().pop();
                        }
                        SearchItem item = (SearchItem) SearchStringQueue.getInstance().pop();
                        GetResponseJsons.inviteUserByCategory(item.getSearch_string(), searchCategory);
                    } else {
                        SearchThread.sleep(1250);//wait for key presses
                    }

                } catch (Exception ex) {
                }

            } else {
                stopSearchThread();
                HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
            }
        }

    }
}
