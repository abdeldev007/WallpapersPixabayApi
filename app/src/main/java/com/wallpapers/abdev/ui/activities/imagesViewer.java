package com.wallpapers.abdev.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.wallpapers.abdev.R;
import com.wallpapers.abdev.adapter.RecycleViewPagerAdapter;
import com.wallpapers.abdev.model.Image;
import com.wallpapers.abdev.util.SetAsWallpaperAsync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class imagesViewer extends AppCompatActivity {
    public static boolean ALLOWED_TO_DOWNLOAD=false;
    public static FancyAlertDialog.Builder alert ;
    ArrayList<Image> listImages = new ArrayList<>();
    private Context context;
    private Activity activity;



    @SuppressLint("StaticFieldLeak")
    public static TextView txtLikes;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtDownloads;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtComment;
    public static int pos=0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_images_viewer);
        activity = this;
        context = imagesViewer.this;

        try {
          ALLOWED_TO_DOWNLOAD=  checkPermission();
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("bundle");
            txtLikes = findViewById(R.id.likes);
            txtDownloads = findViewById(R.id.downloads);
            txtComment = findViewById(R.id.comments);
            ArrayList<Image> a = (ArrayList<Image>) bundle.getSerializable("photos");
            int position = intent.getIntExtra("pos", 0);
            if (a != null)
                listImages.addAll(a);

            HorizontalInfiniteCycleViewPager pager = findViewById(R.id.hhhhhh);

            RecycleViewPagerAdapter adapter = new RecycleViewPagerAdapter(listImages, getBaseContext(),this);
            pager.setAdapter(adapter);
            pager.bringToFront();
            pager.setCurrentItem(position);
            Toast.makeText(imagesViewer.this, "" + pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
            int currentPos = pager.getCurrentItem();

            FrameLayout frame_imageDetails = findViewById(R.id.frame_imageDetails);
            frame_imageDetails.bringToFront();
            Button btnBackImageViewer = findViewById(R.id.btnBackImageViewer);
            btnBackImageViewer.bringToFront();
            btnBackImageViewer.setOnClickListener(v -> finish());


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode==123){
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                checkAgain();
            }else
                ALLOWED_TO_DOWNLOAD=true;
        }




    }
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

      public void checkAgain() {

         if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(context)
                    .setTextTitle("Permission necessary ")
                    .setBody("Write Storage permission is necessary to Download Images")
                    .setNegativeColor(R.color.colorNegative)
                    .setNegativeButtonText("Cancel")
                    .setOnNegativeClicked((view1, dialog) -> dialog.dismiss())
                    .setPositiveButtonText("Delete")
                    .setPositiveColor(R.color.colorPositive)
                    .setOnPositiveClicked((view12, dialog) -> {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    })
                    .build();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }
    private int MY_PERMISSIONS_REQUEST_WRITE_STORAGE=123;

}
