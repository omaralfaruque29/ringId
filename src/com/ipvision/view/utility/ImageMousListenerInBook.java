/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.image.BasicImageViewStructure;
import com.ipvision.view.image.SaveImageInDisk;
import com.ipvision.view.image.SingleImageDetailsForNotifications;
import com.ipvision.view.image.SingleImageDetailsProfileCover;
import com.ipvision.view.image.SingleImgeDetails;

/**
 *
 * @author Faiz
 */
public class ImageMousListenerInBook implements MouseListener {

    int selectedImageIndex;
    Long nfID;
    JsonFields imageDto;
    boolean fullView;
    int type = 0;
    String userId;

    public ImageMousListenerInBook(long nfID, int selectedImageIndex) {
        this.selectedImageIndex = selectedImageIndex;
        this.nfID = nfID;
        this.fullView = true;

    }

    public ImageMousListenerInBook(JsonFields imageDto, boolean fullView) {
        this.imageDto = imageDto;
        this.fullView = fullView;
    }

    public ImageMousListenerInBook(JsonFields imageDto, boolean fullView, int picType, String userId) {
        this.imageDto = imageDto;
        this.fullView = fullView;
        this.type = picType;
        this.userId = userId;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (nfID != null || imageDto != null) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if (imageDto == null) {
                    imageDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(this.nfID).getImageList().get(selectedImageIndex);
                }
                BasicImageViewStructure.saveImageInDiskDialog = new SaveImageInDisk(imageDto, this.fullView);
                BasicImageViewStructure.saveImageInDiskDialog.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                if (this.imageDto != null) {
                    if (this.imageDto.getImageId() != null && userId == null) {
                        if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                            SingleImageDetailsForNotifications.setSingleImgeDetails(null);
                        }
                        SingleImageDetailsForNotifications sidfn = new SingleImageDetailsForNotifications(imageDto.getImageId());
                        sidfn.setVisible(true);
                        sidfn.addloader();
                    } else if (this.imageDto.getImageId() != null && userId != null && type > 0) {
                        if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                            SingleImageDetailsProfileCover.setSingleImgeDetails(null);
                        }
                        SingleImageDetailsProfileCover sidfpc = new SingleImageDetailsProfileCover(imageDto, type, userId);
                        sidfpc.setVisible(true);
                        sidfpc.addloader();

                    }

                } else {
                    if (SingleImgeDetails.getSingleImgeDetails() != null) {
                        SingleImgeDetails.setSingleImgeDetails(null);
                    }
                    SingleImgeDetails single = new SingleImgeDetails(nfID, this.selectedImageIndex);
                    single.setVisible(true);
                    single.loadLoadInitialValues();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
