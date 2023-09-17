/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.view.feed.SingleFriendAlbumNameWithCoverImage;
import com.ipvision.view.feed.SingleImageInAlbum;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.constants.RingColorCode;

/**
 *
 * @author Shahadat Hossain
 */
public class MyFriendAlbumPanel extends JPanel {

    private JPanel content = new JPanel();
    public static MyFriendAlbumPanel albumsHome;
    private JPanel myInfoPanel;
    private int commonWidth = 150;
    private MyFriendProfile myFriendProfile;
    private JPanel tab;
    public JLabel loadingLabel = new JLabel();
    private JLabel photosTab = DesignClasses.makeLableBoldTooLarge("Photos");
    private JLabel albumsTab = DesignClasses.makeLableBoldTooLarge("Albums");
    public int clicked_type;
    public static int TYPE_PHOTOS = 1;
    public static int TYPE_ALBUMS = 2;
    private JPanel tabPanel;
    public boolean isLoading;
    public JScrollPane scrollContent;

    public JPanel getContent() {
        return content;
    }

    public void setContent(JPanel content) {
        this.content = content;
    }

    public MyFriendAlbumPanel(MyFriendProfile myFriendProfile) {
        this.myFriendProfile = myFriendProfile;
        setLayout(new BorderLayout());
        setOpaque(false);
        initContainers();
    }

    private void initContainers() {
        try {
            final JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            mainContainer.setOpaque(false);
            JPanel photosTabPnl = new JPanel(new BorderLayout());
            //photosTabPnl.setOpaque(false);
            photosTabPnl.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 30));
            photosTabPnl.add(photosTab, BorderLayout.WEST);
            photosTab.setBorder(new EmptyBorder(5, 5, 5, 5));
            photosTabPnl.setBackground(Color.WHITE);
            photosTabPnl.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));

            /*            int columns = GuiRingID.getInstance().getMainRight().getWidth() / commonWidth;
             =======
             //            int columns = GuiRingID.getInstance().getMainRight().getWidth() / commonWidth;
             >>>>>>> .r1429

             JPanel tabWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
             tabWrapper.setOpaque(false);
             //  tabWrapper.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
             tab = new JPanel(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
             tab.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 38));
             tab.setOpaque(false);

             tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 3));
             tabPanel.setOpaque(false);
             tab.add(tabPanel, BorderLayout.CENTER);
             tabWrapper.add(tab);
             */
            //content.setOpaque(false);
            //content.setLayout(new GridLayout(0, columns - 1, 20, 20));
            content.setLayout(new WrapLayout(WrapLayout.LEFT, 7, 7, DefaultSettings.COVER_PIC_DISPLAY_WIDTH));
            content.setBackground(Color.WHITE);
            content.setBorder(new MatteBorder(0, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));

