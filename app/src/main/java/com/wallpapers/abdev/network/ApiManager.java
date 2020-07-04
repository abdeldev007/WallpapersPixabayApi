package com.wallpapers.abdev.network;

import com.wallpapers.abdev.response.ImageSearchResponse;
import com.wallpapers.abdev.util.Auth;
import com.wallpapers.abdev.util.Constants;
import retrofit.Callback;
import retrofit.RestAdapter;



public class ApiManager {


    private static ImageService sService;
    private static ApiManager sInstance;

    static {
        initializeService();
    }

    private ApiManager() {

    }

    private static void initializeService() {

        RestAdapter.Builder restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        sService = restAdapter.build().create(ImageService.class);
    }

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }
        return sInstance;
    }

    /**
     * Search for high resolution images
     *
     * @param query
     * @param callback
     */
    public void searchHighRes(String query, Callback<ImageSearchResponse> callback) {
        sService.searchHighRes(
                Auth.getInstance().getUsername(),
                Auth.getInstance().getKey(),
                "high_resolution",
                query, callback);
    }




      public void searchCategory(String query,int page,    Callback<ImageSearchResponse> callback) {
        sService.searchCategoryPage(
                query,
                page,
                callback);
    }
    public void searchCategoryAndKey(String cat, String query,   Callback<ImageSearchResponse> callback) {
        sService.searchCategoryAndKey(
                cat,
                query,
                callback);
    }

}
