/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author faizahmed
 */
public class FeedBackFields extends BasicDto implements Cloneable {

   
    private String mg;
    private String seq;
    private String pckFs;
    private Integer tr;
    private Long grpId;
    private Long nfId;//alternative of status id= newsfeed id
    private String cmnId;
    private Long blc;
    private Long tm;
    private Long at;
    private Integer sCode;
    private String albId;
    private Long imgId;
    private String tg;
    private Long tid;
    private Short type;
    private Long prImId;
    private Long cImId;
    private String nm; //Name
    private String auId;
    private Long ut;
    private Long tut;
    /*book*/
    private Long nl;
    private Long nc;
    private Long utId; //user table id 
    private Short uf;
    private Integer vldt;
    private String fn;
    private Integer lsts; // last status...offline = 1; online = 2, away=3, do not disturb = 4

    public Integer getLastStatus() {
        return lsts;
    }

    public void setLastStatus(Integer lsts) {
        this.lsts = lsts;
    }

    public String getFullName() {
        return fn;
    }

    public void setFullName(String fullName) {
        this.fn = fullName;
    }

    public Integer getValidity() {
        return vldt;
    }

    public void setValidity(Integer vldt) {
        this.vldt = vldt;
    }

    public Long getGroupId() {
        return tid;
    }

    public void setGroupId(Long tid) {
        this.tid = tid;
    }

    public Long getTut() {
        return tut;
    }

    public void setTut(Long tut) {
        this.tut = tut;
    }

    public Short getUf() {
        return uf;
    }

    public void setUf(Short uf) {
        this.uf = uf;
    }

    public Long getUserTableId() {
        return utId;
    }

    public void setUserTabelId(Long utId) {
        this.utId = utId;
    }

    public Long getNl() {
        return nl;
    }

    public void setNl(Long nl) {
        this.nl = nl;
    }

    public Long getNc() {
        return nc;
    }

    public void setNc(Long nc) {
        this.nc = nc;
    }

    public Long getUt() {
        return ut;
    }

    public void setUt(Long ut) {
        this.ut = ut;
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    public String getName() {
        return nm;
    }

    public void setName(String nm) {
        this.nm = nm;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Long getImageId() {
        return imgId;
    }

    public void setImageId(Long imgId) {
        this.imgId = imgId;
    }

    public String getAlbId() {
        return albId;
    }

    public void setAlbId(String albId) {
        this.albId = albId;
    }

    public Integer getstatusCode() {
        return sCode;
    }

    public void setstatusCode(Integer sCode) {
        this.sCode = sCode;
    }

    public Long getBalance() {
        return blc;
    }

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }

    public Long getAddedTime() {
        return at;
    }

    public void setAddedTime(Long at) {
        this.at = at;
    }

    public void setBalance(Long balance) {
        this.blc = balance;
    }

    public String getCmnId() {
        return cmnId;
    }

    public void setCmnId(String cmnId) {
        this.cmnId = cmnId;
    }

    public Long getNfId() {
        return nfId;
    }

    public void setNfId(Long nfId) {
        this.nfId = nfId;
    }

    public Long getCircleId() {
        return grpId;
    }

    public void setCircleId(Long groupId) {
        this.grpId = groupId;
    }

    public Integer getTr() {
        return tr;
    }

    public void setTr(Integer tr) {
        this.tr = tr;
    }

    public String getPacketIdFromServer() {
        return pckFs;
    }

    public void setPacketIdFromServer(String packetIdFromServer) {
        this.pckFs = packetIdFromServer;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMessage() {
        return mg;
    }

    public void setMessage(String message) {
        this.mg = message;
    }


    public String getGroupName() {
        return tg;
    }

    public void setGroupName(String tag) {
        this.tg = tag;
    }

    public Long getProfileImageId() {
        return prImId;
    }

    public void setProfileImageId(Long prImId) {
        this.prImId = prImId;
    }

    public Long getCoverImageId() {
        return cImId;
    }

    public void setCoverImageId(Long cImId) {
        this.cImId = cImId;
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
