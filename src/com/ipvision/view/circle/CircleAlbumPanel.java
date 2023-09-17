/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.view.feed.SingleCircleAlbumNameWithCoverImage;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;

/**
 *
 * @author Shahadat Hossain
 */
public class CircleAlbumPanel extends JPanel {

    private JPanel content = new JPanel();
    public static CircleAlbumPanel albumsHome;
    private JPanel myInfoPanel;
    private int commonWidth = 150;
    private CircleProfile circleDetails;
    public JScrollPane downContent;

    public JPanel getContent() {
        return content;
    }

    public void setContent(JPanel content) {
        this.content = content;
    }

    public CircleAlbumPanel(CircleProfile circleDetails) {
        this.circleDetails = circleDetails;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initContainers();
    }

    private void initContainers() {
        try {
            JPanel headerPanle = new JPanel();
            headerPanle.setLayout(new BorderLayout());
            headerPanle.setPreferredSize(new Dimension(0, DefaultSettings.SMALL_PANEL_HEIGHT));
            headerPanle.setOpaque(false);
            //    headerPanle.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
            JLabel myNameLabel = DesignClasses.makeLableBold1("Albums");
            myNameLabel.setBorder(new EmptyBorder(1, 5, 1, 20));
            headerPanle.add(myNameLabel, BorderLayout.WEST);
            add(headerPanle, BorderLayout.NORTH);
            JPanel profileAndAlbums = new JPanel();
            profileAndAlbums.setLayout(new BorderLayout(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            profileAndAlbums.setOpaque(false);
            profileAndAlbums.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            add(profileAndAlbums, BorderLayout.CENTER);
            JPanel scrollContent = new JPanel(new BorderLayout(0, 0));
            scrollContent.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            scrollContent.setOpaque(false);
            downContent = DesignClasses.getDefaultScrollPane(scrollContent);
            profileAndAlbums.add(downContent, BorderLayout.CENTER);
            JPanel nullPanle = new JPanel();
            nullPanle.setOpaque(false);
            scrollContent.add(nullPanle);
            content.setOpaque(false);
            nullPanle.add(content, BorderLayout.EAST);

            loadFriendList();
        } catch (Exception e) {
        }

    }

    public void loadFriendList() {

        Map<String, JsonFields> map = NewsFeedMaps.getInstance().getFriendsAllbums().get(circleDetails.getCircleId() + "");
        if (map == null || map.isEmpty()) {
            content.removeAll();
            content.setLayout(new FlowLayout(FlowLayout.CENTER));

            JLabel loading = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT);
            content.add(loading);

            content.revalidate();
            content.repaint();
        }

        (new SendFriendsInfoRequest(circleDetails.getCircleId() + "", AppConstants.TYPE_FRIEND_ALBUMS, 0)).start();
    }

    public synchronized void buildAlbumsFolders() {
        content.removeAll();
        int columns = GuiRingID.getInstance().getMainRight().getWidth() / commonWidth;
        content.setLayout(new GridLayout(0, columns - 1, 20, 20));

        Map<String, JsonFields> map = NewsFeedMaps.getInstance().getFriendsAllbums().get(circleDetails.getCircleId() + "");

        if (map != null && !map.isEmpty()) {
            Map<Long, String> tempMap = new HashMap<Long, String>();
            long[] times = new long[map.size()];
            int e = 0;
            for (String albmID : map.keySet()) {
                JsonFields fields = map.get(albmID);
                tempMap.put(fields.getUt(), albmID);
                times[e] = fields.getUt();
                e++;
            }
            Arrays.sort(times);

            for (int k = times.length - 1; k >= 0; k--) {
                String albumId = tempMap.get(times[k]);
                JsonFields fields = map.get(albumId);
                SingleCircleAlbumNameWithCoverImage album = new SingleCircleAlbumNameWithCoverImage(fields, circleDetails.getCircleId());
                content.add(album);
            }
        }
        content.revalidate();
        content.repaint();
    }
}
