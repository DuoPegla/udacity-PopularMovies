package com.duopegla.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;
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
        //String request = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;

        URL request = NetworkUtilities.buildRequestUrl(apiKey, NetworkUtilities.MovieResultSort.MOST_POPULAR);

        new FetchMovieDataTask().execute(request);

    }

    public class FetchMovieDataTask extends AsyncTask<URL, Void, Movie[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(URL... params) {

            if (params.length == 0)
            {
                return null;
            }

//            Uri builtUri = Uri.parse(params[0]).buildUpon().build();
//            URL url = null;
//            try
//            {
//                url = new URL(builtUri.toString());
//            }
//            catch (MalformedURLException e)
//            {
//                e.printStackTrace();
//                return null;
//            }

            //Log.d("URL", url.toString());

            try
            {
                //return NetworkUtilities.getResponseFromHttpUrl(url);
                String moviesJson = NetworkUtilities.getResponseFromHttpUrl(params[0]);
                return TheMovieDbJsonUtilities.getMoviesFromJson(moviesJson);
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movies[]) {
            if (movies != null)
            {
                //mTextView.setText(movies.toString());
                StringBuilder resultStringBuilder = new StringBuilder();

                for (int i = 0; i < movies.length; i++)
                {
                    resultStringBuilder.append(movies[i].toString()).append("\n\n\n");
                }

                mTextView.setText(resultStringBuilder.toString());

                Log.d("LOG", "Received result for " + movies.length + " movies.");
            }
            else
            {
                Log.d("ERROR", "Returned null result");
            }
        }
    }
}
