package com.vincent.yatmdbvandenryse.activities.configActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.vincent.yatmdbvandenryse.R;

import java.util.List;
import java.util.Locale;

public class ConfigActivity extends AppCompatActivity {

    public static final String EXTRA_LOCALE = "EXTRA_LOCALE";
    public static final String EXTRA_POSTER_SIZES = "EXTRA_POSTER_SIZES";
    public static final String EXTRA_BACKDROP_SIZES = "EXTRA_BACKDROP_SIZES";

    public static final int RESULT_OK = 1;
    Locale locale;
    List<String> posterSizes;
    List<String> backdropSizes;
    int posterPos;
    int backdropPos;

    ConfigAdapter adap_posterSizes;
    ConfigAdapter adap_backdropSizes;
    RecyclerView rv_posterSizes;
    RecyclerView rv_backdropSizes;
    SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Intent intent = getIntent();
        Context context = getBaseContext();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preferencesFile), Context.MODE_PRIVATE);

        backdropPos = sharedPref.getInt(getString(R.string.savedBackdropSize),0);
        posterPos = sharedPref.getInt(getString(R.string.savedPosterSize),0);
        locale = new Locale(sharedPref.getString(getString(R.string.savedLocale),Locale.FRENCH.toString()));

        Log.d("LOCALES",locale.toString());

        posterSizes = (List<String>)intent.getSerializableExtra(EXTRA_POSTER_SIZES);
        backdropSizes = (List<String>)intent.getSerializableExtra(EXTRA_BACKDROP_SIZES);

        String frtt = Locale.FRENCH.toString();
        String entt = Locale.ENGLISH.toString();
        RadioButton rbToCheck = null;
        if(locale.toString().equals(Locale.FRENCH.toString()))
            rbToCheck = (RadioButton)findViewById(R.id.rb_french);
        else if (locale.toString().equals(Locale.ENGLISH.toString()))
            rbToCheck = (RadioButton)findViewById(R.id.rb_english);


        if (rbToCheck != null)
            rbToCheck.setChecked(true);

        final Button apply = (Button)findViewById(R.id.bt_apply);
        apply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                apply(v);
            }
        });


        adap_posterSizes = new ConfigAdapter(posterSizes,posterPos);
        adap_backdropSizes = new ConfigAdapter(backdropSizes,backdropPos);
        rv_backdropSizes = (RecyclerView)findViewById(R.id.rv_backdropSize);
        rv_posterSizes = (RecyclerView)findViewById(R.id.rv_posterSize);

        rv_backdropSizes.setAdapter(adap_backdropSizes);
        rv_posterSizes.setAdapter(adap_posterSizes);

        rv_backdropSizes.setLayoutManager(new LinearLayoutManager(this));
        rv_posterSizes.setLayoutManager(new LinearLayoutManager(this));


    }

    public void apply(View v) {

        RadioButton rb_english = (RadioButton)findViewById(R.id.rb_english);
        RadioButton rb_french = (RadioButton)findViewById(R.id.rb_french);
        posterPos = adap_posterSizes.getPositionChecked();
        backdropPos = adap_backdropSizes.getPositionChecked();


        if (rb_english.isChecked())
            locale = Locale.ENGLISH;
        else if (rb_french.isChecked())
            locale = Locale.FRENCH;

        Locale.setDefault(locale);

        Intent data = new Intent();

        Log.d("POSITION","getPositionChecked poster : "+posterPos);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.savedLocale),locale.toString());
        editor.putInt(getString(R.string.savedPosterSize),posterPos);
        editor.putInt(getString(R.string.savedBackdropSize),backdropPos);
        editor.commit();
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void finish() {
         super.finish();
    }



}
