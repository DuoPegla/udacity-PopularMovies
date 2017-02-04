package com.duopegla.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("KEY", getResources().getString(R.string.themoviedb_api_key));

        mImageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(mImageView);
    }
}
