package com.vincent.yatmdbvandenryse.api;

import com.vincent.yatmdbvandenryse.api.model.*;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by vincent on 22/11/17.
 */

public interface tmdbInterface
{

    @GET("movie/popular")
    Call<Result<Movie>> getPopularMovies(@Query("language") String language);

    @GET("movie/now_playing")
    Call<Result<Movie>> getPlayingNowMovies(@Query("language") String language);

    @GET("movie/top_rated")
    Call<Result<Movie>> getTopRatedMovies(@Query("language") String language);

    @GET("movie/latest")
    Call<Movie> getLatestMovies(@Query("language") String language);

    @GET("search/movie")
    Call<Result<Movie>> searchMovieByQuery(@Query("query") String query,@Query("language") String language);


    @GET("movie/{movie_id}/videos")
    Call<Result<Video>> getMovieVideo(@Path("movie_id") int movie_id,@Query("language") String language);

    @GET("configuration")
    Call<Configuration> getConfiguration();

    @GET("movie/{movie_id}/images")
    Call<GetImages> getImagesFromMovieId(@Path("movie_id") int movie_id,@Query("language") String language );

    @GET("tv/popular")
    Call<Result<TvShow>> getPopularTV(@Query("language") String language);

    @GET("tv/on_the_air")
    Call<Result<TvShow>> getOnTheAirTV(@Query("language") String language);


    @GET("tv/top_rated")
    Call<Result<TvShow>> getTopRatedTV(@Query("language") String language);

    @GET("search/tv")
    Call<Result<Movie>> searchTvByQuery(@Query("query") String query,@Query("language") String language);


    @GET("tv/{movie_id}/videos")
    Call<Result<Video>> getTvVideo(@Path("movie_id") int tv_id,@Query("language") String language);
}
