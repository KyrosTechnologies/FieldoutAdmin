package com.kyros.technologies.fieldout.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.databinding.FragmentJobReportTemplateBinding;

/**
 * Created by kyros on 12-01-2018.
 */

public class JobReportTemplateFragment extends Fragment {
    private FragmentJobReportTemplateBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_job_report_template,container,false);
        View view=binding.getRoot();
        bindviews();
        return view;
    }

    private void bindviews() {
        for(int i=0; i<1;i++){
            //Tables Rows
            TableRow tableRow=new TableRow(getContext());
            tableRow.setBackground(getResources().getDrawable(R.color.bg));
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));

            //TextView jobReport Name
            TextView nameTextView=new TextView(getContext());
            nameTextView.setText(R.string.text_standard_job_report_template);
            nameTextView.setTextSize(24);
            TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
            tableRowuserTextParams.setMargins(15,15,15,15);
            nameTextView.setLayoutParams(tableRowuserTextParams);
            nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
            nameTextView.setTextColor(getResources().getColor(R.color.text_light));
            tableRow.addView(nameTextView);

            //TextView Published
            TextView publishedTextView=new TextView(getContext());
            publishedTextView.setText("Yes");
            publishedTextView.setTextSize(24);
            TableRow.LayoutParams tableRowPublishedParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
            tableRowPublishedParams.setMargins(15,15,15,15);
            publishedTextView.setLayoutParams(tableRowPublishedParams);
            publishedTextView.setTextColor(getResources().getColor(R.color.text_light));
            publishedTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
            tableRow.addView(publishedTextView);

            //TextView Default
            TextView defaultTextView=new TextView(getContext());
            defaultTextView.setText("Yes");
            defaultTextView.setTextSize(24);
            TableRow.LayoutParams tableRowDefaultParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
            tableRowDefaultParams.setMargins(15,15,15,15);
            defaultTextView.setLayoutParams(tableRowDefaultParams);
            defaultTextView.setTextColor(getResources().getColor(R.color.text_light));
            defaultTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
            tableRow.addView(defaultTextView);

            //TextView PDF Template
            TextView pdfTemplateTextView=new TextView(getContext());
            pdfTemplateTextView.setText("Default");
            pdfTemplateTextView.setTextSize(24);
            TableRow.LayoutParams tableRowuserNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
            tableRowuserNameParams.setMargins(15,15,15,15);
            pdfTemplateTextView.setLayoutParams(tableRowuserNameParams);
            pdfTemplateTextView.setTextColor(getResources().getColor(R.color.text_light));
            pdfTemplateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
            tableRow.addView(pdfTemplateTextView);
            binding.tableLayoutReportTemplate.addView(tableRow);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
