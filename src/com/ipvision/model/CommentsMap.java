/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.JsonFields;

/**
 *
 * @author Faiz Ahmed
 */
public class CommentsMap extends JsonFields {
    private ArrayList<JsonFields> comments = null;
     public ArrayList<JsonFields> getComments() {
        return comments;
    }
    public void setComments(ArrayList<JsonFields> comments) {
        this.comments = comments;
    }
}