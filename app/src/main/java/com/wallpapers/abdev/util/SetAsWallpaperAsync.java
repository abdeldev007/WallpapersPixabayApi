package com.wallpapers.abdev.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetAsWallpaperAsync extends AsyncTask<Object, Void, Bitmap> {

    private Context context;
    private String urlPath;

    public SetAsWallpaperAsync(Context context, String url) {

        this.context = context;
        this.urlPath = url;

    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            url = new URL(urlPath);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);

            connection.connect();

            input = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmapFrmUrl = BitmapFactory.decodeStream(input);
        return bitmapFrmUrl;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        if (result != null) {

            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(context);
            try {

                myWallpaperManager.setBitmap(result);
                Toast.makeText(context, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

