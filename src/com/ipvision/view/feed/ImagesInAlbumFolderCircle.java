/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;

/**
 *
 * @author Wasif Islam
 */
public class ImagesInAlbumFolderCircle extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImagesInAlbumFolderCircle.class);

    private JPanel content = new JPanel();
    private long circleId;
    private JsonFields album;
    private NewsFeedWithMultipleImage albumDto;
    public static ImagesInAlbumFolderCircle imagesInAlbumFolderCircle;
    public JLabel loading;
    public boolean isLoading;
    private JPanel scrollContent;
    private ArrayList<JsonFields> imgList;

    public static ImagesInAlbumFolderCircle getInstance() {
        return imagesInAlbumFolderCircle;
    }

    public ImagesInAlbumFolderCircle(final JsonFields album, final long circleId) {
        this.imagesInAlbumFolderCircle = this;
        this.album = album;
        this.circleId = circleId;
        this.albumDto = new NewsFeedWithMultipleImage();
        this.albumDto.setUserIdentity(this.circleId + "");
        this.albumDto.setAlbn(this.album.getAlbn());
        this.albumDto.setTm(this.album.getUt());
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JPanel headerPanle = new JPanel();
        headerPanle.setLayout(new BorderLayout());
        headerPanle.setPreferredSize(new Dimension(0, DefaultSettings.SMALL_PANEL_HEIGHT));
        headerPanle.setOpaque(false);
        //   headerPanle.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        JLabel albumName = DesignClasses.makeLableBold1(album.getAlbn());
        albumName.setBorder(new EmptyBorder(1, 5, 1, 20));
        headerPanle.add(albumName, BorderLayout.WEST);

        JPanel closePanel = new JPanel();
        closePanel.setOpaque(false);
        closePanel.setPreferredSize(new Dimension(DefaultSettings.SMALL_PANEL_HEIGHT + 30, DefaultSettings.SMALL_PANEL_HEIGHT + 30));
        headerPanle.add(closePanel, BorderLayout.EAST);
        final JLabel closeButton = DesignClasses.create_imageJlabel(GetImages.BACK_MINI);
        closeButton.setToolTipText("Back");
        closeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
//                if (circleDetails != null) {
//                    circleDetails.onTabClick(CircleProfile.TYPE_ALBUM);
//                    GuiRingID.getInstance().loadIntoMainRightContent(circleDetails);
//                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI_H));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
            }
        });
        closePanel.add(closeButton);
        add(headerPanle, BorderLayout.NORTH);

        content.setOpaque(false);
        int columns = GuiRingID.getInstance().getMainRight().getWidth() / 150;
        // content.setLayout(new GridLayout(0, columns, 10, 10));
        content.setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10));

        scrollContent = new JPanel(new BorderLayout(0, 0));
        scrollContent.setOpaque(false);
        scrollContent.setBorder(new EmptyBorder(10, 2, 10, 10));
        scrollContent.add(content, BorderLayout.CENTER);

        final JScrollPane downContent = DesignClasses.getDefaultScrollPane(scrollContent);
        downContent.setPreferredSize(new Dimension(0, 65));
        downContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        downContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int containerHeight = scrollContent.getHeight();
                int topHeight = e.getValue();
                int barHeight = downContent.getVerticalScrollBar().getHeight();
                int bottomHeight = containerHeight - (topHeight + barHeight);
                int totalCalHeight = (topHeight + barHeight + bottomHeight);
                if (isLoading == false && ((topHeight != 0 && bottomHeight == 0) || (!downContent.getVerticalScrollBar().isVisible() && barHeight > 0))) {
                    isLoading = true;
                    addloding();
                    new SendFriendsInfoRequest(circleId + "", AppConstants.TYPE_FRIEND_ALBUM_IMAGES, NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "").get(album.getAlbId()).size(), album.getAlbId()).start();
                }

            }

        });

        add(downContent, BorderLayout.CENTER);
        addImages();
    }

    public void addloding() {
        loading = DesignClasses.loadingLable(true);
        content.add(loading);
        content.revalidate();
    }

    public void addMoreImages(ArrayList<JsonFields> moreImagesMap) {
        content.remove(loading);
        Map<Long, JsonFields> tempMap = new HashMap<Long, JsonFields>();
        long[] times = new long[moreImagesMap.size()];
        if (moreImagesMap != null && !moreImagesMap.isEmpty()) {
            int e = 0;
            for (JsonFields fi : moreImagesMap) {
                fi.setUserIdentity(circleId + "");
                tempMap.put(fi.getTm(), fi);
                times[e] = fi.getTm();
                e++;
            }
            Arrays.sort(times);
            for (int k = 0; k < times.length; k++) {
                albumDto.getImageList().add(tempMap.get(times[k]));
            }

            /*for (JsonFields js : albumDto.getImageList()) {
             SingleImageInAlbum img = new SingleImageInAlbum(albumDto, js);
             img.setCursor(new Cursor(Cursor.HAND_CURSOR));
             content.add(img);
             }*/
            for (int k = 0; k < times.length; k++) {
                JsonFields nev = tempMap.get(times[k]);
                SingleImageInAlbum img = new SingleImageInAlbum(albumDto, nev);
                img.setCursor(new Cursor(Cursor.HAND_CURSOR));
                content.add(img);
            }
        }
        content.revalidate();

    }

    public void removeLoadingcontent() {
        isLoading = false;
        content.remove(loading);
        content.revalidate();
    }

    public void addImages() {
        new LoadImagesFromAlbum().start();
    }

    class LoadImagesFromAlbum extends Thread {

        @Override
        public void run() {
            try {

                loading = DesignClasses.loadingLable(false);
                content.add(loading, BorderLayout.CENTER);
                content.revalidate();
                Thread.sleep(5000);
                content.removeAll();

                Map<Long, String> tempMap = new HashMap<Long, String>();
                long[] times = new long[NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "").get(album.getAlbId()).size()];
                imgList = new ArrayList<JsonFields>();

                Map<String, JsonFields> albumImages = NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "").get(album.getAlbId());
                if (albumImages != null && !albumImages.isEmpty()) {
                    int e = 0;
                    for (String key : albumImages.keySet()) {
                        JsonFields fi = NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "").get(album.getAlbId()).get(key);
                        fi.setUserIdentity(circleId + "");
                        tempMap.put(fi.getTm(), key);
                        times[e] = fi.getTm();
                        e++;
                    }

                    Arrays.sort(times);

                    for (int k = 0; k < times.length; k++) {
                        String key = tempMap.get(times[k]);
                        JsonFields nev = albumImages.get(key);
                        imgList.add(nev);
                    }
                    albumDto.setImageList(imgList);

                    for (JsonFields js : imgList) {
                        SingleImageInAlbum img = new SingleImageInAlbum(albumDto, js);
                        img.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        content.add(img);
                    }

                    /*for (int k = 0; k < times.length; k++) {
                     String key = tempMap.get(times[k]);
                     JsonFields nev = albumImages.get(key);
                     SingleImageInAlbum img = new SingleImageInAlbum(albumDto, nev);
                     img.setCursor(new Cursor(Cursor.HAND_CURSOR));
                     content.add(img);
                     }*/
                }
                content.revalidate();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in LoadImagesFromAlbum ==>" + e.getMessage());
            }
        }
    }
}
