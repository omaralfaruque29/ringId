/*
 * To change this template, choose Tools | Templates
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import com.ipvision.model.dao.InsertIntoRingMarketStickerCategoryTable;
import com.ipvision.model.dao.InsertIntoRingMarketStickerTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.ringtools.SingleMarketCategoryPanel;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerCollectionDTO;
import com.ipvision.model.StickerDTO;
import com.ipvision.model.StickerImagesDTO;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Shahadat
 */
public class LoadStickersFromRingMarket extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadStickersFromRingMarket.class);
    private DownLoaderHelps dHelp = new DownLoaderHelps();
    private String serviceUrl = ServerAndPortSettings.getRingStickerMarketServiceUrl();
    private String downloadUrl = ServerAndPortSettings.getRingStickerMarketDownloadUrl();

    private Map<Integer, StickerCategoryDTO> tempCategoryMap = new ConcurrentHashMap<Integer, StickerCategoryDTO>();
    private List<StickerImagesDTO> tempImageList = new ArrayList<StickerImagesDTO>();

    public LoadStickersFromRingMarket() {
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            loadDefaultStickers();
            loadStickersCollection();
            loadAllStickersImages();
        }
    }

    private void loadDefaultStickers() {
        try {
            String allUrl = serviceUrl + "?default=1";
            String allResponse = HelperMethods.fetchFromUrl(allUrl);

            StickerDTO stickerDTO = new StickerDTO();
            if (allResponse != null && allResponse.length() > 0) {
                stickerDTO = HelperMethods.mapSticket(allResponse);
            }

            if (stickerDTO.getSuccess()) {
                if (stickerDTO.getCategoriesList() != null && stickerDTO.getCategoriesList().size() > 0) {
                    StickerCategoryDTO entity = stickerDTO.getCategoriesList().get(0);
                    List<StickerCategoryDTO> tempCategoryList = new ArrayList<StickerCategoryDTO>();
                    List<StickerImagesDTO> tempImageList = new ArrayList<StickerImagesDTO>();

                    if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                        entity.setDownloaded(Boolean.TRUE);
                        StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                        tempCategoryList.add(entity);
                    }

                    if (stickerDTO.getImagesList() != null && stickerDTO.getImagesList().size() > 0) {
                        if (StickerStorer.getInstance().getImagesMap().get(entity.getStickerCategoryId()) == null) {
                            StickerStorer.getInstance().getImagesMap().put(entity.getStickerCategoryId(), new ConcurrentHashMap<Integer, StickerImagesDTO>());
                        }

                        for (StickerImagesDTO imagesDTO : stickerDTO.getImagesList()) {
                            if (StickerStorer.getInstance().getImagesMap().get(entity.getStickerCategoryId()).get(imagesDTO.getImageId()) == null) {
                                StickerStorer.getInstance().getImagesMap().get(entity.getStickerCategoryId()).put(imagesDTO.getImageId(), imagesDTO);
                                tempImageList.add(imagesDTO);
                            }
                        }
                    }

                    new InsertIntoRingMarketStickerCategoryTable(tempCategoryList).start();
                    new InsertIntoRingMarketStickerTable(tempImageList).start();

                    MyFnFSettings.DEFAULT_STICKER_CATEGORY_ID = entity.getStickerCategoryId();
                    MyFnFSettings.DEFAULT_STICKER_COLLECTION_ID = entity.getStickerCollectionId();

                    createStickersDirectory(dHelp.getSticketDestinationFolder() + entity.getStickerCollectionId() + File.separator + entity.getStickerCategoryId());
//                    String detailUrl = downloadUrl + "/" + MyFnFSettings.D_MINI + "/" + entity.getStickerCollectionId() + "/" + entity.getStickerCategoryId() + "/" + entity.getDetailImage();
//                    download(detailUrl, entity.getDetailImage(), entity.getStickerCollectionId(), entity.getStickerCategoryId());
                    String iconUrl = downloadUrl + "/" + MyFnFSettings.D_MINI + "/" + entity.getStickerCollectionId() + "/" + entity.getStickerCategoryId() + "/" + entity.getIcon();
                    boolean isDownloaded = download(iconUrl, entity.getIcon(), entity.getStickerCollectionId(), entity.getStickerCategoryId());
                    if (isDownloaded) {
                        buildCategoryIcon();
                    }
                }
            }
        } catch (Exception ex) {
            //  ex.printStackTrace();
            log.error("Error in loadDefaultStickers ==>" + ex.getMessage());
        }
    }

    private void loadStickersCollection() {
        try {
            String allUrl = serviceUrl + "?all=" + 1;
            String allResponse = HelperMethods.fetchFromUrl(allUrl);

            StickerDTO stickerDTO = new StickerDTO();
            if (allResponse != null && allResponse.length() > 0) {
                stickerDTO = HelperMethods.mapSticket(allResponse);
            }

            if (stickerDTO.getSuccess()) {
                if (stickerDTO.getTopCategoriesList() != null && stickerDTO.getTopCategoriesList().size() > 0) {
                    for (StickerCategoryDTO entity : stickerDTO.getTopCategoriesList()) {
                        if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                            StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                            tempCategoryMap.put(entity.getStickerCategoryId(), entity);
                        }
                        StickerStorer.getInstance().getTopCategoriesMap().put(entity.getStickerCategoryId(), StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()));
                    }
                }

                if (stickerDTO.getFreeCategoriesList() != null && stickerDTO.getFreeCategoriesList().size() > 0) {
                    for (StickerCategoryDTO entity : stickerDTO.getFreeCategoriesList()) {
                        if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                            StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                            tempCategoryMap.put(entity.getStickerCategoryId(), entity);
                        }
                        StickerStorer.getInstance().getFreeCategoriesMap().put(entity.getStickerCategoryId(), StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()));
                    }
                }

                if (stickerDTO.getStNewCategoriesList() != null && stickerDTO.getStNewCategoriesList().size() > 0) {
                    for (StickerCategoryDTO entity : stickerDTO.getStNewCategoriesList()) {
                        if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                            StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                            tempCategoryMap.put(entity.getStickerCategoryId(), entity);
                        }
                        StickerStorer.getInstance().getNewCategoriesMap().put(entity.getStickerCategoryId(), StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()));
                    }
                }

                if (stickerDTO.getStickerCollectionsList() != null && stickerDTO.getStickerCollectionsList().size() > 0) {
                    for (StickerCollectionDTO entity : stickerDTO.getStickerCollectionsList()) {
                        loadStickersCategoriesByCollectionId(entity.getStickerCollectionId());
                    }
                }

                List<StickerCategoryDTO> stickerCategoryDTOs = new ArrayList<StickerCategoryDTO>();
                if (tempCategoryMap.size() > 0) {
                    for (Entry<Integer, StickerCategoryDTO> en : tempCategoryMap.entrySet()) {
                        StickerCategoryDTO entity = en.getValue();
                        stickerCategoryDTOs.add(entity);
                    }
                }
                new InsertIntoRingMarketStickerCategoryTable(stickerCategoryDTOs).start();

                Map<Integer, StickerImagesDTO> imageMap = StickerStorer.getInstance().getImagesMap().get(MyFnFSettings.DEFAULT_STICKER_CATEGORY_ID);
                if (imageMap != null && imageMap.size() > 0) {
                    boolean isDownloaded = false;
                    for (StickerImagesDTO entity : imageMap.values()) {
                        String imageUrl = downloadUrl + "/" + MyFnFSettings.D_FULL + "/" + MyFnFSettings.DEFAULT_STICKER_COLLECTION_ID + "/" + MyFnFSettings.DEFAULT_STICKER_CATEGORY_ID + "/" + entity.getImageUrl();
                        if (download(imageUrl, entity.getImageUrl(), MyFnFSettings.DEFAULT_STICKER_COLLECTION_ID, MyFnFSettings.DEFAULT_STICKER_CATEGORY_ID)) {
                            isDownloaded = true;
                        }
                    }
                    if (isDownloaded) {
                        reloadContent(MyFnFSettings.DEFAULT_STICKER_CATEGORY_ID);
                    }
                }
            }

        } catch (Exception ex) {

        }
    }

    private void loadStickersCategoriesByCollectionId(Integer collectionId) {
        try {
            String allUrl = serviceUrl + "?collectionId=" + collectionId;
            String allResponse = HelperMethods.fetchFromUrl(allUrl);

            StickerDTO stickerDTO = new StickerDTO();
            if (allResponse != null && allResponse.length() > 0) {
                stickerDTO = HelperMethods.mapSticket(allResponse);
            }

            if (stickerDTO.getSuccess()) {
                if (stickerDTO.getCategoriesList() != null && stickerDTO.getCategoriesList().size() > 0) {
                    for (StickerCategoryDTO entity : stickerDTO.getCategoriesList()) {
                        if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                            StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                            tempCategoryMap.put(entity.getStickerCategoryId(), entity);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in loadStickersCategoriesByCollectionId ==>" + ex.getMessage());
        }
    }

    private void loadAllStickersImages() {
        List<StickerCategoryDTO> stickerCategoryDTOs = new ArrayList<StickerCategoryDTO>();

        if (StickerStorer.getInstance().getCategoriesMap().size() > 0) {
            for (Entry<Integer, StickerCategoryDTO> en : StickerStorer.getInstance().getCategoriesMap().entrySet()) {
                StickerCategoryDTO entity = en.getValue();
                if (entity.getStickerCategoryId() != MyFnFSettings.DEFAULT_STICKER_CATEGORY_ID) {
                    stickerCategoryDTOs.add(entity);
                    loadStickersImagesByCategoryId(entity);
                }
            }
        }
        new InsertIntoRingMarketStickerTable(tempImageList).start();

        boolean isDownloaded = false;
        for (StickerCategoryDTO entity : stickerCategoryDTOs) {
            createStickersDirectory(dHelp.getSticketDestinationFolder() + entity.getStickerCollectionId() + File.separator + entity.getStickerCategoryId());
            String detailUrl = downloadUrl + "/" + MyFnFSettings.D_MINI + "/" + entity.getStickerCollectionId() + "/" + entity.getStickerCategoryId() + "/" + entity.getDetailImage();
            boolean isDetailDownloaded = download(detailUrl, entity.getDetailImage(), entity.getStickerCollectionId(), entity.getStickerCategoryId());
            if (isDetailDownloaded && (entity.getDownloaded() == null || entity.getDownloaded() == false)) {
                reloadContent(entity.getStickerCategoryId());
            }
            String iconUrl = downloadUrl + "/" + MyFnFSettings.D_MINI + "/" + entity.getStickerCollectionId() + "/" + entity.getStickerCategoryId() + "/" + entity.getIcon();
            boolean isIconDownloaded = download(iconUrl, entity.getIcon(), entity.getStickerCollectionId(), entity.getStickerCategoryId());
            if (isIconDownloaded) {
                isDownloaded = true;
            }
        }

        if (isDownloaded) {
            buildCategoryIcon();
        }
    }

    private void loadStickersImagesByCategoryId(StickerCategoryDTO stickerCategoryDTO) {
        try {
            String allUrl = serviceUrl + "?categoryId=" + stickerCategoryDTO.getStickerCategoryId();
            String allResponse = HelperMethods.fetchFromUrl(allUrl);

            StickerDTO stickerDTO = new StickerDTO();
            if (allResponse != null && allResponse.length() > 0) {
                stickerDTO = HelperMethods.mapSticket(allResponse);
            }

            if (stickerDTO.getSuccess()) {
                if (stickerDTO.getImagesList() != null && stickerDTO.getImagesList().size() > 0) {
                    if (StickerStorer.getInstance().getImagesMap().get(stickerCategoryDTO.getStickerCategoryId()) == null) {
                        StickerStorer.getInstance().getImagesMap().put(stickerCategoryDTO.getStickerCategoryId(), new ConcurrentHashMap<Integer, StickerImagesDTO>());
                    }

                    for (StickerImagesDTO entity : stickerDTO.getImagesList()) {
                        if (StickerStorer.getInstance().getImagesMap().get(stickerCategoryDTO.getStickerCategoryId()).get(entity.getImageId()) == null) {
                            StickerStorer.getInstance().getImagesMap().get(stickerCategoryDTO.getStickerCategoryId()).put(entity.getImageId(), entity);
                            tempImageList.add(entity);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            //  ex.printStackTrace();
            log.error("Exception in loadStickersImagesByCategoryId ==>" + ex.getMessage());
        }
    }

    public boolean download(String imageUrl, String imageName, int collectionId, int categoryId) {
        boolean isDownloaded = false;

        try {
            if (HelperMethods.check_string_contains_substring(imageName, ".")) {
                String destination = dHelp.getSticketDestinationFolder() + collectionId + File.separator + categoryId + File.separator + imageName;
                //  MyfnfHashMaps.getInstance().getBufferedImages().put(imageUrl, imageName);
                boolean isFileExist = isFileExistInDirectory(destination);
                if (!isFileExist) {
                    try {
                        log.warn("Downloading Market Sticker :: " + imageUrl + ", " + imageName);
                        URL url = new URL(imageUrl);
                        BufferedImage bimg = ImageIO.read(url);
                        File outputfile = new File(destination);
                        ImageIO.write(bimg, getExtension(imageName), outputfile);
                        isDownloaded = true;
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
                            isDownloaded = true;
                        } catch (Exception e) {
                            //ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        return isDownloaded;
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

    private void reloadContent(int categoryId) {
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getRingToolsRightContainer() != null
                && GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel() != null) {
            SingleMarketCategoryPanel temp = GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel().getStickerCategoryPanel().get(categoryId);
            if (temp != null) {
                temp.initContents();
            }
        }
    }

    private void buildCategoryIcon() {
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getRingToolsRightContainer() != null
                && GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel() != null) {
            GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel().buildStickerCategoryListPanel();
        }
    }

}
