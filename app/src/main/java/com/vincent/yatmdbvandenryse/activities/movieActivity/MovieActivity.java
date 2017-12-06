package com.vincent.yatmdbvandenryse.activities.movieActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.vincent.yatmdbvandenryse.api.ViewLink;
import com.vincent.yatmdbvandenryse.api.model.Configuration;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.vincent.yatmdbvandenryse.R;
import com.vincent.yatmdbvandenryse.api.model.Movie;
import com.vincent.yatmdbvandenryse.api.model.Result;
import com.vincent.yatmdbvandenryse.api.model.TvShow;
import com.vincent.yatmdbvandenryse.api.model.Video;
import com.vincent.yatmdbvandenryse.api.tmdbAPI;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {



    public static final String EXTRA_LOCALE = "EXTRA_LOCALE";
    public static final String EXTRA_VIEWLINK = "EXTRA_VIEWLINK";
    public static final String EXTRA_CONFIGURATION = "EXTRA_CONFIGURATION";
    public static final String EXTRA_POSTER_SIZES = "EXTRA_POSTER_SIZES";
    public static final String EXTRA_BACKDROP_SIZES = "EXTRA_BACKDROP_SIZES";
    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";

    Intent shareIntent;
    ViewLink viewLink;
    Locale locale;
    Configuration configuration;
    int backdropSize;
    int posterSize;
    int id;
    boolean favorite;
    SharedPreferences sp;
    Set<String> favorites;
    String videoLink = "";

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.movie_menu, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                startActivity(Intent.createChooser(shareIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        this.shareIntent = shareIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        viewLink = (ViewLink)intent.getSerializableExtra(EXTRA_VIEWLINK);
        locale = (Locale)intent.getSerializableExtra(EXTRA_LOCALE);
        configuration = (Configuration)intent.getSerializableExtra(EXTRA_CONFIGURATION);
        posterSize = intent.getIntExtra(EXTRA_POSTER_SIZES,0);
        backdropSize = intent.getIntExtra(EXTRA_BACKDROP_SIZES,0);
        id = intent.getIntExtra(EXTRA_MOVIE_ID,0);
        sp = getSharedpreference();
        favorites = sp.getStringSet(getString(R.string.savedFavorite),null);
        if (favorites != null)
        {
            favorite = favorites.contains(viewLink.getUId());
        }
        else
        {
            favorite = false;
        }

        tmdbAPI api = new tmdbAPI();
        Log.d("CLASS",viewLink.getClass().getName());
        Log.d("CLASS",Movie.class.getName());
        Callback callback = new Callback<Result<Video>>() {
            @Override
            public void onResponse(Call<Result<Video>> call, Response<Result<Video>> response) {
                if(response.body().getResults().size()>0)
                    videoLink = getString(R.string.prepath_youtube)+response.body().getResults().get(0).getKey();
                else
                    videoLink = "";



                String res = getString(R.string.shareMessage) + " : \n";
                res += viewLink.getTitle() + "\n";
                res += viewLink.getOverview() + "\n";
                res += videoLink;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, res);
                sendIntent.setType("text/plain");

                setShareIntent(Intent.createChooser(sendIntent, "Choose sharing method"));


            }

            @Override
            public void onFailure(Call<Result<Video>> call, Throwable t) {
                Toast.makeText(MovieActivity.this, "Problème lors de l'appel a l'API", Toast.LENGTH_SHORT).show();
            }
        };
        if (viewLink.getUId().contains("M") )
            api.getServiceApi().getMovieVideo(viewLink.getId(),locale.toString()).enqueue(callback);
        else if (viewLink.getUId().contains("T"))
            api.getServiceApi().getTvVideo(viewLink.getId(),locale.toString()).enqueue(callback);


        ImageView iv_bacdrop = (ImageView)findViewById(R.id.iv_backdrop);
        ImageView iv_poster = (ImageView)findViewById(R.id.iv_poster);
        TextView tv_desc = (TextView)findViewById(R.id.tv_description);
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        TextView tv_mark = (TextView)findViewById(R.id.tv_mark);
        ToggleButton tg = (ToggleButton)findViewById(R.id.tb_fav);
        
        tg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteClick(v);
            }
        });

        tv_desc.setText(viewLink.getOverview());
        tv_title.setText(viewLink.getTitle());
        tv_mark.setText(viewLink.getVoteAverage().toString());

        ((ToggleButton)findViewById(R.id.tb_fav)).setChecked(favorite);

        if (configuration != null) {
            String posterPath,backdropPath;
            posterPath = configuration.getImages().getBaseUrl() + configuration.getImages().getPosterSizes().get(posterSize) + viewLink.getPosterPath();
            backdropPath = configuration.getImages().getBaseUrl() + configuration.getImages().getPosterSizes().get(backdropSize) + viewLink.getBackdropPath();
            Picasso.with(iv_poster.getContext()).load(posterPath).into(iv_poster);
            Picasso.with(iv_bacdrop.getContext()).load(backdropPath).into(iv_bacdrop);
        }


    }


    SharedPreferences getSharedpreference()
    {
        Context context = getBaseContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preferencesFile), Context.MODE_PRIVATE);
        return sharedPref;
    }

    void favoriteClick(View v)
    {
        ToggleButton tg = (ToggleButton)findViewById(R.id.tb_fav);

        if (favorites == null)
            favorites = new TreeSet<String>();
        if(tg.isChecked())
        {
            favorites.add(viewLink.getUId());
        }
        else
            favorites.remove(viewLink.getUId());

        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(getString(R.string.savedFavorite),favorites);
        for (String s:favorites)
            Log.d("FAV",s);
        editor.commit();
    }

    void video(View v)
    {
        tmdbAPI api = new tmdbAPI();


        Callback callback = new Callback<Result<Video>>() {
            @Override
            public void onResponse(Call<Result<Video>> call, Response<Result<Video>> response) {
                String key;
                if(response.body().getResults().size()>0)
                {
                    key = response.body().getResults().get(0).getKey();
                    watchYoutubeVideo(getBaseContext(),key);
                }
                else
                    Toast.makeText(MovieActivity.this, "Oups Pas de vidéo trouvée", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<Result<Video>> call, Throwable t) {
                Toast.makeText(MovieActivity.this, "Problème lors de l'appel a l'API", Toast.LENGTH_SHORT).show();
            }
        };
        if (viewLink.getUId().contains("M") )
            api.getServiceApi().getMovieVideo(viewLink.getId(),locale.toString()).enqueue(callback);
        else if (viewLink.getUId().contains("T"))
            api.getServiceApi().getTvVideo(viewLink.getId(),locale.toString()).enqueue(callback);
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    void finish(View v)
    {
        finish();
    }

}
