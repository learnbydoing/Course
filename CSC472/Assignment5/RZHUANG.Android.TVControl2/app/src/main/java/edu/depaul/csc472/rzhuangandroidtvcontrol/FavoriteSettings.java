package edu.depaul.csc472.rzhuangandroidtvcontrol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RZHUANG on 10/19/2015.
 */
public class FavoriteSettings implements Parcelable {
    public int id;
    public String title;
    public int channel;

    /**
     * Constructs a FavoriteSettings from values
     */
    public FavoriteSettings (int id, String title, int channel) {
        this.id = id;
        this.title = title;
        this.channel = channel;
    }

    /**
     * Constructs a FavoriteSettings from a Parcel
     * @param parcel Source Parcel
     */
    public FavoriteSettings (Parcel parcel) {
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.channel = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(channel);
    }

    // Method to recreate a FavoriteSettings from a Parcel
    public static Creator<FavoriteSettings> CREATOR = new Creator<FavoriteSettings>() {

        @Override
        public FavoriteSettings createFromParcel(Parcel source) {
            return new FavoriteSettings(source);
        }

        @Override
        public FavoriteSettings[] newArray(int size) {
            return new FavoriteSettings[size];
        }

    };
}
