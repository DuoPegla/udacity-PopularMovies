package com.duopegla.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("KEY", getResources().getString(R.string.themoviedb_api_key));

        mImageView = (ImageView) findViewById(R.id.iv_image_test);
        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(mImageView);

        mTextView = (TextView) findViewById(R.id.tv_movie_test);
        String apiKey = getResources().getString(R.string.themoviedb_api_key);
        String request = "https://api.themoviedb.org/3/movie/550?api_key=" + apiKey;
        new FetchMovieDataTask().execute(request);

    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            if (params.length == 0)
            {
                return null;
            }

            Uri builtUri = Uri.parse(params[0]).buildUpon().build();
            URL url = null;
            try
            {
                url = new URL(builtUri.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return null;
            }

            Log.d("URL", url.toString());

            try
            {
                return NetworkUtilities.getResponseFromHttpUrl(url);
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null)
            {
                mTextView.setText(s);
            }
            else
            {
                Log.d("ERROR", "Returned null result");
            }
        }
    }
}
