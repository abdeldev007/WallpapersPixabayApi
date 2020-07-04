package com.wallpapers.abdev.network;

 import com.wallpapers.abdev.response.ImageSearchResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ImageService {

    @GET("/api/")
    void searchHighRes(@Query("username") String username,
                       @Query("key") String key,
                       @Query("response_group") String responseGroup,
                       @Query("q") String query,
                       Callback<ImageSearchResponse> callback);



    @GET("/api/?key=16096537-e9585a6b9e05a790fd079926d&per_page=200")
    void searchCategoryPage(
            @Query("category") String query,
            @Query("page") int page,
            Callback<ImageSearchResponse> callback);

    @GET("/api/?key=16096537-e9585a6b9e05a790fd079926d&per_page=200")
    void searchCategoryAndKey(
            @Query("category") String cat,
            @Query("q")String query,
            Callback<ImageSearchResponse> callback);

}
