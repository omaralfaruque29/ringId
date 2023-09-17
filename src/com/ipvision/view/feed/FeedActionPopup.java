/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.GetImages;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.feed.FeedDeleteCommentRequest;
import com.ipvision.service.feed.FeedDeleteStatusRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import java.awt.Component;

/**
 * @author Wasif Islam
 */
public class FeedActionPopup implements ActionListener {

    private Color bg_color = Color.WHITE;
    private Color image_hover_border = Color.LIGHT_GRAY;
    private NewsFeedWithMultipleImage statusDto;
    private JsonFields commentDto;
    private String singleCommentid;
    private Long nfid;
    private String cmdid;
    private SingleFeedStructure singleFeedStructure;
    private SingleCommentView singleCommentView;
    public NewComment newComment;
    private final String EDIT = "Edit";
    private final String DELETE = "Delete";
    public boolean isOnlyDelete;
    //public String edit_delete = null;

    JCustomMenuPopup optionsPopup_edit_delete = null;
    JCustomMenuPopup optionsPopup_only_delete = null;

//    
//    private JCustomMenuPopup optionsPopup = null;
//    private final static String EDIT="Edit";
//    private final static String DELETE= "Delete";
//    MouseListener menuListener = new MouseAdapter() {
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
//            if (menu.text.equalsIgnoreCase(EDIT)) {
//                
//                //action_take_photo();
//                  if (this.commentDto != null && singleCommentView != null) {
//                singleCommentView.editComment();
//                hideThis();
//            } else if (this.statusDto != null && singleFeedStructure != null) {
//                singleFeedStructure.editStatusAndLocationInfor();
//                hideThis();
//            }
//                optionsPopup.hideThis();
//            } else if (menu.text.equalsIgnoreCase(DELETE)) {
//                //action_choose_album();
//                optionsPopup.hideThis();
//            } 
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
//            menu.setMouseEntered();
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
//            menu.setMouseExited();
//        }
//    };
    public FeedActionPopup(SingleFeedStructure singleFeedStructure) {
        this.statusDto = singleFeedStructure.statusDto;
        this.singleFeedStructure = singleFeedStructure;
        init();
    }

