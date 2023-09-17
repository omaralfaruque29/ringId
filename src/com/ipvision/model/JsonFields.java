/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author faizahmed
 */
public class JsonFields extends FeedBackFields {
    /*user basicinfo*/

    private String el;
    private String gr;
    private String mbl;
    //private String ln;
    private Integer psnc;
    private String wim;
    private String cnty;
    private Integer frnS;
    private String mblDc;
    private Integer smP;
    private String smIp;
    private String prIm;
    private String bDay;
    private String fdt;
    /**/
    private String usrPw;
    private String vsn;
    private String oPw;
    private String nPw;
    private String nOfHd;
    private String phN;
    private Integer dvc;
    private Long dunt;
    private Long caTm;
    private String caId;
    private String schPm;
    private Integer scat; //search category
    private int[] inOH;
    private String vsnMsg;
    private Boolean dwnMnd;
    private Integer fnfHd;
    private String secCo;//secCo
    private Integer emVsn;
    private short[] pvc;
    private Short ists;
    /**/
    private Integer ePr;
    private Integer mblPhPr;
    private Integer prImPr;
    private Integer bdPr;
    private Integer cImPr;
    private Boolean tCall;
    private String tIP;
    private String re;

    private String gNm;
    /*book*/
    private String sts;
    private String iurl;
    private String albn;
    private String cptn;

    private String cvImg;
    private String cmn;
    private Short il; // if i like in comments or not
    private Short ic;
    private Short is;
    private Long tl;
    private Short scl;

    /* SMS History */
    private Integer st; //Start Limit how much data you will received.
    private Long cut;
    private Long gpmut;
    private String dn;
    /*
     notification start
     */
    private String id;
    private Integer nt;
    private Long acId;
    private Integer mt;
    private String fndN;
    private Integer tn;
    private String ids;
    //notification end
    /* post to friend's wall start*/
    private String ffn;
    //private String fln;
    //post to friend's wall end

    private ArrayList<Long> imgIds;
    private Boolean isRead;
    private String friendIds;
    private String cIm;
    private Integer pst;
    private Integer imT;
    private String lctn;
    private String fGId;
    private Integer lkd;
    private Long sfId;
    private Long ns;
    private Long ts;
    private Long loc;
    //total image
    private Integer tim;
    private String dt;
    private String mailIP;
    private String imgIP;
    private String mrktIP;
    private Integer smtpPrt;
    private Integer imapPrt;
    private String oIP;
    private Integer oPrt;
    private Integer cimX;
    private Integer cimY;
    private Integer ct;
    /*friend feed activity*/
    // private String auId;//activist user id
    private String afn;//activist first name
    //private String aln;//activist last name
    private Short actvt;//type of activity

    private Integer lt; //login type
    private String did; //device unique id
    private Integer iev; //Is Email Verified?
    private Integer ispc;
    private Integer imnv;
    private String evc; //email verification code
    private String vc; //email verification code
    private String mblOrEml;
    private List<AdditionalInfoDTO> adInfo;
    private List<WorkInfoDTO> workInfolist;
    private WorkInfoDTO wrk;
    private SkillInfoDTO skill;
    private EducationInfoDTO edu;
    private UserDetailsInfoDTO userDetails;
    private String rb; // recovery by
    private ArrayList<String> sgtns; //suggestions
    private ArrayList<Long> fids;
    private Map<String, String> frnds;
    private String ntg;
    private Long twid;
    private String chIp;
    private Integer chRp;
    private Integer bv; //blocked
    private Boolean acpt;
    private Integer nct;
    private Boolean iscr;
    private String cc, hc, am;
    private Long mDay;
    private Long lot;
    private Integer lmt; // Newsfeed limit
    private Boolean sc; //Show Continue
    private Integer sn;
    private Integer sv;
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

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public Integer getSv() {
        return sv;
    }

    public void setSv(Integer sv) {
        this.sv = sv;
    }

    public Integer getSearchCategory() {
        return scat;
    }

    public void setSearchCategory(Integer scat) {
        this.scat = scat;
    }

    public Boolean getShowContinue() {
        return sc;
    }

    public void setShowContinue(Boolean sc) {
        this.sc = sc;
    }

    public Integer getLimit() {
        return lmt;
    }

