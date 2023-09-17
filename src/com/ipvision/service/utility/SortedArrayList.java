/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import java.util.ArrayList;

/**
 *
 * @author Shahadat
 */
public class SortedArrayList extends ArrayList<SortedArrayList.Data> implements Cloneable {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SortedArrayList.class);

    public void insertDataFromServer(long nfId, long tm) {
        Data data = new Data(nfId, tm, true);
        insertData(data);
    }
    
    public void insertData(long nfId, long tm) {
        Data data = new Data(nfId, tm, false);
        insertData(data);
    }
    
    public void insertData(Data data) {
        synchronized (this) {
            //System.err.println("insert(long nfId, long tm) >>>  " + nfId + "  ||  " + tm);
            removeData(data.getNfId());
            
            int max = this.size();
            int min = 0;
            int pivot;

            while (max > min) {
                pivot = (min + max) / 2;
                if (data.getTm() < this.get(pivot).getTm()) {
                    min = pivot + 1;
                } else {
                    max = pivot;
                }
            }

            this.add(min, data);
        }
    }

    public void removeData(Long nfId) {
        synchronized (this) {
            Data temp = null;
            if (this.size() > 0 && nfId != null) {
                for (Data data : this) {
                    if (data.getNfId() == nfId) {
                        temp = data;
                        //System.err.println("removeData >>>  " + nfId);
                        break;
                    }
                }
            }

            this.remove(temp);
        }
    }

    public Data getData(Long nfId) {
        Data temp = null;
        if (this.size() > 0 && nfId != null) {
            for (Data data : this) {
                if (data.getNfId() == nfId) {
                    temp = data;
                    break;
                }
            }
        }
        return temp;
    }

    public int getIndex(Long nfId) {
        Data temp = null;
        if (this.size() > 0 && nfId != null) {
            for (Data data : this) {
                if (data.getNfId() == nfId) {
                    temp = data;
                    break;
                }
            }
        }
        return this.indexOf(temp);
    }
    
    public boolean hasDataFromServer() {
        if (this.size() > 0) {
            for (Data data : this) {
                if (data.getFromServer()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getIndex(Data data) {
        return this.indexOf(data);
    }

    public class Data {

        private long nfId;
        private long tm;
        private boolean fromServer;

        public Data(long nfId, long tm, boolean fromServer) {
            this.nfId = nfId;
            this.tm = tm;
            this.fromServer = fromServer;
        }

        public long getNfId() {
            return nfId;
        }

        public long getTm() {
            return tm;
        }
        
        public boolean getFromServer() {
            return fromServer;
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
