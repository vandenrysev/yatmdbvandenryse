package com.vincent.yatmdbvandenryse.activities.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.vincent.yatmdbvandenryse.CacheManager;
import com.vincent.yatmdbvandenryse.activities.movieActivity.MovieActivity;
import com.vincent.yatmdbvandenryse.api.ViewLink;
import com.vincent.yatmdbvandenryse.api.model.Configuration;
import com.vincent.yatmdbvandenryse.api.model.Result;
import com.vincent.yatmdbvandenryse.api.tmdbAPI;

import com.vincent.yatmdbvandenryse.R;
import com.vincent.yatmdbvandenryse.api.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vincent on 26/11/17.
 */



public class PopularAdapter extends RecyclerView.Adapter<PopularMovieView> {


    public enum Mode
    {
        list,
        poster,
        immersive
    }

    public enum Category
    {
        movie,
        tvShow,
        favorite
    }

    public enum Selection
    {
        popular,
        topRated,
        nowPlaying, search
    }

    List<ViewLink> viewElement = new ArrayList<ViewLink>();
    RecyclerView.Adapter<PopularMovieView> thisAdapter = this;
    Configuration configuration = new Configuration();
    int layout ;
    tmdbAPI api;
    Mode mode; //Type de vue a envoyer
    Category category; // Film, Serie ou autre
    Selection selection; // Popular, TopRated, etc
    Locale locale ; // langue
    //si search est vide on affiche les discover sinon on recherche
    String search = ""; //search
    Context context;
    static int posterSize = 0;
    static int backdropSize = 0;

    public int getPosterSize() {
        return posterSize;
    }

    public void setPosterSize(int posterSize) {
        this.posterSize = posterSize;
    }

    public int getBackdropSize() {
        return backdropSize;
    }

