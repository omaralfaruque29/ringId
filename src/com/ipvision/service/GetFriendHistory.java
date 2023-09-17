/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import java.util.Date;
import java.util.List;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Shahadat Hossain
 */
public class GetFriendHistory extends Thread {

    private long daysInSecond = 86400000;
    private String friendId;
    private Long time;
    private Integer limit;
    private Integer scrollType;

    public GetFriendHistory(String friendId, int days, Integer limit) {
        this.friendId = friendId;
        Date currDate = new Date();
        currDate.setHours(0);
        currDate.setMinutes(0);
        currDate.setSeconds(0);
        this.time = currDate.getTime() - (days * daysInSecond);
        this.limit = limit;
        setName(this.getClass().getSimpleName());
    }

    public GetFriendHistory(String friendId, long time, Integer limit, Integer scrollType) {
        this.friendId = friendId;
        this.time = time;
        this.limit = limit;
        this.scrollType = scrollType;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        try {
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(friendId);
            if (scrollType != null) {
                List<RecentDTO> recentList = RecentChatCallActivityDAO.loadRecentChatCallActivityListByScroll(null, friendId, time, limit, scrollType);

                if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null) {
                    if (scrollType == RecentChatCallActivityDAO.TYPE_SCROLL_UP) {
                        friendChatCallPanel.myFriendChatPanel.addHistoryByScrollUp(recentList);
                    } else {
                        friendChatCallPanel.myFriendChatPanel.addHistoryByScrollDown(recentList);
                    }
                }
            } else {
                List<RecentDTO> recentList = RecentChatCallActivityDAO.loadRecentChatCallActivityListByDays(null, friendId, time, limit);

                if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null) {
                    friendChatCallPanel.myFriendChatPanel.addHistory(recentList, limit);
                }
            }
        } catch (Exception ex) {
        }
    }
}
