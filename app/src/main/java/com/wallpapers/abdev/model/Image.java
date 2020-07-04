package com.wallpapers.abdev.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Image implements Serializable {
    @SerializedName("previewURL")
    public String previewUrl;

    @SerializedName("webformatURL")
    public String webformatUrl;
     @SerializedName("likes")
    public String likes;
    @SerializedName("tags")
    public String tags;
    @SerializedName("user")
    public String userName;
    @SerializedName("userImageURL")
    public String userImageUrl;
    @SerializedName("largeImageURL")
    public String largeImageURL;


    @SerializedName("views")
    public long favoritesCount;

    @SerializedName("comments")
    public long commentsCount;

    @SerializedName("downloads")
    public long downloadsCount;

}
