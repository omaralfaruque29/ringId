/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.List;

/**
 *
 * @author user
 */
public class UserBasicInfo extends SingleMemberInCircleDto {

    private String sId;
    private String el;
    private String wim;
    private String cnty;
    private Integer frnS;
    private String mblDc;
    private Integer smP;
    private String smIp;

    private Long blc;
    private String bDay;
    private short[] pvc;
    private short emailPrivacy;
    private short mobilePrivacy;
    private short profileImagePrivacy;
    private short birthdayPrivacy;
    private short coverImagePrivacy;
    private String re;
    private Integer emVsn;
    private Integer fnfHd;
    private Long cut;
    private String dNo;
    private Integer dvc;
    private String dt;
    private String cIm;
    private Long prImId;
    private Long cImId;
    private int cimX = 0;
    private int cimY = 0;
    private String nm;
    private Integer iev;
    private Integer imnv;
    private List<AdditionalInfoDTO> adInfo;
    private Long utId;
    private Integer ct;
    private Boolean incomingNotification;
    private Integer bv; //blocked
    private Integer nct;
    private Boolean iscr;
    private String cc, hc, am;
    private String mDay;
    private Long lot;
    private Long nmf; //NumberOfMutualFriends
    private Integer cfs; //commonFriendSuggestion
    private Integer pns; //PhoneNumberSuggestion
    private Integer cls; //contact list suggestion

    public Integer getCommonFriendsSuggestion() {
        return cfs;
    }

    public void setCommonFriendsSuggestion(Integer cfs) {
        this.cfs = cfs;
    }

    public Integer getPhoneNumberSuggestion() {
        return pns;
    }

    public void setPhoneNumberSuggestion(Integer pns) {
        this.pns = pns;
    }

    public Integer getContactListSuggestion() {
        return cls;
    }

    public void setContactListSuggestion(Integer cls) {
        this.cls = cls;
    }

    public Long getNumberOfMutualFriends() {
        return nmf;
    }

    public void setNumberOfMutualFriends(Long nmf) {
        this.nmf = nmf;
    }

    public Long getLastOnlineTime() {
        return lot;
    }

    public void setLastOnlineTime(Long lot) {
        this.lot = lot;
    }

    public String getCurrentCity() {
        return cc;
    }

    public void setCurrentCity(String cc) {
        this.cc = cc;
    }

    public String getHomeCity() {
        return hc;
    }

    public void setHomeCity(String hc) {
        this.hc = hc;
    }

    public String getMarriageDay() {
        return mDay;
    }

    public void setMarriageDay(String mDay) {
        this.mDay = mDay;
    }

    public String getAboutMe() {
        return am;
    }

    public void setAboutMe(String am) {
        this.am = am;
    }

    public Integer getNewContactType() {
        return nct;
    }

    public void setNewContactType(Integer nct) {
        this.nct = nct;
    }

    public Boolean getIsChangeRequester() {
        return iscr;
    }

    public void setIsChangeRequester(Boolean iscr) {
        this.iscr = iscr;
    }

    public Integer getBlocked() {
        return bv;
    }

    public void setBlocked(Integer bv) {
        this.bv = bv;
    }

    public Long getUserTableId() {
        return utId;
    }

    public void setUserTableId(Long utId) {
        this.utId = utId;
    }

    public List<AdditionalInfoDTO> getAdditionalInfo() {
        return adInfo;
    }

    public void setAdditionalInfo(List<AdditionalInfoDTO> adInfo) {
        this.adInfo = adInfo;
    }

    public Integer getIsEmailVerified() {
        return iev;
    }

    public void setIsEmailVerified(Integer iev) {
        this.iev = iev;
    }

    public Integer getIsMobileNumberVerified() {
        return imnv;
    }

    public void setIsMobileNumberVerified(Integer imnv) {
        this.imnv = imnv;
    }

    public int getCoverImageX() {
        return cimX;
    }

    public void setCoverImageX(int cimX) {
        this.cimX = cimX;
    }

    public int getCoverImageY() {
        return cimY;
    }

    public void setCoverImageY(int cimY) {
        this.cimY = cimY;
    }

    public String getCoverImage() {
        return cIm;
    }

