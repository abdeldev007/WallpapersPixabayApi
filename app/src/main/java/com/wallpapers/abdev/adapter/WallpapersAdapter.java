package com.wallpapers.abdev.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Pulse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wallpapers.abdev.R;
import com.wallpapers.abdev.model.Image;
import com.wallpapers.abdev.ui.activities.MainActivity;
import com.wallpapers.abdev.ui.activities.imagesViewer;
import com.wallpapers.abdev.util.Constants;
import com.wallpapers.abdev.util.SetAsWallpaperAsync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Image> photos;
    private ViewHolder viewHolder;
    private int counter=0;

    public WallpapersAdapter(Context c, ArrayList<Image> listPhotos) {
        context = c;
        photos = listPhotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image photo = photos.get(position);

        String a = photo.userImageUrl;

        Sprite doubleBounce = new Pulse();
        holder.progressBar.setIndeterminateDrawable(doubleBounce);
        Glide.with(context).load(photos.get(position).largeImageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (photo.largeImageURL.endsWith("png"))
                            holder.progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).into(holder.image);
        holder.likes.setText(String.valueOf(photo.likes));
        holder.comments.setText(String.valueOf(photo.commentsCount));
        holder.downloads.setText(String.valueOf(photo.downloadsCount));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter++;
                if(counter% Constants.SHOW_NTTERSTITIAL_COUNTER ==0)
                {
                    if (MainActivity.mInterstitialAd.isLoaded()){
                        MainActivity.mInterstitialAd.show();
                        MainActivity.mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                MainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                Intent intent = new Intent(context, imagesViewer.class);
                                Bundle bundle = new Bundle();
                                intent.putExtra("pos", position);
                                bundle.putSerializable("photos", photos);
                                intent.putExtra("bundle", bundle);
                                context.startActivity(intent);
                            }
                        });
                    }else {
                        MainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent intent = new Intent(context, imagesViewer.class);
                        Bundle bundle = new Bundle();
                        intent.putExtra("pos", position);
                        bundle.putSerializable("photos", photos);
                        intent.putExtra("bundle", bundle);
                        context.startActivity(intent);
                    }
/*
                    if(interstitialAd.isAdLoaded())
                    {

                        interstitialAd.show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {

                                interstitialAd.loadAd();

                                Intent intent = new Intent(context, imagesViewer.class);
                                Bundle bundle = new Bundle();
                                intent.putExtra("pos", position);
                                bundle.putSerializable("photos", photos);
                                intent.putExtra("bundle", bundle);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(Ad ad) {

                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        });

                    }
                    else
                    {
                        Intent intent = new Intent(context, imagesViewer.class);
                        Bundle bundle = new Bundle();
                        intent.putExtra("pos", position);
                        bundle.putSerializable("photos", photos);
                        intent.putExtra("bundle", bundle);
                        context.startActivity(intent);
                    }

 */
                }
                else {

                    Intent intent = new Intent(context, imagesViewer.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("pos", position);
                    bundle.putSerializable("photos", photos);
                    intent.putExtra("bundle", bundle);
                    context.startActivity(intent);
                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(ArrayList<Image> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ProgressBar progressBar;
        TextView likes, comments, downloads;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            progressBar = itemView.findViewById(R.id.progress);
            likes = itemView.findViewById(R.id.likes);

            comments = itemView.findViewById(R.id.comments);
            downloads = itemView.findViewById(R.id.downloads);

        }
    }

}
