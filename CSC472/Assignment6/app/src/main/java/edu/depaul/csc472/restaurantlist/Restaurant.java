package edu.depaul.csc472.restaurantlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RZHUANG on 10/21/2015.
 */
public class Restaurant implements Parcelable {
    enum Type {Restaurant, Dessert, CoffeeTea, Bakeries, IceCream}

    String name;
    Type type;
    String location;
    String image;
    float rating = 4.0f;
    int reviews = 0;
    String image1;
    String image2;
    String image3;

    public Restaurant(String name, Type type, String location, float rating, int reviews, String image, String image1, String image2, String image3) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.rating = rating;
        this.reviews = reviews;
        this.image = image;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getReviewText() {
        return reviews + " Reviews";
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image1 = image3;
    }


    public String toString() {
        return name;
    }


    public static int getIconResource(Type type) {
        switch (type) {
            case Restaurant:
                return R.drawable.restaurant;
            case Dessert:
                return R.drawable.dessert;
            case CoffeeTea:
                return R.drawable.coffeetea;
            case Bakeries:
                return R.drawable.bakeries;
            case IceCream:
                return R.drawable.icecream;
        }
        return -1;
    }

    // implement Parcelable
    private Restaurant(Parcel in) {
        name = in.readString();
        type = Type.values()[in.readInt()];
        location = in.readString();
        rating = in.readFloat();
        reviews = in.readInt();
        image = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        image3 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(type.ordinal());
        out.writeString(location);
        out.writeFloat(rating);
        out.writeInt(reviews);
        out.writeString(image);
        out.writeString(image1);
        out.writeString(image2);
        out.writeString(image3);
    }

    public static final Parcelable.Creator<Restaurant> CREATOR
            = new Parcelable.Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