    public void setCoverImage(String cIm) {
        this.cIm = cIm;
    }

    public Integer getDevice() {
        return dvc;
    }

    public void setDevice(Integer device) {
        this.dvc = device;
    }

    public Long getCut() {
        return cut;
    }

    public void setCut(Long cut) {
        this.cut = cut;
    }

    public String getRingEmail() {
        return re;
    }

    public void setRingEmail(String re) {
        this.re = re;
    }

    public short[] getPrivacy() {
        return pvc;
    }

    public void setPrivacy(short[] privacy) {
        this.pvc = privacy;
    }

    public String getBirthday() {
        return bDay;
    }

    public void setBirthday(String birthday) {
        this.bDay = birthday;
    }

    public Integer getSmsServerPort() {
        return smP;
    }

    public void setSmsServerPort(Integer smsServerPort) {
        this.smP = smsServerPort;
    }

    public String getSmsServerIp() {
        return smIp;
    }

    public void setSmsServerIp(String smsServerIp) {
        this.smIp = smsServerIp;
    }

    public Long getBalance() {
        return blc;
    }

    public void setBalance(Long balance) {
        this.blc = balance;
    }

    public Integer getFriendShipStatus() {
        return frnS;
    }

    public void setFriendShipStatus(Integer friendShipStatus) {
        this.frnS = friendShipStatus;
    }

    public String getSessionId() {
        return sId;
    }

    public void setSessionId(String sessionId) {
        this.sId = sessionId;
    }

    public String getWhatisInYourMind() {
        return wim;
    }

    public void setWhatisInYourMind(String whatisInYourMind) {
        this.wim = whatisInYourMind;
    }

    public String getMobilePhoneDialingCode() {
        return mblDc;
    }

    public void setMobilePhoneDialingCode(String mobilePhoneDialingCode) {
        this.mblDc = mobilePhoneDialingCode;
    }

    public String getCountry() {
        return cnty;
    }

    public void setCountry(String country) {
        this.cnty = country;
    }

    public String getEmail() {
        return el;
    }

    public void setEmail(String email) {
        this.el = email;
    }

    public short getEmailPrivacy() {
        return emailPrivacy;
    }

    public void setEmailPrivacy(short emailPrivacy) {
        this.emailPrivacy = emailPrivacy;
    }

    public short getMobilePrivacy() {
        return mobilePrivacy;
    }

    public void setMobilePrivacy(short mobilePrivacy) {
        this.mobilePrivacy = mobilePrivacy;
    }

    public short getProfileImagePrivacy() {
        return profileImagePrivacy;
    }

    public void setProfileImagePrivacy(short profileImagePrivacy) {
        this.profileImagePrivacy = profileImagePrivacy;
    }

    public short getBirthdayPrivacy() {
        return birthdayPrivacy;
    }

    public void setBirthdayPrivacy(short birthdayPrivacy) {
        this.birthdayPrivacy = birthdayPrivacy;
    }

    public short getCoverImagePrivacy() {
        return coverImagePrivacy;
    }

    public void setCoverImagePrivacy(short coverImagePrivacy) {
        this.coverImagePrivacy = coverImagePrivacy;
    }

    public Integer getEmoticonVersion() {
        return emVsn;
    }

    public void setEmoticonVersion(Integer emoticonVersion) {
        this.emVsn = emoticonVersion;
    }

    public Integer getFnfHeaderVersion() {
        return fnfHd;
    }

    public void setFnfHeaderVersion(Integer fnfHeaderVersion) {
        this.fnfHd = fnfHeaderVersion;
    }

    public String getDialedNo() {
        return dNo;
    }

    public void setDialedNo(String dialedNo) {
        this.dNo = dialedNo;
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

    public String getDeviceToken() {
        return dt;
    }

    public void setDeviceToken(String deviceToken) {
        this.dt = deviceToken;
    }

    public Integer getContactType() {
        return ct;
    }

    public void setContactType(Integer contactType) {
        this.ct = contactType;
    }

    public Boolean getIncomingNotification() {
        return incomingNotification;
    }

    public void setIncomingNotification(Boolean incomingNotification) {
        this.incomingNotification = incomingNotification;
    }

}
