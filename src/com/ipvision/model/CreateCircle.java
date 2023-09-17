/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class CreateCircle extends CircleMapper {

    public static CreateCircle createCircle;

    public static CreateCircle getInstance() {
        if (createCircle == null) {
            createCircle = new CreateCircle();
        }
        return createCircle;
    }
    private ArrayList<NewCircleFields> groupMembers = new ArrayList<NewCircleFields>();

    public ArrayList<NewCircleFields> getCircleMembers() {
        return groupMembers;
    }

    public void setCircleMembers(ArrayList<NewCircleFields> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