    public FeedActionPopup(SingleCommentView singleCommentView) {
        this.singleCommentView = singleCommentView;
        this.singleFeedStructure = this.singleCommentView.singleFeedStructure;
        this.newComment = this.singleFeedStructure.newComment;
        this.commentDto = this.singleCommentView.commentDto;
        this.nfid = this.commentDto.getNfId();
        this.cmdid = this.commentDto.getCmnId();
        this.singleCommentid = HelperMethods.makeStatusIDCommentIDKey(this.nfid, this.cmdid);
        init();
    }

//    public void hideThis() {
//        this.setVisible(false);
//        System.gc();
//        Runtime.getRuntime().gc();
//    }
//
//    public JPanel buildMenuItem(String name) {
//        JPanel menuContainer = new JPanel(new BorderLayout());
//        menuContainer.setBackground(DefaultSettings.APP_DEFAULT_MENUBAR_BG_COLOR);
//        menuContainer.setPreferredSize(new Dimension(50, 25));
//        menuContainer.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
//
//        JPanel lblNameCon = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
//        lblNameCon.setOpaque(false);
//        JLabel lblName = new JLabel(name);
//        lblName.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
//        lblName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
//        lblNameCon.add(lblName);
//
//        menuContainer.add(lblNameCon, BorderLayout.CENTER);
//
//        return menuContainer;
//    }
    private void init() {
        if (this.commentDto != null
                && (this.commentDto.getTl() == null || this.commentDto.getTl() == 0)
                && (this.commentDto.getIl() == null || this.commentDto.getIl() == 0)
                && (NewsFeedMaps.getInstance().getLikes().get(this.singleCommentid) == null || NewsFeedMaps.getInstance().getLikes().get(this.singleCommentid).isEmpty())
                && (this.commentDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID) || this.commentDto.getUserIdentity() == null)
                && singleCommentView != null) {

            isOnlyDelete = false;

        } else if (this.statusDto != null
                && (this.statusDto.getNl() == null || this.statusDto.getNl() == 0)
                && (this.statusDto.getIl() == null || this.statusDto.getIl() == 0)
                && (NewsFeedMaps.getInstance().getLikes().get(this.statusDto.getNfId() + "") == null || NewsFeedMaps.getInstance().getLikes().get(this.statusDto.getNfId() + "").isEmpty())
                && (this.statusDto.getNc() == null || this.statusDto.getNc() == 0)
                && (NewsFeedMaps.getInstance().getComments().get(this.statusDto.getNfId()) == null || NewsFeedMaps.getInstance().getComments().get(this.statusDto.getNfId()).isEmpty())
                && singleFeedStructure != null) {

            isOnlyDelete = false;
//         
        } else {
//            

            isOnlyDelete = true;

        }
        optionsPopup_edit_delete = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_DELETE);
        optionsPopup_only_delete = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_DELETE);

        optionsPopup_edit_delete.addMenu(EDIT, GetImages.EDIT_PHOTO);
        optionsPopup_edit_delete.addMenu(DELETE, GetImages.DELETE_PHOTO);

        optionsPopup_only_delete.addMenu(DELETE, GetImages.DELETE_PHOTO);

    }

    MouseListener menuListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(EDIT)) {
                if (commentDto != null && singleCommentView != null) {
                    singleCommentView.likeCommentsAndTimePanel.setVisible(false);
                    singleCommentView.editRemovePanel.setVisible(false);
                    if (EditComment.getInstance() != null && EditComment.getInstance().isDisplayable()) {
                        Component c = EditComment.getInstance().getParent();
                        while (c != null && !(c instanceof SingleFeedStructure)) {
                            c = c.getParent();
                        }
                        if (c != null && c instanceof SingleFeedStructure) {
                            SingleFeedStructure tempFeedStructure = (SingleFeedStructure) c;
                            if (tempFeedStructure.newComment != null && !tempFeedStructure.newComment.isVisible()) {
                                tempFeedStructure.newComment.setVisible(true);
                            }
                        }
                        EditComment.getInstance().hidePreviousEditPanels();
                    }
                    if (newComment != null && newComment.isDisplayable()) {
                        newComment.setVisible(false);
                    }
                    singleCommentView.editComment();
                    optionsPopup_edit_delete.hideThis();
                } else if (statusDto != null && singleFeedStructure != null) {
                    if (FeedEditStatus.getInstance() != null && FeedEditStatus.getInstance().isDisplayable()) {
                        FeedEditStatus.getInstance().hidePreviousAddStatus();
                    }
                    singleFeedStructure.editStatusAndLocationInfor();
                    optionsPopup_edit_delete.hideThis();

                }
            } else if ((menu.text.equalsIgnoreCase(DELETE))) {
                if (commentDto != null && singleCommentView != null) {
                    HelperMethods.showConfirmationDialogMessage("Are you sure to delete this comment?");
                    if (JOptionPanelBasics.YES_NO) {
                        new FeedDeleteCommentRequest(nfid, cmdid, AppConstants.TYPE_DELETE_STATUS_COMMENT).start();
                    }
                } else if (statusDto != null && singleFeedStructure != null) {
                    HelperMethods.showConfirmationDialogMessage("Are you sure to delete this post?");
                    if (JOptionPanelBasics.YES_NO) {
                        if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusDto.getNfId()) != null) {
                            if (statusDto.getFriendIdentity() != null) {
                                new FeedDeleteStatusRequest(statusDto.getNfId(), statusDto.getFriendIdentity()).start();
                            } else if (statusDto.getCircleId() != null && statusDto.getCircleId() > 0) {
                                new FeedDeleteStatusRequest(statusDto.getNfId(), statusDto.getCircleId()).start();
                            } else {
                                new FeedDeleteStatusRequest(singleFeedStructure).start();
                            }
                        }
                    }
                }

            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
            //super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e
        ) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
            //super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
        }

    };

    @Override
    public void actionPerformed(ActionEvent e) {
//        Object ob = e.getSource();
//        if (ob == deleteThis) {
//            if (this.commentDto != null && singleCommentView != null) {
//                HelperMethods.showConfirmationDialogMessage("Are you sure to delete this comment?");
//                if (JOptionPanelBasics.YES_NO) {
//                    new FeedDeleteCommentRequest(nfid, cmdid, AppConstants.TYPE_DELETE_STATUS_COMMENT).start();
//                }
//            } else if (this.statusDto != null && singleFeedStructure != null) {
//                HelperMethods.showConfirmationDialogMessage("Are you sure to delete this post?");
//                if (JOptionPanelBasics.YES_NO) {
//                    if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusDto.getNfId()) != null) {
//                        if (statusDto.getFriendIdentity() != null) {
//                            new FeedDeleteStatusRequest(statusDto.getNfId(), statusDto.getFriendIdentity()).start();
//                        } else if (statusDto.getCircleId() != null && statusDto.getCircleId() > 0) {
//                            new FeedDeleteStatusRequest(statusDto.getNfId(), statusDto.getCircleId()).start();
//                        } else {
//                            new FeedDeleteStatusRequest(singleFeedStructure).start();
//                        }
//                    }
//                }
//            }
//
//        } else if (ob == editThis) {
//            if (this.commentDto != null && singleCommentView != null) {
//                singleCommentView.editComment();
//                hideThis();
//            } else if (this.statusDto != null && singleFeedStructure != null) {
//                singleFeedStructure.editStatusAndLocationInfor();
//                hideThis();
//            }
//        }
    }

}
