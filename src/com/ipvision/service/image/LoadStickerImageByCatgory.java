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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import com.ipvision.model.dao.InsertIntoRingMarketStickerCategoryTable;
import com.ipvision.model.dao.InsertIntoRingMarketStickerTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.ringtools.SingleMarketCategoryPanel;
import com.ipvision.view.ringtools.SingleMarketImageDownloadPanel;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerDTO;
import com.ipvision.model.StickerImagesDTO;

/**
 *
 * @author Shahadat
 */
public class LoadStickerImageByCatgory extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadStickerImageByCatgory.class);
    private String serviceUrl = ServerAndPortSettings.getRingStickerMarketServiceUrl();
    private String downloadUrl = ServerAndPortSettings.getRingStickerMarketDownloadUrl();
    private List<StickerImagesDTO> stickerImagesDTOs = new ArrayList<StickerImagesDTO>();
    private DownLoaderHelps dHelp = new DownLoaderHelps();
    private SingleMarketImageDownloadPanel.MarketImageDownloadListener listener;
    private StickerCategoryDTO categoryDTO;

    public LoadStickerImageByCatgory(StickerCategoryDTO categoryDTO, SingleMarketImageDownloadPanel.MarketImageDownloadListener listener) {
        this.categoryDTO = categoryDTO;
        this.listener = listener;
         setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        try {
            if (listener != null) {
                listener.onInit();
            }

            String allUrl = serviceUrl + "?categoryId=" + categoryDTO.getStickerCategoryId();
            String allResponse = HelperMethods.fetchFromUrl(allUrl);

            StickerDTO stickerDTO = new StickerDTO();
            if (allResponse != null && allResponse.length() > 0) {
                stickerDTO = HelperMethods.mapSticket(allResponse);
            }

            if (stickerDTO.getSuccess()) {
                if (stickerDTO.getImagesList() != null && stickerDTO.getImagesList().size() > 0) {
                    StickerCategoryDTO tempCategoryDTO = StickerStorer.getInstance().getCategoriesMap().get(categoryDTO.getStickerCategoryId());
                    if (tempCategoryDTO != null) {
                        tempCategoryDTO.setDownloaded(Boolean.TRUE);
                    }

                    List<StickerCategoryDTO> categoryDTOs = new ArrayList<StickerCategoryDTO>();
                    categoryDTO.setDownloaded(Boolean.TRUE);
                    categoryDTOs.add(categoryDTO);
                    new InsertIntoRingMarketStickerCategoryTable(categoryDTOs).start();

                    int size = stickerDTO.getImagesList().size();

                    if (listener != null) {
                        listener.onProgress(size);
                    }

                    createStickersDirectory(dHelp.getSticketDestinationFolder() + categoryDTO.getStickerCollectionId() + File.separator + categoryDTO.getStickerCategoryId());

                    if (StickerStorer.getInstance().getImagesMap().get(categoryDTO.getStickerCategoryId()) == null) {
                        StickerStorer.getInstance().getImagesMap().put(categoryDTO.getStickerCategoryId(), new ConcurrentHashMap<Integer, StickerImagesDTO>());
                    }

                    for (StickerImagesDTO entity : stickerDTO.getImagesList()) {
                        if (StickerStorer.getInstance().getImagesMap().get(categoryDTO.getStickerCategoryId()).get(entity.getImageId()) == null) {
                            StickerStorer.getInstance().getImagesMap().get(categoryDTO.getStickerCategoryId()).put(entity.getImageId(), entity);
                        }

                        stickerImagesDTOs.add(entity);
                        String imageUrl = downloadUrl + "/" + MyFnFSettings.D_FULL + "/" + categoryDTO.getStickerCollectionId() + "/" + categoryDTO.getStickerCategoryId() + "/" + entity.getImageUrl();
                        download(imageUrl, entity.getImageUrl());

                        if (listener != null) {
                            listener.onProgress(--size);
                        }
                    }

                    new InsertIntoRingMarketStickerTable(stickerImagesDTOs).start();
                }
            }

            if (GuiRingID.getInstance() != null
                    && GuiRingID.getInstance().getRingToolsRightContainer() != null
                    && GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel() != null) {
                SingleMarketCategoryPanel marketCategoryPanel = GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel().getStickerCategoryPanel().get(categoryDTO.getStickerCategoryId());
                if (marketCategoryPanel != null) {
                    marketCategoryPanel.initContents();
                }
            }

            Set<String> stickerSet = StickerStorer.getInstance().getChatStickerDownload().get(categoryDTO.getStickerCategoryId());
            if (stickerSet != null && stickerSet.size() > 0) {
                synchronized (stickerSet) {
                    for (String value : stickerSet) {
                        String[] splits = value.split("_");
                        String userId = splits[0];
                        String packetId = splits[1];

                        Map<String, SingleChatPanel> chatMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(userId);
                        SingleChatPanel chatPanel = chatMap != null ? chatMap.get(packetId) : null;
                        if (chatPanel != null) {
                            chatPanel.setMainPanel();
                        }
                    }
                }
            }
            StickerStorer.getInstance().getChatStickerDownload().remove(categoryDTO.getStickerCategoryId());
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Exception in LoadStickerImageByCatgory ==>" + ex.getMessage());
        }
    }

    public void download(String imageUrl, String imageName) {
        try {
            if (HelperMethods.check_string_contains_substring(imageName, ".")) {
                String destination = dHelp.getSticketDestinationFolder() + categoryDTO.getStickerCollectionId() + File.separator + categoryDTO.getStickerCategoryId() + File.separator + imageName;
                //  MyfnfHashMaps.getInstance().getBufferedImages().put(imageUrl, imageName);
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
