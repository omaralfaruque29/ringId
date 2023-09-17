/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.stores;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import com.ipvision.model.AlbumImageMap;
import com.ipvision.model.AlbumMap;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.view.feed.SingleStatusPanelInCircleNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInFriendNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInBookHome;
import com.ipvision.view.feed.SingleStatusPanelInMyBook;
import com.ipvision.view.feed.SingleStatusPanelInNotification;
//import org.ipvision.book.SingleStatusPanelInShareFeed;
//import org.ipvision.book.SingleStatusPanelViewImage;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.service.utility.SortedArrayList;
import com.ipvision.view.image.ViewProfileImageFromQueue;

/**
 *
 * @author Faiz Ahmed
 */
public class NewsFeedMaps {

    public static NewsFeedMaps hash_maps;

    public static NewsFeedMaps getInstance() {
        if (hash_maps == null) {
            hash_maps = new NewsFeedMaps();
        }
        return hash_maps;
    }

    private Map<Long, NewsFeedWithMultipleImage> allNewsFeeds = new ConcurrentHashMap<Long, NewsFeedWithMultipleImage>();

    private SortedArrayList allNewfeedId = new SortedArrayList();
    private SortedArrayList myNewsFeedId = new SortedArrayList();
    private Map<String, SortedArrayList> friendNewsFeedId = new ConcurrentHashMap<String, SortedArrayList>();
    private Map<Long, SortedArrayList> circleNewsFeedId = new ConcurrentHashMap<Long, SortedArrayList>();
    private Map<Long, SortedArrayList> shareNewsFeedId = new ConcurrentHashMap<Long, SortedArrayList>();
    private Map<Long, Long> parentFeedMap = new ConcurrentHashMap<Long, Long>();

    //private SortedArrayList shareNewsFeedId = new SortedArrayList();
    private Set<Long> notificationNewsFeedId = new ConcurrentSkipListSet<Long>();
    private Set<String> allreadyHavePackedIds = new ConcurrentSkipListSet<String>();
    private Map<String, JsonFields> myAllbums = new ConcurrentHashMap<String, JsonFields>();
    private Map<Long, Map<String, JsonFields>> comments = new ConcurrentHashMap<Long, Map<String, JsonFields>>();
    private Map<String, Map<String, SingleMemberInCircleDto>> likes = new ConcurrentHashMap<String, Map<String, SingleMemberInCircleDto>>();
    private Map<String, Map<String, JsonFields>> alBumImages = new ConcurrentHashMap<String, Map<String, JsonFields>>();
    private Map<String, Long> processingLikes = new ConcurrentHashMap<String, Long>();
    private Map<String, Map<String, JsonFields>> friendsAllbums = new ConcurrentHashMap<String, Map<String, JsonFields>>();
    private Map<String, AlbumMap> tempFriendsAllbums = new ConcurrentHashMap<String, AlbumMap>();
    private Map<String, Map<String, Map<String, JsonFields>>> friendsAlbumImages = new ConcurrentHashMap<String, Map<String, Map<String, JsonFields>>>();
    private Map<String, AlbumImageMap> tempFriendsAllbumsImages = new ConcurrentHashMap<String, AlbumImageMap>();

    //private Map<Long, Map<String, SingleMemberInCircleDto>> imageLikes = new ConcurrentHashMap<Long, Map<String, SingleMemberInCircleDto>>();
    //private Map<Long, Map<String, JsonFields>> imageComments = new ConcurrentHashMap<Long, Map<String, JsonFields>>();
    private Set<Long> unreadBookHomeNewsFeeds = new HashSet<Long>();
    private Map<String, Set<Long>> unreadFriendNewsFeeds = new ConcurrentHashMap<String, Set<Long>>();
    private Map<Long, Set<Long>> unreadCircleNewsFeeds = new ConcurrentHashMap<Long, Set<Long>>();
    private Set<String> imagesInDownloadingState = new ConcurrentSkipListSet<>();

    private Map<Long, SingleStatusPanelInBookHome> singleStatusPanelInBookHome = new ConcurrentHashMap<Long, SingleStatusPanelInBookHome>();
    private Map<Long, SingleStatusPanelInMyBook> singleStatusPanelInMyBook = new ConcurrentHashMap<Long, SingleStatusPanelInMyBook>();
    private Map<Long, SingleStatusPanelInFriendNewsFeed> singleStatusPanelInFriendNewsFeed = new ConcurrentHashMap<Long, SingleStatusPanelInFriendNewsFeed>();
    private Map<Long, SingleStatusPanelInCircleNewsFeed> singleStatusPanelInCircleNewsFeed = new ConcurrentHashMap<Long, SingleStatusPanelInCircleNewsFeed>();
    private Map<Long, SingleStatusPanelInNotification> singleStatusPanelInNotification = new ConcurrentHashMap<Long, SingleStatusPanelInNotification>();
    //private Map<Long, SingleStatusPanelInShareFeed> singleStatusPanelInShareFeed = new ConcurrentHashMap<Long, SingleStatusPanelInShareFeed>();
    private Set<String> allreadyTriedToDownload = new ConcurrentSkipListSet<String>();
    private ViewProfileImageFromQueue viewProfileImageQueue = new ViewProfileImageFromQueue();

