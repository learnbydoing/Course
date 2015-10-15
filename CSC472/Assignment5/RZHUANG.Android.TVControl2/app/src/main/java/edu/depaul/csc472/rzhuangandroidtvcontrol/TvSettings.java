/**
 * Created by RZHUANG on 10/15/2015.
 */
package edu.depaul.csc472.rzhuangandroidtvcontrol;

public class TvSettings {
    private int favorite = 0;
    private String title = "";
    private int channel = 0;

    public TvSettings(){

    }
    public TvSettings(int favorite, String title, int channel){
        this.favorite = favorite;
        this.title = title;
        this.channel = channel;
    }

    public int getFavorite() {
        return this.favorite;
    }

    public String getTitle() {
        return this.title;
    }

    public int getChannel() {
        return this.channel;
    }
}
