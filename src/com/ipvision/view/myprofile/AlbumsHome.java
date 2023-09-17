/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Faiz Ahmed
 */
public class AlbumsHome extends JPanel {

    public JScrollPane scrollContent;
    private JPanel content = new JPanel();
    //  public static AlbumsHome albumsHome;
    private JPanel tab;
    private int commonWidth = 150;
    public JLabel loadingLabel = new JLabel();
    private JLabel photosTab = DesignClasses.makeLableBoldTooLarge("Photos");
    private JLabel albumsTab = DesignClasses.makeLableBoldTooLarge("Albums");
    private JButton createAlbum = new RoundedCornerButton("+ Create Album", "Create Album");

    public static int TYPE_CREATE_ALBUM = 3;
    private JPanel tabPanel;
    public boolean isLoading;
    public Photos photos;
    private Albums albums;

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public Photos getPhotos() {
        return photos;
    }

    public AlbumsHome() {
        setOpaque(false);
        setLayout(new BorderLayout());
        initContainers();
    }

    private void initContainers() {
        try {
            content.setOpaque(false);
            content.setLayout(new BorderLayout());
            add(content, BorderLayout.CENTER);

            photosTab.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
            photosTab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

            albumsTab.setForeground(DefaultSettings.disable_font_color);
            albumsTab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

            content.removeAll();
            photos = Photos.getInstance();
            photos.setOpaque(false);
            content.add(photos, BorderLayout.CENTER);
            content.revalidate();
            content.repaint();

        } catch (Exception e) {
        }
    }

//    public void refreshMyAlbumsFolder() {
//        photos.addMyAllImages();
//    }

}
