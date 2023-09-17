/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.uploaddownload.CreateAlbumPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.AlbumsImages;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Wasif Islam
 */
public class ImagesInAlbumFolder extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImagesInAlbumFolder.class);
    private JPanel content = new JPanel();
    private JsonFields album;
    private CreateAlbumPanel crtAlbm;
    private NewsFeedWithMultipleImage albumDto;
    public static ImagesInAlbumFolder imagesInAlbum;
    public JLabel loading;
    public boolean isLoading;
    private JPanel scrollContent;
    private ArrayList<JsonFields> imgList;

    public static ImagesInAlbumFolder getInstance() {
        return imagesInAlbum;
    }

    public ImagesInAlbumFolder(final JsonFields album) {
        this.imagesInAlbum = this;
        this.album = album;
        this.albumDto = new NewsFeedWithMultipleImage();
        this.albumDto.setAlbn(this.album.getAlbn());
        this.albumDto.setTm(this.album.getUt());
        setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
        setBackground(Color.WHITE);
        JPanel headerPanle = new JPanel();
        headerPanle.setLayout(new BorderLayout());
        headerPanle.setPreferredSize(new Dimension(0, DefaultSettings.SMALL_PANEL_HEIGHT));
        headerPanle.setOpaque(false);
        // headerPanle.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        JLabel albumName = DesignClasses.makeLableBold1(album.getAlbn());
        albumName.setBorder(new EmptyBorder(1, 5, 1, 20));
        headerPanle.add(albumName, BorderLayout.WEST);

        JPanel closePanel = new JPanel();
        closePanel.setOpaque(false);
        closePanel.setPreferredSize(new Dimension(DefaultSettings.SMALL_PANEL_HEIGHT + 30, DefaultSettings.SMALL_PANEL_HEIGHT + 30));
        headerPanle.add(closePanel, BorderLayout.EAST);
        final JLabel backButton = DesignClasses.create_imageJlabel(GetImages.BACK_MINI);
        backButton.setToolTipText("Back");
        backButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // BookMain bookMain = BookMain.getBookMain();
                //bookMain.addMyallbums();
                GuiRingID.getInstance().loadIntoMainRightContent(GuiRingID.getInstance().getMainRight().getMyProfilePanel());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI_H));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
            }
        });

        final JButton addNewImagesBtn = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add new photos");
        addNewImagesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        final String album_name = album.getAlbn();
        final String album_id = album.getAlbId();
        addNewImagesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (crtAlbm != null) {
                    crtAlbm.dispose_create_album();
                    crtAlbm = null;
                }
                crtAlbm = new CreateAlbumPanel(album_name, album_id);
                GuiRingID.getInstance().loadIntoMainRightContent(crtAlbm);
            }
        });
        closePanel.add(addNewImagesBtn);
        closePanel.add(backButton);
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

                //log.warn("TH = " + topHeight + ", BH = " + barHeight + ", BOH = " + bottomHeight + ", TOH = " + totalCalHeight);
                if (isLoading == false && topHeight != 0 && bottomHeight == 0) {
                    isLoading = true;
                    addloding();
                    new AlbumsImages(album.getAlbId(), MyfnfHashMaps.getInstance().getAlBumImages().get(album.getAlbId()).size()).start();
                }

            }

        });

        add(downContent, BorderLayout.CENTER);
        addImages();
    }

    public void addloding() {
        //isLoading = true;
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
                fi.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
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
        //isLoading = false;
        content.remove(loading);
        content.revalidate();
        content.repaint();
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

                if (MyfnfHashMaps.getInstance().getAlBumImages().get(album.getAlbId()) != null && MyfnfHashMaps.getInstance().getAlBumImages().get(album.getAlbId()).size() > 0) {
                    Map<Long, String> tempMap = new HashMap<Long, String>();
                    long[] times = new long[MyfnfHashMaps.getInstance().getAlBumImages().get(album.getAlbId()).size()];
                    imgList = new ArrayList<JsonFields>();

                    Map<String, JsonFields> albumImages = MyfnfHashMaps.getInstance().getAlBumImages().get(album.getAlbId());
                    if (albumImages != null && !albumImages.isEmpty()) {

                        int e = 0;
                        for (String key : albumImages.keySet()) {
                            JsonFields fi = MyfnfHashMaps.getInstance().getAlBumImages().get(album.getAlbId()).get(key);
                            fi.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                            //imgList.add(fi);
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

                        /* for (int k = 0; k < times.length; k++) {
                         String key = tempMap.get(times[k]);
                         JsonFields nev = albumImages.get(key);
                         SingleImageInAlbum img = new SingleImageInAlbum(albumDto, nev);
                         img.setCursor(new Cursor(Cursor.HAND_CURSOR));
                         content.add(img);
                         }*/
                    }
                }
                content.revalidate();
            } catch (Exception ex) {
                //  ex.printStackTrace();
                log.error("Error in LoadImagesFromAlbum  ==>" + ex.getMessage());
            }

        }
    }

}
