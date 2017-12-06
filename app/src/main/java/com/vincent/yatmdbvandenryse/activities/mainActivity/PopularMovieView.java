package com.vincent.yatmdbvandenryse.activities.mainActivity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vincent.yatmdbvandenryse.R;
import com.vincent.yatmdbvandenryse.api.model.Movie;

/**
 * Created by vincent on 26/11/17.
 */

public class PopularMovieView extends RecyclerView.ViewHolder
{
    TextView tv_title;
    TextView tv_description;
    TextView tv_mark;
    ImageView iv_image;


    public PopularMovieView(View itemView)
    {
        super(itemView);
        tv_title = (TextView)itemView.findViewById(R.id.tv_title);
        tv_description = (TextView)itemView.findViewById(R.id.tv_description);
        tv_mark = (TextView)itemView.findViewById(R.id.tv_mark);
        iv_image = (ImageView)itemView.findViewById(R.id.iv_image);
    }
}