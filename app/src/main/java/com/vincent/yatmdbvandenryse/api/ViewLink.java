package com.vincent.yatmdbvandenryse.api;

import java.io.Serializable;

/**
 * Created by vincent on 28/11/17.
 */

public interface ViewLink extends Serializable {

    String getTitle();
    String getOverview();
    String getPosterPath();
    String getBackdropPath();
    Float getVoteAverage();
    Integer getId();
    String getUId();
    String toString();

}
