package com.vincent.yatmdbvandenryse.api.model;

/**
 * Created by vincent on 26/11/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetImages {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("backdrops")
    @Expose
    private List<Backdrop> backdrops = null;
    @SerializedName("posters")
    @Expose
    private List<Poster> posters = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

    public static class Poster {

        @SerializedName("aspect_ratio")
        @Expose
        private Float aspectRatio;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("iso_639_1")
        @Expose
        private String iso6391;
        @SerializedName("vote_average")
        @Expose
        private Integer voteAverage;
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        @SerializedName("width")
        @Expose
        private Integer width;

        public Float getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Float aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public Integer getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Integer voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }

    public static class Backdrop {

        @SerializedName("aspect_ratio")
        @Expose
        private Float aspectRatio;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("iso_639_1")
        @Expose
        private Object iso6391;
        @SerializedName("vote_average")
        @Expose
        private Float voteAverage;
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        @SerializedName("width")
        @Expose
        private Integer width;

        public Float getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Float aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Object getIso6391() {
            return iso6391;
        }

        public void setIso6391(Object iso6391) {
            this.iso6391 = iso6391;
        }

        public Float getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Float voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }
}