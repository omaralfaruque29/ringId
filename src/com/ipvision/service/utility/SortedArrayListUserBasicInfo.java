/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.ipvision.model.UserBasicInfo;
import java.util.ArrayList;

/**
 *
 * @author Shahadat
 */
public class SortedArrayListUserBasicInfo extends ArrayList<SortedArrayListUserBasicInfo.Data> implements Cloneable {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SortedArrayListUserBasicInfo.class);

    public void insertData(UserBasicInfo basicInfo, long tm) {
        synchronized (this) {
            //System.err.println("insert(long nfId, long tm) >>>  " + nfId + "  ||  " + tm);
            removeData(basicInfo.getUserIdentity());
            Data data = new Data(basicInfo, tm);
            int max = this.size();
            int min = 0;
            int pivot;

            while (max > min) {
                pivot = (min + max) / 2;
                if (tm < this.get(pivot).getTm()) {
                    min = pivot + 1;
                } else {
                    max = pivot;
                }
            }

            this.add(min, data);
        }
    }

    public void removeData(String value) {
        synchronized (this) {
            Data temp = null;
            if (this.size() > 0 && value != null) {
                for (Data data : this) {
                    if (data.getValue().equalsIgnoreCase(value)) {
                        temp = data;
                        //System.err.println("removeData >>>  " + nfId);
                        break;
                    }
                }
            }

            this.remove(temp);
        }
    }

    public UserBasicInfo getData(String value) {
        Data temp = null;
        if (this.size() > 0 && value != null) {
            for (Data data : this) {
                if (data.getValue().equalsIgnoreCase(value)) {
                    temp = data;
                    break;
                }
            }
        }
        return temp != null ? temp.getUserBasicInfo() : null;
    }

    public int getIndex(String value) {
        Data temp = null;
        if (this.size() > 0 && value != null) {
            for (Data data : this) {
                if (data.getValue().equalsIgnoreCase(value)) {
                    temp = data;
                    break;
                }
            }
        }
        return this.indexOf(temp);
    }

    public int getIndex(Data data) {
        return this.indexOf(data);
    }

    public class Data {

        private UserBasicInfo userBasicInfo;
        private String value;
        private long tm;

        public Data(UserBasicInfo basicInfo, long tm) {
            this.userBasicInfo = basicInfo;
            this.value = (basicInfo.getUserIdentity() == null ? "" : basicInfo.getUserIdentity());
            this.tm = tm;
        }

        public UserBasicInfo getUserBasicInfo() {
            return userBasicInfo;
        }
        
        public String getValue() {
            return value;
        }

        public long getTm() {
            return tm;
        }
    }

    @Override
    public Object clone() {
        try {
            return (Object) super.clone();
        } catch (Exception e) {
            return this;
        }
    }

}
