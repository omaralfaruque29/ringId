/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.List;
import com.ipvision.model.SingleMemberInCircleDto;

/**
 *
 * @author Faiz Ahmed
 */
public class CircleMembersMap {
    /*
     {
     "actn": 99,
     "seq": "26/33",
     "pckFs": 12246,
     "groupMembers": [
                    * {
                    "uId": "ring8801712026023",
                    "admin": true,
                    "psnc": 1,
                    "fn": "sanbir ",
                    "ln": "hasan",
                    "gr": "Male",
                    "mbl": "+8801712026023",
                    "grpId": 106,
                    "ut": 1403665230698,
                    "ists": 0
                    },
                    {
                    "uId": "ring8801756858005",
                    "admin": true,
                    "psnc": 2,
                    "fn": "Alamgir",
                    "ln": "Raj",
                    "gr": "Male",
                    "mbl": "+8801756858005",
                    "grpId": 68,
                    "ut": 1403761924712,
                    "ists": 0
                    },
                    {
                    "uId": "ring8801756858005",
                    "admin": false,
                    "psnc": 2,
                    "fn": "Alamgir",
                    "ln": "Raj",
                    "gr": "Male",
                    "mbl": "+8801756858005",
                    "grpId": 79,
                    "ut": 1403762323957,
                    "ists": 0
                    },
                    {
                    "uId": "ring8801786555535",
                    "admin": false,
                    "psnc": 2,
                    "fn": "anupam",
                    "ln": "017",
                    "gr": "Male",
                    "mbl": "+8801786555535",
                    "grpId": 79,
                    "ut": 1404713045487,
                    "ists": 0
                    },
                    {
                    "uId": "ring8801855810195",
                    "admin": false,
                    "psnc": 1,
                    "fn": "zulfiker ",
                    "ln": "shishir",
                    "gr": "Male",
                    "mbl": "+8801855810195",
                    "grpId": 100,
                    "ut": 1404714993085,
                    "ists": 0
                    }
     * ],
     "tr": 163
     }
     */

    private List<SingleMemberInCircleDto> groupMembers;

    public List<SingleMemberInCircleDto> getCircleMembers() {
        return groupMembers;
    }

    public void setCircleMembers(List<SingleMemberInCircleDto> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
