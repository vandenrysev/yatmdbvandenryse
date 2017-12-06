package com.vincent.yatmdbvandenryse.api.model;

import java.util.List;

/**
 * Created by vincent on 22/11/17.
 */

public class Result<T> {
    public void setResults(List<T> results) {
        this.results = results;
    }

    private List<T> results;

    public List<T> getResults() {
        return results;
    }
}
