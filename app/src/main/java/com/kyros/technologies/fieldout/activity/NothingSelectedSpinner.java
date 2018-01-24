package com.kyros.technologies.fieldout.activity;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

/**
 * Created by Rohin on 15-12-2017.
 */

public class NothingSelectedSpinner implements SpinnerAdapter, ListAdapter {

    private static final int EXTRA = 1;
    private final SpinnerAdapter adapter;
    private final Context context;
    private final int nothingSelectedLayout;
    private final int nothingSelectedDropdownLayout;
    private final LayoutInflater layoutInflater;

    public NothingSelectedSpinner(
            SpinnerAdapter spinnerAdapter,
            int nothingSelectedLayout, Context context) {

        this(spinnerAdapter, nothingSelectedLayout, -1, context);
    }

    private NothingSelectedSpinner(SpinnerAdapter spinnerAdapter,
                                   int nothingSelectedLayout, int nothingSelectedDropdownLayout, Context context) {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.nothingSelectedLayout = nothingSelectedLayout;
        this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return getNothingSelectedView(parent);
        }
        return adapter.getView(position - EXTRA, null, parent); // Could re-use
    }

    private View getNothingSelectedView(ViewGroup parent) {
        PreferenceManager store;
        try{
            TextView business_type_text = (TextView) ((Activity) context).findViewById(R.id.business_type_text);
            store =PreferenceManager.getInstance(context);
            String bstype= store.getTechSpinner();
            if(bstype!=null&&!bstype.equals("")){
                business_type_text.setText(bstype);
            }else {
                business_type_text.setText("Check");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            TextView business_type_nature_text = (TextView) ((Activity) context).findViewById(R.id.Business_type_nature_text);
            store = PreferenceManager.getInstance(context);
            String bnature= store.getActivityTypeSpinner();
            if(bnature!=null&&!bnature.equals("")){
                business_type_nature_text.setText(bnature);
            }
            else {
                business_type_nature_text.setText("Test");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return layoutInflater.inflate(nothingSelectedLayout, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return nothingSelectedDropdownLayout == -1 ?
                    new View(context) :
                    getNothingSelectedDropdownView(parent);
        }
        return adapter.getDropDownView(position - EXTRA, null, parent);
    }

    private View getNothingSelectedDropdownView(ViewGroup parent) {
        return layoutInflater.inflate(nothingSelectedDropdownLayout, parent, false);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : adapter.getItem(position - EXTRA);
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
    public long getItemId(int position) {
        return position >= EXTRA ? adapter.getItemId(position - EXTRA) : position - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
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
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }

}