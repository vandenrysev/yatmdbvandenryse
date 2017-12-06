package com.vincent.yatmdbvandenryse;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import com.vincent.yatmdbvandenryse.api.ViewLink;
import com.vincent.yatmdbvandenryse.api.model.GenericViewLink;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vincent on 06/12/17.
 */

public class CacheManager  {

    private SharedPreferences sp;
    private Context context;
    HashMap<String,ViewLink> vlMap;
    public CacheManager(Context context)
    {
        super();
        sp = context.getSharedPreferences(context.getString(R.string.favoriteFile),context.MODE_PRIVATE);
        this.context = context;
        vlMap = new HashMap<>();


        Set<String> set = sp.getStringSet(context.getString(R.string.savedCache),new HashSet<String>());
        for (String s:set)
        {
            Log.d("CACHEGSON",s);
            put(jsonToViewLink(s));
        }
    }

    public void put(ViewLink vl)
    {
        Log.d("PUTCACHE",vl.toString());
        vlMap.put(vl.getUId(),vl);
    }

    public void putAll(List<ViewLink> vlList)
    {
        for(ViewLink vl:vlList)
            vlMap.put(vl.getUId(),vl);
    }

    public ViewLink get(String key)
    {

        Log.d("GETCACHE",vlMap.get(key).toString());
        return vlMap.get(key);
    }

    public ArrayList<ViewLink> getList(List<String> li)
    {
        ArrayList<ViewLink> vlList = new ArrayList<ViewLink>();
        for(String i:li)
        {
            vlList.add(get(i));
        }
        return vlList;
    }

    public void save()
    {
        SharedPreferences.Editor ed = sp.edit();
        HashSet<String> asString = new HashSet<>();
        Gson gson = new Gson();
        for (ViewLink vl:vlMap.values())
        {
            asString.add(viewLinkToJson(vl));
        }

        ed.putStringSet(context.getString(R.string.savedCache),asString);
        ed.commit();
    }

    ViewLink jsonToViewLink(String json)
    {
        GenericViewLink gvl = new GenericViewLink();
        try
        {

            JSONObject jO = new JSONObject(json);
            gvl.setTitle(jO.getString("Title"));
            gvl.setOverview(jO.getString("Overview"));
            gvl.setBackdropPath(jO.getString("BackdropPath"));
            gvl.setId(jO.getInt("Id"));
            gvl.setPosterPath(jO.getString("PosterPath"));
            gvl.setUId(jO.getString("UId"));
            gvl.setVoteAverage(new Float(jO.getDouble("VoteAverage")));
        }
        catch (Exception e)
        {
            Log.d("CACHEJSONERROR",e.toString());
        }
        return gvl;
    }

    String viewLinkToJson(ViewLink vl)
    {
        JSONObject jO = new JSONObject();
        String r = "";
        try
        {

            jO.put("Title",vl.getTitle());
            jO.put("Overview",vl.getOverview());
            jO.put("BackdropPath",vl.getBackdropPath());
            jO.put("Id",vl.getId());
            jO.put("PosterPath",vl.getPosterPath());
            jO.put("UId",vl.getUId());
            jO.put("VoteAverage",vl.getVoteAverage());
            r = jO.toString();
        }
        catch (Exception e)
        {
            Log.d("CACHEJSONERROR",e.toString());
        }


        return r;
    }
}
