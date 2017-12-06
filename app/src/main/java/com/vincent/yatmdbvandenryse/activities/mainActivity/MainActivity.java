package com.vincent.yatmdbvandenryse.activities.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vincent.yatmdbvandenryse.R;
import com.vincent.yatmdbvandenryse.activities.configActivity.ConfigActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static PopularAdapter.Selection selection = PopularAdapter.Selection.popular;

    static Locale locale = null;

    static PopularAdapter.Mode mode = PopularAdapter.Mode.list;

    static PopularAdapter.Category category = PopularAdapter.Category.movie;

    private RecyclerView popularRecycler;
    private PopularAdapter popularAdapter;
    String search = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    selection = PopularAdapter.Selection.popular;
                    break;
                case R.id.navigation_nowplaying:
                    selection = PopularAdapter.Selection.nowPlaying;
                    break;
                case R.id.navigation_best:
                    selection = PopularAdapter.Selection.topRated;
                    break;
            }
            popularAdapter.setSelection(selection);
            popularAdapter.updateData();
            return true;
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigActivity.RESULT_OK)
        {

            Context context = getBaseContext();
            SharedPreferences sharedPref = getSharedpreference();

            popularAdapter.setBackdropSize(sharedPref.getInt(getString(R.string.savedBackdropSize),0));
            popularAdapter.setPosterSize(sharedPref.getInt(getString(R.string.savedPosterSize),0));

            locale = new Locale(sharedPref.getString(getString(R.string.savedLocale),Locale.FRENCH.toString()));

            popularAdapter.setLocale(locale);
            popularAdapter.updateData();

            //on change la langue de l'appli

            this.recreate();
        }
    }

    SharedPreferences getSharedpreference()
    {
        Context context = getBaseContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preferencesFile), Context.MODE_PRIVATE);
        return sharedPref;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
                Context c = this.getBaseContext();
                Intent intent = new Intent(c, ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_LOCALE,locale);
                intent.putExtra(ConfigActivity.EXTRA_POSTER_SIZES,(ArrayList)popularAdapter.getConfiguration().getImages().getPosterSizes());
                intent.putExtra(ConfigActivity.EXTRA_BACKDROP_SIZES,(ArrayList)popularAdapter.getConfiguration().getImages().getBackdropSizes());
                startActivityForResult(intent,ConfigActivity.RESULT_OK);

                return true;

            case R.id.action_view:
                switch (mode)
                {
                    case poster:
                        mode = PopularAdapter.Mode.immersive;
                        break;
                    case immersive:
                        mode = PopularAdapter.Mode.list;
                        break;
                    case list:
                        mode = PopularAdapter.Mode.poster;
                        break;
                }
                changeView();

                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.action, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (locale == null)
        {
            SharedPreferences sp = getSharedpreference();
            locale = new Locale(sp.getString(getString(R.string.savedLocale),Locale.FRENCH.toString()));
        }

        changeLocale(locale);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        populateRecycler();
        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryTo(v);
            }
        };

        ((Button)findViewById(R.id.bt_movie)).setOnClickListener(ocl);
        ((Button)findViewById(R.id.bt_tvshow)).setOnClickListener(ocl);

        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText)findViewById(R.id.txt_search);
                search = et.getText().toString();
                Log.d("SEARCH_STRING",search);
                popularAdapter.setSearch(search);
                popularAdapter.updateData();
                popularRecycler.refreshDrawableState();
            }
        });

        findViewById(R.id.bt_clearSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = (EditText)findViewById(R.id.txt_search);
                et.setText("");
                search = "";
                popularAdapter.setSearch("");
                popularAdapter.updateData();
                popularRecycler.refreshDrawableState();
            }
        });



    }

    private void setLayoutFromMode()
    {
        switch (mode)
        {
            case list:
            case immersive:
                popularRecycler.setLayoutManager(new LinearLayoutManager(this));
                break;
            case poster:
                popularRecycler.setLayoutManager(new GridLayoutManager(this,3));
                break;
        }
    }

    private void populateRecycler()
    {
        popularRecycler = (RecyclerView)findViewById(R.id.popularRecycler);
        setLayoutFromMode();
        if (popularAdapter == null)
        {
            Log.d("POSITION","popular est null");
            popularAdapter = new PopularAdapter(mode,selection,locale,category,getBaseContext());
        }
        else
        {
            popularAdapter.setMode(mode);
            popularAdapter.setSelection(selection);
            popularAdapter.setLocale(locale);
            popularAdapter.setCategory(category);
        }

        popularRecycler.setAdapter(popularAdapter);
    }

    private void changeView()
    {

        popularRecycler = (RecyclerView)findViewById(R.id.popularRecycler);
        setLayoutFromMode();

        PopularAdapter popAdap = (PopularAdapter)popularRecycler.getAdapter();
        popAdap.setMode(mode);
        popAdap.updateLayout();
        // on refait le set pour forcer a recharger
        //Sinon certain holder ne recharge pas
        popularRecycler.setAdapter(popAdap);
    }

    private void  changeLocale(Locale l)
    {
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        Resources res  = getBaseContext().getResources();
        res.updateConfiguration(conf,res.getDisplayMetrics());

    }

    void setCategoryTo(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_movie:
                category = PopularAdapter.Category.movie;
                break;
            case R.id.bt_tvshow:
                category = PopularAdapter.Category.tvShow;
                break;
        }

        popularAdapter.setCategory(category);
        popularAdapter.updateData();
    }

    public void favorite(View v)
    {
        category = PopularAdapter.Category.favorite;
        popularAdapter.setCategory(category);
        popularAdapter.updateData();
    }


}
