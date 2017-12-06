package com.vincent.yatmdbvandenryse.activities.configActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vincent.yatmdbvandenryse.Config;
import com.vincent.yatmdbvandenryse.R;
import com.vincent.yatmdbvandenryse.activities.mainActivity.PopularMovieView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by vincent on 29/11/17.
 */

public class ConfigAdapter extends RecyclerView.Adapter<ConfigViewHolder> {

    List<String> options;
    int positionChecked;
    final ConfigAdapter thisAdapter = this;

    public ConfigAdapter(List<String> options, int positionChecked) {
        this.options = options;
        this.positionChecked = positionChecked;
        Log.d("CREATEADAPTER",options.get(0) + " ::: "+options.size());
    }

    public int getPositionChecked() {
        Log.d("POSITION","getPositionChecked : "+positionChecked);
        return positionChecked;
    }

    public void setPositionChecked(int positionChecked) {
        this.positionChecked = positionChecked;
    }

    @Override

    public ConfigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        Log.d("CHILDREN_ISSUES","Before");
        View view = inflater.inflate(R.layout.item_config_radio,parent,false);
        Log.d("CHILDREN_ISSUES","After");


        ConfigViewHolder cvh = new ConfigViewHolder(view);


        return cvh;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public ConfigAdapter getThisAdapter() {
        return thisAdapter;
    }

    @Override
    public void onBindViewHolder(ConfigViewHolder holder, final int position) {

        holder.rb.setText(options.get(position));
        Log.d("BIND",position +" : " +options.get(position));
        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisAdapter.setPositionChecked(position);
                thisAdapter.notifyDataSetChanged();

            }
        });

        if (position==positionChecked)
            holder.rb.setChecked(true);
        else

            holder.rb.setChecked(false);


    }


    @Override
    public int getItemCount() {
        return options.size();
    }
}
