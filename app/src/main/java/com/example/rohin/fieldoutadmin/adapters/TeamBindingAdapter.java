package com.example.rohin.fieldoutadmin.adapters;

import android.databinding.BindingAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by kyros on 18-12-2017.
 */

public class TeamBindingAdapter {
    @BindingAdapter({"app:textName"})
    public static void loadTeamName(TextView view,String text){
        view.setText(text);
    }

}
