/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Ashraful
 */
public class SearchItem {

    private long search_time = 0;
    private String search_string = "";
    private String user_id = "";
    private String session_id = "";

    public String getSearch_string() {
        return search_string;
    }

    public void setSearch_string(String search_string) {
        this.search_string = search_string;
    }

    public long getSearch_time() {
        return search_time;
    }

    public void setSearch_time(long search_time) {
        this.search_time = search_time;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
