package com.wallpapers.abdev.adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.wallpapers.abdev.R;
import com.wallpapers.abdev.model.Image;
import com.wallpapers.abdev.ui.activities.GalleryActivity;
import com.wallpapers.abdev.ui.activities.imagesViewer;
import com.wallpapers.abdev.util.SetAsWallpaperAsync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RecycleViewPagerAdapter extends PagerAdapter {
    private ArrayList<Image> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;

    public RecycleViewPagerAdapter(ArrayList<Image> list, Context context, Activity a) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        activity = a;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.image_pager_item, container, false);
        ImageView imageView = view.findViewById(R.id.image_albume);
        Button btnDownload = view.findViewById(R.id.btn_download);
        Button btnSetasBackg = view.findViewById(R.id.btn_setasback);
        imagesViewer.pos = position;
        imagesViewer.txtLikes.setText(String.valueOf(list.get(position).likes));
        imagesViewer.txtComment.setText(String.valueOf(list.get(position).commentsCount));
        imagesViewer.txtDownloads.setText(String.valueOf(list.get(position).downloadsCount));
        Glide.with(context).load(list.get(position).largeImageURL).into(imageView);
        container.addView(view);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = imagesViewer.ALLOWED_TO_DOWNLOAD;
                 if (result) {
                    download(list.get(position).largeImageURL);
                } else
                    Toast.makeText(context, "Agree to the permission first", Toast.LENGTH_SHORT).show();
            }
        });
        btnSetasBackg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please wait... Setting Wallpaper", Toast.LENGTH_SHORT).show();
                new SetAsWallpaperAsync(context.getApplicationContext(),
                        list.get(position).largeImageURL).execute();

            }
        });
        return view;
    }

    private void download(String url) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Toast.makeText(context, saveImage(resource), Toast.LENGTH_SHORT).show();
                        ;

                    }


                });
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + context.getResources().getString(R.string.app_name));
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


}