    public void setBackdropSize(int backdropSize) {
        this.backdropSize = backdropSize;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Category getCategory() {
        return category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }



    PopularAdapter(Mode mode,Selection selection,Locale locale,Category category,Context context)
    {
        super();
        this.category = category;
        this.locale = locale;
        this.mode = mode;
        this.selection = selection;
        Log.d("POSITION","Set to 0");
        updateLayout();
        this.context = context;

        SharedPreferences sp =context.getSharedPreferences(context.getString(R.string.preferencesFile),Context.MODE_PRIVATE);
        posterSize = sp.getInt(context.getString(R.string.savedPosterSize),0);
        backdropSize = sp.getInt(context.getString(R.string.savedBackdropSize),0);

        api = new tmdbAPI();
        api.getServiceApi().getConfiguration().enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                configuration = response.body();
                Log.d("CONFIG",configuration.getImages().getBaseUrl());
                thisAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
                Log.d("OUPS","Failed to retrieve configuration");
            }
        });

        updateData();

    }


    public void updateData()
    {

        Callback callback = new Callback<Result<ViewLink>>() {
            @Override
            public void onResponse(Call<Result<ViewLink>> call, retrofit2.Response<Result<ViewLink>> response) {
                if(response.body()!= null && response.body().getResults() != null)
                {
                    viewElement = response.body().getResults();
                    cache(viewElement);
                }

                else
                {
                    viewElement = new ArrayList<ViewLink>();
                    Movie m = new Movie();
                    m.setTitle("Pas de donnée reçue");
                    m.setOverview("Soit il n'y a pas de connexion internet soit la requête n'est pas bonne");
                }
                thisAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Result<ViewLink>> call, Throwable t) {
                Movie m = new Movie();
                m.setTitle("Oups pas de donnée");
                m.setOverview("Il y a eu un problème lors de l'appel a l'API");
                viewElement.add(0,m);
            }
        };

        if(search == "")
            switch (category)
            {
                case movie:
                    switch (selection)
                    {
                        case popular:
                            api.getServiceApi().getPopularMovies(locale.toString()).enqueue(callback);
                            break;
                        case nowPlaying:
                            api.getServiceApi().getPlayingNowMovies(locale.toString()).enqueue(callback);
                            break;
                        case topRated:
                            api.getServiceApi().getTopRatedMovies(locale.toString()).enqueue(callback);
                            break;

                    }
                    break;
                case tvShow:
                    switch (selection)
                    {
                        case popular:
                            api.getServiceApi().getPopularTV(locale.toString()).enqueue(callback);
                            break;
                        case nowPlaying:
                            api.getServiceApi().getOnTheAirTV(locale.toString()).enqueue(callback);
                            break;
                        case topRated:
                            api.getServiceApi().getTopRatedTV(locale.toString()).enqueue(callback);
                            break;
                    }
                    break;
                case favorite:
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        public void run() {
                            setViewElementFromFavorite();
                            notifyDataSetChanged();
                        }
                    };
                    handler.postDelayed(r, 100);
                    break;

            }
        else
            switch (category)
            { //search mode
                case movie:
                    api.getServiceApi().searchMovieByQuery(search,locale.toString()).enqueue(callback);
                    break;
                case tvShow:
                    api.getServiceApi().searchTvByQuery(search,locale.toString()).enqueue(callback);
                    break;
            }
    }


    @Override
    public PopularMovieView onCreateViewHolder(ViewGroup parent, int viewType) {
    // Appelé lorsque que l'on créer un ViewHolder
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);


        PopularMovieView pmv = new PopularMovieView(view);


        return pmv;
    }



    @Override
    public void onBindViewHolder(PopularMovieView holder, final int position)
    //On rempli le view holder
    {
        try {
            final ViewLink m = viewElement.get(position);

            holder.tv_title.setText(m.getTitle());
            holder.tv_description.setText(m.getOverview());
            holder.tv_mark.setText(m.getVoteAverage().toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context c = v.getContext();
                    Intent intent = new Intent(c,MovieActivity.class);
                    intent.putExtra(MovieActivity.EXTRA_VIEWLINK,m);
                    intent.putExtra(MovieActivity.EXTRA_CONFIGURATION,configuration);
                    intent.putExtra(MovieActivity.EXTRA_LOCALE,locale);
                    intent.putExtra(MovieActivity.EXTRA_POSTER_SIZES,posterSize);
                    intent.putExtra(MovieActivity.EXTRA_BACKDROP_SIZES,backdropSize);
                    intent.putExtra(MovieActivity.EXTRA_MOVIE_ID,m.getId());
                    c.startActivity(intent);
                }
            });
            // On utilise Picasso car c'est 20 fois (au moins) plus facile
            if (configuration != null) {
                String posterPath = "";
                Log.d("POSITION",posterSize+"");
                switch (mode)
                {
                    case immersive:
                        posterPath = configuration.getImages().getBaseUrl() + configuration.getImages().getBackdropSizes().get(backdropSize) + m.getBackdropPath();
                        break;
                    case list:
                    case poster:
                        posterPath = configuration.getImages().getBaseUrl() + configuration.getImages().getPosterSizes().get(posterSize) + m.getPosterPath();
                        break;
                }
                Log.d("POSTERPATH", posterPath);
                Picasso.with(holder.iv_image.getContext()).load(posterPath).into(holder.iv_image);
            }
        }
        catch(Exception e)
        {
            this.updateData();
        }
    }

    @Override
    public int getItemCount() {
        if(viewElement==null)
            return 0;
        return viewElement.size();
    }

    public void updateLayout()
    {
        switch (mode)
        {
            case list:
                layout = R.layout.item_popularmovie_view_list;
                break;
            case poster:
                layout = R.layout.item_popularmovie_view_poster;
                break;
            case immersive:
                layout = R.layout.item_popularmovie_view_immersive;
                break;

        }
        this.notifyDataSetChanged();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setViewElementFromFavorite()
    {
        CacheManager cm = new CacheManager(context);
        viewElement = new ArrayList<>();
        SharedPreferences sp = getSharedpreference();
        Set<String> set = sp.getStringSet(context.getString(R.string.savedFavorite),null);

        int i;
        if(set != null)
            for(String s:set)
            {
                viewElement.add(cm.get(s));
            }


    }

    SharedPreferences getSharedpreference()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preferencesFile), Context.MODE_PRIVATE);
        return sharedPref;
    }

    private void cache(List<ViewLink> avl)
    {

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                CacheManager cm = new CacheManager(context);
                cm.putAll(viewElement);
                cm.save();
            }
        };
        handler.post(r);
    }
}