    public ViewProfileImageFromQueue getViewProfileImageQueue() {
        return viewProfileImageQueue;
    }
    
    /* getter setter*/

    public Set<String> getAllreadyTriedToDownload() {
        return allreadyTriedToDownload;
    }

    public void setAllreadyTriedToDownload(Set<String> allreadyTriedToDownload) {
        this.allreadyTriedToDownload = allreadyTriedToDownload;
    }

    public Set<String> getImagesInDownloadingState() {
        return imagesInDownloadingState;
    }

    public void setImagesInDownloadingState(Set<String> imagesInDownloadingState) {
        this.imagesInDownloadingState = imagesInDownloadingState;
    }

    public Map<Long, NewsFeedWithMultipleImage> getAllNewsFeeds() {
        return allNewsFeeds;
    }

    public void setAllNewsFeeds(Map<Long, NewsFeedWithMultipleImage> allNewsFeeds) {
        this.allNewsFeeds = allNewsFeeds;
    }

    public SortedArrayList getAllNewfeedId() {
        return allNewfeedId;
    }

    public void setAllNewfeedId(SortedArrayList allNewfeedId) {
        this.allNewfeedId = allNewfeedId;
    }

    public SortedArrayList getMyNewsFeedId() {
        return myNewsFeedId;
    }

    public void setMyNewsFeedId(SortedArrayList myNewsFeedId) {
        this.myNewsFeedId = myNewsFeedId;
    }

    public Set<Long> getNotificationNewsFeedId() {
        return notificationNewsFeedId;
    }

    public void setNotificationNewsFeedId(Set<Long> notificationNewsFeedId) {
        this.notificationNewsFeedId = notificationNewsFeedId;
    }

    public Map<String, SortedArrayList> getFriendNewsFeedId() {
        return friendNewsFeedId;
    }

    public void setFriendNewsFeedId(Map<String, SortedArrayList> friendNewsFeedId) {
        this.friendNewsFeedId = friendNewsFeedId;
    }

    public Map<Long, SortedArrayList> getCircleNewsFeedId() {
        return circleNewsFeedId;
    }

    public void setCircleNewsFeedId(Map<Long, SortedArrayList> circleNewsFeedId) {
        this.circleNewsFeedId = circleNewsFeedId;
    }

    public Map<Long, SortedArrayList> getShareNewsFeedId() {
        return shareNewsFeedId;
    }

    public void setShareNewsFeedId(Map<Long, SortedArrayList> shareNewsFeedId) {
        this.shareNewsFeedId = shareNewsFeedId;
    }

//    public SortedArrayList getShareNewsFeedId() {
//        return shareNewsFeedId;
//    }
//
//    public void setShareNewsFeedId(SortedArrayList shareNewsFeedId) {
//        this.shareNewsFeedId = shareNewsFeedId;
//    }
    public Map<String, JsonFields> getMyAllbums() {
        return myAllbums;
    }

    public void setMyAllbums(Map<String, JsonFields> myAllbums) {
        this.myAllbums = myAllbums;
    }

    public Map<Long, Map<String, JsonFields>> getComments() {
        return comments;
    }

    public void setComments(Map<Long, Map<String, JsonFields>> comments) {
        this.comments = comments;
    }

