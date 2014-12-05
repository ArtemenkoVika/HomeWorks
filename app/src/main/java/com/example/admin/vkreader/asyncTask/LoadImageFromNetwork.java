package com.example.admin.vkreader.asyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

public class LoadImageFromNetwork extends AsyncTask<String, Void, Bitmap> {
    protected Bitmap imageT;
    ImageView imageBitmap;
    //String urlI;
    //ImageLoader imageLoader;
    //Context context;

    public LoadImageFromNetwork(ImageView imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        //this.context = context;
    }

    protected Bitmap doInBackground(String... url) {
        try {
            //urlI = url[0];
            //imageLoader = ImageLoader.getInstance();
            //imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            InputStream init = new java.net.URL(url[0]).openStream();
            imageT = BitmapFactory.decodeStream(init);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageT;
    }

    protected void onPostExecute(Bitmap param) {
        imageBitmap.setImageBitmap(param);
        //imageLoader.displayImage(urlI, imageBitmap);
        imageBitmap.setVisibility(View.VISIBLE);
    }
}
