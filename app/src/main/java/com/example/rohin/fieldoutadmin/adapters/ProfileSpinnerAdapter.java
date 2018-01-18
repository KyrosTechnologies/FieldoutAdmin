package com.example.rohin.fieldoutadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

/**
 * Created by kyros on 15-12-2017.
 */


import android.database.DataSetObserver;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * Created by Rohin on 14-09-2017.
 */

public class ProfileSpinnerAdapter implements SpinnerAdapter,ListAdapter {
    private static final int EXTRA=1;
    private final SpinnerAdapter adapter;
    private final Context context;
    private final int nothingSelectedLayout;
    private final int nothingSelectedDropdownLayout;
    private final LayoutInflater layoutInflater;
    public ProfileSpinnerAdapter(SpinnerAdapter spinnerAdapter, int nothingSelectedLayout, Context context){
        this(spinnerAdapter,nothingSelectedLayout,-1,context);
    }
    private ProfileSpinnerAdapter(SpinnerAdapter spinnerAdapter
            , int nothingSelectedLayout, int nothingSelectedDropdownLayout, Context context){
        this.adapter=spinnerAdapter;
        this.context=context;
        this.nothingSelectedLayout=nothingSelectedLayout;
        this.nothingSelectedDropdownLayout=nothingSelectedDropdownLayout;
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position !=0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            return nothingSelectedDropdownLayout==-1?
                    new View(context):
                    getNothingSelectedDropdownView(parent);
        }
        return adapter.getDropDownView(position-EXTRA,null,parent);
    }
    private View getNothingSelectedDropdownView(ViewGroup parent){
        return layoutInflater.inflate(nothingSelectedDropdownLayout,parent,false);

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        int count=adapter.getCount();
        return count==0?0: count+EXTRA;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : adapter.getItem(position - EXTRA);
    }

    @Override
    public long getItemId(int position) {
        return position >= EXTRA ? adapter.getItemId(position - EXTRA) : position - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return getNothingSelectedView(parent);
        }
        return adapter.getView(position - EXTRA, null, parent);
    }
    private View getNothingSelectedView(ViewGroup parent){
        try{
            PreferenceManager store=PreferenceManager.getInstance(context);
             TextView profile_ET = (TextView) ((Activity) context).findViewById(R.id.teams_spinner_prompt_text_view);
            String  profile=store.getProfile();
            if(profile!=null &&!profile.equals("")){
                profile_ET.setText(profile);
            }else{
                profile_ET.setText("Profile");
            }



        }catch (Exception e){
            e.printStackTrace();
        }
        return layoutInflater.inflate(nothingSelectedLayout,parent,false);
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }
}