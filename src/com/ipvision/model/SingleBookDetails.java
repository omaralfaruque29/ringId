/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Faiz Ahmed
 */
public class SingleBookDetails extends SingleMemberInCircleDto {

    private String sts;
    private String iurl;
    private String albId;
    private String albn;
    private String cptn;
    private Short type;
    private Long nc;
    private Long nl;
    private Long tm;
    private Short il; // if i like in comments or not
    private Short ic; // if i comments
    private Long tl;
    /* post to friend's wall start*/
    private String ffn;
    private String fln;
    //post to friend's wall end
    private Integer pst;
    private Integer imT;
    //location
    private String lctn;
    //Multiple image post
    private String fGId;
    //image like
    private Integer lkd;
    //share feed
    private Long sfId;
    //number of share
    private Long ns;
    private Long ts;
    private Long nfId;//alternative of status id= newsfeed id
    private String cmnId;
    private Boolean sucs;
    private String mg;
    private Long imgId;
    private Long at;

    public Long getAddedTime() {
        return at;
    }

    public void setAddedTime(Long at) {
        this.at = at;
    }

    public Boolean getSuccess() {
        return sucs;
    }

    public Long getImageId() {
        return imgId;
    }

    public void setImageId(Long imgId) {
        this.imgId = imgId;
    }

    public void setSuccess(Boolean success) {
        this.sucs = success;
    }

    public Long getNfId() {
        return nfId;
    }

    public String getMessage() {
        return mg;
    }

    public void setMessage(String message) {
        this.mg = message;
    }

    public void setNfId(Long nfId) {
        this.nfId = nfId;
    }

    public String getCmnId() {
        return cmnId;
    }

    public void setCmnId(String cmnId) {
        this.cmnId = cmnId;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public String getAlbId() {
        return albId;
    }

    public void setAlbId(String albId) {
        this.albId = albId;
    }

    public String getAlbn() {
        return albn;
    }

    public void setAlbn(String albn) {
        this.albn = albn;
    }

    public String getCptn() {
        return cptn;
    }

    public void setCptn(String cptn) {
        this.cptn = cptn;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Long getNc() {
        return nc;
    }

    public void setNc(Long nc) {
        this.nc = nc;
    }

    public Long getNl() {
        return nl;
    }

    public void setNl(Long nl) {
        this.nl = nl;
    }

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }

    public Short getIl() {
        return il;
    }

    public void setIl(Short il) {
        this.il = il;
    }

    public Long getTl() {
        return tl;
    }

    public void setTl(Long tl) {
        this.tl = tl;
    }

    public String getFfn() {
        return ffn;
    }

    public void setFfn(String ffn) {
        this.ffn = ffn;
    }

    public String getFln() {
        return fln;
    }

    public void setFln(String fln) {
        this.fln = fln;
    }

    public Integer getPst() {
        return pst;
    }

    public void setPst(Integer pst) {
        this.pst = pst;
    }

    public Integer getImT() {
        return imT;
    }

    public void setImT(Integer imT) {
        this.imT = imT;
    }

    public String getLctn() {
        return lctn;
    }

    public void setLctn(String lctn) {
        this.lctn = lctn;
    }

    public String getfGId() {
        return fGId;
    }

    public void setfGId(String fGId) {
        this.fGId = fGId;
    }

    public Integer getLkd() {
        return lkd;
    }

    public void setLkd(Integer lkd) {
        this.lkd = lkd;
    }

    public Long getSfId() {
        return sfId;
    }

    public void setSfId(Long sfId) {
        this.sfId = sfId;
    }

    public Long getNs() {
        return ns;
    }

    public void setNs(Long ns) {
        this.ns = ns;
    }
    
    public Short getIc() {
        return ic;
    }

    public void setIc(Short ic) {
        this.ic = ic;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
    
    
}
