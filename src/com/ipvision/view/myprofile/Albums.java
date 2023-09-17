/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.ipvision.service.AlbumRequest;
import com.ipvision.view.feed.SingleAlbumNameWithCoverImage;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.model.stores.NewsFeedMaps;

/**
 *
 * @author Wasif Islam
 */
public class Albums extends JPanel {

    private JPanel content = new JPanel();
    public JScrollPane scrollContent;
    // private static Albums albums;
    public JLabel loadingLabel = new JLabel();
    public boolean isLoadMyAlbumFromServer = false;

//    public static Albums getInstance() {
//        if (albums == null) {
//            albums = new Albums();
//        }
//        return albums;
//    }
    public Albums() {
        setLayout(new BorderLayout());
        content.setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10));
        content.setOpaque(false);
        scrollContent = DesignClasses.getDefaultScrollPane(content);
        scrollContent.setOpaque(false);
        scrollContent.setPreferredSize(new Dimension(0, 65));
        scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollContent, BorderLayout.CENTER);

        if (!NewsFeedMaps.getInstance().getMyAllbums().isEmpty() && isLoadMyAlbumFromServer) {
            addMyAlbumsFolders();
        } else {
            loadMyAlbumFromServer();
        }

    }

    public void loadMyAlbumFromServer() {
        isLoadMyAlbumFromServer = true;
        addloding();
        new AlbumRequest(AlbumRequest.TRIGGER_BY_ALBUM_HOME).start();
    }

    public void addloding() {
        //isLoading = true;
        loadingLabel = DesignClasses.loadingLable(true);
        content.add(loadingLabel);
        content.revalidate();
    }

    public synchronized void addMyAlbumsFolders() {
        content.removeAll();

        if (!NewsFeedMaps.getInstance().getMyAllbums().isEmpty()) {
            /*for (String albmID : NewsFeedMaps.getInstance().getMyAllbums().keySet()) {
             JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(albmID);
             SingleAlbumNameWithCoverImage album = new SingleAlbumNameWithCoverImage(fields);
             content.add(album);
             }*/
            Map<Long, String> tempMap = new HashMap<Long, String>();
            long[] times = new long[NewsFeedMaps.getInstance().getMyAllbums().size()];
            int e = 0;
            for (String albmID : NewsFeedMaps.getInstance().getMyAllbums().keySet()) {
                JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(albmID);

                tempMap.put(fields.getUt(), albmID);
                times[e] = fields.getUt();
                e++;

            }

            Arrays.sort(times);
            for (int k = times.length - 1; k >= 0; k--) {
                String albumId = tempMap.get(times[k]);
                JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(albumId);
                SingleAlbumNameWithCoverImage album = new SingleAlbumNameWithCoverImage(fields);
                content.add(album);
            }
        }
        content.revalidate();
    }

    public void addNewMyAlbumsFolder(String album_id) {
        JsonFields fields = NewsFeedMaps.getInstance().getMyAllbums().get(album_id);
        SingleAlbumNameWithCoverImage album = new SingleAlbumNameWithCoverImage(fields);
        content.add(album, 0);
        content.revalidate();
    }
}
