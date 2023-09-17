package com.ipvision.view.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import com.ipvision.service.utility.Scalr;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shahadat
 */
public class FeedImageInfo {

    public static final int VIEW_TYPE_1 = 1;//N-1
    public static final int VIEW_TYPE_2 = 2;//V-2
    public static final int VIEW_TYPE_3 = 3;//H-3
    public static final int VIEW_TYPE_4 = 11;//NN-11
    public static final int VIEW_TYPE_5 = 22;//VV-22
    public static final int VIEW_TYPE_6 = 33;//HH-33
    public static final int VIEW_TYPE_7 = 12;//NV-12
    public static final int VIEW_TYPE_8 = 23;//HV-32
    public static final int VIEW_TYPE_9 = 31;//HN-31
    public static final int VIEW_TYPE_10 = 111;//NNN-111
    public static final int VIEW_TYPE_11 = 333;//HHH-333
    public static final int VIEW_TYPE_12 = 222;//VVV-222
    public static final int VIEW_TYPE_13 = 123;//NVH-123
    public static final int VIEW_TYPE_14 = 311;//HNN-311
    public static final int VIEW_TYPE_15 = 122;//NVV-122
    public static final int VIEW_TYPE_16 = 133;//NHH-133
    public static final int VIEW_TYPE_17 = 233;//VHH-233
    public static final int VIEW_TYPE_18 = 322;//HVV-322
    public static final int VIEW_TYPE_19 = 211;//VNN-211

    public static final int TYPE_NORMAL = 1;//N
    public static final int TYPE_VERTICAL = 2;//V
    public static final int TYPE_HORIZONTAL = 3;//H

    public static int[] sieve;
    public static int[][] size;

    static {
        sieve = new int[334];
        size = new int[334][6];
        sieve[1] = VIEW_TYPE_1;
        sieve[2] = VIEW_TYPE_2;
        sieve[3] = VIEW_TYPE_3;
        sieve[11] = VIEW_TYPE_4;
        sieve[22] = VIEW_TYPE_5;
        sieve[33] = VIEW_TYPE_6;
        sieve[12] = VIEW_TYPE_7;
        sieve[21] = VIEW_TYPE_7;
        sieve[23] = VIEW_TYPE_8;
        sieve[32] = VIEW_TYPE_8;
        sieve[31] = VIEW_TYPE_9;
        sieve[13] = VIEW_TYPE_9;
        sieve[111] = VIEW_TYPE_10;
        sieve[333] = VIEW_TYPE_11;
        sieve[222] = VIEW_TYPE_12;
        sieve[123] = VIEW_TYPE_13;
        sieve[132] = VIEW_TYPE_13;
        sieve[231] = VIEW_TYPE_13;
        sieve[321] = VIEW_TYPE_13;
        sieve[213] = VIEW_TYPE_13;
        sieve[312] = VIEW_TYPE_13;
        sieve[311] = VIEW_TYPE_14;
        sieve[131] = VIEW_TYPE_14;
        sieve[113] = VIEW_TYPE_14;
        sieve[122] = VIEW_TYPE_15;
        sieve[212] = VIEW_TYPE_15;
        sieve[221] = VIEW_TYPE_15;
        sieve[133] = VIEW_TYPE_16;
        sieve[313] = VIEW_TYPE_16;
        sieve[331] = VIEW_TYPE_16;
        sieve[233] = VIEW_TYPE_17;
        sieve[323] = VIEW_TYPE_17;
        sieve[332] = VIEW_TYPE_17;
        sieve[322] = VIEW_TYPE_18;
        sieve[232] = VIEW_TYPE_18;
        sieve[223] = VIEW_TYPE_18;
        sieve[112] = VIEW_TYPE_19;
        sieve[121] = VIEW_TYPE_19;
        sieve[211] = VIEW_TYPE_19;
        
        size[VIEW_TYPE_4] = new int[]{300, 300, 300, 300, 0, 0};
        size[VIEW_TYPE_5] = new int[]{300, 450, 300, 450, 0, 0};
        size[VIEW_TYPE_6] = new int[]{600, 250, 600, 250, 0, 0};
        size[VIEW_TYPE_7] = new int[]{400, 400, 200, 400, 0, 0};
        size[VIEW_TYPE_8] = new int[]{450, 300, 150, 300, 0, 0};
        size[VIEW_TYPE_9] = new int[]{400, 200, 200, 200, 0, 0};
        size[VIEW_TYPE_10] = new int[]{400, 400, 200, 200, 200, 200};
        size[VIEW_TYPE_12] = new int[]{200, 400, 200, 400, 200, 400};
        size[VIEW_TYPE_13] = new int[]{350, 350, 250, 500, 350, 150};
        size[VIEW_TYPE_14] = new int[]{600, 250, 300, 300, 300, 300};
        size[VIEW_TYPE_15] = new int[]{300, 300, 150, 300, 150, 300};
        size[VIEW_TYPE_16] = new int[]{225, 225, 600, 175, 350, 225};
        size[VIEW_TYPE_17] = new int[]{200, 400, 400, 200, 400, 200};
        size[VIEW_TYPE_18] = new int[]{300, 200, 150, 200, 150, 200};
        size[VIEW_TYPE_19] = new int[]{300, 500, 300, 250, 300, 250};
    }

