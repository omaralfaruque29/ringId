/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.List;
import com.ipvision.model.SingleCircleDto;

/**
 *
 * @author Faiz Ahmed
 */
public class CircleListMap {
    /*
     {
     "actn": 70,
     "seq": "1/2",
     "pckFs": 12219,
     "groupList": [{
     "gNm": "RING",
     "grpId": 1,
     "sAd": "ring8801723977727",
     "ut": 1401704270368,
     "ists": 0
     },
     {
     "gNm": "F_ZONE",
     "grpId": 3,
     "sAd": "ring8801834904918",
     "ut": 1401861818775,
     "ists": 0
     },
     {
     "gNm": "Desktop",
     "grpId": 5,
     "sAd": "ring8801728119927",
     "ut": 1401950154937,
     "ists": 0
     },
     {
     "gNm": "Twes",
     "grpId": 7,
     "sAd": "ring8801728119927",
     "ut": 1402292554398,
     "ists": 0
     },
     {
     "gNm": "birds",
     "grpId": 25,
     "sAd": "ring8801557855624",
     "ut": 1402480082672,
     "ists": 0
     },
     {
     "gNm": "tligers",
     "grpId": 26,
     "sAd": "ring8801557855624",
     "ut": 1402480156625,
     "ists": 0
     },
     {
     "gNm": "test group",
     "grpId": 60,
     "sAd": "ring8801552428248",
     "ut": 1402554520798,
     "ists": 0
     },
     {
     "gNm": "test zero",
     "grpId": 63,
     "sAd": "ring8801911013776",
     "ut": 1402568047194,
     "ists": 0
     },
     {
     "gNm": "hiking",
     "grpId": 68,
     "sAd": "ring8801674993381",
     "ut": 1402818959969,
     "ists": 0
     },
     {
     "gNm": "tst",
     "grpId": 79,
     "sAd": "ring8801557855624",
     "ut": 1402915947258,
     "ists": 0
     }],
     "sucs": true,
     "tr": 20
     }
     */

    private List<SingleCircleDto> groupList;

    public List<SingleCircleDto> getCircleList() {
        return groupList;
    }

    public void setCircleList(List<SingleCircleDto> groupList) {
        this.groupList = groupList;
    }
}
