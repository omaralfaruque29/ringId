/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.service.AlbumsImages;
import com.ipvision.service.SendImageUploadRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageHelpers;
import static com.ipvision.view.utility.ImageHelpers.getImageNameFromUrl;
import com.ipvision.view.utility.ImageUtil;
import com.ipvision.view.utility.ImageInformation;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Shahadat Hossain
 */
public class ProfileCoverChangePanel extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProfileCoverChangePanel.class);
    private JPanel mainContentPanel;
    private JButton btnBrowse = DesignClasses.createImageButton(GetImages.OPTION_UPLOAD_PHOTO, GetImages.OPTION_UPLOAD_PHOTO, "Upload from computer");
    private JButton btnCamera = DesignClasses.createImageButton(GetImages.OPTION_TAKE_PHOTO, GetImages.OPTION_TAKE_PHOTO, "Camera");
    private JButton btnBack = new RoundedCornerButton("Cancel", "Back to profile information");
    private JButton btnSave = new RoundedCornerButton("Save Picture", "Save Picture");
    private JPanel imagePanel;
    private JPanel imageWrapperPanel;
    private JPanel container;
    private JCheckBox chkShowInNewsfeed;
    public static ProfileCoverChangePanel instance = null;
    private SingleImagePanel selectedImagePanel = null;
    private static File fileToUpload = null;
    private BufferedImage image = null;
    private int cropX = 0;
    private int cropY = 0;
    private int WIDTH = 145;
    private int HEIGHT = 150;
    private JLabel profileImagelabel = new JLabel();
    private MouseHandler handler;
    private int imageType = MyFnFSettings.PROFILE_IMAGE;
    private ImageIcon defaultProfileImageIcon = DesignClasses.return_image(GetImages.PROFILE_IMAGE_BOX);
    private ImageIcon defaultCoverImageIcon = DesignClasses.return_image(GetImages.COVER_IMAGE_BOX);
    private ImageIcon defaultImageIcon = null;
    private String title = "Profile photo";
    public JPanel scrollPnlTopPC;
    public JPanel buttonAndImageChangePanel;
    JPanel previousRightPanel;

    public static ProfileCoverChangePanel getInstance() {
        if (instance == null) {
            instance = new ProfileCoverChangePanel();
        }
        return instance;
    }

    public static void setFileToUpload(File fileToUpload1) {
        fileToUpload = fileToUpload1;
    }

    public ProfileCoverChangePanel() {
        instance = this;
        fileToUpload = null;
        setLayout(new BorderLayout(0, 0));
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        initContainers();
    }

    public ProfileCoverChangePanel(JPanel prevMainRightPanel) {
        this.previousRightPanel = prevMainRightPanel;
        instance = this;
        fileToUpload = null;
        setLayout(new BorderLayout(0, 0));
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        initContainers();
    }

    private void initContainers() {
        try {
            imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            //imagePanel.setOpaque(false);
            imagePanel.add(profileImagelabel);
            imagePanel.setBackground(Color.WHITE);

            imageWrapperPanel = new JPanel(new BorderLayout());
            imageWrapperPanel.setOpaque(false);
            imageWrapperPanel.add(imagePanel);
            imageWrapperPanel.setBackground(Color.red);

            JPanel wrapper = new JPanel(new BorderLayout());
            //wrapper.setBackground(Color.red);
            wrapper.setOpaque(false);
            add(wrapper, BorderLayout.NORTH);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            topPanel.setBackground(Color.WHITE);
            //topPanel.setBackground(Color.GREEN);
            topPanel.add(imageWrapperPanel);
//            add(topPanel, BorderLayout.NORTH);
            topPanel.setOpaque(false);
            buttonAndImageChangePanel = new JPanel();
            buttonAndImageChangePanel.setLayout(new BoxLayout(buttonAndImageChangePanel, BoxLayout.Y_AXIS));
            buttonAndImageChangePanel.setOpaque(false);

            buttonAndImageChangePanel.add(topPanel);
            wrapper.add(buttonAndImageChangePanel, BorderLayout.CENTER);

            scrollPnlTopPC = new JPanel();
            scrollPnlTopPC.setBackground(Color.ORANGE);
            scrollPnlTopPC.setOpaque(false);
            scrollPnlTopPC.setPreferredSize(new Dimension(10, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT));
            wrapper.add(scrollPnlTopPC, BorderLayout.EAST);
            //scrollPnlTopPC.setVisible(false);

            container = new JPanel(new BorderLayout(0, 0));
            add(container, BorderLayout.CENTER);
            container.setOpaque(false);

            JPanel buttonPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            buttonPanelWrapper.setOpaque(false);
            //buttonPanelWrapper.setBackground(Color.WHITE);
            // buttonPanelWrapper.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            buttonAndImageChangePanel.add(buttonPanelWrapper);

            JPanel buttonPanel = new JPanel(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
            buttonPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH + 4, 38));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
            //buttonPanel.setOpaque(false);

            JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
            leftButtonPanel.setOpaque(false);
            leftButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            leftButtonPanel.add(btnBrowse);
            leftButtonPanel.add(btnCamera);
            buttonPanel.add(leftButtonPanel, BorderLayout.WEST);

            chkShowInNewsfeed = new JCheckBox("");
            chkShowInNewsfeed.setForeground(DefaultSettings.disable_font_color);
            chkShowInNewsfeed.setOpaque(false);
            chkShowInNewsfeed.setVisible(false);

            JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 9));
            centerButtonPanel.setOpaque(false);
            centerButtonPanel.add(chkShowInNewsfeed);
            buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);

            JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 7));
            rightButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 2));
            rightButtonPanel.setOpaque(false);
            rightButtonPanel.add(btnBack);
            rightButtonPanel.add(btnSave);
            btnSave.setEnabled(false);
            buttonPanel.add(rightButtonPanel, BorderLayout.EAST);
            buttonPanelWrapper.add(buttonPanel);
            //masterPanel.add(buttonPanelWrapper, BorderLayout.NORTH);

            //container.add(buttonPanelWrapper, BorderLayout.NORTH);
            JPanel masterPanel = new JPanel();
            masterPanel.setOpaque(false);
            masterPanel.setLayout(new BorderLayout(0, 0));
            masterPanel.setBackground(Color.WHITE);
            masterPanel.setOpaque(false);
            //masterPanel.add(buttonPanelWrapper, BorderLayout.NORTH);

            mainContentPanel = new JPanel(new BorderLayout());
            mainContentPanel.setBackground(Color.WHITE);
            mainContentPanel.setOpaque(false);
            masterPanel.add(mainContentPanel, BorderLayout.CENTER);
            //masterPanel.add(mainContentPanel);
            container.add(masterPanel, BorderLayout.CENTER);

            btnBrowse.addActionListener(actionListener);
            btnCamera.addActionListener(actionListener);
            btnBack.addActionListener(actionListener);
            btnSave.addActionListener(actionListener);

            buildAllPhotosPanel();
        } catch (Exception e) {
        }
    }

    public void initialize(int imageType) {
        try {
            this.imageType = imageType;

            if (this.imageType == MyFnFSettings.PROFILE_IMAGE) {
                title = "Profile photo";
                WIDTH = DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_WIDTH;
                HEIGHT = DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_HEIGHT;
                defaultImageIcon = defaultProfileImageIcon;
                chkShowInNewsfeed.setText(" Show profile photo in NewsFeed");
                btnCamera.setVisible(true);
            } else {
                title = "Cover photo";
                WIDTH = DefaultSettings.COVER_PIC_UPLOAD_SCREEN_WIDTH;
                HEIGHT = DefaultSettings.COVER_PIC_UPLOAD_SCREEN_HEIGHT;
                defaultImageIcon = defaultCoverImageIcon;
                chkShowInNewsfeed.setText(" Show cover photo in NewsFeed");
                btnCamera.setVisible(false);
            }

            imagePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            imagePanel.revalidate();
            imagePanel.repaint();

            imageWrapperPanel.setBorder(BorderFactory.createDashedBorder(new Color(0xa1a1a1), 1.7f, 1.7f, 2.5f, false));
            imageWrapperPanel.setPreferredSize(new Dimension(WIDTH + 4, HEIGHT + 4));
            imageWrapperPanel.revalidate();
            imageWrapperPanel.repaint();

            profileImagelabel.setIcon(defaultImageIcon);
            profileImagelabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            profileImagelabel.revalidate();
            profileImagelabel.repaint();

            fileToUpload = null;
            image = null;
            cropX = 0;
            cropY = 0;

            if (selectedImagePanel != null) {
                selectedImagePanel.setBorder(DefaultSettings.DEFAULT_BORDER);
                selectedImagePanel.revalidate();
                selectedImagePanel.repaint();
                selectedImagePanel = null;
            }

            btnBrowse.setEnabled(true);
            btnCamera.setEnabled(true);
            btnBack.setEnabled(true);
            btnSave.setEnabled(false);
            chkShowInNewsfeed.setVisible(false);
            chkShowInNewsfeed.setEnabled(true);
            chkShowInNewsfeed.setSelected(true);

            showAllPhotos();
            instance.revalidate();
            instance.repaint();

        } catch (Exception ex) {

        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnBrowse) {
                openFile();
                if (selectedImagePanel != null) {
                    selectedImagePanel.setBorder(DefaultSettings.DEFAULT_BORDER);
                    selectedImagePanel.revalidate();
                    selectedImagePanel.repaint();
                    selectedImagePanel = null;
                }
            } else if (e.getSource() == btnCamera) {
                JDialogWebcam webcam = new JDialogWebcam();
                if (JDialogWebcam.fileToUpload != null && JDialogWebcam.fileToUpload.exists()) {
                    fileToUpload = JDialogWebcam.fileToUpload;
                    showPictureInView();
                    if (selectedImagePanel != null) {
                        selectedImagePanel.setBorder(DefaultSettings.DEFAULT_BORDER);
                        selectedImagePanel.revalidate();
                        selectedImagePanel.repaint();
                        selectedImagePanel = null;
                    }
                }
            } else if (e.getSource() == btnBack) {
                //GuiRingID.getInstance().showMyProfile();
                if (GuiRingID.getInstance() != null) {
                    GuiRingID.getInstance().loadIntoMainRightContent(previousRightPanel);
                }
            } else if (e.getSource() == btnSave) {
                btnBrowse.setEnabled(false);
                btnCamera.setEnabled(false);
                btnBack.setEnabled(false);
                btnSave.setEnabled(false);
                chkShowInNewsfeed.setEnabled(false);

                if (fileToUpload != null && fileToUpload.getAbsolutePath() != null && image != null) {
                    SendImageUploadRequest dd = new SendImageUploadRequest(fileToUpload.getAbsolutePath(), image, imageType, cropX, cropY, WIDTH, HEIGHT, (chkShowInNewsfeed.isSelected() ? 1 : 0));
                    dd.start();
                    GuiRingID.getInstance().showMyProfile();
                } else {
                    HelperMethods.showPlaneDialogMessage("Can not upload");
                    initialize(imageType);
                }
            }
        }
    };

    private void openFile() {
        try {
            /*JFileChooser fc = new JFileChooser();
             JFrame dd = new JFrame();
             ImageHelpers.setAppIcon(dd, null);
             fc.setDialogTitle("Open File");
             fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
             //fc.setAccessory(new ImagePreview(fc));
             fc.setCurrentDirectory(new File("."));
             fc.setFileFilter(imageFilter);
             int result = fc.showOpenDialog(dd);

             if (result == JFileChooser.APPROVE_OPTION) {
             fileToUpload = fc.getSelectedFile();
             showPictureInView();
             }*/
            JFrame dd = new JFrame();
            FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
            fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
            fc.setMultipleMode(false);
            fc.show();
            fileToUpload = fc.getFiles()[0];
            if (fileToUpload != null) {
                showPictureInView();
            }
        } catch (Exception ex) {
        }
    }

    public void showPictureInView() {
        image = null;
        try {
            if (fileToUpload != null && DesignClasses.file_exists(fileToUpload.getAbsolutePath())) {
                File file = new File(fileToUpload.getAbsolutePath());
                BufferedImage tmp = ImageIO.read(file);
                try {
                    ImageInformation info = ImageInformation.readImageFileInformation(file);
                    if (info.orientation > 1) {
                        AffineTransform aff = ImageInformation.getExifTransformation(info);
                        tmp = ImageInformation.transformImage(tmp, aff);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.error("Error in showPictureInView ==>" + e.getMessage());
                }

                if (this.imageType == MyFnFSettings.PROFILE_IMAGE) {
                    tmp = ImageUtil.reduceQualityAndSize(tmp, DefaultSettings.MAXIMUM_PROFILE_IMAGE_WIDTH);
                } else {
                    tmp = ImageUtil.reduceQualityAndSize(tmp, DefaultSettings.MAXIMUM_IMAGE_WIDTH);
                }
                if (tmp.getWidth() < WIDTH || tmp.getHeight() < HEIGHT) {
                    HelperMethods.showPlaneDialogMessage(title + "'s minimum resulotion: " + WIDTH + " x " + HEIGHT + "");
                } else {
                    image = tmp;
                }
//                else {
//                    image = ImageUtil.reduceQualityAndSize(tmp, DefaultSettings.MAXIMUM_IMAGE_WIDTH);
//                }
            }
        } catch (Exception ex) {
            log.error("change profile picture ==>" + ex.getMessage());
        }

        if (image != null) {
            chkShowInNewsfeed.setVisible(true);
            btnSave.setEnabled(true);
            profileImagelabel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            profileImagelabel.setIcon(new ImageIcon(image));
            profileImagelabel.setBounds(0, 0, image.getWidth(), image.getHeight());
            profileImagelabel.setHorizontalAlignment(JLabel.LEFT);
            profileImagelabel.setVerticalAlignment(JLabel.TOP);
            handler = new MouseHandler(image, WIDTH, HEIGHT);
            profileImagelabel.addMouseListener(handler);
            profileImagelabel.addMouseMotionListener(handler);
        } else {
            chkShowInNewsfeed.setVisible(false);
            btnSave.setEnabled(false);
            profileImagelabel.setIcon(defaultImageIcon);
            profileImagelabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            profileImagelabel.removeMouseListener(handler);
            profileImagelabel.removeMouseMotionListener(handler);
        }
    }

    protected class MouseHandler extends MouseAdapter {

        private boolean active = false;
        private int xDisp;
        private int yDisp;
        private BufferedImage image;
        private int minX;
        private int minY;
        private int maxX;
        private int maxY;

        public MouseHandler(BufferedImage image, int width, int height) {
            this.image = image;
            this.minX = 0;
            this.minY = 0;
            this.maxX = (-image.getWidth() + width);
            this.maxY = (-image.getHeight() + height);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            active = true;
            JLabel label = (JLabel) e.getComponent();

            xDisp = e.getPoint().x - label.getLocation().x;
            yDisp = e.getPoint().y - label.getLocation().y;

            //label.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            active = false;
            JLabel label = (JLabel) e.getComponent();
            //label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (active) {
                JLabel label = (JLabel) e.getComponent();
                Point point = e.getPoint();

                cropX = point.x - xDisp;
                cropY = point.y - yDisp;
                cropX = cropX > minX ? minX : cropX;
                cropY = cropY > minY ? minY : cropY;
                cropX = cropX < maxX ? maxX : cropX;
                cropY = cropY < maxY ? maxY : cropY;
                //System.err.println("X = " + cropX + ", Y = " + cropY);
                label.setLocation(cropX, cropY);
                label.invalidate();
                label.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    /* *************************************************************
     * SHOW ALL PHOTO
     * ************************************************************/
    private JPanel content = new JPanel();
    public boolean isLoading;
    public JScrollPane scrollContent;
    public JLabel loadingLabel = new JLabel();
    public JPanel mainContainer;

    public void buildAllPhotosPanel() {
        JPanel mainContent = new JPanel(new BorderLayout(0, 0));
        //mainContent.setOpaque(false);
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        JPanel nullPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nullPanel.setBackground(Color.WHITE);
        nullPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH + 1, 0));
        nullPanel.setOpaque(false);
        mainContent.add(nullPanel, BorderLayout.NORTH);

        mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setOpaque(false);
        mainContainer.add(mainContent);

        mainContent.add(content, BorderLayout.CENTER);
        content.setLayout(new WrapLayout(WrapLayout.LEFT, 8, 8, 650));
        content.setOpaque(false);

        scrollContent = DesignClasses.getDefaultScrollPane(mainContainer);
        scrollContent.setOpaque(false);
        scrollContent.setPreferredSize(new Dimension(0, 65));
        scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainContentPanel.add(scrollContent, BorderLayout.CENTER);
    }

    public void showAllPhotos() {
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
                    int size = MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID) == null ? 0 : MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).size();
                    new AlbumsImages((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID, size).start();
                }
            }
        });

        if (MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID) != null) {
            addMyAllImages();
        } else {
            loadMyAllImagesFromServer();
        }

    }

    public void addloding() {
        //isLoading = true;
        loadingLabel = DesignClasses.loadingLable(true);
        content.add(loadingLabel);
        content.revalidate();
    }

    public synchronized void addMyAllImages() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                content.removeAll();
                for (String imageUrl : MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).keySet()) {
                    JsonFields fields = MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).get(imageUrl);
                    SingleImagePanel img = new SingleImagePanel(fields);
                    content.add(img);
                }
                content.revalidate();
            }
        });

    }

    public void loadMyAllImagesFromServer() {
        content.removeAll();
        addloding();
        isLoading = true;
        new AlbumsImages((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID, 0).start();
    }

    public void removeLoadingContent() {
        content.remove(loadingLabel);
        content.revalidate();
        content.repaint();
    }

    public void addMoreMyImages(ArrayList<JsonFields> moreImagesMap) {
        content.remove(loadingLabel);

        for (JsonFields image : moreImagesMap) {
            SingleImagePanel img = new SingleImagePanel(image);
            content.add(img);
        }
        content.revalidate();
    }

    private class SingleImagePanel extends JPanel {

        private int imageWidth = 150;
        private int imageHeight = 150;
        private ImagePane imagePane;
        private SingleImagePanel single;
        private JsonFields nev;
        private String iurl;
        private int imageType;
        private BufferedImage img;

        public SingleImagePanel(JsonFields nev) {
            setLayout(new BorderLayout(0, 2));
            setOpaque(false);
            setToolTipText("Click to select this image");
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(imageWidth, imageHeight));
            setBorder(DefaultSettings.DEFAULT_BORDER);
            this.nev = nev;
            this.iurl = nev.getIurl();
            this.imageType = nev.getImT();
            this.single = this;
            imagePane = new ImagePane();
            add(imagePane);
            init();
        }

        private void init() {
            new ImageLoader().start();
        }

        private class ImageLoader extends Thread {

            @Override
            public void run() {
                try {

                    //imageLabel.setBounds(63, 46, 40, 40);
                    JPanel loadingPanel = new JPanel(new GridBagLayout());
                    loadingPanel.setOpaque(false);

                    JLabel imageLabel = new JLabel();
                    imageLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
                    loadingPanel.add(imageLabel);

                    imagePane.add(loadingPanel, BorderLayout.CENTER);

                    if (iurl != null && iurl.length() > 0) {
                        img = ImageHelpers.getBookBuffedImageFromUrl(iurl, imageType);
                    }

                    if (img != null) {
                        //imageLabel.setBounds(11, 14, imageWidth - 22, imageHeight - 27);
                    /*int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
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
                         imagePane.setIcon(new ImageIcon(resizedImage));*/
                        if (img.getWidth() > img.getHeight()) {
                            if (img.getWidth() > imageWidth) {
                                img = Scalr.resize(img, Scalr.Mode.FIT_TO_WIDTH, imageWidth, imageHeight, Scalr.OP_ANTIALIAS);
                            }
                        } else if (img.getHeight() > img.getWidth()) {
                            if (img.getHeight() > imageHeight) {
                                img = Scalr.resize(img, Scalr.Mode.FIT_TO_HEIGHT, imageWidth, imageHeight, Scalr.OP_ANTIALIAS);
                            }
                        } else {
                            img = Scalr.resize(img, Scalr.Mode.FIT_EXACT, imageWidth, imageHeight, Scalr.OP_ANTIALIAS);
                        }
                        //imageLabel.setIcon(new ImageIcon(img));
                        //imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                        imagePane.remove(loadingPanel);
                        imagePane.setImage(img, imageWidth, imageHeight);
                        imagePane.revalidate();

                        addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                if (selectedImagePanel != null) {
                                    selectedImagePanel.setBorder(DefaultSettings.DEFAULT_BORDER);
                                    selectedImagePanel.revalidate();
                                    selectedImagePanel.repaint();
                                }

                                selectedImagePanel = single;
                                single.setBorder(DefaultSettings.DEFAULT_LINE_BORDER);
                                single.revalidate();
                                single.repaint();

                                String imageName = getImageNameFromUrl(iurl);
                                if (imageName != null) {
                                    File file = new File(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator + imageName);
                                    if (file.exists()) {
                                        fileToUpload = file;
                                        showPictureInView();
                                        return;
                                    }

                                    file = new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName);
                                    if (file.exists()) {
                                        fileToUpload = file;
                                        showPictureInView();
                                        return;
                                    }

                                    file = new File(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + imageName);
                                    if (file.exists()) {
                                        fileToUpload = file;
                                        showPictureInView();
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                single.setBorder(DefaultSettings.DEFAULT_BORDER_H);
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                if (selectedImagePanel != null && selectedImagePanel.equals(single)) {
                                    single.setBorder(DefaultSettings.DEFAULT_LINE_BORDER);
                                } else {
                                    single.setBorder(DefaultSettings.DEFAULT_BORDER);
                                }
                            }
                        });
                    } else {
                        BufferedImage loadNotFound = DesignClasses.return_buffer_image(GetImages.NO_IMAGE_FOUND_150x150);
                        imagePane.remove(loadingPanel);
                        imagePane.setImage(loadNotFound, imageWidth, imageHeight);
                        setToolTipText(null);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        imagePane.revalidate();
                        loadNotFound.flush();
                        loadNotFound = null;
                    }
                    single.revalidate();
                    single.repaint();
                } catch (Exception e) {
                    // e.printStackTrace();
                    log.error("Error in here ==>" + e.getMessage());
                    if (img != null) {
                        img.flush();
                        img = null;
                    }
                } finally {
                    if (img != null) {
                        img.flush();
                        img = null;
                    }
                }

            }
        }

    }
}