    private BufferedImage bufferedImage;
    private Long imageId;
    private String iurl;
    private int width;
    private int height;
    private int reWidth;
    private int reHeight;
    private Float ratio;
    private JLabel imageLabel;
    private int index;
    private Integer imgType = TYPE_NORMAL;
    private Scalr.Mode fitMode = Scalr.Mode.FIT_TO_WIDTH;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getRatio() {
        setRatioAndType();
        return ratio;
    }

    public Integer getImgType() {
        setRatioAndType();
        return imgType;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Scalr.Mode getFitMode() {
        return fitMode;
    }

    public void setFitMode(Scalr.Mode fitMode) {
        this.fitMode = fitMode;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(JLabel imageLabel) {
        this.imageLabel = imageLabel;
    }

    public int getReWidth() {
        return reWidth;
    }

    public void setReWidth(int reWidth) {
        this.reWidth = reWidth;
    }

    public int getReHeight() {
        return reHeight;
    }

    public void setReHeight(int reHeight) {
        this.reHeight = reHeight;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private void setRatioAndType() {
        if (ratio == null) {
            ratio = (float) width / (float) height;
            if (ratio >= 0.75 && ratio <= 1.34) {
                imgType = TYPE_NORMAL;
            } else if (ratio > 1.34) {
                imgType = TYPE_HORIZONTAL;
            } else if (ratio < 0.75) {
                imgType = TYPE_VERTICAL;
            }
        }
    }
    
    public static int viewType(List<FeedImageInfo> list) {
        int index = 0;
        
        for (int i = (list.size() - 1), mult = 1 ; i >= 0; i--, mult *= 10) {
            index += list.get(i).getImgType() * mult;
        }
        
        return index; 
    }

    public static FeedImageInfo[] getSortedFeedImageArray(List<FeedImageInfo> list, int type) {
        
        List<FeedImageInfo> tempList = new ArrayList<FeedImageInfo>();
        for (FeedImageInfo temp : list) {
            tempList.add(temp);
        }

        FeedImageInfo[] imageArray = new FeedImageInfo[tempList.size()];
        
        for (int idx = (tempList.size() - 1), value = sieve[type]; idx >= 0; idx--) {
            
            int mod = value % 10;
            value = value / 10;
            
            for (int i = (tempList.size() - 1); i < tempList.size(); i--) {
                if(tempList.get(i).getImgType() == mod) {
                    imageArray[idx] = tempList.get(i);
                    tempList.remove(i);
                    break;
                }
            }
        }
        
        return imageArray;
    }

}
