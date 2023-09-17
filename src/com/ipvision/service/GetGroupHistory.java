/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import java.util.Date;
import java.util.List;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Shahadat Hossain
 */
public class GetGroupHistory extends Thread {

    private long daysInSecond = 86400000;
    private Long groupId;
    private Long time;
    private Integer limit;
    private Integer scrollType;

    public GetGroupHistory(long groupId, int days, Integer limit) {
        this.groupId = groupId;
        Date currDate = new Date();
        currDate.setHours(0);
        currDate.setMinutes(0);
        currDate.setSeconds(0);
        this.time = currDate.getTime() - (days * daysInSecond);
        this.limit = limit;
        setName(this.getClass().getSimpleName());
    }

    public GetGroupHistory(long groupId, long time, Integer limit, Integer scrollType) {
        this.groupId = groupId;
        this.time = time;
        this.limit = limit;
        this.scrollType = scrollType;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        try {
            GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
            if (scrollType != null) {
                List<RecentDTO> recentList = RecentChatCallActivityDAO.loadRecentChatCallActivityListByScroll(groupId, null, time, limit, scrollType);

                if (groupPanel != null) {
                    if (scrollType == RecentChatCallActivityDAO.TYPE_SCROLL_UP) {
                        groupPanel.addHistoryByScrollUp(recentList);
                    } else {
                        groupPanel.addHistoryByScrollDown(recentList);
                    }
                }
            } else {
                List<RecentDTO> recentList = RecentChatCallActivityDAO.loadRecentChatCallActivityListByDays(groupId, null, time, limit);

                if (groupPanel != null) {
                    groupPanel.addHistory(recentList, limit);
                }
            }
        } catch (Exception ex) {
        }
    }
}
