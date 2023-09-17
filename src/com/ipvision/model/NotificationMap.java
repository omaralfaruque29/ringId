/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;

/**
 *
 * @author Wasif Islam
 */
public class NotificationMap extends FeedBackFields {
    /*
     {"nList":
     [
     {"id":"1163","fndId":"ring8801756858005","ut":1403517387427,"nt":2,"acId":697,"mt":7,"fndN":"Raj Alamgir"},
     {"id":"1164","fndId":"ring8801756858005","ut":1403517400892,"nt":2,"acId":697,"mt":6,"fndN":"Raj Alamgir"},
     {"id":"1165","fndId":"ring8801520080982","ut":1403517407420,"nt":2,"acId":697,"mt":6,"fndN":"Ashiqur Rahman"},
     {"id":"1166","fndId":"ring8801756858005","ut":1403517409462,"nt":2,"acId":697,"mt":6,"fndN":"Raj Alamgir"},
     {"id":"1167","fndId":"ring8801520080982","ut":1403517412726,"nt":2,"acId":697,"mt":7,"fndN":"Ashiqur Rahman"}
     ],
     "actn":111,
     "pckFs":12529,
     "tr":7,
     "sucs":true,
     "seq":"1/2",
     "tn":7}
     */

    private ArrayList<JsonFields> nList = null;


    public ArrayList<JsonFields> getnList() {
        return nList;
    }

    public void setnList(ArrayList<JsonFields> nList) {
        this.nList = nList;
    }
    
}
