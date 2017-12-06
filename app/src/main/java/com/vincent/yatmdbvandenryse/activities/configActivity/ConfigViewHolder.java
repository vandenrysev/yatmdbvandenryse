package com.vincent.yatmdbvandenryse.activities.configActivity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.vincent.yatmdbvandenryse.R;

/**
 * Created by vincent on 29/11/17.
 */

public class ConfigViewHolder extends RecyclerView.ViewHolder {

    RadioButton rb ;

    public ConfigViewHolder(View itemView) {
        super(itemView);
        rb = itemView.findViewById(R.id.rb_radio);

    }
}
