package com.kyros.technologies.fieldout.adapters;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by kyros on 18-12-2017.
 */

public class TeamBindingAdapter {
    @BindingAdapter({"app:textName"})
    public static void loadTeamName(TextView view,String text){
        view.setText(text);
    }

}
