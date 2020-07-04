package com.wallpapers.abdev.ui.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wallpapers.abdev.R;
import com.wallpapers.abdev.adapter.GridGalleryAdapter;
import com.wallpapers.abdev.model.GalleryModel;
import com.wallpapers.abdev.util.Constants;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {
     @SuppressLint("StaticFieldLeak")
    public static GridGalleryAdapter recyclerViewAdapter;
    private GridView recyclerView;
    private SwipeRefreshLayout recyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_gallery);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initComponents();


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
     //   recyclerView.setHasFixedSize(true);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new GridGalleryAdapter(GalleryActivity.this, getData());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<GalleryModel> getData() {
        ArrayList<GalleryModel> filesList = new ArrayList<>();
        GalleryModel f;
        String targetPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+getResources().getString(R.string.app_name)    ;
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();
        if (files == null) {
        //   findViewById(R.id.non_image_text).setVisibility(View.INVISIBLE);
        }
        try {
            Arrays.sort(files, (o1, o2) -> {

                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
            );

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                f = new GalleryModel();
                f.setName(getStringSizeLengthFile(file.length()));
                f.setFilename(file.getName());
                f.setUri(Uri.fromFile(file));
                f.setPath(files[i].getAbsolutePath());
                filesList.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filesList;
    }
    public  String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }
    private void initComponents() {
        recyclerView = (GridView) findViewById(R.id.recycler_view);
        recyclerLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRecyclerView);
        recyclerLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerLayout.setRefreshing(true);
                setUpRecyclerView();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerLayout.setRefreshing(false);
                        Toast.makeText(GalleryActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);

            }
        });

  /*       LinearLayout linearlayout = (LinearLayout) findViewById(R.id.unitads);
       try{
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, StaticData.BANNER_ID, com.facebook.ads.AdSize.BANNER_320_50);
            linearlayout.addView(adView);
            adView.loadAd();

            StaticData.SHOW_INTERTISIAL_COUNT++;

            if (StaticData.SHOW_INTERTISIAL_COUNT%StaticData.SHOW_INTERTISIAL_DELAY==0){
                FacebookAds facebookInterstitial=new FacebookAds(this.getApplicationContext(),this);


                facebookInterstitial.loadInterstitial();



            }


        }catch(Exception ignored){
        }
*/
    }
    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state

    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore RecyclerView state

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
