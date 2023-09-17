/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.constants.MyFnFSettings;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author Wasif Islam
 */
public class ViewProfileImageFromQueue extends ConcurrentLinkedDeque<ViewProfileImage> {

    public static int STATUS_OPEN = 0;
    public static int STATUS_PENDING = 1;
    public int CURRENT_STATUS = STATUS_OPEN;

    public void addItem(ViewProfileImage obj) {
        add(obj);
        getItem();
    }

    public void getItem() {
        if (!isEmpty() && CURRENT_STATUS == STATUS_OPEN && MyFnFSettings.FRIEND_LIST_LOADED) {
            CURRENT_STATUS = STATUS_PENDING;
            ViewProfileImage profileImage = poll();
            if (profileImage != null) {
                profileImage.startDownloadThread(new ViewProfileImageLoadHandler());
            } else {
                CURRENT_STATUS = STATUS_OPEN;
            }
        }
    }

    public class ViewProfileImageLoadHandler implements ViewProfileImageLoadListener {

        @Override
        public void onComplete() {
            CURRENT_STATUS = STATUS_OPEN;
            getItem();
        }
    }

}
