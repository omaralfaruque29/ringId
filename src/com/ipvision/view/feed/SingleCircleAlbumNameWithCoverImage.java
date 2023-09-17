/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ipvision.model.JsonFields;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Shahadat Hoosain
 */
public class SingleCircleAlbumNameWithCoverImage extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleCircleAlbumNameWithCoverImage.class);
    private JLabel folderImage;
    private JPanel albumNameDate;
    private int commonWidth = 146;
    private int maxImageHeight = 120;
    private JsonFields singleAlbum;
    private ImagePane albumPicPanel;
    private JPanel albumContainerPane;
    private String dateformat = "hh:mm aaa dd MMM yyyy";
    private long circleId;

    public SingleCircleAlbumNameWithCoverImage(JsonFields singleAlbum, long circleId) {
        this.circleId = circleId;
        this.singleAlbum = singleAlbum;

        setLayout(new BorderLayout(0, 2));
        setOpaque(false);
        setPreferredSize(new Dimension(commonWidth + 20, maxImageHeight + 40));
        folderImage = new JLabel();
        folderImage.setBorder(DefaultSettings.DEFAULT_BORDER);
        folderImage.setBounds(11, 14, commonWidth - 22, maxImageHeight - 27);

        albumPicPanel = new ImagePane();
        albumPicPanel.setLayout(null);
        // albumPicPanel.setBackground(new Color(0, 0, 0, 0));
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

        albumContainerPane = new JPanel(new GridBagLayout());
        ///albumContainerPane.setBorder(new LineBorder(DEFAUTL_BORDER_COLOR, 5));
        albumContainerPane.setOpaque(false);
        albumContainerPane.setBorder(DefaultSettings.DEFAULT_BORDER);
        albumContainerPane.add(albumPicPanel);

        addImageDetails(singleAlbum.getCvImg(), singleAlbum.getImT() == null ? 0 : singleAlbum.getImT());

        JLabel albumName = DesignClasses.makeLableBoldTooLarge(singleAlbum.getAlbn());
        albumName.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel albumDate = DesignClasses.makeJLabel_normal(getDate(singleAlbum.getUt()), 10, DefaultSettings.text_color2);

        albumNameDate = new JPanel(new BorderLayout());
        albumNameDate.setOpaque(false);
        albumNameDate.add(albumName, BorderLayout.NORTH);
        albumNameDate.add(albumDate, BorderLayout.CENTER);

        add(albumNameDate, BorderLayout.NORTH);
        add(albumContainerPane, BorderLayout.SOUTH);

        albumContainerPane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addImagesInDetailsView();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // albumContainerPane.setBorder(new LineBorder(APP_DEFAULT_THEME_COLOR, 5));
                albumContainerPane.setBorder(DefaultSettings.DEFAULT_BORDER_H);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //albumContainerPane.setBorder(new LineBorder(DEFAUTL_BORDER_COLOR, 5));
                albumContainerPane.setBorder(DefaultSettings.DEFAULT_BORDER);
            }
        });
        albumNameDate.addMouseListener(mouse_listner);
    }

    private void addImageDetails(String url, int imageType) {
        try {
            new LoadImage(folderImage, url, imageType).start();
        } catch (Exception e) {
        }
    }
    private MouseListener mouse_listner = new MouseAdapter() {
        Font original;

        @Override
        public void mouseEntered(MouseEvent e) {
            original = e.getComponent().getFont();
            Map attributes = original.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            e.getComponent().setFont(original.deriveFont(attributes));

        }

        @Override
        public void mouseExited(MouseEvent e) {
            e.getComponent().setFont(original);
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {
            addImagesInDetailsView();
        }
    };

    private void addImagesInDetailsView() {
        Map<String, Map<String, JsonFields>> map = NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "");
        try {
            if (map == null || map.isEmpty()) {
                (new SendFriendsInfoRequest(circleId + "", AppConstants.TYPE_FRIEND_ALBUM_IMAGES, 0, singleAlbum.getAlbId())).start();
                Thread.sleep(1000);
            } else if (map.get(singleAlbum.getAlbId()) == null || map.get(singleAlbum.getAlbId()).isEmpty()) {
                (new SendFriendsInfoRequest(circleId + "", AppConstants.TYPE_FRIEND_ALBUM_IMAGES, 0, singleAlbum.getAlbId())).start();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        }

        ImagesInAlbumFolderCircle imagesInAlbumFolderCircle = new ImagesInAlbumFolderCircle(singleAlbum, circleId);
        GuiRingID.getInstance().loadIntoMainRightContent(imagesInAlbumFolderCircle);
        new checkForMore(imagesInAlbumFolderCircle).start();
    }

    class checkForMore extends Thread {

        ImagesInAlbumFolderCircle imagesInAlbumFolderCircle;

        private checkForMore(ImagesInAlbumFolderCircle imagesInAlbumFolderCircle) {
            this.imagesInAlbumFolderCircle = imagesInAlbumFolderCircle;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                //ex.printStackTrace();

                log.error("Error in here ==>" + ex.getMessage());
            }
            if (NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "").get(singleAlbum.getAlbId()) != null && (imagesInAlbumFolderCircle.getWidth() > DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH - 50 || imagesInAlbumFolderCircle.getHeight() > DefaultSettings.MAIN_RIGHT_CONTENT_HEIGHT)) {
                ImagesInAlbumFolderFriend.getInstance().isLoading = true;
                ImagesInAlbumFolderFriend.getInstance().addloding();
                new SendFriendsInfoRequest(circleId + "", AppConstants.TYPE_FRIEND_ALBUM_IMAGES, NewsFeedMaps.getInstance().getFriendsAlbumImages().get(circleId + "").get(singleAlbum.getAlbId()).size(), singleAlbum.getAlbId()).start();
            }

        }
    }

    class LoadImage extends Thread {

        private JLabel imageContainerLabel;
        private String url;
        private int imageType;

        LoadImage(JLabel imageLabel, String url, int imageType) {
            imageContainerLabel = imageLabel;
            this.url = url;
            this.imageType = imageType;
        }

        @Override
        public void run() {

            BufferedImage img = null;
            imageContainerLabel.setBounds(63, 46, 40, 40);
            imageContainerLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT));
            try {
                if (url != null && url.length() > 0) {
                    img = ImageHelpers.getBookBuffedImageFromUrl(ImageHelpers.get300Url(url), imageType);
                    if (img == null) {
                        img = ImageHelpers.getBookBuffedImageFromUrl(url, imageType);
                    }
                }
                //HelperMethods.setImageInLabelFromBufferedImage(imageContainerLabel, commonWidth, maxImageHeight  , img);
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

    private String getDate(long milliSeconds) {
        Date date = new Date(milliSeconds);
        DateFormat format = new SimpleDateFormat(dateformat);
        String formatted = format.format(date);
        return formatted;
    }
}