//            JPanel nullPanle = new JPanel();
//             nullPanle.setOpaque(false);
//             nullPanle.add(content, BorderLayout.EAST);
//
//             JPanel scrollContotainer = new JPanel(new BorderLayout(0, 0));
//             scrollContotainer.setOpaque(false);
//             scrollContotainer.add(nullPanle);  
            JPanel tabAndAlbums = new JPanel(new BorderLayout());
            tabAndAlbums.setLayout(new BorderLayout());
            tabAndAlbums.setOpaque(false);
            //tabAndAlbums.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, 0, MyFnFSettings.DEFAULT_MARGIN, 0));
            tabAndAlbums.add(photosTabPnl, BorderLayout.NORTH);
            tabAndAlbums.add(content, BorderLayout.CENTER);

            mainContainer.add(tabAndAlbums);

            scrollContent = DesignClasses.getDefaultScrollPane(mainContainer);
            scrollContent.setOpaque(false);
            scrollContent.setPreferredSize(new Dimension(0, 65));
            scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    if (clicked_type == TYPE_PHOTOS) {
                        int containerHeight = mainContainer.getHeight();
                        int topHeight = e.getValue();
                        int barHeight = scrollContent.getVerticalScrollBar().getHeight();
                        int bottomHeight = containerHeight - (topHeight + barHeight);
                        int totalCalHeight = (topHeight + barHeight + bottomHeight);

                        // System.err.println("TH = " + topHeight + ", BH = " + barHeight + ", BOH = " + bottomHeight + ", TOH = " + totalCalHeight);
                        if (isLoading == false && topHeight != 0 && bottomHeight == 0) {
                            isLoading = true;
                            addloding();
                            new SendFriendsInfoRequest(myFriendProfile.getUserIdentity(), AppConstants.TYPE_FRIEND_ALBUM_IMAGES, NewsFeedMaps.getInstance().getFriendsAlbumImages().get(myFriendProfile.getUserIdentity()).get("0").size()).start();
                        }
                    }
                }
            });
            add(scrollContent, BorderLayout.CENTER);

            //          buildTabPanel();
            tabChange(TYPE_PHOTOS);
        } catch (Exception e) {
        }

    }

    /*   private void buildTabPanel() {
     try {
     JPanel photosTabPnl = new JPanel();
     photosTabPnl.setOpaque(false);
     photosTabPnl.setPreferredSize(new Dimension(70, 30));
     photosTabPnl.add(photosTab);
     photosTabPnl.setBorder(DefaultSettings.DEFAULT_BORDER);
     tabPanel.add(photosTabPnl);
     photosTab.setCursor(new Cursor(Cursor.HAND_CURSOR));
     photosTab.addMouseListener(mouseListener);

     JPanel albumsTabPnl = new JPanel();
     albumsTabPnl.setOpaque(false);
     albumsTabPnl.setPreferredSize(new Dimension(70, 30));
     albumsTabPnl.add(albumsTab);
     albumsTabPnl.setBorder(DefaultSettings.DEFAULT_BORDER);
     tabPanel.add(albumsTabPnl);
     albumsTab.setCursor(new Cursor(Cursor.HAND_CURSOR));
     albumsTab.addMouseListener(mouseListener);

     } catch (Exception ex) {
     }
     } */
    public void tabChange(int type) {
        clicked_type = type;
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (clicked_type == TYPE_PHOTOS) {
                    photosTab.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
                    photosTab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

                    albumsTab.setForeground(DefaultSettings.disable_font_color);
                    albumsTab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

                    if (NewsFeedMaps.getInstance() != null && NewsFeedMaps.getInstance().getFriendsAlbumImages() != null
                            && NewsFeedMaps.getInstance().getFriendsAlbumImages().get(myFriendProfile.getUserIdentity()) != null) {
                        addFriendAllImages();
                    } else {
                        loadFriendAllImagesFromServer();

                    }

                }
                /*    else if (clicked_type == TYPE_ALBUMS) {
                 albumsTab.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
                 albumsTab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

                 photosTab.setForeground(DefaultSettings.disable_font_color);
                 photosTab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

                 if (NewsFeedMaps.getInstance().getFriendsAllbums().get(myFriendProfile.getUserIdentity()) != null) {
                 addFriendAlbumFolders();
                 } else {
                 loadFriendAlbumFromServer();
                 }
                 } */

            }
        }).start();

    }

    public void addloding() {
        //isLoading = true;
        loadingLabel = DesignClasses.loadingLable(true);
        content.add(loadingLabel);
        if(content.getComponentCount()<=1)
        content.setBorder(null);
        content.revalidate();
        content.repaint();
    }

    public void removeLoadingcontent() {
        //isLoading = false;
        content.remove(loadingLabel);
//        loadingLabel.setOpaque(false);
        content.revalidate();
        content.repaint();
    }

    public synchronized void addFriendAlbumFolders() {
        content.removeAll();
        int columns = GuiRingID.getInstance().getMainRight().getWidth() / commonWidth;

        Map<String, JsonFields> map = NewsFeedMaps.getInstance().getFriendsAllbums().get(myFriendProfile.getUserIdentity());

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
                SingleFriendAlbumNameWithCoverImage album = new SingleFriendAlbumNameWithCoverImage(fields, myFriendProfile.getUserIdentity());
                content.add(album);
            }
        }
        content.revalidate();
        content.repaint();
    }

    public void loadFriendAlbumFromServer() {
        content.removeAll();
        addloding();
        isLoading = true;
        (new SendFriendsInfoRequest(myFriendProfile.getUserIdentity(), AppConstants.TYPE_FRIEND_ALBUMS, 0)).start();
    }

    public void loadFriendAllImagesFromServer() {
        content.removeAll();
        addloding();
        isLoading = true;
        new SendFriendsInfoRequest(myFriendProfile.getUserIdentity(), AppConstants.TYPE_FRIEND_ALBUM_IMAGES, 0).start();

    }

    public synchronized void addFriendAllImages() {
        content.removeAll();

        if (!NewsFeedMaps.getInstance().getFriendsAlbumImages().isEmpty() && myFriendProfile.getUserIdentity() != null) {
            for (String imageUrl : NewsFeedMaps.getInstance().getFriendsAlbumImages().get(myFriendProfile.getUserIdentity()).get("0").keySet()) {
                JsonFields fields = NewsFeedMaps.getInstance().getFriendsAlbumImages().get(myFriendProfile.getUserIdentity()).get("0").get(imageUrl);
                SingleImageInAlbum img = new SingleImageInAlbum(null, fields);
                img.setCursor(new Cursor(Cursor.HAND_CURSOR));
                content.add(img);
            }
        }
        content.revalidate();
        content.repaint();
    }

    public void addMoreFriendImages(ArrayList<JsonFields> moreImagesMap) {
        content.remove(loadingLabel);

        for (JsonFields image : moreImagesMap) {
            SingleImageInAlbum img = new SingleImageInAlbum(null, image);
            img.setCursor(new Cursor(Cursor.HAND_CURSOR));
            content.add(img);
        }
        content.revalidate();
        content.repaint();

    }
}
