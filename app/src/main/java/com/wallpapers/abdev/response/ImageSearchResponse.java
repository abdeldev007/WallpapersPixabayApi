package com.wallpapers.abdev.response;

 import com.google.gson.annotations.SerializedName;
 import com.wallpapers.abdev.model.Image;

 import java.util.ArrayList;
 import java.util.List;


public class ImageSearchResponse {


    @SerializedName("hits")
    public ArrayList<Image> images;
}