    public void setLimit(Integer lmt) {
        this.lmt = lmt;
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

    public Long getMarriageDay() {
        return mDay;
    }

    public void setMarriageDay(long mDay) {
        this.mDay = mDay;
    }

    public String getAboutMe() {
        return am;
    }

    public void setAboutMe(String am) {
        this.am = am;
    }

    public Short getIShare() {
        return is;
    }

    public void setIShare(Short is) {
        this.is = is;
    }

    public Boolean getIsAccepted() {
        return acpt;
    }

    public void setIsAccepted(Boolean acpt) {
        this.acpt = acpt;
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

    public String getNewGroupName() {
        return ntg;
    }

    public void setNewGroupName(String ntg) {
        this.ntg = ntg;
    }

    public ArrayList<Long> getFriendIdList() {
        return fids;
    }

    public void setFriendIdList(ArrayList<Long> fids) {
        this.fids = fids;
    }

    public Map<String, String> getFriendMap() {
        return frnds;
    }

    public void setFriendMap(Map<String, String> frnds) {
        this.frnds = frnds;
    }

    public ArrayList<String> getSuggetions() {
        return sgtns;
    }

    public void setSuggetions(ArrayList<String> sgtns) {
        this.sgtns = sgtns;
    }

    public String getRecoveryBy() {
        return rb;
    }

    public void setRecoveryBy(String rb) {
        this.rb = rb;
    }

    public UserDetailsInfoDTO getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsInfoDTO userDetails) {
        this.userDetails = userDetails;
    }

    public List<AdditionalInfoDTO> getAdditionalInfo() {
        return adInfo;
    }

    public void setAdditionalInfo(List<AdditionalInfoDTO> adInfo) {
        this.adInfo = adInfo;
    }

    public List<WorkInfoDTO> getWorkInfoList() {
        return workInfolist;
    }

    public void setWorkInfoList(List<WorkInfoDTO> workInfolist) {
        this.workInfolist = workInfolist;
    }

    public WorkInfoDTO getWorkInfo() {
        return wrk;
    }

    public void setWorkInfo(WorkInfoDTO wrk) {
        this.wrk = wrk;
    }

    public SkillInfoDTO getSkillInfo() {
        return skill;
    }

    public void setSkillInfo(SkillInfoDTO skill) {
        this.skill = skill;
    }

    public EducationInfoDTO getEducationInfo() {
        return edu;
    }

    public void setEducationInfo(EducationInfoDTO edu) {
        this.edu = edu;
    }

    public String getAdditionalMblOrEml() {
        return mblOrEml;
    }

    public void setAdditionalMblOrEml(String mblOrEml) {
        this.mblOrEml = mblOrEml;
    }

    public String getVerificationCode() {
        return vc;
    }

    public void setVerificationCode(String vc) {
        this.vc = vc;
    }

    public String getEmailVerificationCode() {
        return evc;
    }

    public void setEmailVerificationCode(String evc) {
        this.evc = evc;
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

    public Integer getIspc() {
        return ispc;
    }

    public void setIspc(Integer ispc) {
        this.ispc = ispc;
    }

    public Integer getLoginType() {
        return lt;
    }

    public void setLoginType(Integer lt) {
        this.lt = lt;
    }

    public String getDeviceUniqueId() {
        return did;
    }

    public void setDeviceUniqueId(String did) {
        this.did = did;
    }
//
//    public String getAuId() {
//        return auId;
//    }
//
//    public void setAuId(String auId) {
//        this.auId = auId;
//    }

    public String getAfn() {
        return afn;
    }

    public void setAfn(String afn) {
        this.afn = afn;
    }

    /*public String getAln() {
     return aln;
     }

     public void setAln(String aln) {
     this.aln = aln;
     }*/
    public Short getActvt() {
        return actvt;
    }

    public void setActvt(Short actvt) {
        this.actvt = actvt;
    }
    /*end of activity*/

    public Integer getCoverImageX() {
        return cimX;
    }

    public void setCoverImageX(Integer cimX) {
        this.cimX = cimX;
    }

    public Integer getCoverImageY() {
        return cimY;
    }

    public void setCoverImageY(Integer cimY) {
        this.cimY = cimY;
    }

    public Short getIc() {
        return ic;
    }

    public void setIc(Short ic) {
        this.ic = ic;
    }

    public Integer getTotalImage() {
        return tim;
    }

    public void setTotalImage(Integer tim) {
        this.tim = tim;
    }

    public Long getLoc() {
        return loc;
    }

    public void setLoc(Long loc) {
        this.loc = loc;
    }

    public Long getNs() {
        return ns;
    }

    public void setNs(Long ns) {
        this.ns = ns;
    }

    public Long getSfId() {
        return sfId;
    }

    public void setSfId(Long sfId) {
        this.sfId = sfId;
    }

    public Integer getLkd() {
        return lkd;
    }

    public void setLkd(Integer lkd) {
        this.lkd = lkd;
    }

    public String getfGId() {
        return fGId;
    }

    public void setfGId(String fGId) {
        this.fGId = fGId;
    }

    public String getLocation() {
        return lctn;
    }

    public void setLocation(String lctn) {
        this.lctn = lctn;
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

    public String getCoverImage() {
        return cIm;
    }

    public void setCoverImage(String cIm) {
        this.cIm = cIm;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(String friendIds) {
        this.friendIds = friendIds;
    }

    public String getId() {
        return id;
    }

    public String getFfn() {
        return ffn;
    }

    public void setFfn(String ffn) {
        this.ffn = ffn;
    }

    /* public String getFln() {
     return fln;
     }

     public void setFln(String fln) {
     this.fln = fln;
     }*/
    public void setId(String id) {
        this.id = id;
    }

    public Integer getNt() {
        return nt;
    }

    public void setNt(Integer nt) {
        this.nt = nt;
    }

    public Long getAcId() {
        return acId;
    }

    public void setAcId(Long acId) {
        this.acId = acId;
    }

    public Integer getMt() {
        return mt;
    }

    public void setMt(Integer mt) {
        this.mt = mt;
    }

    public String getFndN() {
        return fndN;
    }

    public void setFndN(String fndN) {
        this.fndN = fndN;
    }

    public Integer getTn() {
        return tn;
    }

    public void setTn(Integer tn) {
        this.tn = tn;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getDestination() {
        return dn;
    }

    public void setDestination(String destination) {
        this.dn = destination;
    }

    public Long getCut() {
        return cut;
    }

    public void setCut(Long cut) {
        this.cut = cut;
    }

    public Long getGpmut() {
        return gpmut;
    }

    public void setGpmut(Long gpmut) {
        this.gpmut = gpmut;
    }

    public String getStatus() {
        return sts;
    }

    public void setStatus(String sts) {
        this.sts = sts;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
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

    public String getCvImg() {
        return cvImg;
    }

    public void setCvImg(String cvImg) {
        this.cvImg = cvImg;
    }

    public String getComment() {
        return cmn;
    }

    public void setComment(String cmn) {
        this.cmn = cmn;
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

    public Short getScl() {
        return scl;
    }

    public void setScl(Short scl) {
        this.scl = scl;
    }


    /*book end*/
    public String getRingEmail() {
        return re;
    }

    public void setRingEmail(String re) {
        this.re = re;
    }

    public Boolean getTdmCall() {
        return tCall;
    }

    public void setTdmCall(Boolean tdmCall) {
        this.tCall = tdmCall;
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

    public Integer getEmoticonVersion() {
        return emVsn;
    }

    public void setEmoticonVersion(Integer emoticonVersion) {
        this.emVsn = emoticonVersion;
    }

    public String getSecretCode() {
        return secCo;
    }

    public void setSecretCode(String secretCode) {
        this.secCo = secretCode;
    }

    public Integer getFnfHeaderVersion() {
        return fnfHd;
    }

    public void setFnfHeaderVersion(Integer fnfHeaderVersion) {
        this.fnfHd = fnfHeaderVersion;
    }

    public String getFriendDeviceToken() {
        return fdt;
    }

    public void setFriendDeviceToken(String fdt) {
        this.fdt = fdt;
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

    public Integer getDevice() {
        return dvc;
    }

    public void setDevice(Integer device) {
        this.dvc = device;
    }

    public Boolean getDownloadMandatory() {
        return dwnMnd;
    }

    public void setDownloadMandatory(Boolean downloadMandatory) {
        this.dwnMnd = downloadMandatory;
    }

    public String getVersionMessage() {
        return vsnMsg;
    }

    public void setVersionMessage(String versionMessage) {
        this.vsnMsg = versionMessage;
    }

    public int[] getIndexOfHeaders() {
        return inOH;
    }

    public void setIndexOfHeaders(int[] indexOfHeaders) {
        this.inOH = indexOfHeaders;
    }

    public String getProfileImage() {
        return prIm;
    }

    public void setProfileImage(String profileImage) {
        this.prIm = profileImage;
    }

    public Integer getFriendShipStatus() {
        return frnS;
    }

    public void setFriendShipStatus(Integer friendShipStatus) {
        this.frnS = friendShipStatus;
    }

    public Integer getPresence() {
        return psnc;
    }

    public void setPresence(Integer presence) {
        this.psnc = presence;
    }

    public String getOldPass() {
        return oPw;
    }

    public void setOldPass(String oldPass) {
        this.oPw = oldPass;
    }

    public String getNewPass() {
        return nPw;
    }

    public void setNewPass(String newPass) {
        this.nPw = newPass;
    }

    public String getWhatisInYourMind() {
        return wim;
    }

    public void setWhatisInYourMind(String whatisInYourMind) {
        this.wim = whatisInYourMind;
    }

    public String getNoOfHeaders() {
        return nOfHd;
    }

    public void setNoOfHeaders(String noOfHeaders) {
        this.nOfHd = noOfHeaders;
    }

    public String getGender() {
        return gr;
    }

    public void setGender(String gender) {
        this.gr = gender;
    }


    /*public String getLastName() {
     return ln;
     }

     public void setLastName(String lastName) {
     this.ln = lastName;
     }*/
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

    public String getMobilePhone() {
        return mbl;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mbl = mobilePhone;
    }

    public String getPhoneNo() {
        return phN;
    }

    public void setPhoneNo(String phoneNo) {
        this.phN = phoneNo;
    }

    public Long getDuration() {
        return dunt;
    }

    public void setDuration(Long duration) {
        this.dunt = duration;
    }

    public Long getCallingTime() {
        return caTm;
    }

    public void setCallingTime(Long callingTime) {
        this.caTm = callingTime;
    }

    public String getCallId() {
        return caId;
    }

    public void setCallId(String callId) {
        this.caId = callId;
    }

    public String getPassword() {
        return usrPw;
    }

    public void setPassword(String password) {
        this.usrPw = password;
    }

    public String getVersion() {
        return vsn;
    }

    public void setVersion(String version) {
        this.vsn = version;
    }

    public String getSearchParam() {
        return schPm;
    }

    public void setSearchParam(String searchParam) {
        this.schPm = searchParam;
    }

    public int getEmailPrivacy() {
        return ePr;
    }

    public void setEmailPrivacy(Integer emailPrivacy) {
        this.ePr = emailPrivacy;
    }

    public int getMobilePhonePrivacy() {
        return mblPhPr;
    }

    public void setMobilePhonePrivacy(Integer mobilePhonePrivacy) {
        this.mblPhPr = mobilePhonePrivacy;
    }

    public int getProfileImagePrivacy() {
        return prImPr;
    }

    public void setProfileImagePrivacy(Integer profileImagePrivacy) {
        this.prImPr = profileImagePrivacy;
    }

    public int getBirthdayPrivacy() {
        return bdPr;
    }

    public void setBirthdayPrivacy(Integer birthdayPrivacy) {
        this.bdPr = birthdayPrivacy;
    }

    public Integer getCoverImagePrivacy() {
        return cImPr;
    }

    public void setCoverImagePrivacy(Integer cImPr) {
        this.cImPr = cImPr;
    }

    public String getTerminationIPPort() {
        return tIP;
    }

    public void setTerminationIPPort(String terminationIPPort) {
        this.tIP = terminationIPPort;
    }

    public Integer getStartLimit() {
        return st;
    }

    public void setStartLimit(Integer startLimit) {
        this.st = startLimit;
    }

    public String getCircleName() {
        return gNm;
    }

    public void setCircleName(String gNm) {
        this.gNm = gNm;
    }

    public Short getIntegerStatus() {
        return ists;
    }

    public void setIntegerStatus(Short ists) {
        this.ists = ists;
    }

    public ArrayList<Long> getImageIds() {
        return imgIds;
    }

    public void setImageIds(ArrayList<Long> imgIds) {
        this.imgIds = imgIds;
    }

    public String getDeviceToken() {
        return dt;
    }

    public void setDeviceToken(String deviceToken) {
        this.dt = deviceToken;
    }

    public String getMailServerIP() {
        return mailIP;
    }

    public void setMailServerIP(String mailServerIP) {
        this.mailIP = mailServerIP;
    }

    public String getImageServerIP() {
        return imgIP;
    }

    public void setImageServerIP(String imageServerIP) {
        this.imgIP = imageServerIP;
    }

    public String getMarketServerIP() {
        return mrktIP;
    }

    public void setMarketServerIP(String marketIP) {
        this.mrktIP = marketIP;
    }

    public Integer getSmtpPort() {
        return smtpPrt;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPrt = smtpPort;
    }

    public Integer getImapPort() {
        return imapPrt;
    }

    public void setImapPort(Integer imapPort) {
        this.imapPrt = imapPort;
    }

    public String getOfflineServerIP() {
        return oIP;
    }

    public void setOfflineServerIP(String offlineServerIP) {
        this.oIP = offlineServerIP;
    }

    public Integer getOfflineServerPort() {
        return oPrt;
    }

    public void setOfflineServerPort(Integer offlineServerPort) {
        this.oPrt = offlineServerPort;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Integer getContactType() {
        return ct;
    }

    public void setContactType(Integer contactType) {
        this.ct = contactType;
    }

    public Long getGroupOwnerUserTableId() {
        return twid;
    }

    public void setGroupOwnerUserTableId(Long twid) {
        this.twid = twid;
    }

    public String getChatServerIp() {
        return chIp;
    }

    public void setChatServerIp(String chatServerIp) {
        this.chIp = chatServerIp;
    }

    public Integer getChatRegistrationPort() {
        return chRp;
    }

    public void setChatRegistrationPort(Integer registrationPort) {
        this.chRp = registrationPort;
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
