package com.wallpapers.abdev.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.wallpapers.abdev.R;
import com.wallpapers.abdev.model.GalleryModel;
import com.wallpapers.abdev.ui.activities.GalleryActivity;
import com.wallpapers.abdev.util.Constants;

import java.io.File;
import java.util.ArrayList;

public class GridGalleryAdapter extends BaseAdapter {

    Context context ;
    ArrayList<GalleryModel> filesList ;
   // private AdmobeAds manager;

    public GridGalleryAdapter(Context c, ArrayList<GalleryModel> l){
        filesList=l;
        context=c;
     //   manager = AdmobeAds.getinstence();
      //  manager.interInstence(context);
       // manager.loadads();
    }

    @Override
    public int getCount() {
        return filesList.size();
    }

    @Override
    public Object getItem(int position) {
        return filesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView =View.inflate(context, R.layout.gallery_card_row,null);
        final GalleryModel files = filesList.get(position);

       TextView    userName =  convertView.findViewById(R.id.profileUserName);
        ImageView   savedImage =   convertView.findViewById(R.id.mainImageView);
        ImageView  shareID =   convertView.findViewById(R.id.shareID);
        ImageView  deleteID = convertView.findViewById(R.id.deleteID);
        final Uri uri = Uri.parse(files.getUri().toString());
        final File file = new File(uri.getPath());
         userName.setText(files.getName());

        Glide.with(context)
                .load(files.getUri())
                .into( savedImage);
         savedImage.setOnClickListener(v -> {
       /*      try{
                 StaticData.SHOW_INTERTISIAL_COUNT++;

                 if (StaticData.SHOW_INTERTISIAL_COUNT%StaticData.SHOW_INTERTISIAL_DELAY==0){
                     FacebookAds facebookInterstitial=new FacebookAds(context, MainActivity.activity);


                     facebookInterstitial.loadInterstitial();



                 }
             }catch (Exception ignored){

             }


        */
  if(files.getUri().toString().endsWith(".jpg")){
                 Uri VideoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",file);
                 Intent intent = new Intent();
                 intent.setAction(Intent.ACTION_VIEW);
                 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                 intent.setDataAndType(VideoURI, "image/*");
                 try {
                     context.startActivity(intent);
                 } catch (ActivityNotFoundException e) {
                     Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                 }
             }
         });

        deleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String path = ( filesList.get(position)).getPath();
                final File file = new File(path);
                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(context)
                        .setTextTitle("DELETE?")
                        .setBody("Are you sure you want to delete this file?")
                        .setNegativeColor(R.color.colorNegative)
                        .setNegativeButtonText("Cancel")
                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonText("Delete")
                        .setPositiveColor(R.color.colorPositive)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                try {
                                    if (file.exists()) {
                                        boolean del = file.delete();
                                        filesList.remove(position);

                                        GalleryActivity.recyclerViewAdapter.notifyDataSetChanged ();

                                        notifyDataSetChanged();
                                        Toast.makeText(context, "File was Deleted", Toast.LENGTH_SHORT).show();
                                        if(del){
                                            MediaScannerConnection.scanFile(
                                                    context,
                                                    new String[]{ path, path},
                                                    new String[]{ "image/jpg","video/mp4"},
                                                    new MediaScannerConnection.MediaScannerConnectionClient()
                                                    {
                                                        public void onMediaScannerConnected()
                                                        {
                                                        }
                                                        public void onScanCompleted(String path, Uri uri)
                                                        {
                                                            Log.d("Video path: ",path);
                                                        }
                                                    });
                                        }
                                    }
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    // TODO let the user know the file couldn't be deleted
                                    e.printStackTrace();
                                }
                            }
                        })
                        .build();
                alert.show();


             }
        });

        shareID.setOnClickListener(v -> {
            Uri mainUri = Uri.fromFile(file);
                 Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                }




        });
         return convertView;
    }




}
