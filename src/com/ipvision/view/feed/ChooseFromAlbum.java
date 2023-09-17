/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.AlbumRequest;
import com.ipvision.service.AlbumsImages;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageHelpers;
import static com.ipvision.view.utility.ImageHelpers.getImageNameFromUrl;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Faiz Ahmed
 */
public class ChooseFromAlbum extends JDialog {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChooseFromAlbum.class);
    private JLabel containerName;
    private JPanel closePanel;
    private Container contentPane;// = getContentPane();
    int width = DefaultSettings.FRAME_DEFAULT_WIDTH - 150;
    private NewStatus newStatus;
    private String selectionText = "Selected Images : #";
    private JButton usePicture = new RoundedCornerButton("Use picture", "Use Selected Pictures");//DesignClasses.createImageButton(GetImages.USE_PICTURE, GetImages.USE_PICTURE_H, "Use Selected Pictures");
    private JButton resetPicture = new RoundedCornerButton("Reset", "Reset Selection");
    //DesignClasses.createImageButton(GetImages.RESET, GetImages.RESET_H, "Reset Selection");
    private JLabel selectionLabel;
    public JsonFields selectedAlbum;
    public JLabel loading;
    private Map<String, Entity> selectedImagesMap = new ConcurrentHashMap<String, Entity>();
    private String dateformat = "hh:mm aaa dd MMM yyyy";
    private JPanel scrollContent;
    private JScrollPane downContent;
    private JPanel content = new JPanel();
    private int commonWidth = 150;
    private int maxImageHeight = 120;
    private JPanel profileAndAlbums;
    private JPanel addImagesFromAlbum;
    public boolean isLoading;
    public JLabel albumLoadingLabel = new JLabel();
    private static final int TYPE_FULL_URL = 1;
    private static final int TYPE_300_URL = 2;

    private static ChooseFromAlbum instance;

    public static ChooseFromAlbum getInstance() {
        return instance;
    }

    public ChooseFromAlbum(NewStatus newStatusPanel) {
        this.instance = this;
        this.newStatus = newStatusPanel;
        setSize(width, DefaultSettings.FRAME_DEFAULT_HEIGHT - 50);
        setModal(true);
        setTitle("Choose from Album");
        setResizable(false);
        ImageIcon imageIcon = DesignClasses.return_image(GetImages.APP_LOGO);
        Image image = imageIcon.getImage();
        setIconImage(image);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                selectedImagesMap.clear();
                resetSelectedCount();
                disposethis();
            }
        });
        contentPane = getContentPane();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout());

        JPanel headerPanle = new JPanel();
        headerPanle.setBorder(new EmptyBorder(0, 15, 0, 0));
        headerPanle.setLayout(new BorderLayout());
        headerPanle.setPreferredSize(new Dimension(width - 40, DefaultSettings.SMALL_PANEL_HEIGHT));
        headerPanle.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);

        containerName = DesignClasses.makeLableBold1("My Album", 16);
        containerName.setBorder(new EmptyBorder(1, 5, 1, 20));
        headerPanle.add(containerName, BorderLayout.WEST);

        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectedPanel.setOpaque(false);

        selectionLabel = DesignClasses.makeLableBold1(selectionText);
        selectionLabel.setForeground(DefaultSettings.APP_DEFAULT_THEME_COLOR);
        selectionLabel.setBorder(new EmptyBorder(1, 5, 1, 20));
        selectedPanel.add(selectionLabel);
        headerPanle.add(selectedPanel, BorderLayout.CENTER);

        closePanel = new JPanel();
        closePanel.setOpaque(false);
        closePanel.setPreferredSize(new Dimension(60, DefaultSettings.SMALL_PANEL_HEIGHT));
        closePanel.setPreferredSize(new Dimension(DefaultSettings.SMALL_PANEL_HEIGHT, DefaultSettings.SMALL_PANEL_HEIGHT));

        final JButton closeButton = DesignClasses.createImageButton(GetImages.BACK, GetImages.BACK_H, "Back");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMyAlbumsFolders();
            }
        });

        closePanel.add(closeButton);
        headerPanle.add(closePanel, BorderLayout.EAST);
        contentPane.add(headerPanle, BorderLayout.NORTH);

        profileAndAlbums = new JPanel();
        profileAndAlbums.setLayout(new BorderLayout(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
        profileAndAlbums.setBackground(Color.WHITE);
        profileAndAlbums.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
        contentPane.add(profileAndAlbums, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 5, 15));
        buttonPanel.add(resetPicture);
        buttonPanel.add(usePicture);

        usePicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Entry<String, Entity> entry : selectedImagesMap.entrySet()) {
                    final String iurl = entry.getKey();
                    final Entity entity = entry.getValue();
                    String imageName = getImageNameFromUrl(iurl);
                    // final String imageName = MyfnfHashMaps.getInstance().getBufferedImages().get(iurl);
                    if (imageName != null) {
                        try {
                            File file = new File(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator + imageName);
                            if (file.exists()) {
                                newStatus.previewStatusImagePanel.singleUploadedImageBlock(file, entity.getImageId(), iurl, entity.getCptn());
                                continue;
                            }

                            file = new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName);
                            if (file.exists()) {
                                newStatus.previewStatusImagePanel.singleUploadedImageBlock(file, entity.getImageId(), iurl, entity.getCptn());
                                continue;
                            }

                            file = new File(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + imageName);
                            if (file.exists()) {
                                newStatus.previewStatusImagePanel.singleUploadedImageBlock(file, entity.getImageId(), iurl, entity.getCptn());
                                continue;
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
                newStatus.refreshPreviewImageAlbum();
                selectedImagesMap.clear();
                resetSelectedCount();
                disposethis();
            }
        });

        resetPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedAlbum != null) {
                    buildAlbumImage(selectedAlbum);
                }
                selectedImagesMap.clear();
                resetSelectedCount();
            }
        });

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        closePanel.setVisible(false);
        selectionLabel.setVisible(false);
        usePicture.setVisible(false);
        resetPicture.setVisible(false);
    }

    public void initContainers() {
        try {
            selectedAlbum = null;
            isLoading = false;

            selectedImagesMap.clear();
            content.setOpaque(false);

            JPanel nullPanle = new JPanel();
            nullPanle.setOpaque(false);
            nullPanle.add(content, BorderLayout.CENTER);

            scrollContent = new JPanel(new BorderLayout(0, 0));
            scrollContent.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            scrollContent.setOpaque(false);
            scrollContent.setBackground(Color.WHITE);
            scrollContent.add(nullPanle, BorderLayout.CENTER);

            downContent = DesignClasses.getDefaultScrollPane(scrollContent);// DesignClasses.getDefaultScorllPane(scrollContent);
            downContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    if (selectedAlbum != null) {
                        int containerHeight = scrollContent.getHeight();
                        int topHeight = e.getValue();
                        int barHeight = downContent.getVerticalScrollBar().getHeight();
                        int bottomHeight = containerHeight - (topHeight + barHeight);
                        int totalCalHeight = (topHeight + barHeight + bottomHeight);

                        if (isLoading == false && (topHeight != 0 && bottomHeight == 0)) {
                            log.warn("TH = " + topHeight + ", BH = " + barHeight + ", BOH = " + bottomHeight + ", TOH = " + totalCalHeight);
                            isLoading = true;
                            addloadingContent();
                            new AlbumsImages(selectedAlbum.getAlbId(), MyfnfHashMaps.getInstance().getAlBumImages().get(selectedAlbum.getAlbId()).size()).start();
                        }
                    }
                }
            });
            profileAndAlbums.add(downContent, BorderLayout.CENTER);
            closePanel.setVisible(false);
            resetSelectedCount();

            if (!NewsFeedMaps.getInstance().getMyAllbums().isEmpty()) {
                addMyAlbumsFolders();
            } else {
                loadMyAlbumFromServer();
            }
        } catch (Exception e) {
        }
    }

    public void loadMyAlbumFromServer() {
        albumLoadingLabel = new JLabel();
        albumLoadingLabel.setOpaque(false);
        albumLoadingLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_60));
        content.add(albumLoadingLabel);
        new AlbumRequest(AlbumRequest.TRIGGER_BY_CHOOSE_IMAGE_POST).start();
    }

    public synchronized void addMyAlbumsFolders() {
        containerName.setText("My Albums");
        closePanel.setVisible(false);
        selectedAlbum = null;
        isLoading = false;
        if (!NewsFeedMaps.getInstance().getMyAllbums().isEmpty()) {
            int columns = width / commonWidth;

            content.removeAll();
            content.setLayout(new GridLayout(0, columns - 1, 10, 10));

            Map<Long, String> tempMap = new HashMap<Long, String>();
            long[] times = new long[NewsFeedMaps.getInstance().getMyAllbums().size()];
            int e = 0;
            for (String albmID : NewsFeedMaps.getInstance().getMyAllbums().keySet()) {
                final JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(albmID);
                tempMap.put(fields.getUt(), albmID);
                times[e] = fields.getUt();
                e++;
            }
            Arrays.sort(times);
            for (int k = times.length - 1; k >= 0; k--) {

                String albumId = tempMap.get(times[k]);
                final JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(albumId);
                //  int e = 0;
                /*for (String albmID : NewsFeedMaps.getInstance().getMyAllbums().keySet()) {
                 JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(albmID);
                 tempMap.put(fields.getUt(), albmID);
                 times[e] = fields.getUt();
                 e++;
                 }*/

                JPanel album = new JPanel();
                album.setLayout(new BorderLayout(0, 2));
                album.setOpaque(false);
                album.setPreferredSize(new Dimension(commonWidth + 20, maxImageHeight + 40));

                JLabel folderImage = new JLabel();
                folderImage.setBorder(DefaultSettings.DEFAULT_BORDER);
                folderImage.setBounds(11, 14, commonWidth - 22, maxImageHeight - 27);

                ImagePane albumPicPanel = new ImagePane();
                albumPicPanel.setLayout(null);
                try {
                    albumPicPanel.setImage(ImageIO.read(new Object() {
                    }.getClass().getClassLoader().getResource(GetImages.ALBUM_DEFAULT)));
                } catch (IOException ex) {
                    //log.error("Buffer img error" + ex.getMessage());
                }
                albumPicPanel.setPreferredSize(new Dimension(commonWidth, maxImageHeight));
                albumPicPanel.setBackground(new Color(0, 0, 0, 0));
                albumPicPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                albumPicPanel.add(folderImage);

                final JPanel albumContainerPane = new JPanel(new GridBagLayout());
                ///albumContainerPane.setBorder(new LineBorder(DEFAUTL_BORDER_COLOR, 5));
                albumContainerPane.setOpaque(false);
                albumContainerPane.setBorder(DefaultSettings.DEFAULT_BORDER);
                albumContainerPane.add(albumPicPanel);

                new LoadImageInLabel(folderImage, fields.getCvImg(), fields.getImT() == null ? 0 : fields.getImT(), TYPE_300_URL).start();

                JLabel albumName = DesignClasses.makeLableBoldTooLarge(fields.getAlbn());
                albumName.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel albumDate = DesignClasses.makeJLabel_normal(getDate(fields.getUt()), 10, DefaultSettings.text_color2);

                JPanel albumNameDate = new JPanel(new BorderLayout());
                albumNameDate.setOpaque(false);
                albumNameDate.add(albumName, BorderLayout.NORTH);
                albumNameDate.add(albumDate, BorderLayout.CENTER);

                album.setToolTipText("Click to show all images of " + fields.getAlbn());
                album.add(albumNameDate, BorderLayout.NORTH);
                album.add(albumContainerPane, BorderLayout.SOUTH);
                album.setCursor(new Cursor(Cursor.HAND_CURSOR));

                album.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            if (MyfnfHashMaps.getInstance().getAlBumImages().isEmpty()) {
                                new AlbumsImages(fields.getAlbId(), 0).start();
                            } else if (MyfnfHashMaps.getInstance().getAlBumImages().get(fields.getAlbId()) == null) {
                                new AlbumsImages(fields.getAlbId(), 0).start();
                            }
                        } catch (Exception ex) {
                        }
                        buildAlbumImage(fields);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        albumContainerPane.setBorder(DefaultSettings.DEFAULT_BORDER_H);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        albumContainerPane.setBorder(DefaultSettings.DEFAULT_BORDER);
                    }
                });

                content.add(album);
            }
            content.revalidate();
            content.repaint();
        }
        resetSelectedCount();
    }

    private String getDate(long milliSeconds) {
        Date date = new Date(milliSeconds);
        DateFormat format = new SimpleDateFormat(dateformat);
        String formatted = format.format(date);
        return formatted;
    }

    private class LoadImageInLabel extends Thread {

        private JLabel imageContainerLabel;
        private String url;
        private int imageType;
        private int urlType;

        public LoadImageInLabel(JLabel imageLabel, String url, int imageType, int urlType) {
            imageContainerLabel = imageLabel;
            this.url = url;
            this.imageType = imageType;
            this.urlType = urlType;
        }

        @Override
        public void run() {
            BufferedImage img = null;
            imageContainerLabel.setBounds(63, 46, 40, 40);
            imageContainerLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT));
            try {
                if (url != null && url.length() > 0) {
                    if (urlType == TYPE_300_URL) {
                        img = ImageHelpers.getBookBuffedImageFromUrl(ImageHelpers.get300Url(url), imageType);
                        if (img == null) {
                            img = ImageHelpers.getBookBuffedImageFromUrl(url, imageType);
                        }
                    } else {
                        img = ImageHelpers.getBookBuffedImageFromUrl(url, imageType);
                    }
                }
            } catch (Exception ex) {
                img = null;
                imageContainerLabel.setIcon(null);
            }

            if (img != null) {
                imageContainerLabel.setBounds(11, 14, commonWidth - 22, maxImageHeight - 27);
                int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
                BufferedImage resizedImage = new BufferedImage((int) commonWidth, (int) maxImageHeight, type);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(img, 0, 0, (int) commonWidth, (int) maxImageHeight, null);
                g.dispose();

                g.setComposite(AlphaComposite.Src);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                imageContainerLabel.setIcon(new ImageIcon(resizedImage));
            }
            imageContainerLabel.revalidate();
            imageContainerLabel.repaint();
        }
    }

    public void buildAlbumImage(JsonFields album) {
        containerName.setText(album.getAlbn());
        closePanel.setVisible(true);
        selectedAlbum = album;

        content.removeAll();
        content.setLayout(new BorderLayout(5, 5));
        content.revalidate();

        int columns = width / commonWidth;
        int h = content.getHeight() / (commonWidth + 5);
        int capacity = columns * h;

        addImagesFromAlbum = new JPanel();
        addImagesFromAlbum.setOpaque(false);
        addImagesFromAlbum.setLayout(new GridLayout(0, columns, 10, 10));
        addloadingContent();
        content.add(addImagesFromAlbum, BorderLayout.CENTER);

        content.revalidate();
        content.repaint();
        new LoadImageFromAlbum(album, capacity).start();
        resetSelectedCount();
    }

    private class LoadImageFromAlbum extends Thread {

        JsonFields albm;
        int capacity;

        LoadImageFromAlbum(JsonFields albm, int capacity) {
            this.albm = albm;
            this.capacity = capacity;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                addImagesFromAlbum.removeAll();
                Map<String, JsonFields> albumImages = MyfnfHashMaps.getInstance().getAlBumImages().get(albm.getAlbId());
                if (albumImages != null && !albumImages.isEmpty()) {
                    for (String key : albumImages.keySet()) {
                        buildSingleImage(albumImages.get(key));
                    }
                    addImagesFromAlbum.revalidate();
                    addImagesFromAlbum.repaint();
                }

                if (albumImages != null && capacity >= albumImages.size()) {
                    isLoading = true;
                    addloadingContent();
                    new AlbumsImages(albm.getAlbId(), albumImages.size()).start();
                }
            } catch (Exception ex) {
            }
        }
    }

    public void buildSingleImage(final JsonFields nev) {
        final JLabel imageInsideAlbum = new JLabel();
        imageInsideAlbum.setToolTipText("Click to select this image");
        imageInsideAlbum.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imageInsideAlbum.setPreferredSize(new Dimension(commonWidth, commonWidth));

        if (selectedImagesMap.get(nev.getIurl()) == null) {
            imageInsideAlbum.setBorder(DefaultSettings.DEFAULT_BORDER);
        } else {
            imageInsideAlbum.setBorder(DefaultSettings.DEFAULT_LINE_BORDER);
        }

        if (nev.getIurl() != null) {
            new LoadImageInLabel(imageInsideAlbum, nev.getIurl(), nev.getImT(), TYPE_FULL_URL).start();
            addImagesFromAlbum.add(imageInsideAlbum);
            imageInsideAlbum.addMouseListener(new MouseAdapter() {
                //boolean isAlreadyOneClick;

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedImagesMap.get(nev.getIurl()) == null) {
                        selectedImagesMap.put(nev.getIurl(), new Entity(nev.getImageId(), nev.getCptn()));
                        imageInsideAlbum.setBorder(DefaultSettings.DEFAULT_LINE_BORDER);
                        resetSelectedCount();
                    } else {
                        selectedImagesMap.remove(nev.getIurl());
                        resetSelectedCount();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    imageInsideAlbum.setBorder(DefaultSettings.DEFAULT_BORDER_H);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (selectedImagesMap.get(nev.getIurl()) == null) {
                        imageInsideAlbum.setBorder(DefaultSettings.DEFAULT_BORDER);
                    } else {
                        imageInsideAlbum.setBorder(DefaultSettings.DEFAULT_LINE_BORDER);
                    }
                }
            });
        }
    }

    public void addMoreImages(ArrayList<JsonFields> moreImagesMap) {
        if (addImagesFromAlbum != null) {

            removeLoadingContent();
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
                    buildSingleImage(tempMap.get(times[k]));
                }
                addImagesFromAlbum.revalidate();
                addImagesFromAlbum.repaint();
            }
        }
    }

    public void addloadingContent() {
        loading = DesignClasses.loadingLable(true);
        addImagesFromAlbum.add(loading);
        addImagesFromAlbum.revalidate();
        addImagesFromAlbum.repaint();
    }

    public void removeLoadingContent() {
        addImagesFromAlbum.remove(loading);
        addImagesFromAlbum.revalidate();
        addImagesFromAlbum.repaint();
    }

    public void resetSelectedCount() {
        if (!selectedImagesMap.isEmpty()) {
            selectionLabel.setText(selectionText + selectedImagesMap.size());
            usePicture.setVisible(true);
            resetPicture.setVisible(true);
            selectionLabel.setVisible(true);
        } else {
            selectionLabel.setText("");
            usePicture.setVisible(false);
            resetPicture.setVisible(false);
            selectionLabel.setVisible(false);
        }
    }

    public void disposethis() {
        instance = null;
        dispose();
    }

    public class Entity {

        private Long imageId;
        private String cptn;

        public Entity(Long imageId, String cptn) {
            this.imageId = imageId;
            this.cptn = cptn;
        }

        public Long getImageId() {
            return imageId;
        }

        public void setImageId(Long imageId) {
            this.imageId = imageId;
        }

        public String getCptn() {
            return cptn;
        }

        public void setCptn(String cptn) {
            this.cptn = cptn;
        }
    }
}
