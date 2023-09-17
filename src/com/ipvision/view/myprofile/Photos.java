/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.AlbumsImages;
import com.ipvision.view.feed.SingleImageInAlbum;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.model.stores.MyfnfHashMaps;
import static com.ipvision.view.myprofile.MyProfilePanel.MENU_TYPE_ABOUT;
import static com.ipvision.view.myprofile.MyProfilePanel.MENU_TYPE_ALBUM;
import static com.ipvision.view.myprofile.MyProfilePanel.MENU_TYPE_BOOK;
import static com.ipvision.view.myprofile.MyProfilePanel.TYPE_ABOUT;
import static com.ipvision.view.myprofile.MyProfilePanel.TYPE_ALBUM;
import static com.ipvision.view.myprofile.MyProfilePanel.TYPE_MY_BOOK;
import com.ipvision.view.utility.MenuPanel;
import com.ipvision.view.utility.SeparatorPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Wasif Islam
 */
public final class Photos extends JPanel {

    private JPanel content = new JPanel();
    public boolean isLoading;
    public JScrollPane scrollContent;
    private static Photos photos;
    public JLabel profiePhotosTab;
    public JLabel coverPhotosTab;
    public JLabel feedPhotosTab;
    public JLabel loadingLabel = new JLabel();
    public static final int TYPE_MY_PROFILE_PHOTO = 1;
    public static final int TYPE_MY_COVER_PHOTO = 2;
    public static final int TYPE_MY_FEED_PHOTO = 3;
    public int type = 0;
    private boolean isLabelSelected = false;
    private JPanel photosTabPnl;
    public static String PROFILE_IMAGES_STR = "Profile Photos";
    public static String COVER_IMAGES_STR = "Cover Photos";
    public static String FEED_IMAGES_STR = "Feed Photos";
    public static int PROFILE_IMAGES_COUNT = 0;
    public static int COVER_IMAGES_COUNT = 0;
    public static int FEED_IMAGES_COUNT = 0;

    public static Photos getInstance() {
        if (photos == null) {
            photos = new Photos();
        }
        return photos;
    }

    public static void setInstance(Photos photos1) {
        photos = photos1;
    }

    public void setType(int type) {
        this.type = type;
    }

    private Photos() {
        setLayout(new BorderLayout());

        final JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // mainContainer.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 24, 0));
        mainContainer.setOpaque(false);

        content.setLayout(new WrapLayout(WrapLayout.LEFT, 7, 7, DefaultSettings.COVER_PIC_DISPLAY_WIDTH));
        //content.setLayout(new FlowLayout(FlowLayout.LEFT, 11, 11));
        content.setBorder(new MatteBorder(0, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        content.setBackground(Color.WHITE);
        content.setOpaque(false);

        photosTabPnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        photosTabPnl.setOpaque(false);
        photosTabPnl.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 30));
        photosTabPnl.setBackground(Color.WHITE);

        profiePhotosTab = DesignClasses.makeLableBoldTooLarge("");
        profiePhotosTab.setText(PROFILE_IMAGES_STR + " (" + PROFILE_IMAGES_COUNT + ")");
        profiePhotosTab.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        profiePhotosTab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        profiePhotosTab.setBorder(new EmptyBorder(0, 10, 0, 10));
        photosTabPnl.add(profiePhotosTab);
        photosTabPnl.add(new SeparatorPanel(1, 28, 4));
        profiePhotosTab.addMouseListener(mouseListener);

        coverPhotosTab = DesignClasses.makeLableBoldTooLarge("");
        coverPhotosTab.setText(COVER_IMAGES_STR + " (" + COVER_IMAGES_COUNT + ")");
        coverPhotosTab.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        coverPhotosTab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        coverPhotosTab.setBorder(new EmptyBorder(0, 10, 0, 10));
        photosTabPnl.add(coverPhotosTab);
        photosTabPnl.add(new SeparatorPanel(1, 28, 4));
        coverPhotosTab.addMouseListener(mouseListener);

        feedPhotosTab = DesignClasses.makeLableBoldTooLarge("");
        feedPhotosTab.setText(FEED_IMAGES_STR + "(" + FEED_IMAGES_COUNT + ")");
        feedPhotosTab.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        feedPhotosTab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        feedPhotosTab.setBorder(new EmptyBorder(0, 10, 0, 10));
        photosTabPnl.add(feedPhotosTab);
        feedPhotosTab.addMouseListener(mouseListener);
        //photosTab.setBorder(new EmptyBorder(5, 5, 5, 5));

        photosTabPnl.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));

        JPanel tabAndAlbums = new JPanel(new BorderLayout());
        tabAndAlbums.setLayout(new BorderLayout());
        //tabAndAlbums.setOpaque(false);
        //tabAndAlbums.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, 0, MyFnFSettings.DEFAULT_MARGIN, 0));
        tabAndAlbums.add(photosTabPnl, BorderLayout.NORTH);
        tabAndAlbums.add(content, BorderLayout.CENTER);
        tabAndAlbums.setBackground(Color.WHITE);
        //tabAndAlbums.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 25, 0));
