/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.ipvision.view.feed.EditComment;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author Faiz
 */
public class EditDeleteImageCommentPopup implements ActionListener {
    
    SingeImageCommentDetails singeImageCommentDetails;
    private final String EDIT = "Edit";
    private final String DELETE = "Delete";
    public boolean isOnlyDelete;
    JCustomMenuPopup optionsPopup_edit_delete = null;
    JCustomMenuPopup optionsPopup_only_delete = null;
    
    public EditDeleteImageCommentPopup(SingeImageCommentDetails singeImageCommentDetails) {
        this.singeImageCommentDetails = singeImageCommentDetails;
//        editThis.addActionListener(this);
//        deleteThis.addActionListener(this);

        activeDeactiveEditOption();
        optionsPopup_edit_delete = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_DELETE);
        optionsPopup_only_delete = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_DELETE);
        
        optionsPopup_edit_delete.addMenu(EDIT, GetImages.EDIT_PHOTO);
        optionsPopup_edit_delete.addMenu(DELETE, GetImages.DELETE_PHOTO);
        
        optionsPopup_only_delete.addMenu(DELETE, GetImages.DELETE_PHOTO);
    }
    
    private void activeDeactiveEditOption() {
        //[{"cmnId":1504,"cmn":"55","tm":1416303111689,"uId":"ring8801728119927","fn":"Faiz","ln":"Ahmed","il":0,"tl":0}
        JsonFields singleComment = singeImageCommentDetails.commentDto;
        
        if (singleComment.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID) && (singleComment.getTl() == null || singleComment.getTl() <= 0)) {
//            editThis.setEnabled(true);
            isOnlyDelete = false; // It will show the pop up window both EDIT and Delete----> Rabiul 3-8-2015

        } else {
            isOnlyDelete = true;
        }

        // The Below Code COmmented Out   ------->3-8-2015 
        //   else if (singleComment.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID) && (singleComment.getTl() != null && singleComment.getTl() > 0)) {
////            editThis.setEnabled(false);
//             isOnlyDelete=true;
//        } else {
////            editThis.setVisible(false);
//            isOnlyDelete=true;
//        }
//        if (SingleImgeDetails.getSingleImgeDetails() != null
//                && (SingleImgeDetails.getSingleImgeDetails().statusDto.getUserIdentity() == null
//                || SingleImgeDetails.getSingleImgeDetails().statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID))) {
////            deleteThis.setEnabled(true);
//             isOnlyDelete=true;
//        } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
//                && (SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getUserIdentity() == null
//                || SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID))) {
////            deleteThis.setEnabled(true);
//             isOnlyDelete=true;
//        } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
//                && (SingleImageDetailsForNotifications.getSingleImgeDetails().imageDTo.getUserIdentity() == null
//                || SingleImageDetailsForNotifications.getSingleImgeDetails().imageDTo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID))) {
////            deleteThis.setEnabled(true);
//             isOnlyDelete=true;
//        } else if (singleComment.getUserIdentity() == null || singleComment.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
////            deleteThis.setEnabled(true);
//            isOnlyDelete=true;
//        } else {
////            deleteThis.setVisible(false);
//             isOnlyDelete=true;
//        }
    }
    
    MouseListener menuListener = new MouseAdapter() {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(EDIT)) {
                if (EditComment.getInstance() != null && EditComment.getInstance().isDisplayable()) {
                    EditComment.getInstance().hidePreviousEditPanels();
                }
                singeImageCommentDetails.likeCommentsAndTimePanel.setVisible(false);
                singeImageCommentDetails.editComment();
                if (SingleImgeDetails.getSingleImgeDetails() != null) {
                    SingleImgeDetails.getSingleImgeDetails().clieckedEditDeletepopup = false;
                } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                    SingleImageDetailsProfileCover.getSingleImgeDetails().clieckedEditDeletepopup = false;
                } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                    SingleImageDetailsForNotifications.getSingleImgeDetails().clieckedEditDeletepopup = false;
                }
                
            } else if (menu.text.equalsIgnoreCase(DELETE)) {
                HelperMethods.showConfirmationDialogMessage("Are you sure to delete this comment?");
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteImageComments().start();
                }
            }

//            super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
//            super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
//            super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
        }
        
    };
    
    @Override
    public void actionPerformed(ActionEvent e) {

//        Object ob = e.getSource();
//        if (ob == editThis) {
//            singeImageCommentDetails.editComment();
//            if (SingleImgeDetails.getSingleImgeDetails() != null) {
//                SingleImgeDetails.getSingleImgeDetails().clieckedEditDeletepopup = false;
//            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
//                SingleImageDetailsProfileCover.getSingleImgeDetails().clieckedEditDeletepopup = false;
//            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
//                SingleImageDetailsForNotifications.getSingleImgeDetails().clieckedEditDeletepopup = false;
//            }
//        } else if (ob == deleteThis) {
//            HelperMethods.showConfirmationDialogMessage("Are you sure to delete this comment?");
//            if (JOptionPanelBasics.YES_NO) {
//                new DeleteImageComments().start();
//            }
//        }
    }
    
    class DeleteImageComments extends Thread {
//

        public DeleteImageComments() {
            
        }
        
        @Override
        public void run() {
            try {
                /*
                 {"stId":16,"actn":183,"sId":"1396512223921nazmul","pckId":"1396512263101","cmnId":10}
                 {"actn":182,"pckId":"1409481486023ring8801717634317","sId":"1409481517136ring8801717634317","cmnId":4,"imgId":295}
                 */
                JsonFields pakToSend = new JsonFields();
                
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_DELETE_IMAGE_COMMENT);
                pakToSend.setImageId(singeImageCommentDetails.commentDto.getImageId());
                pakToSend.setCmnId(singeImageCommentDetails.commentDto.getCmnId());
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);
                
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        
                        FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (responsefields.getSuccess()) {
                            try {

//                                if (SingleImgeDetails.getSingleImgeDetails() != null) {
//                                    SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel.getImageComments().remove(singeImageCommentDetails.commentDto.getCmnId());
//                                    int bookcommentindex = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel.comments.getComponentZOrder(SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel.getCommentsOfthisImage().get(singeImageCommentDetails.commentDto.getCmnId()));
//                                    if (bookcommentindex > -1) {
//                                        SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel.removeAComment(bookcommentindex);
//                                    }
//                                }
                                if (singeImageCommentDetails.addImageCaptionCommentsPanel != null) {
                                    AddImageCaptionCommentsPanel aiccp = singeImageCommentDetails.addImageCaptionCommentsPanel;
                                    aiccp.getImageComments().remove(singeImageCommentDetails.commentDto.getCmnId());
                                    int bookcommentindex = aiccp.comments.getComponentZOrder(aiccp.getCommentsOfthisImage().get(singeImageCommentDetails.commentDto.getCmnId()));
                                    if (bookcommentindex > -1) {
                                        aiccp.removeAComment(bookcommentindex);
                                    }
                                }
                                
                            } catch (Exception e) {
                            }
                        } else {
                            if (SingleImgeDetails.getSingleImgeDetails() != null) {
                                HelperMethods.showWarningDialogMessage("Failed!!" + responsefields.getMessage());
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                    
                }
                if (SingleImgeDetails.getSingleImgeDetails() != null) {
                    HelperMethods.showWarningDialogMessage("Can not process this comment delete request right now");
                }
            } catch (Exception ex) {
            }
        }
    }
    
}
