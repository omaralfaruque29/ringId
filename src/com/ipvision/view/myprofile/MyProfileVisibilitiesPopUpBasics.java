/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Sirat Samyoun
 */
public class MyProfileVisibilitiesPopUpBasics extends JPopupMenu{
    public JMenuItem _public;
    public JMenuItem onlyFriend;
    public JMenuItem onlyMe;

    public MyProfileVisibilitiesPopUpBasics() {
        _public = new JMenuItem("    Public   ", DesignClasses.return_image(GetImages._PUBLIC));
        _public.setToolTipText("See All"); 
        onlyFriend = new JMenuItem("   Only Friend  ", DesignClasses.return_image(GetImages._ONLY_FRIEND));
        onlyFriend.setToolTipText("See only your friends");
        onlyMe = new JMenuItem("   Only Me  ", DesignClasses.return_image(GetImages._ONLY_ME));
        onlyMe.setToolTipText("See only me");
        add(_public);
        add(onlyFriend);
        add(onlyMe);
        // setBorder(new BevelBorder(BevelBorder.RAISED));
    }    

}