//        JPanel nullPanel = new JPanel(new BorderLayout());
//        nullPanel.add(tabAndAlbums, BorderLayout.CENTER);
        //nullPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 0));
        mainContainer.add(tabAndAlbums);
        // mainContainer.add(nullPanel);

        scrollContent = DesignClasses.getDefaultScrollPane(mainContainer);
        scrollContent.setOpaque(false);
        scrollContent.setPreferredSize(new Dimension(0, 65));
        scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                int containerHeight = mainContainer.getHeight();
                int topHeight = e.getValue();
                int barHeight = scrollContent.getVerticalScrollBar().getHeight();
                int bottomHeight = containerHeight - (topHeight + barHeight);
                int totalCalHeight = (topHeight + barHeight + bottomHeight);

                if (isLoading == false && topHeight != 0 && bottomHeight == 0) {
                    isLoading = true;
                    addloding();
                    if (type == TYPE_MY_PROFILE_PHOTO) {
                        new AlbumsImages(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID, 0).start();
                    } else if (type == TYPE_MY_COVER_PHOTO) {
                        new AlbumsImages(MyFnFSettings.COVER_IMAGE_ALBUM_ID, 0).start();
                    } else if (type == TYPE_MY_FEED_PHOTO) {
                        new AlbumsImages(MyFnFSettings.FEED_IMAGE_ALBUM_ID, 0).start();
                    }
                    //new AlbumsImages(MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.LOGIN_USER_ID).size()).start();
                }

            }
        });
        add(scrollContent, BorderLayout.CENTER);

        loadMyAllImagesFromServer();

    }

    public void addloding() {
        //isLoading = true;
        loadingLabel = DesignClasses.loadingLable(true);
        content.add(loadingLabel);
        content.revalidate();
        content.repaint();
    }

    public synchronized void addMyAllImages(String album_id) {
        content.removeAll();
        this.setTabSelectionColor(type);
        this.photosTabPnl.revalidate();
        this.photosTabPnl.repaint();
        for (String imageUrl : MyfnfHashMaps.getInstance().getAlBumImages().get(album_id).keySet()) {
            JsonFields fields = MyfnfHashMaps.getInstance().getAlBumImages().get(album_id).get(imageUrl);
            SingleImageInAlbum img = new SingleImageInAlbum(null, fields);
            img.setCursor(new Cursor(Cursor.HAND_CURSOR));
            content.add(img);
        }

        content.revalidate();
    }

    public void loadMyImagesByAlbumIDFromServer() {
        content.removeAll();
        addloding();
        isLoading = true;
        this.setTabSelectionColor(type);
        this.photosTabPnl.revalidate();
        this.photosTabPnl.repaint();
        if (type == TYPE_MY_PROFILE_PHOTO) {
            new AlbumsImages(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID, 0).start();
        } else if (type == TYPE_MY_COVER_PHOTO) {
            new AlbumsImages(MyFnFSettings.COVER_IMAGE_ALBUM_ID, 0).start();
        } else if (type == TYPE_MY_FEED_PHOTO) {
            new AlbumsImages(MyFnFSettings.FEED_IMAGE_ALBUM_ID, 0).start();
        }
        content.revalidate();
        content.repaint();
//        new AlbumsImages(0).start();
    }

    public void loadMyAllImagesFromServer() {
        content.removeAll();
        addloding();
        isLoading = true;
        this.setTabSelectionColor(type);
        this.photosTabPnl.revalidate();
        this.photosTabPnl.repaint();
        new AlbumsImages(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID, 0).start();
        new AlbumsImages(MyFnFSettings.COVER_IMAGE_ALBUM_ID, 0).start();
        new AlbumsImages(MyFnFSettings.FEED_IMAGE_ALBUM_ID, 0).start();
        content.revalidate();
        content.repaint();
    }

    public void removeLoadingcontent() {
        //isLoading = false;
        content.remove(loadingLabel);
        content.revalidate();
        content.repaint();
    }

    public void addMoreMyImages(ArrayList<JsonFields> moreImagesMap) {
        content.remove(loadingLabel);

        for (JsonFields image : moreImagesMap) {
            SingleImageInAlbum img = new SingleImageInAlbum(null, image);
            img.setCursor(new Cursor(Cursor.HAND_CURSOR));
            content.add(img);
        }
        content.revalidate();

    }
    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == profiePhotosTab) {
                setType(TYPE_MY_PROFILE_PHOTO);
                if (MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID) != null
                        && !MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID).isEmpty()) {
                    addMyAllImages(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID);

                } else {
                    loadMyImagesByAlbumIDFromServer();
                }

            } else if (e.getSource() == coverPhotosTab) {
                setType(TYPE_MY_COVER_PHOTO);
                if (MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.COVER_IMAGE_ALBUM_ID) != null
                        && !MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.COVER_IMAGE_ALBUM_ID).isEmpty()) {
                    addMyAllImages(MyFnFSettings.COVER_IMAGE_ALBUM_ID);
                } else {
                    loadMyImagesByAlbumIDFromServer();
                }

            } else if (e.getSource() == feedPhotosTab) {
                setType(TYPE_MY_FEED_PHOTO);
                if (MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.FEED_IMAGE_ALBUM_ID) != null
                        && !MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.FEED_IMAGE_ALBUM_ID).isEmpty()) {
                    addMyAllImages(MyFnFSettings.FEED_IMAGE_ALBUM_ID);
                } else {
                    loadMyImagesByAlbumIDFromServer();

                }

            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            //setForeground(RingColorCode.RING_THEME_COLOR);
            if (e.getSource() == profiePhotosTab) {
                //setCursor(new Cursor(Cursor.HAND_CURSOR));               
                // setEntered(profiePhotosTab);
            } else if (e.getSource() == coverPhotosTab) {
                //setCursor(new Cursor(Cif (!isLabelSelected) {
                //setEntered(coverPhotosTab);
            } else if (e.getSource() == feedPhotosTab) {
                //setCursor(new Cursor(Cursor.HAND_CURSOR));                
                //setEntered(feedPhotosTab);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (e.getSource() == profiePhotosTab) {
                //setCursor(new Cursor(Cursor.HAND_CURSOR));               
                //setExit(profiePhotosTab);

                //profiePhotosTab.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
            } else if (e.getSource() == coverPhotosTab) {
                //setCursor(new Cursor(Cursor.HAND_CURSOR));                
                // setExit(coverPhotosTab);

            } else if (e.getSource() == feedPhotosTab) {
                //setCursor(new Cursor(Cursor.HAND_CURSOR));               
                // setExit(feedPhotosTab);
            }
        }
    };

    public void setTabSelectionColor(int type) {
        try {
            if (type == TYPE_MY_PROFILE_PHOTO) {
                setSelected(profiePhotosTab);
                setExit(coverPhotosTab);
                setExit(feedPhotosTab);
            } else if (type == TYPE_MY_COVER_PHOTO) {
                setExit(profiePhotosTab);
                setSelected(coverPhotosTab);
                setExit(feedPhotosTab);
            } else if (type == TYPE_MY_FEED_PHOTO) {
                setExit(profiePhotosTab);
                setExit(coverPhotosTab);
                setSelected(feedPhotosTab);
            }
        } catch (Exception e) {
        }
    }

    private void setSelected(JLabel label) {
        label.setForeground(RingColorCode.RING_THEME_COLOR);
    }

    private void setEntered(JLabel label) {
        label.setForeground(RingColorCode.RING_THEME_COLOR);
    }

    private void setExit(JLabel label) {
        label.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
    }
}