    public Map<String, Map<String, SingleMemberInCircleDto>> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Map<String, SingleMemberInCircleDto>> likes) {
        this.likes = likes;
    }

    public Map<String, Map<String, JsonFields>> getAlBumImages() {
        return alBumImages;
    }

    public void setAlBumImages(Map<String, Map<String, JsonFields>> alBumImages) {
        this.alBumImages = alBumImages;
    }

    public Map<String, Long> getProcessingLikes() {
        return processingLikes;
    }

    public void setProcessingLikes(Map<String, Long> processingLikes) {
        this.processingLikes = processingLikes;
    }

    public Map<String, Map<String, JsonFields>> getFriendsAllbums() {
        return friendsAllbums;
    }

    public void setFriendsAllbums(Map<String, Map<String, JsonFields>> friendsAllbums) {
        this.friendsAllbums = friendsAllbums;
    }

    public Map<String, AlbumMap> getTempFriendsAllbums() {
        return tempFriendsAllbums;
    }

    public void setTempFriendsAllbums(Map<String, AlbumMap> tempFriendsAllbums) {
        this.tempFriendsAllbums = tempFriendsAllbums;
    }

    public Map<String, Map<String, Map<String, JsonFields>>> getFriendsAlbumImages() {
        return friendsAlbumImages;
    }

    public void setFriendsAlbumImages(Map<String, Map<String, Map<String, JsonFields>>> friendsAlbumImages) {
        this.friendsAlbumImages = friendsAlbumImages;
    }

    public Map<String, AlbumImageMap> getTempFriendsAllbumsImages() {
        return tempFriendsAllbumsImages;
    }

    public void setTempFriendsAllbumsImages(Map<String, AlbumImageMap> tempFriendsAllbumsImages) {
        this.tempFriendsAllbumsImages = tempFriendsAllbumsImages;
    }

    /*public Map<Long, Map<String, SingleMemberInCircleDto>> getImageLikes() {
     return imageLikes;
     }

     public void setImageLikes(Map<Long, Map<String, SingleMemberInCircleDto>> imageLikes) {
     this.imageLikes = imageLikes;
     }*/

    /*public Map<Long, Map<String, JsonFields>> getImageComments() {
     return imageComments;
     }

     public void setImageComments(Map<Long, Map<String, JsonFields>> imageComments) {
     this.imageComments = imageComments;
     }*/
    public Set<String> getAllreadyHavePackedIds() {
        return allreadyHavePackedIds;
    }

    public void setAllreadyHavePackedIds(Set<String> allreadyHavePackedIds) {
        this.allreadyHavePackedIds = allreadyHavePackedIds;
    }

    public Set<Long> getUnreadBookHomeNewsFeeds() {
        return unreadBookHomeNewsFeeds;
    }

    public void setUnreadBookHomeNewsFeeds(Set<Long> aUnreadBookHomeNewsFeeds) {
        unreadBookHomeNewsFeeds = aUnreadBookHomeNewsFeeds;
    }

    public Map<String, Set<Long>> getUnreadFriendNewsFeeds() {
        return unreadFriendNewsFeeds;
    }

    public void setUnreadFriendNewsFeeds(Map<String, Set<Long>> aUnreadFriendNewsFeeds) {
        unreadFriendNewsFeeds = aUnreadFriendNewsFeeds;
    }

    public Map<Long, Set<Long>> getUnreadCircleNewsFeeds() {
        return unreadCircleNewsFeeds;
    }

    public void setUnreadCircleNewsFeeds(Map<Long, Set<Long>> aUnreadCircleNewsFeeds) {
        unreadCircleNewsFeeds = aUnreadCircleNewsFeeds;
    }

    public Map<Long, SingleStatusPanelInBookHome> getSingleStatusPanelInBookHome() {
        return singleStatusPanelInBookHome;
    }

    public void setSingleStatusPanelInBookHome(Map<Long, SingleStatusPanelInBookHome> singleStatusPanelInHome) {
        this.singleStatusPanelInBookHome = singleStatusPanelInHome;
    }

    public Map<Long, SingleStatusPanelInMyBook> getSingleStatusPanelInMyBook() {
        return singleStatusPanelInMyBook;
    }

    public void setSingleStatusPanelInMyBook(Map<Long, SingleStatusPanelInMyBook> singleStatusPanelInMyBook) {
        this.singleStatusPanelInMyBook = singleStatusPanelInMyBook;
    }

    public Map<Long, SingleStatusPanelInFriendNewsFeed> getSingleStatusPanelInFriendNewsFeed() {
        return singleStatusPanelInFriendNewsFeed;
    }

    public void setSingleStatusPanelInFriendNewsFeed(Map<Long, SingleStatusPanelInFriendNewsFeed> singleStatusPanelInFriendNewsFeed) {
        this.singleStatusPanelInFriendNewsFeed = singleStatusPanelInFriendNewsFeed;
    }

    public Map<Long, SingleStatusPanelInCircleNewsFeed> getSingleStatusPanelInCircleNewsFeed() {
        return singleStatusPanelInCircleNewsFeed;
    }

    public void setSingleStatusPanelInCircleNewsFeed(Map<Long, SingleStatusPanelInCircleNewsFeed> singleStatusPanelInCircleNewsFeed) {
        this.singleStatusPanelInCircleNewsFeed = singleStatusPanelInCircleNewsFeed;
    }

    public Map<Long, SingleStatusPanelInNotification> getSingleStatusPanelInNotification() {
        return singleStatusPanelInNotification;
    }

    public void setSingleStatusPanelInNotification(Map<Long, SingleStatusPanelInNotification> singleStatusPanelInNoification) {
        this.singleStatusPanelInNotification = singleStatusPanelInNoification;
    }

    public Map<Long, Long> getParentFeedMap() {
        return parentFeedMap;
    }

    public void setParentFeedMap(Map<Long, Long> parentFeedMap) {
        this.parentFeedMap = parentFeedMap;
    }

//    public Map<Long, SingleStatusPanelInShareFeed> getSingleStatusPanelInShareFeed() {
//        return singleStatusPanelInShareFeed;
//    }
//
//    public void setSingleStatusPanelInShareFeed(Map<Long, SingleStatusPanelInShareFeed> singleStatusPanelInShareFeed) {
//        this.singleStatusPanelInShareFeed = singleStatusPanelInShareFeed;
//    }
}
