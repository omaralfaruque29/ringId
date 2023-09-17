/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.image;

import com.ipvision.model.stores.StickerStorer;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import com.ipvision.model.dao.InsertIntoRingMarketStickerCategoryTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerDTO;

/**
 *
 * @author raj
 */
public class LoadUnknownStickerCategory extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadUnknownStickerCategory.class);
    private String serviceUrl = ServerAndPortSettings.getRingStickerMarketIServiceUrl();
    private String downloadUrl = ServerAndPortSettings.getRingStickerMarketDownloadUrl();
    private DownLoaderHelps dHelp = new DownLoaderHelps();

    private int categoryId;
    private int collectionId;
    private StickerCategoryDTO stickerCategoryDTO;

    public LoadUnknownStickerCategory(int categoryId, int collectionId) {
        this.categoryId = categoryId;
        this.collectionId = collectionId;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        loadStickersCategory();
        if (stickerCategoryDTO != null) {
            String detailUrl = downloadUrl + "/" + MyFnFSettings.D_MINI + "/" + stickerCategoryDTO.getStickerCollectionId() + "/" + stickerCategoryDTO.getStickerCategoryId() + "/" + stickerCategoryDTO.getDetailImage();
            String iconUrl = downloadUrl + "/" + MyFnFSettings.D_MINI + "/" + stickerCategoryDTO.getStickerCollectionId() + "/" + stickerCategoryDTO.getStickerCategoryId() + "/" + stickerCategoryDTO.getIcon();
            createStickersDirectory(dHelp.getSticketDestinationFolder() + stickerCategoryDTO.getStickerCollectionId() + File.separator + stickerCategoryDTO.getStickerCategoryId());
            download(iconUrl, stickerCategoryDTO.getIcon());
            download(detailUrl, stickerCategoryDTO.getDetailImage());
        }

        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getRingToolsRightContainer() != null
                && GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel() != null) {
            GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel().buildStickerCategoryListPanel();
        }
    }

    private void loadStickersCategory() {
        try {
            String allUrl = serviceUrl + "?clId=" + collectionId + "&ctId=" + categoryId;
            String allResponse = HelperMethods.fetchFromUrl(allUrl);

            StickerDTO stickerDTO = new StickerDTO();
            if (allResponse != null && allResponse.length() > 0) {
                stickerDTO = HelperMethods.mapSticket(allResponse);
            }

            if (stickerDTO.getSuccess()) {
                if (stickerDTO.getSingleCategoryInfo() != null && stickerDTO.getSingleCategoryInfo().size() > 0) {
                    for (StickerCategoryDTO entity : stickerDTO.getSingleCategoryInfo()) {
                        if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                            StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                        }
                        stickerCategoryDTO = entity;
                        List<StickerCategoryDTO> categoryDTOs = new ArrayList<StickerCategoryDTO>();
                        categoryDTOs.add(entity);
                        new InsertIntoRingMarketStickerCategoryTable(categoryDTOs).start();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Exception in loadStickersCategory ==>" + ex.getMessage());
        }
    }

    public void download(String imageUrl, String imageName) {
        try {
            if (HelperMethods.check_string_contains_substring(imageName, ".")) {
                String destination = dHelp.getSticketDestinationFolder() + stickerCategoryDTO.getStickerCollectionId() + File.separator + stickerCategoryDTO.getStickerCategoryId() + File.separator + imageName;
                boolean isFileExist = isFileExistInDirectory(destination);

                if (!isFileExist) {
                    try {
                        log.warn("Downloading Market Sticker :: " + imageUrl + ", " + imageName);
                        URL url = new URL(imageUrl);
                        BufferedImage bimg = ImageIO.read(url);
                        File outputfile = new File(destination);
                        ImageIO.write(bimg, getExtension(imageName), outputfile);
                    } catch (Exception ex) {
                        try {
                            URL url = new URL(imageUrl);
                            URLConnection yc = url.openConnection();
                            InputStream is = yc.getInputStream();
                            FileOutputStream fos = new FileOutputStream(destination);
                            int size = 5120;
                            byte[] buffer = new byte[size];
                            int bytesRead = 0;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                            fos.close();
                            is.close();
                        } catch (Exception e) {
                            //ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    private String getExtension(String imageName) {
        if (imageName.contains(".")) {
            imageName = imageName.substring(imageName.lastIndexOf('.') + 1);
        }
        return imageName;
    }

    private boolean isFileExistInDirectory(String directory) {
        File f = new File(directory);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    private void createStickersDirectory(String subFolder) {
        try {
            File dir = new File(subFolder);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
            }
        } catch (Exception ex) {
        }
    }

}
