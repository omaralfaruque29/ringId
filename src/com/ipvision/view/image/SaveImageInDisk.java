/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.service.utility.HelperMethods;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author Faiz
 */
public class SaveImageInDisk extends JPopupMenu implements ActionListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveImageInDisk.class);
    //JPopupMenu popupMenu;
    JMenuItem viewImageMenuItem;
    JMenuItem saveImageMenuItem;
    JComponent label;
    Point point;
    JsonFields imageDto;
    BufferedImage bufferedImage;
    boolean fullView;
    JFileChooser chooser;

    public SaveImageInDisk(JsonFields imageDto, boolean fullView) {
        this.imageDto = imageDto;
        setInitInfo();
        this.fullView = fullView;
        // popupMenu = new JPopupMenu();

        saveImageMenuItem = new JMenuItem("Save image as...");
        add(saveImageMenuItem);
        saveImageMenuItem.addActionListener(this);
        if (fullView) {
            viewImageMenuItem = new JMenuItem("View image");
            add(viewImageMenuItem);
            viewImageMenuItem.addActionListener(this);
        }

    }

    private void setInitInfo() {
        this.bufferedImage = ImageHelpers.getBookBuffedImageFromUrl(this.imageDto.getIurl(), this.imageDto.getImT());
    }

//    public void setVisible(boolean isVisible) {
//        if (popupMenu != null) {
//            popupMenu.show(label, point.x, point.y);
//        }
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewImageMenuItem) {
            SingleImageDetailsForNotifications sidfn = new SingleImageDetailsForNotifications(imageDto.getImageId());
            sidfn.setVisible(true);
            sidfn.addloader();
        } else if (e.getSource() == saveImageMenuItem) {

            String imageName = "";

            imageName = getImageName(imageDto.getIurl());

            final String format = getExtension(imageName);

            File saveFile = new File(imageName);
            chooser = new JFileChooser();

            JFrame dd = new JFrame();
            ImageHelpers.setAppIcon(dd, null);

            chooser.setDialogTitle("Save Image");
            chooser.setSelectedFile(saveFile);

            if (format != null && format.trim().length() > 0) {
                FileFilter ff = new FileFilter() {
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else if (f.getName().endsWith("." + format)) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    public String getDescription() {
                        return format.toUpperCase() + " Files (*." + format + ")";
                    }
                };
                chooser.setFileFilter(ff);
            }

            int saveValue = chooser.showSaveDialog(dd);
            if (saveValue == chooser.APPROVE_OPTION) {
                try {
                    saveFile = chooser.getSelectedFile();
                    if (saveFile.exists()) {
                        HelperMethods.showConfirmationDialogMessage("Replace existing file?");
                        if (!JOptionPanelBasics.YES_NO) {
                            return;
                        }
                    }

                    ImageIO.write(bufferedImage, format, saveFile);

                    if (!getExtension(saveFile.getName()).contains("." + format)) {
                        new File(saveFile.getAbsolutePath()).renameTo(new File(saveFile.getAbsolutePath() + "." + format));
                    }
                } catch (IOException ex) {
                    //ex.printStackTrace();
                    log.error("Error in here ==>" + ex.getMessage());

                }
            }
        }
    }

    public static String getImageName(String imageUrl) {
        String imageName = "";
        if (imageUrl.contains("/")) {
            imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        }
        return imageName;
    }

    public static String getExtension(String imageName) {
        if (imageName.contains(".")) {
            imageName = imageName.substring(imageName.lastIndexOf('.') + 1);
        }
        return imageName;
    }

}
