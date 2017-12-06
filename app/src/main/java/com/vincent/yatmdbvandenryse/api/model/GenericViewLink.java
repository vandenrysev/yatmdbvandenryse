package com.vincent.yatmdbvandenryse.api.model;

import com.vincent.yatmdbvandenryse.api.ViewLink;

/**
 * Created by vincent on 06/12/17.
 */

public class GenericViewLink implements ViewLink {
    String Title = "";
    String Overview = "";
    String PosterPath = "";
    String BackdropPath = "";
    Float voteAverage = new Float(0.0);
    Integer Id = new Integer(0);
    String UId = "";



    @Override
    public String toString() {
        return "GenericViewLink{" +
                "Title='" + Title + '\'' +
                ", Overview='" + Overview + '\'' +
                ", PosterPath='" + PosterPath + '\'' +
                ", BackdropPath='" + BackdropPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", Id=" + Id +
                ", UId='" + UId + '\'' +
                '}';
    }

    @Override
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    @Override
    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    @Override
    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }

    @Override
    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @Override
    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }
}
