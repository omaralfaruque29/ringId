/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import com.ipvision.service.utility.Scalr;

/**
 *
 * @author shahadat
 */
public class FeedImageViewPanel extends JPanel {

    private JPanel container;
    private JPanel additionalPanel;

    public int type = 1;
    private JLabel label1 = null;
    private JLabel label2 = null;
    private JLabel label3 = null;
    private JPanel panel1 = null;
    private JPanel panel2 = null;
    private JPanel panel3 = null;
    public FeedImageInfo[] imageArray;
    private final int SEPERATOR = 3;
    private int imageViewHeight = 100;

    public FeedImageViewPanel(List<FeedImageInfo> list) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel containerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        containerWrapper.setOpaque(false);
        this.add(containerWrapper, BorderLayout.CENTER);

        container = new JPanel();
        container.setOpaque(false);
        containerWrapper.add(container);

        additionalPanel = new JPanel();
        additionalPanel.setOpaque(false);

        initContents(list);
    }

    private void initContents(List<FeedImageInfo> images) {

        Collections.sort(images, new Comparator<FeedImageInfo>() {
            @Override
            public int compare(FeedImageInfo o1, FeedImageInfo o2) {
                return o1.getImgType().compareTo(o2.getImgType());
            }
        });

        Collections.sort(images, new Comparator<FeedImageInfo>() {
            @Override
            public int compare(FeedImageInfo o1, FeedImageInfo o2) {
                Integer dim1 = (o1.getWidth() * o1.getHeight());
                Integer dim2 = (o2.getWidth() * o2.getHeight());
                return dim2.compareTo(dim1);
            }
        });

        type = FeedImageInfo.viewType(images);
        imageArray = FeedImageInfo.getSortedFeedImageArray(images, type);
        buildView();
    }

    private void buildView() {
        int currType = FeedImageInfo.sieve[type];

        int i = 0;
        for (FeedImageInfo image : imageArray) {
            image.setReWidth(FeedImageInfo.size[currType][i++]);
            image.setReHeight(FeedImageInfo.size[currType][i++]);
        }

        label1 = new JLabel();
        label1.setOpaque(false);
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.COMMENTS_BORDER_COLOR));
        panel1.setOpaque(false);
        panel1.add(label1);

        if (currType >= FeedImageInfo.VIEW_TYPE_4) {
            label2 = new JLabel();
            label2.setOpaque(false);
            panel2 = new JPanel(new GridBagLayout());
            panel2.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.COMMENTS_BORDER_COLOR));
            panel2.setOpaque(false);
            panel2.add(label2);
        }

        if (currType >= FeedImageInfo.VIEW_TYPE_10) {
            label3 = new JLabel();
            label3.setOpaque(false);
            panel3 = new JPanel(new GridBagLayout());
            panel3.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.COMMENTS_BORDER_COLOR));
            panel3.setOpaque(false);
            panel3.add(label3);
        }

        if (currType == FeedImageInfo.VIEW_TYPE_1 
                || currType == FeedImageInfo.VIEW_TYPE_2 
                || currType == FeedImageInfo.VIEW_TYPE_3) {
            
            int width = imageArray[0].getWidth();
            int height = imageArray[0].getHeight();
            if (width > 600) {
                width = 600;
                height = (imageArray[0].getHeight() * width) / imageArray[0].getWidth();
            }

            imageArray[0].setReWidth(width);
            imageArray[0].setReHeight(height);

            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
            container.add(panel1);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            imageArray[0].setImageLabel(label1);
            setImageViewHeight(imageArray[0].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_4 
                || currType == FeedImageInfo.VIEW_TYPE_5 
                || currType == FeedImageInfo.VIEW_TYPE_7 
                || currType == FeedImageInfo.VIEW_TYPE_8 
                || currType == FeedImageInfo.VIEW_TYPE_9) {
            
            additionalPanel.setLayout(new BoxLayout(additionalPanel, BoxLayout.X_AXIS));
            additionalPanel.add(panel1);
            additionalPanel.add(Box.createHorizontalStrut(SEPERATOR));
            additionalPanel.add(panel2);
            container.add(additionalPanel);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            setImageViewHeight(imageArray[0].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_6) {
            
            additionalPanel.setLayout(new BoxLayout(additionalPanel, BoxLayout.Y_AXIS));
            additionalPanel.add(panel1);
            additionalPanel.add(Box.createVerticalStrut(SEPERATOR));
            additionalPanel.add(panel2);
            container.add(additionalPanel);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            setImageViewHeight(imageArray[0].getReHeight() + imageArray[1].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_10 
                || currType == FeedImageInfo.VIEW_TYPE_17 
                || currType == FeedImageInfo.VIEW_TYPE_19) {
            
            container.setLayout(new BorderLayout(SEPERATOR, 0));
            container.add(panel1, BorderLayout.WEST);
            additionalPanel.setLayout(new BoxLayout(additionalPanel, BoxLayout.Y_AXIS));
            additionalPanel.add(panel2);
            additionalPanel.add(Box.createVerticalStrut(SEPERATOR));
            additionalPanel.add(panel3);
            container.add(additionalPanel, BorderLayout.CENTER);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            panel3.setPreferredSize(new Dimension(imageArray[2].getReWidth(), imageArray[2].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            imageArray[2].setImageLabel(label3);
            setImageViewHeight(imageArray[0].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_11) {
            
            int W = 600;
            float r1 = (float) imageArray[0].getHeight() / (float) imageArray[2].getHeight();
            float r2 = (float) imageArray[1].getHeight() / (float) imageArray[2].getHeight();
            float r3 = (float) 1.00;
            float unit = (float) 500 / (r1 + r2 + r3);

            imageArray[0].setReWidth(W);
            imageArray[0].setReHeight((int) (unit * r1));
            imageArray[1].setReWidth(W);
            imageArray[1].setReHeight((int) (unit * r2));
            imageArray[2].setReWidth(W);
            imageArray[2].setReHeight((int) (unit * r3));

            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.add(panel1);
            container.add(Box.createVerticalStrut(SEPERATOR));
            container.add(panel2);
            container.add(Box.createVerticalStrut(SEPERATOR));
            container.add(panel3);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            panel3.setPreferredSize(new Dimension(imageArray[2].getReWidth(), imageArray[2].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            imageArray[2].setImageLabel(label3);
            setImageViewHeight(imageArray[0].getReHeight() + imageArray[1].getReHeight() + imageArray[2].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_12 
                || currType == FeedImageInfo.VIEW_TYPE_15 
                || currType == FeedImageInfo.VIEW_TYPE_18) {
            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
            container.add(panel1);
            container.add(Box.createHorizontalStrut(SEPERATOR));
            container.add(panel2);
            container.add(Box.createHorizontalStrut(SEPERATOR));
            container.add(panel3);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            panel3.setPreferredSize(new Dimension(imageArray[2].getReWidth(), imageArray[2].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            imageArray[2].setImageLabel(label3);
            setImageViewHeight(imageArray[0].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_13) {
            
            container.setLayout(new BorderLayout(SEPERATOR, 0));
            additionalPanel.setLayout(new BoxLayout(additionalPanel, BoxLayout.Y_AXIS));
            additionalPanel.add(panel1);
            additionalPanel.add(Box.createVerticalStrut(SEPERATOR));
            additionalPanel.add(panel3);
            container.add(additionalPanel, BorderLayout.CENTER);
            container.add(panel2, BorderLayout.EAST);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            panel3.setPreferredSize(new Dimension(imageArray[2].getReWidth(), imageArray[2].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            imageArray[2].setImageLabel(label3);
             setImageViewHeight(imageArray[1].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_14) {
            
            container.setLayout(new BorderLayout(0, SEPERATOR));
            container.add(panel1, BorderLayout.NORTH);
            additionalPanel.setLayout(new BoxLayout(additionalPanel, BoxLayout.X_AXIS));
            additionalPanel.add(panel2);
            additionalPanel.add(Box.createHorizontalStrut(SEPERATOR));
            additionalPanel.add(panel3);
            container.add(additionalPanel, BorderLayout.CENTER);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            panel3.setPreferredSize(new Dimension(imageArray[2].getReWidth(), imageArray[2].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            imageArray[2].setImageLabel(label3);
            setImageViewHeight(imageArray[0].getReHeight() + imageArray[1].getReHeight());
            
        } else if (currType == FeedImageInfo.VIEW_TYPE_16) {
            
            container.setLayout(new BorderLayout(0, SEPERATOR));
            additionalPanel.setLayout(new BoxLayout(additionalPanel, BoxLayout.X_AXIS));
            additionalPanel.add(panel1);
            additionalPanel.add(Box.createHorizontalStrut(SEPERATOR));
            additionalPanel.add(panel3);
            container.add(additionalPanel, BorderLayout.CENTER);
            container.add(panel2, BorderLayout.SOUTH);

            panel1.setPreferredSize(new Dimension(imageArray[0].getReWidth(), imageArray[0].getReHeight()));
            panel2.setPreferredSize(new Dimension(imageArray[1].getReWidth(), imageArray[1].getReHeight()));
            panel3.setPreferredSize(new Dimension(imageArray[2].getReWidth(), imageArray[2].getReHeight()));
            imageArray[0].setImageLabel(label1);
            imageArray[1].setImageLabel(label2);
            imageArray[2].setImageLabel(label3);
            setImageViewHeight(imageArray[0].getReHeight() + imageArray[1].getReHeight());
            
        }

        assignFitMode();
    }

    private void assignFitMode() {
        for (int i = 0; i < imageArray.length; i++) {
            float oRation = (float) imageArray[i].getWidth() / (float) imageArray[i].getHeight();
            float tRation = (float) imageArray[i].getReWidth() / (float) imageArray[i].getReHeight();

            if (imageArray[i].getWidth() < ((float)imageArray[i].getReWidth() * 0.70) && imageArray[i].getHeight() < ((float)imageArray[i].getReHeight() * 0.70)) {
                imageArray[i].setFitMode(null);
            } else if (oRation < tRation) {
                imageArray[i].setFitMode(Scalr.Mode.FIT_TO_WIDTH);
            } else {
                imageArray[i].setFitMode(Scalr.Mode.FIT_TO_HEIGHT);
            }
        }
    }

    public int getImageViewHeight() {
        return imageViewHeight;
    }

    public void setImageViewHeight(int imageViewHeight) {
        this.imageViewHeight = imageViewHeight;
    }
    
    

}
